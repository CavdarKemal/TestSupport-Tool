package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IMatchInfoListener;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.ISavedStreamContent;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IZipEntryInfo;

import java.io.File;

/**
 * Abstrakte Basis-Klasse für die Implementierungen von {@link IMatchInfoListener}
 */
public abstract class AbstractMatchInfoListener
implements IMatchInfoListener {

   private final String optName;

   public AbstractMatchInfoListener(String optName) {
      if (optName!=null) {
         this.optName = optName;
      }
      else {
         this.optName = getClass().getSimpleName();
      }
   }

   public String getOptName()
   {
     return optName;
   }

   protected File getBaseDir(String searchResultsPath, String searchConfigurationName) {
      File parentDir = childOf(null, searchResultsPath);
      File childDir = childOf(parentDir, searchConfigurationName);
      if (childDir!=null && !childDir.exists()) {
         childDir.mkdirs();
      }
      return childDir;
   }

   protected File childOf(File parent, String nameOfChild) {
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

   @Override
   public void notifyEntryNotMatched(ISavedStreamContent savedStreamContent, IZipEntryInfo zipEntryInfo) throws Exception {
      // intentionally empty
   }

}
