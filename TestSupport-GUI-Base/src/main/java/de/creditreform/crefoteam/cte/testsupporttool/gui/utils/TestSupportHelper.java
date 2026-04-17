package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import de.creditreform.crefoteam.cte.rest.RestInvokerConfig;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.rest.TesunRestService;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunPendingJob;
import de.creditreform.crefoteam.cte.tesun.rest.dto.TesunPendingJobs;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.JobInfo;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import de.creditreform.crefoteam.cte.testsupporttool.resume.ResumeState;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.utils.TestSupportHelper}.
 *
 * <p><b>CLAUDE_MODE:</b> Die Original-Klasse haengt sehr stark an Klassen,
 * die im Activiti-freien Spike nicht existieren — vor allem
 * {@code CteStateEngineService/Process/Task} (Activiti-REST-Wrapper),
 * {@code Apache4RestInvokerFactory/RestInvoker/RestInvokerResponse}
 * (apache-http-basierter REST-Invoker) und die XML-Bindings
 * {@code TesunPendingJob[s]}. Der Port lasst die Methoden-Signaturen
 * stehen, die Bodies werden zu CLAUDE_MODE-Stubs (UnsupportedOperation),
 * der Original-Code bleibt als Block-Kommentar lesbar.
 *
 * <p>Voll funktionsfaehig portiert sind nur die zwei Bild-Helper
 * {@link #getScaledDimension(JLabel, BufferedImage)} und
 * {@link #refreshProcessImage(InputStream, JLabel, boolean)} — die
 * brauchen nur JDK + commons-io und sind allgemein nuetzlich.
 */
public class TestSupportHelper {

    private final TesunClientJobListener tesunClientJobListener;
    private final EnvironmentConfig environmentConfig;
    private final TesunRestService tesunRestServiceWLS;
    private final TesunRestService tesunRestServiceJvmImportC;

    public TestSupportHelper(EnvironmentConfig environmentConfig,
                             RestInvokerConfig masterConsoleRestInvokerConfig,
                             RestInvokerConfig impCyleRestInvokerConfig,
                             TesunClientJobListener tesunClientJobListener) {
        this.environmentConfig = environmentConfig;
        this.tesunClientJobListener = tesunClientJobListener;
        this.tesunRestServiceWLS = masterConsoleRestInvokerConfig != null
                ? new TesunRestService(masterConsoleRestInvokerConfig, tesunClientJobListener) : null;
        this.tesunRestServiceJvmImportC = impCyleRestInvokerConfig != null
                ? new TesunRestService(impCyleRestInvokerConfig, tesunClientJobListener) : null;
    }

    public void checkStartCoinditions(Map<String, TestCustomer> activeTestCustomersMap, boolean isDemoMode, boolean confirmDlg) {
        if (isDemoMode) {
            return;
        }
        tesunClientJobListener.notifyClientJob(Level.INFO, "\nPrüfe die Prozess-Start-Bedingungen...");
        try {
            String errString = checkRunningJobs(activeTestCustomersMap);
            if (!errString.isEmpty()) {
                int confirmOpt = (int) tesunClientJobListener.askClientJob(TesunClientJobListener.ASK_FOR.ASK_OBJECT_RETRY, errString);
                if (confirmOpt == 1) {
                    throw new RuntimeException(errString);
                }
            }
            errString = checkJvms(activeTestCustomersMap);
            if (!errString.isEmpty()) {
                throw new RuntimeException(errString);
            }
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception ex) {
            throw new RuntimeException("Fehler bei Prozess-Start-Bedingungsprüfung", ex);
        }
    }

    public boolean killOrContinueRunningStateEngineProcess(String prozessKey, String prozessDefName, boolean confirmDlg) throws de.creditreform.crefoteam.cte.tesun.util.PropertiesException {
        File resumeFile = new File(environmentConfig.getTestOutputsRoot(), ResumeState.FILE_NAME);
        if (!resumeFile.exists()) {
            return false;
        }
        if (!confirmDlg) {
            resumeFile.delete();
            tesunClientJobListener.notifyClientJob(Level.INFO, "\nUnterbrochener Prozess-State gelöscht — Neustart.");
            return false;
        }
        int answer = (int) tesunClientJobListener.askClientJob(
                TesunClientJobListener.ASK_FOR.ASK_OBJECT_CONTINUE,
                "Der Prozess wurde zuvor unterbrochen!\nSoll der Prozess fortgesetzt oder neu gestartet werden?");
        if (answer == JOptionPane.YES_OPTION) {
            return true;
        }
        resumeFile.delete();
        return false;
    }

    public String checkRunningJobs(Map<String, TestCustomer> activeTestCustomersMap) throws Exception {
        tesunClientJobListener.notifyClientJob(Level.INFO, "\nPrüfe, ob Jobs aktiv sind...");
        StringBuilder errSb = new StringBuilder();
        StringBuilder jobsSb = new StringBuilder();
        TesunPendingJobs pending = tesunRestServiceWLS.getTesunPendingJobs();
        List<TesunPendingJob> pendingList = pending.getJobs();
        if (!pendingList.isEmpty()) {
            errSb.append("\nDer Test in der Umgebung '").append(environmentConfig.getCurrentEnvName())
                 .append("' kann nicht gestartet werden, solange noch JVM-Jobs aktiv sind!")
                 .append("\nEs sind derzeit folgende JVM-Jobs aktiv:");
            for (TesunPendingJob job : pendingList) {
                String key = job.getProzessIdentifier().replace("EXPORT_CTE_TO_", "");
                if (activeTestCustomersMap.containsKey(key)) {
                    jobsSb.append("\n\t").append(job.getProzessIdentifier())
                          .append(" mit ").append(job.getAnzahlTodoBloecke()).append(" Blöcken\n");
                }
            }
            if (!jobsSb.toString().isEmpty()) {
                errSb.append(jobsSb).append("\nSoll der Test dennoch gestartet werden?");
            }
        }
        return jobsSb.toString().isEmpty() ? "" : errSb.toString();
    }

    public String checkJvms(Map<String, TestCustomer> activeTestCustomersMap) throws Exception {
        tesunClientJobListener.notifyClientJob(Level.INFO, "\nPrüfe, ob alle JVM's erreichbar sind...");
        TesunRestService batchGuiService = new TesunRestService(
                environmentConfig.getRestServiceConfigsForBatchGUI().get(0), tesunClientJobListener);
        Map<String, String> jvmInstallationMap = batchGuiService.getJvmInstallationMap();
        JobInfo jobInfoForImportCycle = environmentConfig.getJobInfoForImportCycle();

        HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
        List<String> notReachable = new ArrayList<>();
        for (Map.Entry<String, String> entry : jvmInstallationMap.entrySet()) {
            String jvmName = entry.getKey();
            String jvmUrl  = entry.getValue();
            if (!activeTestCustomersMap.containsKey(jvmName) && !jobInfoForImportCycle.getJvmName().equals(jvmName)) {
                continue;
            }
            try {
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(jvmUrl + "/jvm-info/maven-modules"))
                        .timeout(Duration.ofSeconds(10)).GET().build();
                HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
                String body = resp.body() == null ? "" : resp.body();
                if (!body.contains("<maven-module-list")) {
                    throw new RuntimeException("Response enthält nicht: <maven-module-list");
                }
                String expectedId = "<java-client-id>" + jvmName.toLowerCase().substring(0, 3) + "</java-client-id>";
                if (!body.contains(expectedId)) {
                    throw new RuntimeException("Response enthält nicht: " + expectedId);
                }
                tesunClientJobListener.notifyClientJob(Level.INFO,
                        String.format("\n\tJVM '%s' (%s) ist erreichbar.", jvmName, jvmUrl));
            } catch (Exception ex) {
                notReachable.add(jvmName);
            }
        }
        if (!notReachable.isEmpty()) {
            return String.format("\nFolgende JVM's in '%s' nicht erreichbar: %s",
                    environmentConfig.getCurrentEnvName(), notReachable);
        }
        return "";
    }

    public Dimension getScaledDimension(JLabel jLabel, BufferedImage lastProcessImage) {
        Dimension imageSize = new Dimension(lastProcessImage.getWidth(), lastProcessImage.getHeight());
        Dimension boundary = new Dimension(jLabel.getWidth(), jLabel.getHeight());
        double widthRatio = boundary.getWidth() / imageSize.getWidth();
        double heightRatio = boundary.getHeight() / imageSize.getHeight();
        double ratio = Math.min(widthRatio, heightRatio);
        Dimension scaledDimension = new Dimension((int) (imageSize.width * ratio), (int) (imageSize.height * ratio));
        return scaledDimension;
    }

    public BufferedImage refreshProcessImage(InputStream inputStream, JLabel jLabel, boolean resizeProcessImage) throws IOException {
        if (inputStream == null) {
            return null;
        }
        InputStream bufferedInputStream = IOUtils.toBufferedInputStream(inputStream);
        byte[] byteArray = IOUtils.toByteArray(bufferedInputStream);
        InputStream temp2 = new ByteArrayInputStream(byteArray);
        BufferedImage lastProcessImage = ImageIO.read(temp2);
        if (resizeProcessImage) {
            Dimension scaledDimension = getScaledDimension(jLabel, lastProcessImage);
            Image resizedImage = lastProcessImage.getScaledInstance((int) scaledDimension.getWidth(), (int) scaledDimension.getHeight(), java.awt.Image.SCALE_DEFAULT);
            jLabel.setIcon(new ImageIcon(resizedImage));
        } else {
            jLabel.setIcon(new ImageIcon(lastProcessImage));
        }
        temp2.close();
        inputStream.close();
        return lastProcessImage;
    }

    public TesunRestService getTesunRestServiceWLS() {
        return tesunRestServiceWLS;
    }

}
