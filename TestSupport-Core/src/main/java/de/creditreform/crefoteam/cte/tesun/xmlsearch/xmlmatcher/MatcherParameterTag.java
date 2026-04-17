package de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Container für den Parameter bezüglich eines zu suchenden XML-Tags
 * @author CavdarK
 *
 */
public class MatcherParameterTag
{
  private final String xmlTagName;
  private final List<String> pathOhneTag;

  public MatcherParameterTag( String suchSpezifikation )
  {
    super();
    if (suchSpezifikation==null || suchSpezifikation.trim().isEmpty()) {
      throw new IllegalArgumentException( "Der im XML gesuchte Pfad darf nicht NULL oder \"\" sein" );
    }
    String [] pathElements = suchSpezifikation.split( "\\." );
    List<String> newRequiredPath = new ArrayList<>( pathElements.length - 1 );
    for( int i = 0; i < pathElements.length - 1; i++ )
    {
      newRequiredPath.add( pathElements[i] );
    }
    this.pathOhneTag = Collections.unmodifiableList( newRequiredPath );
    this.xmlTagName = pathElements[pathElements.length-1];
  }

  public String getXmlTagName()
  {
    return xmlTagName;
  }

  public boolean isPathEmpty() {
    return pathOhneTag.isEmpty();
  }

  public List<String> getPathOhneTag()
  {
    return pathOhneTag;
  }

}
