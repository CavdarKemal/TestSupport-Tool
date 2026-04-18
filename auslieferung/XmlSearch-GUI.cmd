@@ECHO OFF
REM ECHO Parameter: %*
SET DEBUG_OPT=
SET CONFIG_FILE=
IF NOT "%1" == "" (
  IF "%1" == "D" (
    SET DEBUG_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
    IF NOT "%2" == "" (
      SET CONFIG_FILE=%2
    )
  ) ELSE (
    SET CONFIG_FILE=%1
    IF "%2" == "D" (
      SET DEBUG_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
    )
  )
)
SET JVM_ARGS=-Dfile.encoding=UTF8 -Xms8g -Xmx12g -XX:+HeapDumpOnOutOfMemoryError -classpath .;lib/*
SET AUFRUF=@start javaw -splash:Creditreform.png %DEBUG_OPTS% %JVM_ARGS% de.creditreform.crefoteam.cte.tesun.gui.xmlsearch.SearchXMLsGUI %CONFIG_FILE%
REM ECHO %AUFRUF%
%AUFRUF%

GOTO ENDE

:ENDE

