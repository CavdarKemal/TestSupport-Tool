@@ECHO OFF
REM ECHO Parameter: %*
SET SRC_DIR=%1
SET CTR_DIR=%2
SET RES_DIR=%3
SET DISABLE_QUOTING=%4

SET JVM_ARGS=-Dfile.encoding=UTF8 -Xms1024m -Xmx1280m -XX:+HeapDumpOnOutOfMemoryError -classpath .;lib/*

SET AUFRUF=@start javaw -splash:Creditreform.png %DEBUG_OPTS% %JVM_ARGS% de.creditreform.crefoteam.cte.tesun.zipped_xmls_compare.CompareZippedXmls %SRC_DIR% %CTR_DIR% %RES_DIR% %DISABLE_QUOTING%
REM ECHO %AUFRUF%
%AUFRUF%

GOTO ENDE

:ENDE

