#!/bin/bash
whoami

VERSION=$(grep -m 1 "<version>" ../pom.xml | sed -e 's/.*<version>//' -e 's/<\/version>.*//' | xargs)
echo "Die Version ist: $VERSION"

# Input Validation
if [ -z "$VERSION" ]; then
  echo "Error: Empty version provided"
  exit -1
fi

# Quelldatei
SOURCE_DIR=/opt/jenkins/workspace/TestUtils_Ausliefern_BRANCH/TestSupportGUI/target/TestSupportGUI-$VERSION-distribution.zip
echo SOURCE_DIR=$SOURCE_DIR

# PRIVATER ssh-Key für SCP Befehl
SSHKEY_FILE="/home/jenkins/.ssh/id_ed25519_ctcb"
# ssh Benutzer dem der SSHKey zugeordnet ist
SSHUSER="ctcb"


# Zielangaben die per ssh erreicht werden sollen
SSHZIELSYSTEM01="pc10006610.verband.creditreform.de"
SSHZIELSYSTEM02="PC10010380.verband.creditreform.de"
# Defaultmässig lande ich im c:\USERS\BENUTZERNAME Verzeichnis
ZIELSYSTEMVERZEICHNIS01="../../CTE/KC-TEST/"
ZIELSYSTEMVERZEICHNIS02="../../CTE/KC-TEST/"

PATH_TO_CTE_SOFTWARE_PC10006610=$SSHZIELSYSTEM01:$ZIELSYSTEMVERZEICHNIS01
PATH_TO_CTE_SOFTWARE_PC10010380=$SSHZIELSYSTEM02:$ZIELSYSTEMVERZEICHNIS02

#if [ ! -d "$SOURCE_DIR" ]; then
#  echo "Error: Source directory '$SOURCE_DIR' does not exist"
#  exit -1
#fi


# Create destination directory
#mkdir -p "$PATH_TO_CTE_SOFTWARE_PC10010380/TestSupportGUI-$VERSION"
#mkdir -p "$PATH_TO_CTE_SOFTWARE_PC10006610/TestSupportGUI-$VERSION"

# Copy files/directories to SYSTEM01
echo "$SOURCE_DIR" "$PATH_TO_CTE_SOFTWARE_PC10006610/"
# 2 leitet den sogenannten Standard-Error (stderr) um.

if scp -i $SSHKEY_FILE $SOURCE_DIR $SSHUSER@$PATH_TO_CTE_SOFTWARE_PC10006610 1>log-PC10006610; then
    echo "OK"
else
    echo "Failed"
fi

# Copy files/directories to SYSTEM02
echo "$SOURCE_DIR" "$PATH_TO_CTE_SOFTWARE_PC10010380/"
if scp -i $SSHKEY_FILE $SOURCE_DIR $SSHUSER@$PATH_TO_CTE_SOFTWARE_PC10010380 1>log-PC10010380; then
    echo "OK"
else
    echo "Failed"
fi