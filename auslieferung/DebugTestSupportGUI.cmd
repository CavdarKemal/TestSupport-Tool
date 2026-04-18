@@ECHO OFF
REM Prog- Args: -e:<Umgebung> -r:<Test-Root> -b:<Branch>
REM  z.B.:       -e=ENE -r=ITSQ -b=ZWEI_PHASEN

SET restInvoker.traceInvokers=true

SET DEBUG_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005
SET JVM_ARGS=-Dfile.encoding=UTF8 -Xms1024m -Xmx1280m -XX:+HeapDumpOnOutOfMemoryError -classpath .;lib/*

SET AUFRUF=@start javaw -splash:Creditreform.png %DEBUG_OPTS% %JVM_ARGS% de.creditreform.crefoteam.cte.tesun.gui.TestSupportGUI %1 %2 %2
ECHO %AUFRUF%
%AUFRUF%

GOTO ENDE

:ENDE
