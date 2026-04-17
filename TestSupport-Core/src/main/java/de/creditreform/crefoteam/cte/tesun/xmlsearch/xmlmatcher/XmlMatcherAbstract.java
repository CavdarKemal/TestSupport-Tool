package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IPerEntryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;

public abstract class XmlMatcherAbstract implements XmlMatcher
{
  protected static Logger logger       = LoggerFactory.getLogger( XmlMatcherAbstract.class );
  private final String    xmlTagName;

  public XmlMatcherAbstract( MatcherParameterTag matcherParameterTag )
  {
    if( matcherParameterTag == null )
    {
      this.xmlTagName = null;
    }
    else
    {
      this.xmlTagName = matcherParameterTag.getXmlTagName();
    }
  }

  protected String getXmlTagName()
  {
    return xmlTagName;
  }

   @Override
   public void notifyZipEntryCompleted(boolean success, IPerEntryListener perEntryListener) {
      // intentionally empty
   }

   /**
    * Utility-Klasse für das Erhöhen eines Zählers im Value einer {@link java.util.Map<String,Integer>}
    * siehe {@link java.util.Map#compute(Object, BiFunction)}. Dies erlaubt, insbesondere bei Thread-sicheren
    * bzw. synchronisierten Maps das Setzen oder Erhöhen eines Value mit einem einzigen Zugriff,
    * Das Hochzählen der Values erfolgt mit einer festen Schrittweite, Instanzen dieser Klasse sind thread-safe
    * @param <K> Typ-Parameter für den Map-Key
    */
   public static class CountValuesUpdateFixedFunction<K>
   implements BiFunction<K, Integer, Integer> {
      private int increment;

      public CountValuesUpdateFixedFunction() {
         this(1);
      }

      public CountValuesUpdateFixedFunction(int fixedIncrement) {
         this.increment = fixedIncrement;
      }

      private void setIncrementInternal(int increment) {
         this.increment = increment;
      }

      @Override
      public Integer apply(K key, Integer prevValue) {
         if (prevValue==null) {
            return increment;
         }
         else {
            return prevValue+increment;
         }
      }
   }

   /**
    * Utility-Klasse für das Erhöhen eines Zählers im Value einer {@link java.util.Map<String,Integer>}
    * siehe {@link java.util.Map#compute(Object, BiFunction)}. Das Hochzählen der Values erfolgt mit einer
    * variablen Schrittweite. Im Gegensatz zu {@link CountValuesUpdateFixedFunction} sind Instanzen dieser
    * Klasse _nicht_ thread-safe
    * @param <K> Typ-Parameter für den Map-Key
    */
   public static class CountValuesUpdateVariableFunction<K>
   extends CountValuesUpdateFixedFunction<K> {
      /**
       * setze ein Inkrement größer oder gleich 0
       */
      public void setNonNegativeIncrement(Integer increment) {
         final int newIncrement = (increment!=null && increment>=0) ? increment : 0;
         setAnyIncrement(newIncrement);
      }

      /**
       * setze ein beliebiges Inkrement
       */
      public void setAnyIncrement(int increment) {
         super.setIncrementInternal(increment);
      }

   }

}
