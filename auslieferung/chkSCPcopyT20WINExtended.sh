#!/bin/bash
SKRIPTVERSION="T20_Version_04.03.2026"
zeitstempel=$(date "+%d%m%Y_%H%M%S")
chkdateiname=$SKRIPTVERSION".chk"								 
echo "chkSCPCopyWINExtended.sh $SKRIPTVERSION"
echo "Das Skript legt temporär eine lokale Datei $chkdateiname an"
echo "Diese Datei soll dann auf das Zielsystem per SCP kopiert werden."
touch $chkdateiname
echo "TESTDATEI FÜR SCP - kann bedenkenlos gelöscht werden! $zeitstempel"  > $chkdateiname
QUELLE=$chkdateiname
# privater sshkey der benutzt werden soll
SSHKEY_FILE="/home/jenkins/.ssh/id_ed25519_ctcb"
# Benutzer der Inhaber des sshkey
SSHUSER="ctcb"
# Zielsysteme auf die kopiert werden soll
SSHZIELSYSTEM01="pc10006610.verband.creditreform.de"
ZIELSYSTEMVERZEICHNIS01="../../CTE/KC-TEST/"
SSHZIELSYSTEM02="PC10010380.verband.creditreform.de"
ZIELSYSTEMVERZEICHNIS02="../../CTE/KC-TEST/"

# System 01
# Nur wenn noch nicht existiert dann kopieren
Zieldatei01=$ZIELSYSTEMVERZEICHNIS01$QUELLE
echo "zieldatei $Zieldatei01 existiert?"
  echo "           kopiere Datei: $QUELLE per SCP"
  echo "            mit Benutzer: $SSHUSER und SSH-Key: $SSHKEY_FILE"
  echo "              auf System: $SSHZIELSYSTEM01"
  echo "in ZIELSYSTEMVERZEICHNIS: $ZIELSYSTEMVERZEICHNIS01"
  scp -i $SSHKEY_FILE $QUELLE  $SSHUSER@$SSHZIELSYSTEM01:$ZIELSYSTEMVERZEICHNIS01

# System 02
# Nur wenn noch nicht existiert dann kopieren
Zieldatei02=$ZIELSYSTEMVERZEICHNIS02$QUELLE
echo "Zieldatei $Zieldatei02 existiert?"
  echo "           kopiere Datei: $QUELLE per SCP"
  echo "            mit Benutzer: $SSHUSER und SSH-Key: $SSHKEY_FILE"
  echo "              auf System: $SSHZIELSYSTEM02"
  echo "in ZIELSYSTEMVERZEICHNIS: $ZIELSYSTEMVERZEICHNIS02"
  scp -i $SSHKEY_FILE $QUELLE  $SSHUSER@$SSHZIELSYSTEM02:$ZIELSYSTEMVERZEICHNIS02

echo "Testdatei $QUELLE wird lokal wieder entfernt"
rm $QUELLE
echo "Bei Bedarf bitte Testdatei $QUELLE auf den remote Systemen"
echo "$SSHZIELSYSTEM01 und $SSHZIELSYSTEM02"
echo "in Verzeichnis $ZIELSYSTEMVERZEICHNIS01 bzw. $ZIELSYSTEMVERZEICHNIS02 prüfen und manuell entfernen!"
