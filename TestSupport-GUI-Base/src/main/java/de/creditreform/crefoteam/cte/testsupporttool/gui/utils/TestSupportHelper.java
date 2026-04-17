package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import de.creditreform.crefoteam.cte.rest.RestInvokerConfig;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
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

    /* CLAUDE_MODE — original-Felder:
    private final CteStateEngineService cteActivitiService;
    private final TesunRestService tesunRestServiceWLS;
    private final TesunRestService tesunRestServiceJvmImportC;
    */
    private final TesunClientJobListener tesunClientJobListener;
    private final EnvironmentConfig environmentConfig;

    public TestSupportHelper(EnvironmentConfig environmentConfig,
                             RestInvokerConfig masterConsoleRestInvokerConfig,
                             RestInvokerConfig impCyleRestInvokerConfig,
                             TesunClientJobListener tesunClientJobListener) {
        this.environmentConfig = environmentConfig;
        this.tesunClientJobListener = tesunClientJobListener;
        /* CLAUDE_MODE
        tesunRestServiceWLS = new TesunRestService(masterConsoleRestInvokerConfig, tesunClientJobListener);
        tesunRestServiceJvmImportC = new TesunRestService(impCyleRestInvokerConfig, tesunClientJobListener);
        */
    }

    public void checkStartCoinditions(Map<String, TestCustomer> activeTestCustomersMap, boolean isDemoMode, boolean confirmDlg) {
        if (isDemoMode) {
            return;
        }
        // Real-Mode: REST-Checks (checkRunningJobs + checkJvms) — folgen in Kategorie C
        tesunClientJobListener.notifyClientJob(Level.INFO, "\nReal-Mode: Prozess-Start-Bedingungen werden noch nicht geprüft (REST-Port ausstehend).");
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

    public String checkRunningJobs(Map<String, TestCustomer> activeTestCustomersMap) {
        /* CLAUDE_MODE — Original nutzt TesunPendingJob[s] (XML-Binding):
        tesunClientJobListener.notifyClientJob(Level.INFO, "\nPrüfe, ob Jobs aktiv sind...");
        StringBuilder errStringBuilder = new StringBuilder();
        StringBuilder jobsStringBuilder = new StringBuilder();
        final RestInvokerConfig restServiceConfigTesun = environmentConfig.getRestServiceConfigsForMasterkonsole().get(0);
        TesunRestService tesunRestService = new TesunRestService(restServiceConfigTesun, tesunClientJobListener);
        TesunPendingJobs tesunPendingJobs = tesunRestService.getTesunPendingJobs();
        List<TesunPendingJob> pendingJobsList = tesunPendingJobs.getJobs();
        if (!pendingJobsList.isEmpty()) {
            errStringBuilder.append("\nDer Test in der Umgebung '");
            errStringBuilder.append(environmentConfig.getCurrentEnvName());
            errStringBuilder.append("' kann nicht gestartet werden, solange noch JVM-Jobs aktiv sind!");
            errStringBuilder.append("\nEs sind derzeit folgende JVM-Jobs aktiv:");
            for (TesunPendingJob tesunPendingJob : pendingJobsList) {
                String theKey = tesunPendingJob.getProzessIdentifier().replace("EXPORT_CTE_TO_", "");
                if (activeTestCustomersMap.containsKey(theKey)) {
                    jobsStringBuilder.append("\n\t");
                    jobsStringBuilder.append(tesunPendingJob.getProzessIdentifier());
                    jobsStringBuilder.append(" mit ");
                    jobsStringBuilder.append(tesunPendingJob.getAnzahlTodoBloecke());
                    jobsStringBuilder.append(" Blöcken\n");
                }
            }
            if (!jobsStringBuilder.toString().isEmpty()) {
                errStringBuilder.append(jobsStringBuilder);
                errStringBuilder.append("\nSoll der Test dennoch gestartet werden?");
            }
        }
        return jobsStringBuilder.toString().isEmpty() ? "" : errStringBuilder.toString();
        */
        throw new UnsupportedOperationException("CLAUDE_MODE: TesunPendingJob[s]-XML-Binding im Spike nicht portiert.");
    }

    public String checkJvms(Map<String, TestCustomer> activeTestCustomersMap) {
        /* CLAUDE_MODE — Original nutzt Apache4-RestInvokerFactory:
        tesunClientJobListener.notifyClientJob(Level.INFO, "\nPrüfe, ob alle JVM's erreichbar sind...");
        TesunRestService tesunRestServiceCteBatchGUI = new TesunRestService(environmentConfig.getRestServiceConfigsForBatchGUI().get(0), tesunClientJobListener);
        Map<String, String> jvmInstallationMap = tesunRestServiceCteBatchGUI.getJvmInstallationMap();
        JobInfo jobInfoForImportCycle = environmentConfig.getJobInfoForImportCycle();

        StringBuilder errStringBuilder = new StringBuilder();
        List<String> notReachableJvmsList = new ArrayList<>();
        RestInvokerConfig restServiceConfigTesun = environmentConfig.getRestServiceConfigsForMasterkonsole().get(0);
        RestInvokerFactory restInvokerFactory = new Apache4RestInvokerFactory(restServiceConfigTesun.getServiceUser(), restServiceConfigTesun.getServicePassword(), 10000);
        for (Map.Entry<String, String> entry : jvmInstallationMap.entrySet()) {
            if (activeTestCustomersMap.containsKey(entry.getKey()) || jobInfoForImportCycle.getJvmName().equals(entry.getKey())) {
                try {
                    RestInvoker restInvoker = restInvokerFactory.getRestInvoker(entry.getValue());
                    restInvoker.appendPath("/jvm-info/maven-modules");
                    RestInvokerResponse restInvokerResponse = restInvoker.invokeGetWithRetry(null, 10, 5000L).expectStatusOK();
                    String responseBody = restInvokerResponse.getResponseBody();
                    String s1 = "<maven-module-list";
                    if (!responseBody.contains(s1)) {
                        throw new RuntimeException("getMavenModulesForJvm() : Response enthält nicht: " + s1);
                    }
                    String s2 = "<java-client-id>" + entry.getKey().toLowerCase().substring(0, 3) + "</java-client-id>";
                    if (!responseBody.contains(s2)) {
                        throw new RuntimeException("getMavenModulesForJvm() : Response enthält nicht: " + s2);
                    }
                    tesunClientJobListener.notifyClientJob(Level.INFO, String.format("\n\tJVM '%s' mit der URL '%s' ist erreichbar.", entry.getKey(), entry.getValue()));
                } catch (Exception ex) {
                    notReachableJvmsList.add(entry.getKey());
                } finally {
                    restInvokerFactory.close();
                }
            }
        }
        if (!notReachableJvmsList.isEmpty()) {
            errStringBuilder.append(String.format("\nFolgende JVM's konnten in der Umgebung '%s' nicht angesprochen werden!\n", environmentConfig.getCurrentEnvName()));
            errStringBuilder.append(notReachableJvmsList);
        }
        return errStringBuilder.toString();
        */
        throw new UnsupportedOperationException("CLAUDE_MODE: Apache4-RestInvoker im Spike nicht portiert.");
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

    public Object getTesunRestServiceWLS() {
        /* CLAUDE_MODE: Original liefert die im Konstruktor gebaute TesunRestService —
           im Stub nicht instantiert. */
        return null;
    }

}
