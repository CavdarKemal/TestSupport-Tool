package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GitProjectInfoTest {

    @Test
    void parsesFourPartString_keepsAllFields() {
        GitProjectInfo info = new GitProjectInfo(
                "ssh://git@host;ITSQ/Testfaelle-CTE;feature-x;tesfaelle_cte");

        assertThat(info.getGitRepoHost()).isEqualTo("ssh://git@host");
        assertThat(info.getGitRepoName()).isEqualTo("ITSQ/Testfaelle-CTE");
        assertThat(info.getGitRepoRevision()).isEqualTo("feature-x");
        assertThat(info.getLocalRepoName()).isEqualTo("tesfaelle_cte");
    }

    @Test
    void parsesThreePartString_overridesRevisionToMaster() {
        // Original-Verhalten: bei nur 3 Teilen wird Revision zu "master" ueberschrieben.
        GitProjectInfo info = new GitProjectInfo("ssh://git@host;ITSQ/Repo;ignoredRevision");

        assertThat(info.getGitRepoHost()).isEqualTo("ssh://git@host");
        assertThat(info.getGitRepoName()).isEqualTo("ITSQ/Repo");
        assertThat(info.getGitRepoRevision()).isEqualTo("master");
        assertThat(info.getLocalRepoName()).isNull();
    }

    @Test
    void revisionSetter_overridesParsedValue() {
        GitProjectInfo info = new GitProjectInfo("h;n;v1;localName");
        info.setGitRepoRevision("v2");
        assertThat(info.getGitRepoRevision()).isEqualTo("v2");
    }

    @Test
    void cloneTargetDirSetter_persistsValue() {
        GitProjectInfo info = new GitProjectInfo("h;n;v;loc");
        File dir = new File("/tmp/clone");
        info.setCloneTargetDir(dir);
        assertThat(info.getCloneTargetDir()).isSameAs(dir);
    }

    @Test
    void malformedString_throwsRuntimeWithUsageHint() {
        // weniger als 3 Teile -> ArrayIndexOutOfBoundsException -> in RuntimeException eingewickelt.
        assertThatThrownBy(() -> new GitProjectInfo("only;two"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("GIT-Projekt-Info")
                .hasMessageContaining("ist falsch");
    }
}
