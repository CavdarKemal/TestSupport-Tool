#!/bin/bash
echo Starte das Skript auto_cte_tests.sh ..
DEBUG_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
#DEBUG_OPTS=
JVM_ARGS="-Dfile.encoding=UTF8 -Xmx2048M -XX:+HeapDumpOnOutOfMemoryError -cp .:./lib/*"

if [ $# -eq 0 ]
then
  ENV=ENE
  ITSQ_BRANCH=master
else
  ENV=$1
  ITSQ_BRANCH=$2
fi

PRG_ARGS="-e=$ENV -r=ITSQ -b=$ITSQ_BRANCH

echo PRG_ARGS :   $PRG_ARGS
echo JVM_ARGS :   $JVM_ARGS
echo DEBUG_OPTS : $DEBUG_OPTS

echo Rufe Java-Programm auf: java $DEBUG_OPTS $JVM_ARGS de.creditreform.crefoteam.cte.tesun.auto.ActivitiTestAutomatisierung $PRG_ARGS

java $DEBUG_OPTS $JVM_ARGS de.creditreform.crefoteam.cte.tesun.auto.ActivitiTestAutomatisierung $PRG_ARGS

rc=$?
if [ $rc -eq 255 ]
then
  echo ERRORLEVEL: $rc
  echo "Teufelswerk:  *** Java ActivitiTestAutomatisierung lieferte an Jenkins 255 (-1) zurück! *** "
  echo "Could be:StrictHostKeyChecking=no? Please ADD StrictHostKeyChecking=no to your CODE!!!"
  echo "https://stackoverflow.com/questions/47608849/jenkins-host-key-verification-failed-script-returned-exit-code-255"
  rc=0
else
  echo ERRORLEVEL: $rc
fi

exit $rc
