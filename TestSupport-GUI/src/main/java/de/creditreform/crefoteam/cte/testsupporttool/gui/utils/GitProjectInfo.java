package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import java.io.File;

/**
 * 1:1-Port der Daten-Klasse aus dem Original — dort als
 * {@code public static class GitProjectInfo} <b>nested in</b>
 * {@code EnvironmentConfig}. Beim Port wird sie zu einer eigenstaendigen
 * Klasse im GUI-Modul herausgezogen, damit der Headless-Core keine
 * GUI-only-Inner-Class tragen muss.
 *
 * <p>Format des Konstruktor-Strings (semikolon-separiert):
 * <pre>
 *   gitRepoHost;gitRepoName;gitRepoRevision[;localRepoName]
 * </pre>
 * Beispiel: {@code /ITSQ/Testfaelle-CTE;testutils_cte;master}
 */
public class GitProjectInfo {
    private final String gitRepoHost;
    private final String gitRepoName;
    private String gitRepoRevision;
    private String localRepoName;
    private File cloneTargetDir;

    public GitProjectInfo(String gitPrjInfo) {
        String[] split = gitPrjInfo.split(";");
        try {
            this.gitRepoHost = split[0];
            this.gitRepoName = split[1];
            this.gitRepoRevision = split[2];
            if (split.length < 4) {
                this.gitRepoRevision = "master";
            } else {
                this.localRepoName = split[3];
            }
        } catch (Exception ex) {
            throw new RuntimeException("GIT-Projekt-Info '" + gitPrjInfo + "' ist falsch!\nDas Format muss wie folgt sein: <gitRepoName>;<localRepoName>;<gitRepoRevision>\nBeispoiel: '/ITSQ/Testfaelle-CTE;testutils_cte;master'");
        }
    }

    public String getGitRepoHost() {
        return gitRepoHost;
    }

    public String getGitRepoName() {
        return gitRepoName;
    }

    public String getGitRepoRevision() {
        return gitRepoRevision;
    }

    public void setGitRepoRevision(String gitRepoRevision) {
        this.gitRepoRevision = gitRepoRevision;
    }

    public String getLocalRepoName() {
        return localRepoName;
    }

    public File getCloneTargetDir() {
        return cloneTargetDir;
    }

    public void setCloneTargetDir(File cloneTargetDir) {
        this.cloneTargetDir = cloneTargetDir;
    }
}
