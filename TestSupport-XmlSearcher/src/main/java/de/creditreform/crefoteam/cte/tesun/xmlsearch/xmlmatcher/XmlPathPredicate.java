package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.XmlSearchCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class XmlPathPredicate
implements Predicate<XmlSearchCursor> {

   private static final Logger logger = LoggerFactory.getLogger(XmlPathPredicate.class);

   private final List<String> requiredPath;

   public XmlPathPredicate(MatcherParameterTag matcherParameterTag) {
      this(matcherParameterTag.getPathOhneTag());
   }

   public XmlPathPredicate(List<String> requiredPath) {
      this.requiredPath = new ArrayList<>(requiredPath);
   }

   public boolean isPathMatch( List<String> actualPath )
   {
      int r = requiredPath.size();
      int a = actualPath.size();
      while( r > 0 )
      {
         r--;
         a--;
         if( a < 0 )
         {
            return false;
         }
         String requiredPathR = requiredPath.get( r );
         String actualPathA = actualPath.get( a );
         logger.debug( "Prüfe Pfad: {} <--> {}", requiredPathR, actualPathA );
         if( !requiredPathR.equals( actualPathA ) )
         {
            return false;
         }
      }
      return true;
   }

   @Override
   public boolean test(XmlSearchCursor xmlSearchCursor) {
      return isPathMatch( xmlSearchCursor.getPathsList() );
   }

}
