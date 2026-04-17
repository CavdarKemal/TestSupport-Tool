package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.*;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IGroupByRow;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher.XmlMatcher;

import java.util.Map;

/**
 * Package-interne Utility-Klasse für die pro Thread benötigten / Zustands-behafteten Instanzen
 */
class XmlMatcherThreadLocals {

   static class Instances {
      private final XmlMatcherWrapperFactory xmlMatcherWrapperFactory;
      private final XmlMatcher xmlMatcher;
      private final XmlStreamProcessorIF xmlStreamProcessor;
      private final IPerEntryListener perEntryListener;
      private final ProgressListenerIF progressListener;

      public Instances(XmlMatcherWrapperFactory xmlMatcherWrapperFactory, XmlMatcher xmlMatcher, XmlStreamProcessorIF xmlStreamProcessor,
                       IPerEntryListener perEntryListener, ProgressListenerIF progressListener) {
         this.xmlMatcherWrapperFactory = xmlMatcherWrapperFactory;
         this.xmlMatcher = xmlMatcher;
         this.xmlStreamProcessor = xmlStreamProcessor;
         this.perEntryListener = perEntryListener;
         this.progressListener = progressListener;
      }

      public XmlMatcher getXmlMatcher() {
         return xmlMatcher;
      }

      public XmlStreamProcessorIF getXmlStreamProcessor() {
         return xmlStreamProcessor;
      }

      public IPerEntryListener getPerEntryListener() {
         return perEntryListener;
      }

      public ProgressListenerIF getProgressListener() {
         return progressListener;
      }

      public String getResultIdentification() {
         return xmlMatcherWrapperFactory.getResultIdentification(this.xmlMatcher);
      }

      public Map<IGroupByRow, Integer> getGroupByResults() {
         return xmlMatcherWrapperFactory.getGroupByResults(this.xmlMatcher);
      }

   }

   /**
    * Threadlocal für das Speichern der Thread-spezifischen / Zustands-behafteten Instanzen (nicht static!)
    */
   private final ThreadLocal<Instances> threadLocalStorage = new ThreadLocal<>();
   private final RuntimeSearchSpec      runtimeSearchSpec;
   private final XmlMatcherWrapperFactory xmlMatcherWrapperFactory;
   private final XmlStreamListenerGroup listenerGroup;

   public XmlMatcherThreadLocals(RuntimeSearchSpec runtimeSearchSpec,
                                 XmlMatcherWrapperFactory xmlMatcherWrapperFactory,
                                 XmlStreamListenerGroup listenerGroup) {
      this.runtimeSearchSpec = runtimeSearchSpec;
      this.xmlMatcherWrapperFactory = xmlMatcherWrapperFactory;
      this.listenerGroup = listenerGroup;
   }

   Instances get() {
      Instances tl = threadLocalStorage.get();
      if (tl==null) {
         XmlMatcher xmlMatcher = runtimeSearchSpec.buildXmlMatcherSearch(this.xmlMatcherWrapperFactory);
         XmlStreamProcessorIF xmlStreamProcessor = runtimeSearchSpec.createXmlStreamProcessor(xmlMatcher);
         IPerEntryListener perEntryListener = listenerGroup.createPerEntryListener();
         ProgressListenerIF progressListener = listenerGroup.getProgressListener();
         tl = new Instances(xmlMatcherWrapperFactory, xmlMatcher, xmlStreamProcessor,
                            perEntryListener, progressListener);
         threadLocalStorage.set(tl);
      }
      return tl;
   }

}
