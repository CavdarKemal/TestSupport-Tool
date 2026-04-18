@ECHO OFF
REM Prog- Args: [-D] -e:<Umgebung> -r:<Test-Root> -b:<Branch>
setlocal enabledelayedexpansion

REM --- 1. PARAMETER-CHECK AUF -D ---
SET "DEBUG_ENABLED=false"
SET "DEBUG_OPTS="

IF /I "%1"=="-D" (
    SET "DEBUG_ENABLED=true"
    echo Debug-Modus aktiviert.
    SHIFT
)

REM --- 2. JAVA-SUCHE (Java 11) ---
SET "FOUND_JAVA_HOME="
if defined JAVA_HOME (
    call :CheckJava11 "%JAVA_HOME%"
    if !errorlevel! equ 0 set "FOUND_JAVA_HOME=%JAVA_HOME%"
)

if "%FOUND_JAVA_HOME%"=="" (
    echo Suche nach Java 11 in C:\Program Files\AdoptOpenJDK...
    for /d %%D in ("C:\Program Files\AdoptOpenJDK\jdk-11*" "C:\Program Files\AdoptOpenJDK\jdk11*" "C:\Program Files (x86)\AdoptOpenJDK\jdk-11*") do (
        if "!FOUND_JAVA_HOME!"=="" (
            call :CheckJava11 "%%~fD"
            if !errorlevel! equ 0 set "FOUND_JAVA_HOME=%%~fD"
        )
    )
)

if "%FOUND_JAVA_HOME%"=="" (
    echo.
    echo FEHLER: Java 11 konnte nicht gefunden werden.
    pause
    exit /b 1
)

set "JAVA_HOME=%FOUND_JAVA_HOME%"
set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
echo Erfolg: Java 11 gefunden in: %JAVA_HOME%

:START_APP
REM Port-LogIK basierend auf der Umgebung
SET "DEBUG_PORT=5005"
IF /I "%1"=="-e:ENE" (SET "DEBUG_PORT=5005")
IF /I "%1"=="-e:GEE" (SET "DEBUG_PORT=5006")
IF /I "%1"=="-e:ABE" (SET "DEBUG_PORT=5007")

REM --- DEBUG-PRÜFUNG ---
IF "%DEBUG_ENABLED%"=="true" (
    echo Pruefe Debug-Port %DEBUG_PORT%...

    SET "PORT_BLOCKED=false"
    REM Wir nutzen findstr ohne komplexe Regex innerhalb der FOR-Schleife, um Escaping-Fehler zu vermeiden
    for /f "tokens=*" %%A in ('netstat -ano ^| findstr /C:":%DEBUG_PORT% "') do (
        SET "PORT_BLOCKED=true"
        SET "CONF_LINE=%%A"
    )

    if "!PORT_BLOCKED!"=="true" (
        echo.
        echo #######################################################
        echo FEHLER: Der Debug-Port %DEBUG_PORT% ist bereits belegt!
        echo Gefundene Zeile: !CONF_LINE!
        echo #######################################################
        echo.
        pause
        exit /b 1
    )

    SET "DEBUG_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=%DEBUG_PORT%"
    ECHO DEBUG_OPTS aktiv: !DEBUG_OPTS!
)

SET "restInvoker.traceInvokers=false"
SET "JVM_ARGS=-Dfile.encoding=UTF8 -Xms1024m -Xmx1280m -XX:+HeapDumpOnOutOfMemoryError -classpath .;lib/*"

ECHO.
ECHO AUFRUF: "%JAVA_EXE%" %DEBUG_OPTS% ...

"%JAVA_EXE%" -splash:Creditreform.png %DEBUG_OPTS% %JVM_ARGS% de.creditreform.crefoteam.cte.tesun.gui.TestSupportGUI %1 %2 %3

exit /b 0


REM --- UNTERFUNKTIONEN ---
:CheckJava11
if not exist "%~1\bin\java.exe" exit /b 1
"%~1\bin\java.exe" -version 2>&1 | find "11." >nul
exit /b %errorlevel%
