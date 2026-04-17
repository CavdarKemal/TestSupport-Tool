package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import java.util.Objects;

public class SearchCriteria
{
  public static String XML_MATCHER_ONE_OF_LIST_TAG_NAME = "file::";
  public static String XML_MATCHER_STATISTICS_TAG_NAME = "STATISTICS";
  private boolean isActivated = true;
  private boolean dirty;
  private String  searchTag;
  private String  searchValue;

  public SearchCriteria( String searchTag, String searchValue )
  {
    this.searchTag = searchTag;
    this.searchValue = searchValue;
  }

  public SearchCriteria( SearchCriteria clone )
  {
    this.searchTag = clone.getSearchTag();
    this.searchValue = clone.getSearchValue();
    this.isActivated = clone.isActivated();
    this.dirty = clone.isDirty();
  }

  public boolean isActivated()
  {
    return isActivated;
  }

  public void setActivated( boolean isActivated )
  {
    setDirty(!this.isActivated == isActivated);
    this.isActivated = isActivated;
  }

  public void setSearchTag( String searchTag )
  {
    setDirty(!Objects.equals(this.searchTag, searchTag));
    this.searchTag = searchTag;
  }

  public String getSearchTag()
  {
    return searchTag;
  }

  public void setSearchValue( String searchValue )
  {
    setDirty(!Objects.equals(this.searchValue, searchValue));
    this.searchValue = searchValue;
  }

  public String getSearchValue()
  {
    return searchValue;
  }

  public boolean isDirty() {
    return dirty;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }
}
