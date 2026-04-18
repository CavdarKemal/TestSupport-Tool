@@ECHO OFF
echo Starte das Skript auto_cte_tests.cmd ...
SET DEBUG_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
REM SET DEBUG_OPTS=
REM Arguments für RT und Mercurial-Clone als Test-Inputs
SET ARGUMENTS=-e=ENE -r=ITSQ -b=$ITSQ_BRANCH

REM Arguments für RT2 und Mercurial-Clone als Test-Inputs

echo ARGUMENTS : %ARGUMENTS%

SET JVM_ARGS=-Dfile.encoding=UTF8 -Xms1024m -Xmx1280m -XX:+HeapDumpOnOutOfMemoryError -classpath .;lib/*
java -splash:Creditreform.png %DEBUG_OPTS% %JVM_ARGS% de.creditreform.crefoteam.cte.tesun.auto.ActivitiTestAutomatisierung %ARGUMENTS%
echo ERRORLEVEL: %ERRORLEVEL%

