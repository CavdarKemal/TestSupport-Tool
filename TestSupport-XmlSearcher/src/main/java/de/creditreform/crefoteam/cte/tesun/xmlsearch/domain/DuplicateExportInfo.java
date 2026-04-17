package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.util.TesunUtilites;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DuplicateExportInfo {
    private static final Pattern DATETIME_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}"); // 2023-11-17_06-00
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault());
    private final Map<Long, List<File>> crefoToFileMap;

    public DuplicateExportInfo(Map<Long, List<File>> crefoToFileMap) {
        this.crefoToFileMap = crefoToFileMap;
    }

    public Map<Long, List<File>> getCrefoToFileMap() {
        return crefoToFileMap;
    }

    public List<Long> getDuplicateCrefoNummernList() {
        return new ArrayList<>(crefoToFileMap.keySet());
    }

    public static List<Date> createExportDatesFromFileList(List<File> crefoFilesList) {
        List<Date> exportDatesList = new ArrayList<>();
        crefoFilesList.stream().forEach(crefoFile -> {
            // E:\Projekte\CTE\testsupport_client\TestSupportGUI\X-TESTS\ExportSuche\TEST-Results\Analyse\CREFOS_XML\2023-11-17_06-00\abCrefo_2070000000.zip\loeschsatz_2070197338.xml
            exportDatesList.add(parseDateFromFileName(crefoFile.getAbsolutePath()));
        });
        return exportDatesList;
    }

    public static Date parseDateFromFileName(String crefoFilePAth) {
        Matcher matcher = DATETIME_PATTERN.matcher(crefoFilePAth);
        if (matcher.find()) {
            String strFound = matcher.group();
            try {
                Date parsedDate = DATE_FORMATTER.parse(strFound);
                return parsedDate;
            } catch (ParseException ex) {
                throw new IllegalStateException("Datei-Pfad '" + crefoFilePAth + "' ergibt kein gültiges Datum!");
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return dumpCrefoToExportDateMap();
    }

    private String dumpCrefoToExportDateMap() {
        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("\nAnzahl gefundenen Duplikate : " + crefoToFileMap.size());
//        stringBuilder.append("\nCrefonummer;ExportDate;ExportFile");
        crefoToFileMap.keySet().stream().forEach(crefoNr -> {
            stringBuilder.append("\n" + crefoNr + "\n");
            List<File> fileList = crefoToFileMap.get(crefoNr);
            fileList.stream().forEach(xmlFile -> {
                String xmlFileType = getXmlFileType(xmlFile.getName());
                stringBuilder.append(xmlFileType + ";");
                String dateStr = DATE_FORMATTER.format(parseDateFromFileName(xmlFile.getAbsolutePath()));
                stringBuilder.append(dateStr + ";");
                String absolutePath = TesunUtilites.shortPath(xmlFile.getAbsolutePath(), 70);
                stringBuilder.append(absolutePath + "\n");
            });
        });
        return stringBuilder.toString();
    }

    private String getXmlFileType(String name) {
        String xmlFileType = null;
        xmlFileType = name.split("_")[0];
        return xmlFileType;
    }

}
