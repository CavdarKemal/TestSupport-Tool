package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.FileVisitResult;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AnalyseStrategyAbstract implements AnalyseStrategyIF {
    @Override
    public abstract DuplicateExportInfo doAnalyse(SearchSpecification searchSpecification) throws Exception;

    protected static class MyFileFilter implements FileFilter {

        private final Pattern pattern;

        MyFileFilter(Pattern pattern) {
            this.pattern = pattern;
        }

        @Override
        public boolean accept(File theFile) {
            Matcher matcher = pattern.matcher(theFile.getName());
            return matcher.find();
        }
    }

    protected static class MyFileVisitor<Path> extends SimpleFileVisitor<Path> {
        final FileFilter fileFilter;
        final Map<Long, List<File>> crefoToFileMap;

        public MyFileVisitor(FileFilter fileFilter, Map<Long, List<File>> crefonummerToFileMap) {
            this.fileFilter = fileFilter;
            this.crefoToFileMap = crefonummerToFileMap;
        }

        @Override
        public FileVisitResult visitFile(Path thePath, BasicFileAttributes attrs) {
            try {
                if (fileFilter.accept(new File(thePath.toString()))) {
                    int indexOfXML = thePath.toString().indexOf(".xml");
                    Long crefoNummer = Long.valueOf(thePath.toString().substring(indexOfXML - 10, indexOfXML));
                    // System.out.println("\t\tCrefo: " + crefoNummer + " -> Datei " + thePath + " wurde gefunden.");
                    List<File> fileList = crefoToFileMap.get(crefoNummer);
                    if (fileList == null) {
                        fileList = new ArrayList();
                        crefoToFileMap.put(crefoNummer, fileList);
                    }
                    fileList.add(new File(thePath.toString()));
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            } finally {
                return FileVisitResult.CONTINUE;
            }
        }
    }

}
