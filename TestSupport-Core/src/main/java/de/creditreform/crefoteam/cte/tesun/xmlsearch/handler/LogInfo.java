package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.domain.ProgressListenerIF.LOG_LEVEL;

public class LogInfo
{

  final private LOG_LEVEL logLevel;
  final private String searchName;
  final private String    logMessage;
  final private Throwable throwable;

  public LogInfo(String searchName, LOG_LEVEL logLevel, String logMessage, Throwable throwable )
  {
    this.logLevel = logLevel;
    this.logMessage = logMessage;
    this.throwable = throwable;
    this.searchName = searchName;
  }

  public String getLogMessage()
  {
    return logMessage;
  }

  public Enum<LOG_LEVEL> getLogLevel()
  {
    return logLevel;
  }

  public Throwable getThrowable()
  {
    return throwable;
  }

  @Override
  public String toString() {
    String strResult = logMessage.replaceAll("\t", " ");
    if(throwable != null) {
      strResult += "\nException-Msg:\n\t";
      strResult += throwable.getMessage();
    }
    return strResult;
  }

  public String getSearchName() {
    return searchName;
  }
}
