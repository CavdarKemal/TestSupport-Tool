package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.utils.TestCaseFilesFromGit}.
 *
 * <p>Klont/aktualisiert ITSQ-Test-Pakete aus Git-Repositories via externem
 * {@code git}-Binary (ProcessBuilder). Verteilt Fortschrittsmeldungen
 * an einen {@link CommandExecutorListener}.
 *
 * <p><b>Port-Notiz:</b> Im Original referenziert der Code
 * {@code EnvironmentConfig.GitProjectInfo} (nested class). Hier wird die
 * Standalone-Variante {@link GitProjectInfo} aus dem GUI-Modul benutzt.
 */
public class TestCaseFilesFromGit {
    public static final String REPOSITORY_USER = "tesuntestene";

    protected final CommandExecutorListener commandExecutorListener;

    public TestCaseFilesFromGit(CommandExecutorListener commandExecutorListener) {
        this.commandExecutorListener = commandExecutorListener;
    }

    public File updateItsqTestPaket(File testBaseDir, List<GitProjectInfo> gitProjectsInfoList) throws Exception {
        File itsqDir = new File(testBaseDir, "ITSQ");
        for (GitProjectInfo gitProjectInfo : gitProjectsInfoList) {
            File cloneTargetDir = new File(itsqDir, gitProjectInfo.getLocalRepoName());
            cloneOrUpdateGitProject(gitProjectInfo, cloneTargetDir);
        }
        for (GitProjectInfo gitProjectInfo : gitProjectsInfoList) {
            if (gitProjectInfo.getLocalRepoName().equalsIgnoreCase("tesfaelle_cte")) {
                return gitProjectInfo.getCloneTargetDir();
            }
        }
        return null; // TODO!!!
    }

    public void cloneOrUpdateGitProject(GitProjectInfo gitProjectInfo, File cloneTargetDir) throws Exception {
        if (cloneTargetDir.exists()) {
            commandExecutorListener.progress("\nITSQ-Projekt existiert bereits, führe ein Update durch...");
            try {
                updateGitProject(gitProjectInfo, cloneTargetDir);
            } catch (Exception ex) {
                throw ex;
            }
        } else {
            int nTries = 1;
            commandExecutorListener.progress("\nClone ITSQ-Projekt '" + cloneTargetDir.getName() + "'für aus dem Git-Repository...");
            while (true) {
                try {
                    cloneGitProject(gitProjectInfo, cloneTargetDir);
                    return;
                } catch (Throwable ex) {
                    if (nTries++ > 2) {
                        throw ex;
                    }
                    commandExecutorListener.progress("\n\t" + nTries + ".te Versuch...");
                }
            }
        }
    }

    public String tagAndPushITSQProject(String localRepoPath, String tagName) throws Exception {
        if (tagName != null) {
            List<String> repoCommandsList = new ArrayList<>();
            repoCommandsList.add("git");
            repoCommandsList.add("tag");
            repoCommandsList.add("-a");
            repoCommandsList.add(tagName);
            repoCommandsList.add("-m");
            repoCommandsList.add("'Tagged by " + REPOSITORY_USER);
            commandExecutorListener.progress(String.format("\nTagge ITSQ-Projekt mit neuem Tag %s...", tagName));
            doExecCommand(repoCommandsList, localRepoPath, commandExecutorListener);

            repoCommandsList.clear();
            repoCommandsList.add("git");
            repoCommandsList.add("push");
            repoCommandsList.add("origin");
            repoCommandsList.add(tagName);
            commandExecutorListener.progress("\nPushe ITSQ-Projekt ins Repository...");
            doExecCommand(repoCommandsList, localRepoPath, commandExecutorListener);
        } else {
            commandExecutorListener.progress("\nTaggen ist ausgeschaltet, das ITSQ-Projekt braucht nicht gepushed zu werden!\n");
        }
        return tagName;
    }

    protected void updateGitProject(GitProjectInfo gitProjectInfo, File cloneTargetDir) throws Exception {
        List<String> repoCommandsList = new ArrayList<>();
        repoCommandsList.add("git");
        repoCommandsList.add("pull");
        repoCommandsList.add("origin");
        repoCommandsList.add(gitProjectInfo.getGitRepoRevision());
        try {
            doExecCommand(repoCommandsList, cloneTargetDir.getAbsolutePath(), commandExecutorListener);
            gitProjectInfo.setCloneTargetDir(cloneTargetDir);
        } catch (Exception ex) {
            if (ex.getMessage().contains("nicht zusammengef\u00fchrt")) {
                repoCommandsList = new ArrayList<>();
                repoCommandsList.add("git");
                repoCommandsList.add("reset");
                repoCommandsList.add("--hard");
                String path = System.getProperty("java.io.tmpdir") + File.separator + "ITSQ" + File.separator + "itsq_testfaelle";
                doExecCommand(repoCommandsList, path, commandExecutorListener);
                gitProjectInfo.setCloneTargetDir(cloneTargetDir);
            } else {
                throw ex;
            }
        }
    }

    protected void cloneGitProject(GitProjectInfo gitProjectInfo, File cloneTargetDir) throws Exception {
        List<String> repoCommandsList = new ArrayList<>();
        repoCommandsList.add("git");
        repoCommandsList.add("clone");
        repoCommandsList.add(String.format("%s%s", gitProjectInfo.getGitRepoHost(), gitProjectInfo.getGitRepoName()));
        repoCommandsList.add(cloneTargetDir.getAbsolutePath());
        repoCommandsList.add("-b");
        repoCommandsList.add(gitProjectInfo.getGitRepoRevision());
        String path = "";
        if (cloneTargetDir.listFiles() != null && cloneTargetDir.listFiles().length > 0) {
            path = cloneTargetDir.getAbsolutePath();
        }
        doExecCommand(repoCommandsList, path, commandExecutorListener);
        gitProjectInfo.setCloneTargetDir(cloneTargetDir);
    }

    private void doExecCommand(List<String> execCmdsList, String changeToPath, CommandExecutorListener commandExecutorListener) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(execCmdsList);
        if (!changeToPath.isEmpty()) {
            // commandExecutorListener.progress("\nWechsle ins Verzeichnis " + changeToPath);
            builder = builder.directory(new File(changeToPath));
        }
        if (commandExecutorListener != null) {
            String strInfo = String.format("\n\tProcessBuilder.start mit Parametern: %s", execCmdsList.toString().replaceAll(",", ""));
            strInfo = strInfo.replaceAll("auth.x.password=\\w+", "auth.x.password=#####");
            commandExecutorListener.progress(strInfo);
        }
        Process theProcess = builder.start();
        while (theProcess.isAlive()) {
            if (commandExecutorListener != null) {
                commandExecutorListener.progress(".");
            }
            Thread.sleep(300);
        }
        String infoFromErrStream = getInfoFromErrorStream(theProcess);
        if (infoFromErrStream.contains("fatal")) {
            theProcess.destroy();
            throw new RuntimeException(infoFromErrStream);
        }
        if (!infoFromErrStream.isEmpty() && !infoFromErrStream.contains("Updating files:")) {
            commandExecutorListener.progress("\n" + infoFromErrStream);
        }
        theProcess.destroy();
    }

    private String getInfoFromInputStream(Process theProcess) throws IOException {
        byte[] byteArray = new byte[1024];
        theProcess.getInputStream().read(byteArray);
        String strInfo = new String(byteArray, StandardCharsets.UTF_8);
        return strInfo;
    }

    private String getInfoFromErrorStream(Process theProcess) throws IOException {
        StringBuilder strOutput = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(theProcess.getErrorStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            strOutput.append(line);
            strOutput.append("\n");
        }
        return strOutput.toString();
    }

}
