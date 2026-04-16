package de.creditreform.crefoteam.cte.tesun.util;

import java.util.Map;

/**
 * Teilport von {@code testsupport_client.tesun_util.TestFallFileUtil}.
 *
 * <p>Portiert ist nur {@link #dumAllCustomers(Map)} — gebraucht von der GUI
 * (Test-Results Speichern). Die classpath/resource-basierten Lade-Methoden
 * ({@code listFolderContentAsFilenames}, {@code downloadFolderContentFromFolder})
 * und die {@code ReplacementMapping}-Helpers werden bei Bedarf nachgezogen.
 */
public final class TestFallFileUtil {
    public static final String FOLDERNAME_SYNTH_TESTCREFOS = "mappingcoverage-test-crefos";

    private TestFallFileUtil() { }

    public static StringBuilder dumAllCustomers(Map<String, TestCustomer> testCustomerMap) {
        StringBuilder stringBuilderAll = new StringBuilder();
        testCustomerMap.entrySet().forEach(testCustomerEntry -> {
            TestCustomer testCustomer = testCustomerEntry.getValue();
            StringBuilder stringBuilder = new StringBuilder();
            testCustomer.dumpResults(stringBuilder, "\n");
            if (stringBuilder.length() > 0) {
                stringBuilderAll.append(stringBuilder);
            }
        });
        return stringBuilderAll;
    }

    /* CLAUDE_MODE
     * Nicht portiert — folgen bei Bedarf:
     *   public static List<Path> listFolderContentAsFilenames(String folderName, String extension)
     *   public static List<File> downloadFolderContentFromFolder(String folderName, String extension, File outputDir)
     *   public static Map<String, ReplacementMapping> readReplacementMappingFromFile(File mappingsFile)
     *   public static Map<String, ReplacementMapping> swapReplacementMapping(Map<String, ReplacementMapping>)
     *   public static List<Long> readCrefosFromResourceFile(File sourceFile)
     * (siehe Original testsupport_client.tesun_util.TestFallFileUtil)
     */
}
