package de.creditreform.crefoteam.cte.tesun.util;

import de.creditreform.crefoteam.cte.rest.RestInvokerConfig;
import org.apache.commons.collections.list.TreeList;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Literal-Port aus {@code testsupport_client.tesun_util} ohne die
 * GIT-Repo-bezogenen Methoden (werden im Spike nicht benötigt).
 *
 * <p>Zusätzlich: {@link #forDemo(String)} / {@link #forDemo(String, long, long)}
 * als schlanke Factories, die das Laden aus einer Properties-Datei umgehen
 * und ein in-memory-Config für Tests und das Console-Demo liefern.
 */
public class EnvironmentConfig {
    public static final String DELIMITER = "[,;]";

    public static final String ENV_PROPFILES_NAME_PATTERN = "-config.properties";
    public static final String PROPNAME_ADMIN_FUNCS_ENABLED = "ADMIN_FUNCS_ENABLED";
    public static final String PROPNAME_MUST_EXTEND_ENTGS_FROM_REST = "MUST_EXTEND_ENTGS_FROM_REST";

    public static final String PROPNAME_ACTIVITI_URLS = "ACTIVITI_URLS";
    public static final String PROPNAME_MASTERKONSOLE_URLS = "MASTERKONSOLE_URLS";
    public static final String PROPNAME_BATCH_GUI_URLS = "BATCH_GUI_URLS";
    public static final String PROPNAME_JVM_IMPCYCLE_URLS = "JVM_IMPCYCLE_URLS";
    public static final String PROPNAME_JVM_INSO_URLS = "JVM_INSO_URLS";
    public static final String PROPNAME_JVM_INSOBACKEND_URLS = "JVM_INSOBACKEND_URLS";
    public static final String PROPNAME_JVM_BIC_URLS = "JVM_BIC_URLS";

    public static final String PROPNAME_JOB_NAME_IMPORT_CYCLE = "JOB-NAME-IMPORT-CYCLE";
    public static final String PROPNAME_JOB_NAME_BTLG_IMPORT_DELTA = "JOB-NAME-BTLG-IMPORT-DELTA";
    public static final String PROPNAME_JOB_NAME_ENTG_BERECHNUNG = "JOB-NAME-ENTG-BERECHNUNG";
    public static final String PROPNAME_JOB_NAME_BTLG_UPDATE_TRIGGER = "JOB-NAME-BTLG-AKTUALISIERUNG";
    public static final String PROPNAME_JOB_NAME_CT_IMPORT_DELTA = "JOB-NAME-CT-IMPORT-DELTA";

    public static final String PROPNAME_ACTIVITI_TESTER_EMAIL_FROM = "ACTIVITI-TESTER-EMAIL-FROM";
    public static final String PROPNAME_ACTIVITI_TESTER_EMAIL_SUCCESS_TO = "ACTIVITI-TESTER-EMAIL-SUCCESS-TO";
    public static final String PROPNAME_ACTIVITI_TESTER_EMAIL_FAILURE_TO = "ACTIVITI-TESTER-EMAIL-FAILURE-TO";
    public static final String PROPNAME_EMAIL_SMTP_HOST = "EMAIL-SMTP-HOST";
    public static final String PROPNAME_EMAIL_SMTP_PORT = "EMAIL-SMTP-PORT";
    public static final String PROPNAME_TIME_BEFORE_CT_IMPORT = "TIME_BEFORE_CT_IMPORT";
    public static final String PROPNAME_TIME_BEFORE_BTLG_IMPORT = "TIME_BEFORE_BTLG_IMPORT";
    public static final String TIME_BEFORE_EXPORTS_COLLECT = "TIME_BEFORE_EXPORTS_COLLECT";
    public static final String TIME_BEFORE_SFTP_COLLECT = "TIME_BEFORE_SFTP_COLLECT";
    public static final String PROPNAME_TIME_BEFORE_EXPORT = "TIME_BEFORE_EXPORT";
    public static final String PROPNAME_TIME_BEFORE_INSO_EXPORT = "TIME_BEFORE_INSO_EXPORTS";

    public static final String PROPNAME_SFTP_UPLOAD_ACTIVE = "SFTP_UPLOAD_ACTIVE";
    public static final String PROPNAME_CHECK_EXPORT_PROTOKOLL_ACTIVE = "CHECK-EXPORT-PROTOKOLL-ACTIVE";
    public static final String PROPNAME_ACTIVITI_PROCESS_NAME = "ACTIVITI-PROCESS-NAME";
    public static final String PROPNAME_ACTIVITI_PROCESS_TO_UPLOAD = "ACTIVITI-PROCESS-TO-UPLOAD";
    public static final String PROPNAME_TARGET_CLZ_FOR_DE_PSEUDO_CREFOS = "TARGET_CLZ_FOR_DE_PSEUDO_CREFOS";
    public static final String PROPNAME_TARGET_CLZ_FOR_AT_PSEUDO_CREFOS = "TARGET_CLZ_FOR_AT_PSEUDO_CREFOS";
    public static final String PROPNAME_TARGET_CLZ_FOR_LU_PSEUDO_CREFOS = "TARGET_CLZ_FOR_LU_PSEUDO_CREFOS";
    public static final String PROPNAME_EXTRA_CLZS_TO_VC_LIST = "EXTRA_CLZS_TO_VC_LIST";

    public static final String PROPNAME_TEST_TYPES = "TEST-TYPES";
    public static final String PROPNAME_TEST_SOURCES = "TEST-SOURCES";

    public static final String PROPNAME_USE_LOCAL_EXPORTS = "USE_LOCAL_EXPORTS";
    public static final String PROPNAME_AVAILABLE_CUSTOMERS = "AVAILABLE_CUSTOMERS";
    public static final String PROPNAME_CUSTOMER = "CUSTOMER_";
    public static final String PROPNAME_IMPORT_CYCLE_TIME_OUT = "IMPORT_CYCLE_TIME_OUT";
    public static final String PROPNAME_EXPORTS_TIME_OUT = "EXPORTS_TIME_OUT";
    public static final String PROPNAME_JOBSTATUS_QUERY_SLEEPTIME = "JOBSTATUS_QUERY_SLEEPTIME";
    public static final String PROPNAME_TEST_RSC_DIR = "TEST-RSC-DIR";
    public static final String PROPNAME_REPOSITORY_HOST = "REPOSITORY_HOST";
    public static final String PROPNAME_REPOSITORY_USER = "REPOSITORY_USER";
    public static final String PROPNAME_REPOSITORY_PASSWORD = "REPOSITORY_PASSWORD";
    public static final String PROPNAME_ITSQ_REVISIONS = "ITSQ_REVISIONS";
    public static final String PROPNAME_ITSQ_TAG_NAME_FORMAT = "ITSQ_TAG_NAME_FORMAT";
    public static final String PROPNAME_MAX_CUSTOMERS_PER_TEST = "MAX_CUSTOMERS_PER_TEST";
    public static final String PROPNAME_DHL_TO_VVC_SFTP_PATH = "DHL_TO_VVC_SFTP_PATH";
    public static final String PROPNAME_VVC_TO_DHL_SFTP_PATH = "VVC_TO_DHL_SFTP_PATH";
    public static final String PROPNAME_DHL_EXPORT_SFTP_HOST = "DHL_EXPORT_SFTP_HOST";
    public static final String PROPNAME_DHL_EXPORT_PREFIX = "DHL_EXPORT_PREFIX";
    public static final String PROPNAME_DHL_UPLOAD_SFTP_HOST = "DHL_UPLOAD_SFTP_HOST";

    public static final String PROPNAME_LAST_TEST_SOURCE = "LAST_TEST_SOURCE";
    public static final String PROPNAME_LAST_TEST_TYPE = "LAST_TEST_TYPE";
    public static final String PROPNAME_LAST_ITSQ_REVISION = "LAST_ITSQ_REVISION";
    public static final String PROPNAME_LAST_LOAD_PATH = "LAST_LOAD_PATH";
    public static final String PROPNAME_LAST_CFG_FILENAMES_LIST = "LAST_CFG_FILENAMES_LIST";
    public static final String PROPNAME_LAST_CFG_FILENAME = "LAST_CFG_FILENAME";
    public static final String PROPNAME_LAST_UPLOAD_SYNTHETICS = "LAST_UPLOAD_SYNTHETICS";
    public static final String PROPNAME_LAST_USE_ONLY_TEST_CLZ = "LAST_USE_ONLY_TEST_CLZ";
    public static final String PROPNAME_LAST_WINDOW_HEIGHT = "LAST_WINDOW_HEIGHT";
    public static final String PROPNAME_LAST_WINDOW_WIDTH = "LAST_WINDOW_WIDTH";
    public static final String PROPNAME_LAST_WINDOW_X_POS = "LAST_WINDOW_X_POS";
    public static final String PROPNAME_LAST_WINDOW_Y_POS = "LAST_WINDOW_Y_POS";
    public static final String PROPNAME_LAST_LOOK_AND_FEEL_CLASS = "LAST_LOOK_AND_FEEL_CLASS";

    private final OrderedProperties mainProperties = new OrderedProperties(StandardCharsets.UTF_8);
    private String cteVersion;
    private String currentEnvName;
    private File testResourcesRoot;
    private File environmentConfigFile;
    private File testResourcesDir;
    private String testToolVersion;
    private String appVerionsInfo;
    private TestSupportClientKonstanten.TEST_TYPES lastTestType;

    private String lastItsqRevision;
    private String lastTestSource;
    private String lastRepositoryUserName;
    private String lastRepositoryUserPassword;
    int lastWindowHeight;
    int lastWindowWidth;
    int lastWindowXPos;
    int lastWindowYPos;
    String lastLookAndFeelClass = "";
    List<String> lastCfgFileNamesList = new ArrayList<>();
    String lastLoadPath;
    private String lastCfgFileName;
    private boolean lastUploadSynthetics;
    private boolean lastUseOnlyTestClz;

    public EnvironmentConfig() throws PropertiesException {
        loadEnvironmentConfig("ENE");
    }

    public EnvironmentConfig(File envConfigFile) {
        loadEnvironmentConfig(envConfigFile);
    }

    public EnvironmentConfig(String envName) {
        loadEnvironmentConfig(envName);
    }

    /** Privater Konstruktor für die Demo-Factories — kein File-Load. */
    private EnvironmentConfig(String envName, boolean skipLoad) {
        this.currentEnvName = envName;
    }

    /**
     * Demo/Test-Factory: liefert eine in-memory-Config ohne Properties-Datei.
     * Setzt die MASTERKONSOLE_URLS so, dass {@link #getRestServiceConfigsForMasterkonsole()}
     * eine einzige URL zurückgibt.
     */
    public static EnvironmentConfig forDemo(String masterkonsoleUrl) {
        return forDemo(masterkonsoleUrl, 100L, 5_000L);
    }

    public static EnvironmentConfig forDemo(String masterkonsoleUrl,
                                            long jobStatusPollingMillis,
                                            long jobTimeoutMillis) {
        EnvironmentConfig ec = new EnvironmentConfig("DEMO", true);
        ec.mainProperties.setProperty(PROPNAME_MASTERKONSOLE_URLS, masterkonsoleUrl);
        ec.mainProperties.setProperty(PROPNAME_JVM_INSOBACKEND_URLS, masterkonsoleUrl);
        ec.mainProperties.setProperty(PROPNAME_BATCH_GUI_URLS, masterkonsoleUrl);
        ec.mainProperties.setProperty(PROPNAME_JVM_INSO_URLS, masterkonsoleUrl);
        ec.mainProperties.setProperty(PROPNAME_JVM_IMPCYCLE_URLS, masterkonsoleUrl);
        ec.mainProperties.setProperty(PROPNAME_JVM_BIC_URLS, masterkonsoleUrl);
        ec.mainProperties.setProperty(PROPNAME_ACTIVITI_URLS, masterkonsoleUrl);
        // PT-Format unterstützt nur ganze Sekunden → auf 1s runden
        long pollSecs = Math.max(1, (jobStatusPollingMillis + 999) / 1000);
        long timeoutSecs = Math.max(1, (jobTimeoutMillis + 999) / 1000);
        ec.mainProperties.setProperty(PROPNAME_JOBSTATUS_QUERY_SLEEPTIME, "PT" + pollSecs + "S");
        ec.mainProperties.setProperty(PROPNAME_IMPORT_CYCLE_TIME_OUT, "PT" + timeoutSecs + "S");
        ec.mainProperties.setProperty(PROPNAME_TEST_RSC_DIR, "X-TESTS");
        // Kunden-Initialisierung aus X-TESTS/ITSQ ist auch im Demo-Mode Pflicht;
        // ohne AVAILABLE_CUSTOMERS wirft getAvailableCustomersFromProperties. Der
        // Dummy-Wert existiert nie als Subdir in REF-EXPORTS/<PHASE> → leere Map.
        ec.mainProperties.setProperty(PROPNAME_AVAILABLE_CUSTOMERS, "_DEMO_PLACEHOLDER_");
        // ACTIVITI-Properties: zur Migration noch in TaskVariablen sichtbar,
        // werden aber von keinem State-Machine-Handler ausgewertet.
        ec.mainProperties.setProperty(PROPNAME_ACTIVITI_PROCESS_NAME, "DEMO-TestAutomationProcess");
        ec.mainProperties.setProperty(PROPNAME_ACTIVITI_TESTER_EMAIL_FROM, "test-automatisierung@local");
        ec.mainProperties.setProperty(PROPNAME_ACTIVITI_TESTER_EMAIL_SUCCESS_TO, "demo@local");
        ec.mainProperties.setProperty(PROPNAME_ACTIVITI_TESTER_EMAIL_FAILURE_TO, "demo@local");
        ec.mainProperties.setProperty(PROPNAME_JOB_NAME_IMPORT_CYCLE,
                "IMPORTCYCLE;importcycle.importCycle;BETEILIGUNGEN_IMPORT,ENTSCHEIDUNGSTRAEGER_BERECHNUNG,BTLG_UPDATE_TRIGGER,FROM_STAGING_INTO_CTE");
        ec.mainProperties.setProperty(PROPNAME_JOB_NAME_BTLG_IMPORT_DELTA,
                "IMPORTCYCLE;importcycle.beteiligungenImportDelta;BETEILIGUNGEN_IMPORT");
        ec.mainProperties.setProperty(PROPNAME_JOB_NAME_ENTG_BERECHNUNG,
                "IMPORTCYCLE;importcycle.entgBerechnung;ENTSCHEIDUNGSTRAEGER_BERECHNUNG");
        ec.mainProperties.setProperty(PROPNAME_JOB_NAME_BTLG_UPDATE_TRIGGER,
                "IMPORTCYCLE;importcycle.btlnAktualisierung;BTLG_UPDATE_TRIGGER");
        ec.mainProperties.setProperty(PROPNAME_JOB_NAME_CT_IMPORT_DELTA,
                "IMPORTCYCLE;importcycle.ctImportDelta;FROM_STAGING_INTO_CTE");
        // Demo-Mode schließt nur REST-Aufrufe und Worker-Teile der Handler aus —
        // die Kunden-Initialisierung aus X-TESTS ist auch im Demo-Mode Pflicht.
        try {
            ec.testResourcesRoot = ec.findTestResourcesRoot();
        } catch (Exception ex) {
            throw new RuntimeException("forDemo benötigt ein '"
                    + ec.mainProperties.getProperty(PROPNAME_TEST_RSC_DIR)
                    + "'-Verzeichnis im Pfadbaum ab '" + System.getProperty("user.dir") + "'", ex);
        }
        return ec;
    }

    public void loadEnvironmentConfig(String envName) {
        Map<String, File> evnvironmentsMap = getEnvironmentsMap();
        if (envName.isBlank()) {
            envName = "ENE";
        }
        environmentConfigFile = evnvironmentsMap.get(envName.toUpperCase());
        loadEnvironmentConfig(environmentConfigFile);
    }

    private void loadEnvironmentConfig(File envConfigFile) {
        if ((envConfigFile == null) || !envConfigFile.exists()) {
            throw new RuntimeException("Die Konfigurationsdatei '"
                    + (envConfigFile == null ? "null" : envConfigFile.getAbsolutePath()) + "' existiert nicht!");
        }
        this.environmentConfigFile = envConfigFile;
        try {
            loadProperties(envConfigFile);
            this.testResourcesRoot = findTestResourcesRoot();
            String[] split = envConfigFile.getName().split("-config.properties");
            this.currentEnvName = split[0];

            setLastTestType(TestSupportClientKonstanten.TEST_TYPES.valueOf(getProperty(PROPNAME_LAST_TEST_TYPE, false, TestSupportClientKonstanten.DEFAUL_TESTS_TYPE.name())));
            setLastTestSource(getProperty(PROPNAME_LAST_TEST_SOURCE, false, TestSupportClientKonstanten.DEFAUL_TESTS_SOURCE));
            setLastItsqRevision(getProperty(PROPNAME_LAST_ITSQ_REVISION, false, TestSupportClientKonstanten.DEFAULT_ITSQ_REVISION));
            this.setLastLoadPath(getProperty(PROPNAME_LAST_LOAD_PATH, false, testResourcesRoot.getAbsolutePath().replaceAll("\\\\", "/")));
            this.setLastCfgFileName(getProperty(PROPNAME_LAST_CFG_FILENAME, false, testResourcesRoot.getAbsolutePath().replaceAll("\\\\", "/")));
            String property = getProperty(PROPNAME_LAST_CFG_FILENAMES_LIST, false, "");
            if (property != null && !property.isEmpty()) {
                for (String s : property.split("[;,]")) {
                    lastCfgFileNamesList.add(s.trim());
                }
            }
            this.setLastUploadSynthetics(getBooleanPropertyValue(PROPNAME_LAST_UPLOAD_SYNTHETICS, false, false));
            this.setLastUseOnlyTestClz(getBooleanPropertyValue(PROPNAME_LAST_USE_ONLY_TEST_CLZ, false, false));
            this.setLastLookAndFeelClass(getProperty(PROPNAME_LAST_LOOK_AND_FEEL_CLASS, false, "javax.swing.plaf.metal.MetalLookAndFeel"));
            this.setLastWindowXPos(Integer.parseInt(getProperty(PROPNAME_LAST_WINDOW_X_POS, false, "200")));
            this.setLastWindowYPos(Integer.parseInt(getProperty(PROPNAME_LAST_WINDOW_Y_POS, false, "100")));
            this.setLastWindowHeight(Integer.parseInt(getProperty(PROPNAME_LAST_WINDOW_HEIGHT, false, "400")));
            this.setLastWindowWidth(Integer.parseInt(getProperty(PROPNAME_LAST_WINDOW_WIDTH, false, "800")));
            appVerionsInfo = getVersionFromBuildInfo();
        } catch (PropertiesException ex) {
            throw new RuntimeException(ex);
        }
    }

    private File findTestResourcesRoot() throws PropertiesException {
        String testRscDir = getProperty(PROPNAME_TEST_RSC_DIR, true, "");
        Path currentPath = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        while (currentPath != null) {
            File testFolder = currentPath.resolve(testRscDir).toFile();
            if (testFolder.exists() && testFolder.isDirectory()) {
                return testFolder;
            }
            currentPath = currentPath.getParent();
        }
        throw new RuntimeException("Verzeichnis " + testRscDir + " konnte im Pfadbaum nicht gefunden werden!");
    }

    public File getEnvironmentConfigFile() { return environmentConfigFile; }

    public String getAppVersionsInfo() { return appVerionsInfo; }

    public static Map<String, File> getEnvironmentsMap() {
        Map<String, File> map = new TreeMap<>();
        FileFilter filter = f -> f.isFile() && f.getName().endsWith(ENV_PROPFILES_NAME_PATTERN);
        URL resource = EnvironmentConfig.class.getResource("/");
        File[] listFiles = resource == null ? null : new File(resource.getPath()).listFiles(filter);
        if (listFiles == null || listFiles.length < 1) {
            listFiles = new File(System.getProperty("user.dir")).listFiles(filter);
        }
        // Zusätzlich: X-TESTS/<env>-config.properties (Spike-Konvention)
        if (listFiles == null || listFiles.length < 1) {
            File xTests = new File(System.getProperty("user.dir"), "X-TESTS");
            if (xTests.isDirectory()) {
                listFiles = xTests.listFiles(filter);
            }
        }
        if (listFiles != null) {
            for (File file : listFiles) {
                String[] split = file.getName().split(ENV_PROPFILES_NAME_PATTERN);
                map.put(split[0].toUpperCase(), file);
            }
        }
        return map;
    }

    public Properties getDirectoryCompareProperties() throws Exception {
        String propsFileName = getProperty(TestSupportClientKonstanten.OPT_DIRECTORY_COMPARE_PROPS, true, "");
        File propsFile = new File(propsFileName);
        Properties properties = new Properties();
        try (InputStream in = new FileInputStream(propsFile)) {
            properties.load(in);
        }
        properties.setProperty("PROPES_FILE_PATH", propsFile.getParent());
        return properties;
    }

    private void loadProperties(File propsFile) {
        try (InputStream in = new FileInputStream(propsFile.getPath())) {
            mainProperties.clear();
            mainProperties.load(in);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Boolean getBooleanPropertyValue(String propName, boolean isRequired, Boolean defaultValue) throws PropertiesException {
        String strPropValue = getProperty(propName, isRequired, defaultValue.toString());
        return strPropValue.equalsIgnoreCase("ja") || strPropValue.equalsIgnoreCase("true") || strPropValue.equals("1");
    }

    public String getProperty(String propName, boolean isRequired, String defaultValue) throws PropertiesException {
        String propValue = mainProperties.getProperty(propName);
        try {
            propValue = propValue.trim();
        } catch (Exception ex) {
            propValue = null;
        }
        boolean aNull = propValue == null || propValue.equals("null");
        if (aNull && isRequired) {
            throw new PropertiesException("Der Wert für '" + propName + "' wurde nicht gesetzt!");
        } else if (aNull) {
            propValue = defaultValue;
        }
        return propValue.trim();
    }

    public Integer getMaxCustomersForTest() throws PropertiesException {
        String maxCustomers = getProperty(PROPNAME_MAX_CUSTOMERS_PER_TEST, false, "5");
        return maxCustomers == null ? 14 : Integer.valueOf(maxCustomers);
    }

    public Integer getTargetClzForDePseudoCrefos() throws PropertiesException {
        return Integer.valueOf(getProperty(PROPNAME_TARGET_CLZ_FOR_DE_PSEUDO_CREFOS, true, "412"));
    }

    public Integer getTargetClzForAtPseudoCrefos() throws PropertiesException {
        return Integer.valueOf(getProperty(PROPNAME_TARGET_CLZ_FOR_AT_PSEUDO_CREFOS, true, "912"));
    }

    public Integer getTargetClzForLuPseudoCrefos() throws PropertiesException {
        return Integer.valueOf(getProperty(PROPNAME_TARGET_CLZ_FOR_LU_PSEUDO_CREFOS, true, "938"));
    }

    public List<Integer> getExtraClzsToVcListCrefos() throws PropertiesException {
        List<Integer> vcList = new ArrayList<>();
        String extraClz = getProperty(PROPNAME_EXTRA_CLZS_TO_VC_LIST, false, "912,938");
        if (!extraClz.isBlank()) {
            for (String s : extraClz.split(DELIMITER)) {
                vcList.add(Integer.valueOf(s));
            }
        }
        vcList.add(getTargetClzForDePseudoCrefos());
        return vcList;
    }

    public boolean mustExtendEntgsFromREST() throws PropertiesException {
        return getBooleanPropertyValue(PROPNAME_MUST_EXTEND_ENTGS_FROM_REST, false, Boolean.FALSE);
    }

    public boolean isAdminFuncsEnabled() throws PropertiesException {
        return getBooleanPropertyValue(PROPNAME_ADMIN_FUNCS_ENABLED, false, Boolean.FALSE);
    }

    public boolean isSftpUploadEnabled() throws PropertiesException {
        return getBooleanPropertyValue(PROPNAME_SFTP_UPLOAD_ACTIVE, false, Boolean.FALSE);
    }

    public boolean isCheckExportProtokollEnabled() throws PropertiesException {
        return getBooleanPropertyValue(PROPNAME_CHECK_EXPORT_PROTOKOLL_ACTIVE, false, Boolean.FALSE);
    }

    public boolean isActivitiProcessToBeUploaded() throws PropertiesException {
        return getBooleanPropertyValue(PROPNAME_ACTIVITI_PROCESS_TO_UPLOAD, false, Boolean.FALSE);
    }

    public String getActivitiProcessName() throws PropertiesException {
        return getProperty(PROPNAME_ACTIVITI_PROCESS_NAME, true, "ENE-TestAutomationProcess");
    }

    public String getActivitiEmailFrom() throws PropertiesException {
        return getProperty(PROPNAME_ACTIVITI_TESTER_EMAIL_FROM, true, "test-automatisierung@creditreform.de");
    }

    public String getActivitiSuccessEmailTo() throws PropertiesException {
        return getProperty(PROPNAME_ACTIVITI_TESTER_EMAIL_SUCCESS_TO, true, "k.cavdar@verband.creditreform.de");
    }

    public String getActivitiFailureEmailTo() throws PropertiesException {
        return getProperty(PROPNAME_ACTIVITI_TESTER_EMAIL_FAILURE_TO, true, "k.cavdar@verband.creditreform.de");
    }

    public String getSmtpHost() throws PropertiesException {
        return getProperty(PROPNAME_EMAIL_SMTP_HOST, true, TestSupportClientKonstanten.EMAIL_HOST_NAME);
    }

    public int getSmtpPort() throws PropertiesException {
        return Integer.parseInt(getProperty(PROPNAME_EMAIL_SMTP_PORT, true, String.valueOf(TestSupportClientKonstanten.EMAIL_PORT)));
    }

    public String getRepositoryHost() throws PropertiesException {
        return getProperty(PROPNAME_REPOSITORY_HOST, true, "");
    }

    public void setLastRepositoryUserName(String currentRepositoryUserName) {
        this.lastRepositoryUserName = currentRepositoryUserName;
    }

    public String getRepositoryUserName() throws PropertiesException {
        if (lastRepositoryUserName != null) {
            return lastRepositoryUserName;
        }
        return getProperty(PROPNAME_REPOSITORY_USER, true, "tesuntestene");
    }

    public void setLastRepositoryUserPassword(String currentRepositoryUserPassword) {
        this.lastRepositoryUserPassword = currentRepositoryUserPassword;
    }

    public String getLastRepositoryPassword() throws PropertiesException {
        if (lastRepositoryUserPassword != null) {
            return lastRepositoryUserPassword;
        }
        return getProperty(PROPNAME_REPOSITORY_PASSWORD, true, "tesuntestene");
    }

    public List<String> getItsqRevisions() throws PropertiesException {
        String itsqRevisions = getProperty(PROPNAME_ITSQ_REVISIONS, true, TestSupportClientKonstanten.DEFAULT_ITSQ_REVISION);
        return Arrays.asList(itsqRevisions.split(DELIMITER));
    }

    public String getItsqTagNameFormat() throws PropertiesException {
        return getProperty(PROPNAME_ITSQ_TAG_NAME_FORMAT, false, "USER-ENV-DATE-TIME");
    }

    public String getItsqTagName(String selectedTestType) throws PropertiesException {
        return getItsqTagName(getItsqTagNameFormat(), selectedTestType);
    }

    public String getItsqTagName(String itsqTagNameFormat, String selectedTestType) throws PropertiesException {
        if (itsqTagNameFormat != null) {
            try {
                if (itsqTagNameFormat.contains("CTE")) {
                    itsqTagNameFormat = itsqTagNameFormat.replace("CTE", getCteVersion());
                }
                if (itsqTagNameFormat.contains("ENV")) {
                    itsqTagNameFormat = itsqTagNameFormat.replace("ENV", getCurrentEnvName());
                }
                if (itsqTagNameFormat.contains("USER")) {
                    itsqTagNameFormat = itsqTagNameFormat.replace("USER", getRepositoryUserName());
                }
                if (itsqTagNameFormat.contains("DATE")) {
                    SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                    itsqTagNameFormat = itsqTagNameFormat.replace("DATE", fmt.format(Calendar.getInstance().getTime()));
                }
                if (itsqTagNameFormat.contains("TIME")) {
                    SimpleDateFormat fmt = new SimpleDateFormat("HH.mm.ss", Locale.getDefault());
                    itsqTagNameFormat = itsqTagNameFormat.replace("TIME", fmt.format(Calendar.getInstance().getTime()));
                }
                if (itsqTagNameFormat.contains("TOOL")) {
                    itsqTagNameFormat = itsqTagNameFormat.replace("TOOL", getTestToolVersionLong());
                }
                itsqTagNameFormat += ";";
                itsqTagNameFormat += selectedTestType;
            } catch (Exception ex) {
                throw new PropertiesException(String.format("Property %s falsch gesetzt!", PROPNAME_ITSQ_TAG_NAME_FORMAT));
            }
        }
        if (itsqTagNameFormat.startsWith("-")) {
            itsqTagNameFormat = itsqTagNameFormat.substring(1);
        }
        return itsqTagNameFormat;
    }

    public File getTestResourcesDir() {
        if (testResourcesDir != null) {
            return testResourcesDir;
        }
        // Auto-Resolve: <testResourcesRoot>/<lastTestSource>/<lastItsqRevision>
        // Beispiel: X-TESTS/ITSQ/ZWEI_PHASEN. Entspricht dem Default-Setup,
        // das die GUI im Original-Projekt vornimmt.
        File root = testResourcesRoot != null
                ? testResourcesRoot
                : new File(System.getProperty("user.dir"), "X-TESTS");
        String source = lastTestSource != null ? lastTestSource : TestSupportClientKonstanten.DEFAUL_TESTS_SOURCE;
        String revision = lastItsqRevision != null ? lastItsqRevision : TestSupportClientKonstanten.DEFAULT_ITSQ_REVISION;
        return new File(new File(root, source), revision);
    }

    public void setTestResourcesDir(File testResourcesDir) {
        this.testResourcesDir = testResourcesDir;
    }

    public File getTestResultsRoot() throws PropertiesException {
        return new File(getTestOutputsRoot(), TestSupportClientKonstanten.TEST_RESULTS);
    }

    public File getTestResultsRoot(TestSupportClientKonstanten.TEST_PHASE testPhase) throws PropertiesException {
        return new File(getTestResultsRoot(), testPhase.getDirName());
    }

    public File getArchivBestandsRoot() {
        return new File(getTestResourcesDir(), TestSupportClientKonstanten.ARCHIV_BESTAND);
    }

    public File getArchivBestandsRoot(TestSupportClientKonstanten.TEST_PHASE testPhase) {
        return new File(getArchivBestandsRoot(), testPhase.getDirName());
    }

    public File getPseudoArchivBestandsRoot() throws PropertiesException {
        return new File(getTestOutputsRoot(), TestSupportClientKonstanten.PSEUDO_ARCHIV_BESTAND);
    }

    public File getPseudoArchivBestandsRoot(TestSupportClientKonstanten.TEST_PHASE testPhase) throws PropertiesException {
        return new File(getPseudoArchivBestandsRoot(), testPhase.getDirName());
    }

    public File getNewTestCasesRoot() {
        return new File(getTestResourcesDir(), TestSupportClientKonstanten.NEW_TEST_CASES);
    }

    public File getItsqRefExportsRoot() {
        return new File(getTestResourcesDir(), TestSupportClientKonstanten.REF_EXPORTS);
    }

    public File getItsqRefExportsRoot(TestSupportClientKonstanten.TEST_PHASE testPhase) {
        return new File(getItsqRefExportsRoot(), testPhase.getDirName());
    }

    public File getPseudoRefExportsRoot() throws PropertiesException {
        return new File(getTestOutputsRoot(), TestSupportClientKonstanten.PSEUDO_REF_EXPORTS);
    }

    public File getPseudoRefExportsRoot(TestSupportClientKonstanten.TEST_PHASE testPhase) throws PropertiesException {
        return new File(getPseudoRefExportsRoot(), testPhase.getDirName());
    }

    public File getCollectsRoot() throws PropertiesException {
        return new File(getTestOutputsRoot(), TestSupportClientKonstanten.COLLECTED);
    }

    public File getCollectsRoot(TestSupportClientKonstanten.TEST_PHASE testPhase) throws PropertiesException {
        return new File(getCollectsRoot(), testPhase.getDirName());
    }

    public File getRestoredCollectsRoot() throws PropertiesException {
        return new File(getTestOutputsRoot(), TestSupportClientKonstanten.RESTORED_COLLECTS);
    }

    public File getRestoredCollectsRoot(TestSupportClientKonstanten.TEST_PHASE testPhase) throws PropertiesException {
        return new File(getRestoredCollectsRoot(), testPhase.getDirName());
    }

    public File getCheckedRoot() throws PropertiesException {
        return new File(getTestOutputsRoot(), TestSupportClientKonstanten.CHECKED);
    }

    public File getCheckedRoot(TestSupportClientKonstanten.TEST_PHASE testPhase) throws PropertiesException {
        return new File(getCheckedRoot(), testPhase.getDirName());
    }

    public File getTestOutputsRoot() throws PropertiesException {
        if (testResourcesRoot == null) {
            return new File(new File(System.getProperty("user.dir"), "X-TESTS"),
                    TestSupportClientKonstanten.TEST_OUTPUTS + File.separator + currentEnvName);
        }
        File testOutputsDir = new File(testResourcesRoot, TestSupportClientKonstanten.TEST_OUTPUTS);
        return new File(testOutputsDir, currentEnvName);
    }

    public File getTestOutputsRootForEnv(String envName) throws PropertiesException {
        File root = testResourcesRoot != null
                ? testResourcesRoot
                : new File(System.getProperty("user.dir"), "X-TESTS");
        return new File(new File(root, TestSupportClientKonstanten.TEST_OUTPUTS), envName);
    }

    public File getLogOutputsRoot() throws PropertiesException {
        return new File(getTestOutputsRoot(), "logs");
    }

    public File getLogOutputsRootForEnv(String envName) throws PropertiesException {
        return new File(getTestOutputsRootForEnv(envName), "logs");
    }

    public File getSftpUploadsRoot() throws PropertiesException {
        return new File(getTestOutputsRoot(), TestSupportClientKonstanten.SFTP_UPLOADS);
    }

    public File getSftpUploadsRoot(TestSupportClientKonstanten.TEST_PHASE testPhase) throws PropertiesException {
        return new File(getSftpUploadsRoot(), testPhase.getDirName());
    }

    public List<File> getDiffToolsList() throws PropertiesException, IOException {
        File diffToolsDir = new File(getTestResourcesRoot(), "Diff-Tools");
        if (!diffToolsDir.exists()) {
            diffToolsDir = new File(getTestResourcesRoot().getParentFile(), "auslieferung/Diff-Tools");
            if (!diffToolsDir.exists()) {
                diffToolsDir = new File(getTestResourcesRoot().getParentFile(), "Diff-Tools");
            }
        }
        if (!diffToolsDir.exists()) {
            throw new PropertiesException("Das Verzeichnis " + diffToolsDir.getAbsolutePath() + " für die Diff-Tools existiert nicht!");
        }
        Path directory = Paths.get(diffToolsDir.getAbsolutePath());
        List<File> diffToolsList = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                File pathFile = path.toFile();
                File exeFile = new File(pathFile, pathFile.getName() + ".exe");
                if (exeFile.exists()) {
                    diffToolsList.add(exeFile);
                }
            }
        }
        return diffToolsList;
    }

    public List<RestInvokerConfig> getRestServiceConfigsList(String strUrls) {
        List<RestInvokerConfig> list = new ArrayList<>();
        String[] urlsArray = strUrls.split(";");
        if (urlsArray[0].startsWith("?")) {
            return list;
        }
        for (String strUrl : urlsArray) {
            String serviceURI;
            String serviceUser = "";
            String servicePassword = "";
            if (strUrl.startsWith("http")) {
                serviceURI = strUrl;
            } else {
                String[] split = strUrl.split("::");
                serviceURI = split[1];
                String[] userPwd = split[0].split("@");
                serviceUser = userPwd[0];
                servicePassword = userPwd.length > 1 ? userPwd[1] : "";
            }
            list.add(new RestInvokerConfig(serviceURI, serviceUser, servicePassword));
        }
        return list;
    }

    public List<RestInvokerConfig> getRestServiceConfigForBIC() throws PropertiesException {
        return getRestServiceConfigsList(getProperty(PROPNAME_JVM_BIC_URLS, true, "http://rhsctem016.ecofis:7083"));
    }

    public List<RestInvokerConfig> getRestServiceConfigsForJvmInso() throws PropertiesException {
        return getRestServiceConfigsList(getProperty(PROPNAME_JVM_INSO_URLS, true, "http://rhsctem016.ecofis.de:7079"));
    }

    public List<RestInvokerConfig> getRestServiceConfigsForJvmInsoBackend() throws PropertiesException {
        return getRestServiceConfigsList(getProperty(PROPNAME_JVM_INSOBACKEND_URLS, true, "http://rhsctem016.ecofis.de:7080"));
    }

    public List<RestInvokerConfig> getRestServiceConfigsForJvmImpCycle() throws PropertiesException {
        return getRestServiceConfigsList(getProperty(PROPNAME_JVM_IMPCYCLE_URLS, true, "http://rhsctem015.ecofis.de:7051"));
    }

    public List<RestInvokerConfig> getRestServiceConfigsForMasterkonsole() throws PropertiesException {
        return getRestServiceConfigsList(getProperty(PROPNAME_MASTERKONSOLE_URLS, true,
                "tesuntestene@tesuntestene::http://rhsctem015.ecofis.de:7077;tesuntestene@tesuntestene::http://localhost:7001"));
    }

    public List<RestInvokerConfig> getRestServiceConfigsForBatchGUI() throws PropertiesException {
        return getRestServiceConfigsList(getProperty(PROPNAME_BATCH_GUI_URLS, true, "http://rhsctem015.ecofis.de:7071"));
    }

    public List<RestInvokerConfig> getRestServiceConfigsForActiviti() throws PropertiesException {
        return getRestServiceConfigsList(getProperty(PROPNAME_ACTIVITI_URLS, true,
                "CAVDARK-ENE@cavdark::http://NB10007268:9999;CAVDARK-ENE@cavdark::http://rhsctew003.ecofis.de:9999"));
    }

    public String getDhlToVvcSftpPath() throws PropertiesException {
        return getProperty(PROPNAME_DHL_TO_VVC_SFTP_PATH, true, "sftp_upload/dhl/abnahme/dhl_to_vvc");
    }

    public String getVvcToDhlSftpPath() throws PropertiesException {
        return getProperty(PROPNAME_VVC_TO_DHL_SFTP_PATH, true, "sftp_upload/dhl/abnahme/vvc_to_dhl");
    }

    public RestInvokerConfig getDhlExportSftpHost() throws PropertiesException {
        String url = getProperty(PROPNAME_DHL_EXPORT_SFTP_HOST, true, "ctcb:Consumer00Horst@rhsctem015.ecofis.de");
        String[] hp = url.split("@");
        String[] up = hp[0].split(":");
        return new RestInvokerConfig(hp[1] + ":22", up[0], up[1]);
    }

    public String getDhlExportPrefix() throws PropertiesException {
        return getProperty(PROPNAME_DHL_EXPORT_PREFIX, true, "alle_exporte/bic/export/delta");
    }

    public RestInvokerConfig getDhlUploadSftpHost() throws PropertiesException {
        String url = getProperty(PROPNAME_DHL_UPLOAD_SFTP_HOST, true, "ctcb:Consumer00Horst@rhsctem011.ecofis.de");
        String[] hp = url.split("@");
        String[] up = hp[0].split(":");
        return new RestInvokerConfig(hp[1] + ":22", up[0], up[1]);
    }

    public List<String> getAvailableCustomersFromProperties() throws PropertiesException {
        String customers = getProperty(PROPNAME_AVAILABLE_CUSTOMERS, true,
                "bdr,bvd,cef,crm,dfo,drd,eh,foo,fsu,fw,gdl,ika,inso_kundenplz,inso_test-tool,ism,mic,mip,nim,pni,ppa,rtn,SDF_DAILY,SDF_MONTHLY,SDF_WEEKLY,trdi,vsd,vsh,vso,zew");
        List<String> customersList = new ArrayList<>();
        for (String s : customers.split(DELIMITER)) {
            customersList.add(s.toUpperCase());
        }
        return customersList;
    }

    public Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> getCustomerTestInfoMapMap() throws Exception {
        Map<TestSupportClientKonstanten.TEST_PHASE, Map<String, TestCustomer>> mapMap = new TreeMap<>();
        for (TestSupportClientKonstanten.TEST_PHASE testPhase : TestSupportClientKonstanten.TEST_PHASE.values()) {
            mapMap.put(testPhase, getCustomerTestInfoMap(testPhase));
        }
        return mapMap;
    }

    public Map<String, TestCustomer> getCustomerTestInfoMap(TestSupportClientKonstanten.TEST_PHASE testPhase) throws Exception {
        File itsqRefExportsPhaseXFile = getItsqRefExportsRoot(testPhase);
        Collection<File> fileCollection = FileUtils.listFiles(itsqRefExportsPhaseXFile, new String[]{"xml"}, true);
        List<File> archivBestandXmlFilesList = fileCollection.stream().collect(Collectors.toList());
        File testOutputsFile = getTestOutputsRoot();
        Map<String, TestCustomer> customerTestInfoMap = new TreeMap<>();
        List<String> availableCustomers = getAvailableCustomersFromProperties();
        for (String customerKey : availableCustomers) {
            File theFile = new File(itsqRefExportsPhaseXFile, customerKey.toLowerCase(Locale.ROOT));
            if (theFile.exists()) {
                TestCustomer testCustomer = buildTestCustomer(customerKey, testPhase, testOutputsFile);
                initTestScenarios(testCustomer, archivBestandXmlFilesList);
                customerTestInfoMap.put(customerKey, testCustomer);
            }
        }
        return customerTestInfoMap;
    }

    private void initTestScenarios(TestCustomer testCustomer, List<File> archivBestandXmlFilesList) {
        File itsqRefExportsFile = testCustomer.getItsqRefExportsDir();
        File[] subdirs = itsqRefExportsFile.listFiles(File::isDirectory);
        if (subdirs == null) return;
        for (File subdir : subdirs) {
            try {
                TestScenario testScenario = new TestScenario(testCustomer, subdir.getName(), archivBestandXmlFilesList);
                testCustomer.addTestScenario(testScenario);
            } catch (Exception ex) {
                testCustomer.addResultInfo("INIT_CUSTOMERS",
                        new TestResults.ResultInfo("\n\t\tException beim Initialisieren aus dem ITSQ-Test-Paket!\n" + ex.getMessage()));
            }
        }
    }

    private TestCustomer buildTestCustomer(String customerKey, TestSupportClientKonstanten.TEST_PHASE testPhase, File testOutputsFile) throws PropertiesException {
        TestCustomer testCustomer = new TestCustomer(customerKey, getTestResourcesDir(), testOutputsFile, testPhase);
        String propName = PROPNAME_CUSTOMER + customerKey.toUpperCase();
        String customerInfo = getProperty(propName, true, "TODO!!!!");
        String[] split = customerInfo.split(DELIMITER);
        if (split.length < 4) {
            throw new PropertiesException("Wert für " + propName + " wurde nicht oder unpassend gesetzt!\n<Kundenname>;<JVM-Name>;<Exportjobname>;<VC-List-Masterkonsole-Propertyname>");
        }
        testCustomer.setCustomerName(split[0].trim());
        testCustomer.setJvmName(split[1].trim());
        testCustomer.setExportJobName(split[2].trim());
        testCustomer.setUploadJobName(split[3].trim());
        if (split.length > 4) testCustomer.setProcessIdentifier(split[4].trim());
        if (split.length > 5) testCustomer.setCustomerPropertyPrefix(split[5].trim());
        return testCustomer;
    }

    public JobInfo getJobInfoForImportCycle() throws PropertiesException {
        return new JobInfo(getProperty(PROPNAME_JOB_NAME_IMPORT_CYCLE, true, "IMPORTCYCLE;importcycle.importCycle;BETEILIGUNGEN_IMPORT,ENTSCHEIDUNGSTRAEGER_BERECHNUNG,BTLG_UPDATE_TRIGGER,FROM_STAGING_INTO_CTE"));
    }

    public JobInfo getJobInfoForBtlgImport() throws PropertiesException {
        return new JobInfo(getProperty(PROPNAME_JOB_NAME_BTLG_IMPORT_DELTA, true, "IMPORTCYCLE;importcycle.beteiligungenImportDelta;BETEILIGUNGEN_IMPORT"));
    }

    public JobInfo getJobInfoForEntgBerechnung() throws PropertiesException {
        return new JobInfo(getProperty(PROPNAME_JOB_NAME_ENTG_BERECHNUNG, true, "IMPORTCYCLE;importcycle.entgBerechnung;ENTSCHEIDUNGSTRAEGER_BERECHNUNG"));
    }

    public JobInfo getJobInfoForBtlgAktualisierung() throws PropertiesException {
        return new JobInfo(getProperty(PROPNAME_JOB_NAME_BTLG_UPDATE_TRIGGER, true, "IMPORTCYCLE;importcycle.btlnAktualisierung;BTLG_UPDATE_TRIGGER"));
    }

    public JobInfo getJobInfoForCtImport() throws PropertiesException {
        return new JobInfo(getProperty(PROPNAME_JOB_NAME_CT_IMPORT_DELTA, true, "IMPORTCYCLE;importcycle.ctImportDelta;FROM_STAGING_INTO_CTE"));
    }

    public String getCurrentEnvName() { return currentEnvName; }

    public File getTestResourcesRoot() { return testResourcesRoot; }

    public long getMillisForImportCycleTimeOut() throws PropertiesException {
        return calculateTimeInMillis(getProperty(PROPNAME_IMPORT_CYCLE_TIME_OUT, true, "PT60M"));
    }

    public long getMillisForJobStatusQuerySleepTime() throws PropertiesException {
        return calculateTimeInMillis(getProperty(PROPNAME_JOBSTATUS_QUERY_SLEEPTIME, true, "PT2S"));
    }

    public long getMillisForExportsTimeOut() throws PropertiesException {
        return calculateTimeInMillis(getProperty(PROPNAME_EXPORTS_TIME_OUT, true, "PT20M"));
    }

    public String getStrTimeBeforeCollectExports(boolean isDemoMode) throws PropertiesException {
        return isDemoMode ? "PT1S" : getProperty(TIME_BEFORE_EXPORTS_COLLECT, true, "PT30S");
    }

    public long getMillisBeforeCollectExports(boolean isDemoMode) throws PropertiesException {
        return calculateTimeInMillis(getStrTimeBeforeCollectExports(isDemoMode));
    }

    public String getStrTimeBeforeCollectSftpUploads(boolean isDemoMode) throws PropertiesException {
        return isDemoMode ? "PT1S" : getProperty(TIME_BEFORE_SFTP_COLLECT, true, "PT30S");
    }

    public long getMillisBeforeCollectSftpUploads(boolean isDemoMode) throws PropertiesException {
        return calculateTimeInMillis(getStrTimeBeforeCollectSftpUploads(isDemoMode));
    }

    public String getStrTimeBeforeBtlgImport(boolean isDemoMode) throws PropertiesException {
        return isDemoMode ? "PT1S" : getProperty(PROPNAME_TIME_BEFORE_BTLG_IMPORT, true, "PT6M");
    }

    public long getMillisBeforeBtlgImport(boolean isDemoMode) throws PropertiesException {
        return calculateTimeInMillis(getStrTimeBeforeBtlgImport(isDemoMode));
    }

    public String getStrTimeBeforeExport(boolean isDemoMode) throws PropertiesException {
        return isDemoMode ? "PT1S" : getProperty(PROPNAME_TIME_BEFORE_EXPORT, true, "PT6M");
    }

    public String getStrTimeBeforeInsoExports(boolean isDemoMode) throws PropertiesException {
        return isDemoMode ? "PT1S" : getProperty(PROPNAME_TIME_BEFORE_INSO_EXPORT, true, "PT15S");
    }

    public long getMillisBeforeExports(boolean isDemoMode) throws PropertiesException {
        return calculateTimeInMillis(getStrTimeBeforeExport(isDemoMode));
    }

    public String getStrTimeBeforeCtImport(boolean isDemoMode) throws PropertiesException {
        return isDemoMode ? "PT1S" : getProperty(PROPNAME_TIME_BEFORE_CT_IMPORT, true, "PT30S");
    }

    public long getMillisBeforeCtImport(boolean isDemoMode) throws PropertiesException {
        return calculateTimeInMillis(getStrTimeBeforeCtImport(isDemoMode));
    }

    public boolean useLocalExports() throws PropertiesException {
        return getBooleanPropertyValue(PROPNAME_USE_LOCAL_EXPORTS, false, Boolean.FALSE);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<TestSupportClientKonstanten.TEST_TYPES> getTestTypes() throws PropertiesException {
        List<TestSupportClientKonstanten.TEST_TYPES> testTypesList = new TreeList();
        String testTypes = getProperty(PROPNAME_TEST_TYPES, true, "KEINE!TODO!");
        for (String item : testTypes.split(DELIMITER)) {
            testTypesList.add(TestSupportClientKonstanten.TEST_TYPES.valueOf(item));
        }
        return testTypesList;
    }

    public List<String> getTestSetSources() throws PropertiesException {
        String testTypes = getProperty(PROPNAME_TEST_SOURCES, true, TestSupportClientKonstanten.DEFAUL_TESTS_SOURCE);
        return new ArrayList<>(Arrays.asList(testTypes.split(DELIMITER)));
    }

    private long calculateTimeInMillis(String strTime) {
        String strTemp = strTime.substring(2, strTime.length() - 1);
        long time = Long.parseLong(strTemp);
        String strEinheit = strTime.substring(strTime.length() - 1);
        if (strEinheit.equals("S")) {
            time *= 1000;
        } else if (strEinheit.equals("M")) {
            time *= 60 * 1000;
        } else if (strEinheit.equals("H")) {
            time *= 3600 * 1000;
        }
        return time;
    }

    public String getCteVersion() { return cteVersion; }

    public void setCteVersion(String strCteVersion) {
        String str = strCteVersion.replaceAll("_", ".");
        if (str.startsWith("MOCK")) {
            str = str.substring(str.indexOf("Version") + "Version".length() + 1);
            str = str.replace("-SNAPSHOT", "");
        }
        cteVersion = str;
    }

    public void setTestToolVersion(String testToolVersion) { this.testToolVersion = testToolVersion; }

    public String getTestToolVersionLong() { return testToolVersion; }

    public String getTestToolVersion() {
        if (testToolVersion != null) {
            return testToolVersion.split("#")[0];
        }
        return "";
    }

    public String getActivitProcessKey() { return getCurrentEnvName(); }

    public TestSupportClientKonstanten.TEST_TYPES getLastTestType() { return lastTestType; }
    public void setLastTestType(TestSupportClientKonstanten.TEST_TYPES lastTestType) { this.lastTestType = lastTestType; }

    public String getLastRepositoryUserName() { return lastRepositoryUserName; }
    public String getLastRepositoryUserPassword() { return lastRepositoryUserPassword; }

    public int getLastWindowWidth() { return lastWindowWidth == 0 ? (lastWindowWidth = 800) : lastWindowWidth; }
    public void setLastWindowWidth(int lastWindowWidth) { this.lastWindowWidth = lastWindowWidth; }

    public void setLastWindowHeight(int lastWindowHeight) { this.lastWindowHeight = lastWindowHeight; }
    public int getLastWindowHeight() { return lastWindowHeight == 0 ? (lastWindowHeight = 600) : lastWindowHeight; }

    public int getLastWindowXPos() { return lastWindowXPos == 0 ? (lastWindowXPos = 100) : lastWindowXPos; }
    public void setLastWindowXPos(int lastWindowXPos) { this.lastWindowXPos = lastWindowXPos; }

    public int getLastWindowYPos() { return lastWindowYPos == 0 ? (lastWindowYPos = 100) : lastWindowYPos; }
    public void setLastWindowYPos(int lastWindowYPos) { this.lastWindowYPos = lastWindowYPos; }

    public String getLastLookAndFeelClass() { return lastLookAndFeelClass; }
    public void setLastLookAndFeelClass(String lastLookAndFeelClass) { this.lastLookAndFeelClass = lastLookAndFeelClass; }

    public void setLastTestSource(String lastTestSource) { this.lastTestSource = lastTestSource; }
    public String getLastTestSource() { return lastTestSource; }

    public void setLastItsqRevision(String lastItsqRevision) { this.lastItsqRevision = lastItsqRevision; }
    public String getLastItsqRevision() { return lastItsqRevision; }

    public String getLastLoadPath() { return lastLoadPath; }
    public void setLastLoadPath(String lastLoadPath) { this.lastLoadPath = lastLoadPath; }

    public boolean isLastUseOnlyTestClz() { return lastUseOnlyTestClz; }
    public void setLastUseOnlyTestClz(boolean lastUseOnlyTestClz) { this.lastUseOnlyTestClz = lastUseOnlyTestClz; }

    public boolean isLastUploadSynthetics() { return lastUploadSynthetics; }
    public void setLastUploadSynthetics(boolean lastUploadSynthetics) { this.lastUploadSynthetics = lastUploadSynthetics; }

    public void setLastCfgFileNamesList(List<String> lastCfgFileNamesList) { this.lastCfgFileNamesList = lastCfgFileNamesList; }
    public List<String> getLastCfgFileNamesList() { return lastCfgFileNamesList; }
    public String getLastCfgFileNamesListAsString() { return String.join(";", lastCfgFileNamesList); }

    public void setLastCfgFileName(String lastCfgFileName) { this.lastCfgFileName = lastCfgFileName; }
    public String getLastCfgFileName() { return lastCfgFileName; }

    public void updateEnvironmentConfig() throws IOException {
        File destFile = new File(environmentConfigFile.getAbsolutePath() + ".old");
        FileUtils.copyFile(environmentConfigFile, destFile);
        List<String> envConfigLines = FileUtils.readLines(environmentConfigFile, "UTF-8");
        List<String> lines = envConfigLines.stream().filter(l -> !l.startsWith("LAST_")).collect(Collectors.toList());
        lines.add(PROPNAME_LAST_TEST_SOURCE + " = " + getLastTestSource());
        lines.add(PROPNAME_LAST_ITSQ_REVISION + " = " + getLastItsqRevision());
        lines.add(PROPNAME_LAST_LOAD_PATH + " = " + getLastLoadPath());
        lines.add(PROPNAME_LAST_CFG_FILENAMES_LIST + " = " + getLastCfgFileNamesListAsString());
        lines.add(PROPNAME_LAST_CFG_FILENAME + " = " + getLastCfgFileName());
        lines.add(PROPNAME_LAST_UPLOAD_SYNTHETICS + " = " + isLastUploadSynthetics());
        lines.add(PROPNAME_LAST_USE_ONLY_TEST_CLZ + " = " + isLastUseOnlyTestClz());
        lines.add(PROPNAME_LAST_WINDOW_HEIGHT + " = " + getLastWindowHeight());
        lines.add(PROPNAME_LAST_WINDOW_WIDTH + " = " + getLastWindowWidth());
        lines.add(PROPNAME_LAST_WINDOW_X_POS + " = " + getLastWindowXPos());
        lines.add(PROPNAME_LAST_WINDOW_Y_POS + " = " + getLastWindowYPos());
        lines.add(PROPNAME_LAST_LOOK_AND_FEEL_CLASS + " = " + getLastLookAndFeelClass());
        FileUtils.writeLines(environmentConfigFile, "UTF-8", lines);
    }

    public String getVersionFromBuildInfo() {
        Properties props = new Properties();
        String version = "?.?.?";
        String buildDate = "?:?:?";
        String resourcePath = "/META-INF/buildinfo/testsupport-tool/buildinfo.properties";
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is != null) {
                props.load(is);
                version = props.getProperty("version", "?.?.?");
                buildDate = props.getProperty("build.date", "?:?:?");
            } else {
                try (InputStream isRoot = getClass().getResourceAsStream("/buildinfo.properties")) {
                    if (isRoot != null) {
                        props.load(isRoot);
                        version = props.getProperty("version", "?.?.?");
                        buildDate = props.getProperty("build.date", "?:?:?");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Fehler beim Laden der Build-Infos: " + ex.getMessage());
        }
        return String.format("Version: %s, Build: %s", version, buildDate);
    }
}
