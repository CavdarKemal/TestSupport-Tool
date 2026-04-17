package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class AnalyseStrategyEHMehrfacheLoeschsaetze extends AnalyseStrategyAbstract {
    MyFileFilter fileFilter;

    public AnalyseStrategyEHMehrfacheLoeschsaetze(Pattern pattern) {
        fileFilter = new MyFileFilter(pattern);
    }

    @Override
    public DuplicateExportInfo doAnalyse(SearchSpecification searchSpecification) throws Exception {
        File sourceFile = new File(searchSpecification.getSearchResultsPath(), searchSpecification.getName());
        sourceFile = new File(sourceFile, searchSpecification.getSearchResultsType().name());
        Map<Long, List<File>> crefoToFileMap = new TreeMap<>();
        try {
            MyFileVisitor myFileVisitor = new MyFileVisitor(fileFilter, crefoToFileMap);
            Files.walkFileTree(Paths.get(sourceFile.getAbsolutePath()), myFileVisitor);
        } catch (Exception ex) {
            throw new IllegalStateException("Exception beim Iterieren des Verzeichnisses\n" + sourceFile.getAbsolutePath(), ex);
        }
        DuplicateExportInfo duplicateExportInfo = new DuplicateExportInfo(filterDuplicates(crefoToFileMap));
        return duplicateExportInfo;
    }

    private Map<Long, List<File>> filterDuplicates(Map<Long, List<File>> crefoToFileMap) {
        Map<Long, List<File>> filteredMap = new TreeMap<>();
        Iterator<Long> iterator = crefoToFileMap.keySet().iterator();
        while (iterator.hasNext()) {
            Long crefoNr = iterator.next();
            List<File> fileList = crefoToFileMap.get(crefoNr);
            if (fileList.size() > 1) {
                filteredMap.put(crefoNr, fileList);
            }
        }
        return filteredMap;
    }
}
