#!/bin/bash

echo Starte das Skript XmlSearchGUI.sh ...
#DEBUG_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005
DEBUG_OPTS=
echo DEBUG_OPTS=$DEBUG_OPTS

#CONFIG_FILE=

echo Konfig-Datei: $CONFIG_FILE
JVM_ARGS="-Xmx2048M -XX:+HeapDumpOnOutOfMemoryError -cp .:./lib/*"

#java -splash:Creditreform.png -Xms1024m -Xmx1280m -XX:+HeapDumpOnOutOfMemoryError -classpath .:./lib/* $DEBUG_OPT de.creditreform.crefoteam.cte.tesun.gui.xmlsearch.SearchXMLsGUI %CONFIG_FILE
java -splash:Creditreform.png $JVM_ARGS $DEBUG_OPT de.creditreform.crefoteam.cte.tesun.gui.xmlsearch.SearchXMLsGUI $CONFIG_FILE

rc=$?
echo ERRORLEVEL: $rc
exit $rc
