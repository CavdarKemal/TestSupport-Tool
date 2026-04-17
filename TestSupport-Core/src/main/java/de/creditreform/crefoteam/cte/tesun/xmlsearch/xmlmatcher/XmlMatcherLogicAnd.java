package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import java.util.Arrays;
import java.util.List;

public class XmlMatcherLogicAnd
extends XmlMatcherLogicAbstract<XmlMatcher>
{

  public XmlMatcherLogicAnd( XmlMatcher... matcherArray )
  {
    this( Arrays.asList( matcherArray ) );
  }

  public XmlMatcherLogicAnd( List<XmlMatcher> matcherList )
  {
    super( matcherList );
  }

  /*********************************************************************************************/
  /*****************************     XmlMatcherLogicAbstract        ****************************/
  /*********************************************************************************************/
  @Override public boolean isSatisfied()
  {
    for( XmlMatcher xm : getChildXmlMatchers() )
    {
      boolean satisfied = xm.isSatisfied();
      if( !satisfied )
      {
        return false;
      }
    }
    return true;
  }

}
