package de.creditreform.crefoteam.cte.tesun.util;

import java.io.File;

/** Literal-Port aus {@code testsupport_client.tesun_util}. */
public class TestCrefo {
    private String testFallName;
    private String testFallInfo;
    private Long itsqTestCrefoNr;
    private File itsqRexExportXmlFile;
    private Long pseudoCrefoNr;
    private File pseudoRefExportXmlFile;
    private File collectedXmlFile;
    private File restoredXmlFile;
    private File itsqPhase2XmlFile;
    private boolean shouldBeExported;
    private boolean activated = true;
    private boolean exported = false;

    public TestCrefo(String testFallName, Long itsqTestCrefoNr, String testFallInfo, boolean shouldBeExported, File itsqPhase2XmlFile) {
        this.testFallName = testFallName;
        this.itsqTestCrefoNr = itsqTestCrefoNr;
        this.testFallInfo = testFallInfo;
        this.shouldBeExported = shouldBeExported;
        this.itsqPhase2XmlFile = itsqPhase2XmlFile;
    }

    public TestCrefo(String testFallName, Long pseudoTestCrefoNr) {
        this.testFallName = testFallName;
        this.pseudoCrefoNr = pseudoTestCrefoNr;
    }

    public TestCrefo(TestCrefo theClone) {
        setTestFallName(theClone.getTestFallName());
        setItsqTestCrefoNr(theClone.getItsqTestCrefoNr());
        setPseudoCrefoNr(theClone.getPseudoCrefoNr());
        setTestFallInfo(theClone.getTestFallInfo());
        setItsqRexExportXmlFile(theClone.getItsqRexExportXmlFile());
        setPseudoRefExportXmlFile(theClone.getPseudoRefExportXmlFile());
        setCollectedXmlFile(theClone.getCollectedXmlFile());
        setRestoredXmlFile(theClone.getRestoredXmlFile());
        setActivated(theClone.isActivated());
        setExported(theClone.isExported());
        setShouldBeExported(theClone.isShouldBeExported());
        setItsqPhase2XmlFile(theClone.getItsqPhase2XmlFile());
    }

    public Long getItsqTestCrefoNr() { return itsqTestCrefoNr; }
    public void setItsqTestCrefoNr(Long itsqTestCrefoNr) { this.itsqTestCrefoNr = itsqTestCrefoNr; }

    public File getItsqPhase2XmlFile() { return itsqPhase2XmlFile; }
    public void setItsqPhase2XmlFile(File itsqPhase2XmlFile) { this.itsqPhase2XmlFile = itsqPhase2XmlFile; }

    public Long getPseudoCrefoNr() { return pseudoCrefoNr; }
    public void setPseudoCrefoNr(Long pseudoCrefoNr) { this.pseudoCrefoNr = pseudoCrefoNr; }

    public boolean isActivated() { return activated; }
    public void setActivated(boolean activated) { this.activated = activated; }

    public boolean isShouldBeExported() { return shouldBeExported; }
    public void setShouldBeExported(boolean shouldBeExported) { this.shouldBeExported = shouldBeExported; }

    public boolean isExported() { return exported; }
    public void setExported(boolean exported) { this.exported = exported; }

    public void setTestFallInfo(String testFallInfo) { this.testFallInfo = testFallInfo; }
    public String getTestFallInfo() { return testFallInfo; }

    public String getTestFallName() { return testFallName; }
    public void setTestFallName(String testFallName) { this.testFallName = testFallName; }

    public File getItsqRexExportXmlFile() { return itsqRexExportXmlFile; }
    public void setItsqRexExportXmlFile(File itsqRexExportXmlFile) { this.itsqRexExportXmlFile = itsqRexExportXmlFile; }

    public File getPseudoRefExportXmlFile() { return pseudoRefExportXmlFile; }
    public void setPseudoRefExportXmlFile(File pseudoRefExportXmlFile) { this.pseudoRefExportXmlFile = pseudoRefExportXmlFile; }

    public File getCollectedXmlFile() { return collectedXmlFile; }
    public void setCollectedXmlFile(File collectedXmlFile) { this.collectedXmlFile = collectedXmlFile; }

    public File getRestoredXmlFile() { return restoredXmlFile; }
    public void setRestoredXmlFile(File restoredXmlFile) { this.restoredXmlFile = restoredXmlFile; }

    @Override
    public String toString() {
        return testFallName + ":" + itsqTestCrefoNr + "[" + pseudoCrefoNr + "]";
    }

    public StringBuilder dump(String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(testFallName).append("\t").append(itsqTestCrefoNr).append("\t").append(pseudoCrefoNr);
        if (itsqPhase2XmlFile != null) sb.append("\t").append(itsqPhase2XmlFile.getName());
        sb.append("\t");
        if (itsqRexExportXmlFile != null) sb.append(itsqRexExportXmlFile.getName());
        sb.append("\t");
        if (pseudoRefExportXmlFile != null) sb.append(pseudoRefExportXmlFile.getName());
        sb.append("\t");
        if (collectedXmlFile != null) sb.append(collectedXmlFile.getName());
        sb.append("\t");
        if (restoredXmlFile != null) sb.append(restoredXmlFile.getName());
        return sb;
    }
}
