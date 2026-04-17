package de.creditreform.crefoteam.cte.tesun.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/** Literal-Port aus {@code testsupport_client.tesun_util}. */
public interface TestSupportClientKonstanten {
    Pattern XML_FILES_PATTERN = Pattern.compile(".*\\.xml", Pattern.CASE_INSENSITIVE);
    Pattern PDF_FILES_PATTERN = Pattern.compile(".*\\.pdf", Pattern.CASE_INSENSITIVE);
    String EMAIL_HOST_NAME = "smtprelay.creditreform.de";
    int EMAIL_PORT = 25;
    String TEST_CLZ_412 = "412";
    String TEST_CLZ_912 = "912";
    String TEST_CLZ_918 = "918";
    int[] TEST_CLZ_LIST = new int[]{Integer.parseInt(TEST_CLZ_412), Integer.parseInt(TEST_CLZ_912), Integer.parseInt(TEST_CLZ_918)};
    String ALGORITHM_SSH_RSA = "ssh-rsa";
    String DEFAUL_TESTS_SOURCE = "ITSQ";
    TEST_TYPES DEFAUL_TESTS_TYPE = TEST_TYPES.PHASE1_AND_PHASE2;
    String TESTS_SOURCE_LOCAL = "LOCAL";
    String TESTS_SOURCE_LOCAL_S = "LOCAL-S";
    String DEFAULT_ITSQ_REVISION = "ZWEI_PHASEN";
    String ERRORS_TXT = "errors.txt";
    String MAPPINGCOVERAGE_TEST_CREFOS = "mappingcoverage-test-crefos";
    String AB2_0_XSD = "http://www.creditreform.de/crefoteam/archivbestandv2_0";
    String AB3_0_XSD = "http://www.creditreform.de/crefoteam/archivbestandv3_0";
    Pattern DATETIME_PATTERN_SFTP = Pattern.compile("\\d{4}\\.\\d{2}\\.\\d{2}_\\d{2}\\.\\d{2}");
    Pattern DATETIME_PATTERN_SFTP_EH = Pattern.compile("\\d{4}_\\d{2}_\\d{2}_\\d{2}_\\d{2}");
    Pattern CREFONUMMER_PATTERN = Pattern.compile("\\d{10}");
    Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;
    String LOW_THRESHOLD_LOGGER = "de.creditreform.crefoteam.info";
    String OPT_VALUE_NONE = "none";
    String JOB_TRACE_ID = "JobTraceId";
    String TEST_INFOS_FILENAME = "TestInfos.txt";
    String OPTIONS_CONFIG_FILENAME = "Options.cfg";
    String FW_CONFIG_OVERVIEW_FILENAME = "FWConfigOverview.csv";
    String COLLECTED_INFOS_FILENAME = "CollectedInfos.txt";
    String CHECKEDED_INFOS_FILENAME = "CheckedInfos.txt";
    String UPLOADED_INFOS_FILENAME = "UploadedInfos.txt";
    String TEST_CREFOS_PROPS_FILENAME = "TestCrefos.properties";
    String USED_CREFOS_PROPS_FILENAME = "UsedTestCrefos.properties";
    String EXTENDED_CREFOS_PROPS_FILENAME = "ExtendedTestCrefos.properties";
    String CREFOS_TO_CUSTOMERS_MAP_FILENAME = "CrefosToCustomersMap.txt";
    String fileNameCrefosFuerNachlieferung = "CrefosFuerNachlieferung.txt";
    String fileNameCrefosMapping = "CrefosMapping.txt";
    String fileNameExportProtokollResults = "ExportProtokollResults.txt";
    String ADDITIONAL_INFO_FILENAME_POSTFIX = "_Testfallbeschreibung.txt";
    String ADDITIONAL_BIC_FILENAME_PREFIX = "BIC_Imports_";
    String COLUMN_DELIMITER = ";";
    String SUPPORTED_EXPORT_MATCHER = ".*\\.xml|.*\\.csv";
    String CREFO_XML_FILE_MATCHER = ".*\\d{10}.xml";
    String CREFO_TXT_FILE_MATCHER = ".*\\d{10}.txt";

    enum TEST_TYPES {
        PHASE1_AND_PHASE2("PHASE-1 und PHASE-2");

        private final String description;

        TEST_TYPES(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    enum TEST_PHASE {
        PHASE_1("PHASE-1"),
        PHASE_2("PHASE-2");
        private final String dirName;

        TEST_PHASE(String dirName) {
            this.dirName = dirName;
        }

        public String getDirName() {
            return dirName;
        }
    }

    String ARCHIV_BESTAND = "ARCHIV-BESTAND";
    String TEST_RESULTS = "TEST-RESULTS";
    String PSEUDO_ARCHIV_BESTAND = "PSEUDO-ARCHIV-BESTAND";
    String NEW_TEST_CASES = "NEW-TEST-CASES";
    String REF_EXPORTS = "REF-EXPORTS";
    String PSEUDO_REF_EXPORTS = "PSEUDO-REF-EXPORTS";
    String COLLECTED = "COLLECTED";
    String SFTP_UPLOADS = "SFTP-UPLOADS";
    String RESTORED_COLLECTS = "RESTORED-COLLECTS";
    String CHECKED = "CHECKED";
    String TEST_OUTPUTS = "TEST-OUTPUTS";
    String EXPORTS = "EXPORTS";

    String UPLOAD_EMPTY_PAYLOAD = "UPLOAD_EMPTY_PAYLOAD";
    String OLD_VC_LIST = "OLD_VC-LIST";
    String NEW_VC_LIST = "NEW_VC-LIST";
    String OPT_IGNORABLE_XPATHS = "IGNORABLE-XPATHS";

    String IMPORT_STARTET_AT = "Startzeit ImportCycle";
    String BTLG_IMPORT_STARTET_AT = "Startzeit Beteiligten-Import";
    String ENTG_BERECHNUNG_STARTET_AT = "Startzeit ENTG-Berechnung";
    String BTLG_UPDATE_TRIGGER_STARTET_AT = "Startzeit Beteiligten-Aktualisierung";
    String CT_IMPORT_STARTET_AT = "Startzeit CT-Import";
    String LAST_COMPLETITION_TIME = "Letzte Export-Completition-Zeit";
    String OPT_DIRECTORY_COMPARE_PROPS = "DIRECTORY_COMPARE_PROPS";
    String OPT_DIRECTORY_TO_COMPARE_FIRST = "DIRECTORY_TO_COMPARE_FIRST";
    String OPT_DIRECTORY_TO_COMPARE_SECOND = "DIRECTORY_TO_COMPARE_SECOND";
    String OPT_DIRECTORY_COMPARE_RESULTS = "DIRECTORY_COMPARE_RESULTS";
    String OPT_COMPARE_EXTRACT_FIRST = "COMPARE_EXTRACT_FIRST";
    Pattern CREFO_NUMBER_PATTERN = Pattern.compile("\\d{10}.xml");
    String[] LOESCHSATZ_FILENAMES_PREFIX = new String[]{"loesch", "delet", "stopmessage"};

    String PROP_NAME_XML_SECTION_DEFAULT = "DEFAULT";
    String PROP_NAME_XML_STREAM_PROCESSOR = "XML_STREAM_PROCESSOR";
    String PROP_NAME_XML_SEARCH_SOURCE = "SOURCE";
    String PROP_NAME_XML_SEARCH_CRITERIA = "SEARCH";
    String PROP_NAME_XML_CREFO_TAGNAME = "CREFO_TAGNAME";
    String PROP_NAME_LOGICAL_CONNECTION = "LOGICAL_CONNECTION";
    String PROP_NAME_XML_INVERT_RESULTS = "INVERT_RESULTS";
    String PROP_NAME_XML_RESULT_TYPE = "RESULT_TYPE";

    enum XML_STREAM_PROCESSOR {RECURSIVE, LINEAR}

    enum SEARCH_RESULT_TYPE {
        CREFOS_COUNT("count", "speichere die XML-Datei zu jedem Suchergebnis"),
        CREFOS_LIST("list", "speichere die Crefo-Nummer zu jedem Suchergebnis"),
        CREFOS_XML("xml", "berechne die Anzahl der Suchergebnisse");
        private final String identifier;
        private final String description;

        SEARCH_RESULT_TYPE(final String identifier, String description) {
            this.identifier = identifier;
            this.description = description;
        }

        public String getIdentifier() { return identifier; }
        public String getDescription() { return description; }
    }

    enum LOGICAL_CONNECTION {
        LOGIC_OR,
        LOGIC_AND
    }

    String CHECK_REF_EXPORTS_COMMAND = "UserTask CHECK-REF-EXPORTS";
    String CHECK_COLLECTS_COMMAND = "UserTask CHECK-COLLECTS";

    String OPT_PATH_CRITERIA = "kriterien";
    String OPT_MUTABLE_TARGET_VC = "generatorTargetVC";
    String OPT_MUTABLE_NEXT_CREFO = "generatorNextCrefo";
    String HEADER_PARAM_DATE_PATTERN = "yyyy-MM-dd HH:mm";
    String HEADER_PARAM_LAST_UPDATE_CT = "letzteAktualisierung";
    String HEADER_PARAM_LAST_UPDATE_CTO = "aktualisierungCto";
    String HEADER_PARAM_IKAROS_RCV_CLZ = "empfaenger";
    String HEADER_PARAM_IKAROS_CREFO = "crefo";
    String QUERY_PARAM_DSGVO_SPERREN = "sperren";
}
