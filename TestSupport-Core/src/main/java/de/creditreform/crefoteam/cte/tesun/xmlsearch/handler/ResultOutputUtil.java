package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

public class ResultOutputUtil {

   protected static Charset ioCharset = Charset.forName("UTF-8");

   private final Logger logger;
   private final File resultsBaseDir;
   private final String subDirName;

   public ResultOutputUtil(Logger logger, File resultsBaseDir, String subDirName) {
      this.logger = logger;
      this.resultsBaseDir = resultsBaseDir;
      this.subDirName = subDirName;
   }

   /**
    * erstelle ein {@link File}-Objekt zu dem angegebenen Verzeichnis innerhalb von parent
    */
   public File childOf(File parent, String nameOfChild) {
      final File child;
      if (nameOfChild==null) {
         child = parent;
      }
      else if (parent==null) {
         child = new File(nameOfChild);
      }
      else {
         child = new File(parent, nameOfChild);
      }
      return child;
   }

   public OutputStream createOutputStream(String fileName, boolean append)
   throws FileNotFoundException {
      final File outDir= childOf(resultsBaseDir, subDirName);
      if( !outDir.exists() )
      {
         outDir.mkdirs();
      }
      final File outFile = childOf(outDir, fileName);
      logger.info("Teil-Ergebnis aus dem Abgleich der Crefonummern in der Datei: {}", outFile.getAbsolutePath());
      return new BufferedOutputStream(new FileOutputStream(outFile, append), 2048 );
   }

   public Writer prepareWriter(List<Writer> writerList, OutputStream os, String headerMessage)
   throws IOException {
      OutputStreamWriter osw = new OutputStreamWriter(os, ioCharset);
      osw.write(headerMessage);
      writerList.add(osw);
      return osw;
   }

   public void flushWriters(List<Writer> writerList) throws IOException {
      for (Writer w : writerList) {
         w.flush();
      }
   }

}
