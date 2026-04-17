package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import java.util.Arrays;
import java.util.List;

public class XmlMatcherLogicOr
extends XmlMatcherLogicAbstract<XmlMatcher>
{

  public XmlMatcherLogicOr( XmlMatcher... matcherArray )
  {
    this( Arrays.asList( matcherArray ) );
  }

  public XmlMatcherLogicOr( List<XmlMatcher> matcherList )
  {
    super( matcherList );
  }

  /*********************************************************************************************/
  /*****************************         XmlMatcherAbstract         ****************************/
  /*********************************************************************************************/
  @Override public boolean isSatisfied()
  {
    for( XmlMatcher xm : getChildXmlMatchers() )
    {
      if( xm.isSatisfied() )
      {
        return true;
      }
    }
    return false;
  }

}
