# TestSupport-Tool (Spike)

Activiti-freier TestSupport-Client auf Basis der
[TestSupport-StateMachine](https://github.com/CavdarKemal/TestSupport-StateMachine)-Library.

**Status:** Spike / Proof-of-Concept. Drei repräsentative Handler decken die
drei Handler-Muster aus dem Original-Projekt ab. GUI und die restlichen ~32
Handler folgen.

## Build

```
ci.cmd 11        # ohne Tests
cit.cmd 11       # mit Tests (WireMock)
```

## Ausführen (Demo-Mode)

```
java -cp target/testsupport-tool-0.1.0-SNAPSHOT.jar:... \
     de.creditreform.crefoteam.cte.testsupporttool.Main
```

Im Demo-Mode werden REST-Aufrufe simuliert, der Prozess läuft End-to-End
durch.

## Architektur

```
de.creditreform.crefoteam.cte.testsupporttool/
├── Main                              Console-Demo Entry Point
├── ProcessRunner                     Wrapper um die Engine (ehem. ActivitiProcessController)
├── ConsoleProcessListener            log4j-Lifecycle-Listener (ehem. TesunClientJobListener)
├── config/
│   ├── EnvironmentConfig             schlanke Stub-Variante
│   └── TestSupportConstants          Variablen-Schlüssel
├── rest/
│   ├── TesunRestService              JDK-HttpClient-basiert, kein Apache HttpComponents
│   └── JobExecutionInfo              Reduziertes DTO
├── handlers/                         3 Demo-Handler in der Form, die echte Handler haben werden
│   ├── PrepareTestSystemHandler      Muster: einfache Aktion
│   ├── StartCtImportHandler          Muster: REST-Job-Start
│   ├── WaitForCtImportHandler        Muster: Polling auf externes System
│   └── NotifyHandler                 Trivial-Step für Success-/Failure-Mails
└── process/
    └── TestAutomationProcess         ProcessDefinition-Factory (ehem. CteAutomatedTestProcess.bpmn)
```

## Übernahmen aus testsupport_client

Bewusst **nicht** 1:1 übernommen — die Original-Klassen schleppen einen
großen Dependency-Graph (CTE-XML-Bindings, Apache HttpComponents, jersey,
groovy, h2, …). Stattdessen sind die analogen Klassen hier auf das
absolute Minimum reduziert, damit die Patterns sichtbar bleiben:

| Original                                    | Hier                                          |
|---------------------------------------------|-----------------------------------------------|
| `tesun_util.EnvironmentConfig`              | `config.EnvironmentConfig` (4 Felder)         |
| `tesun_util.TestSupportClientKonstanten`    | `config.TestSupportConstants` (5 Konstanten)  |
| `tesun.rest.TesunRestService`               | `rest.TesunRestService` (JDK-HttpClient)      |
| `xmlbinding.TesunJobexecutionInfo`          | `rest.JobExecutionInfo` (3 Felder)            |
| `tesun.TesunClientJobListener`              | via `ProcessListener` aus der Library         |
| `activiti.handlers.UserTaskPrepareTestSystem` | `handlers.PrepareTestSystemHandler`         |
| `activiti.handlers.UserTaskStartCtImport`   | `handlers.StartCtImportHandler`               |
| `activiti.handlers.UserTaskWaitForCtImport` | `handlers.WaitForCtImportHandler`             |
| `activiti.handlers.UserTaskSuccessMail/Failure` | `handlers.NotifyHandler` (parametrisiert) |
| `ActivitiProcessController`                 | `ProcessRunner`                               |
| `TesunClientJobListener` (verbose impl)     | `ConsoleProcessListener`                      |
| `CteAutomatedTestProcess.bpmn`              | `process.TestAutomationProcess` (Java-Code)   |

## Nächste Schritte

- [ ] Restliche Handler aus `tesun_activiti.handlers` portieren
- [ ] Echten `EnvironmentConfig` mit Property-File-Loading übernehmen
- [ ] Vollständigen REST-Service mit allen Endpoints
- [ ] Sub-Prozesse `CteAutomatedTestProcessSUB` bauen
- [ ] GUI-Anbindung (`TestSupportGUI` → ersetzt `ActivitiProcessController`)
- [ ] Integration-Tests gegen echte Test-Umgebung (ENE/GEE)
