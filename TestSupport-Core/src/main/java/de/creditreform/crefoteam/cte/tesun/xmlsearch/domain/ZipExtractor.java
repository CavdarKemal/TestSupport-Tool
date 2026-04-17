package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor {

   public List<File> extractFilesFromZip(File zipFile, FileFilter clzZipFileFilter) throws IOException {
      List<File> zipFilesList = new ArrayList<>();

      final File tmpDir = new File(zipFile.getAbsolutePath().replace(".zip", ""));
      tmpDir.mkdirs();

      FileInputStream fileInputStream = new FileInputStream(zipFile);
      BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
      ZipInputStream zipInputStream = new ZipInputStream(bufferedInputStream);
      File theFile = null;
      try {
         ZipEntry zipEntry = zipInputStream.getNextEntry();
         while (zipEntry != null) {
            theFile = new File(tmpDir, zipEntry.getName());
            theFile.getParentFile().mkdirs();
            if ((clzZipFileFilter == null) || clzZipFileFilter.accept(theFile)) {
               extractZipEntry(zipInputStream, theFile);
               zipFilesList.add(theFile);
            }
            zipEntry = zipInputStream.getNextEntry();
         }
      }
      catch (Exception ex) {
         System.out.println("Exception bei " + theFile.getAbsolutePath());
         ex.printStackTrace();
      }
      finally {
         zipInputStream.close();
      }
      return zipFilesList;
   }

   public void extractZipEntry(ZipInputStream zipInputStream, File theFile) throws IOException {
      FileOutputStream fileOutputStream = new FileOutputStream(theFile);
      byte[] buffer = new byte[102400];
      int count;
      while ((count = zipInputStream.read(buffer)) != -1) {
         fileOutputStream.write(buffer, 0, count);
      }
      fileOutputStream.close();
   }

}
