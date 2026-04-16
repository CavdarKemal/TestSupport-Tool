package de.creditreform.crefoteam.cte.testsupporttool.resume;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ResumeStateTest {

    @Test
    void saveAndLoad_roundtripPreservesAllFields(@TempDir Path tmp) throws Exception {
        File target = new File(tmp.toFile(), ResumeState.FILE_NAME);
        ResumeState before = new ResumeState(new int[]{3, 9}, "UserTaskStartCtImport", "PHASE_2");
        before.save(target);
        assertThat(target).exists();

        ResumeState after = ResumeState.load(target);
        assertThat(after).isNotNull();
        assertThat(after.indexPath()).containsExactly(3, 9);
        assertThat(after.lastStepName()).isEqualTo("UserTaskStartCtImport");
        assertThat(after.testPhase()).isEqualTo("PHASE_2");
        assertThat(after.savedAt()).isEqualTo(before.savedAt());
    }

    @Test
    void load_onMissingFileReturnsNull(@TempDir Path tmp) throws Exception {
        assertThat(ResumeState.load(new File(tmp.toFile(), "does-not-exist.properties"))).isNull();
    }

    @Test
    void delete_removesFileIfPresent(@TempDir Path tmp) throws Exception {
        File target = new File(tmp.toFile(), ResumeState.FILE_NAME);
        new ResumeState(new int[]{1}, "x", "PHASE_1").save(target);
        assertThat(target).exists();
        ResumeState.delete(target);
        assertThat(target).doesNotExist();
    }

    @Test
    void delete_isNoOpOnMissingFile(@TempDir Path tmp) {
        ResumeState.delete(new File(tmp.toFile(), "not-there.properties"));
    }

    @Test
    void indexPathToString_and_back_roundtrip() {
        int[] original = {2, 14, 7};
        String str = ResumeState.indexPathToString(original);
        assertThat(str).isEqualTo("2,14,7");
        assertThat(ResumeState.indexPathFromString(str)).containsExactly(2, 14, 7);
    }

    @Test
    void indexPathFromString_handlesEmptyInputGracefully() {
        assertThat(ResumeState.indexPathFromString(null)).isEmpty();
        assertThat(ResumeState.indexPathFromString("")).isEmpty();
        assertThat(ResumeState.indexPathFromString("   ")).isEmpty();
    }

    @Test
    void compareIndexPaths_lexicographically() {
        assertThat(ResumeState.compareIndexPaths(new int[]{2, 9}, new int[]{3, 5})).isNegative();
        assertThat(ResumeState.compareIndexPaths(new int[]{3, 5}, new int[]{3, 5})).isZero();
        assertThat(ResumeState.compareIndexPaths(new int[]{3, 9}, new int[]{3, 5})).isPositive();
        // Kuerzerer Praefix < laengerer Praefix bei sonst gleichem Anfang.
        assertThat(ResumeState.compareIndexPaths(new int[]{3}, new int[]{3, 5})).isNegative();
    }

    @Test
    void saveAndLoad_withNullOptionalsPreservesNull(@TempDir Path tmp) throws Exception {
        File target = new File(tmp.toFile(), ResumeState.FILE_NAME);
        new ResumeState(new int[]{5}, null, null).save(target);
        ResumeState loaded = ResumeState.load(target);
        assertThat(loaded).isNotNull();
        assertThat(loaded.indexPath()).containsExactly(5);
        assertThat(loaded.lastStepName()).isNull();
        assertThat(loaded.testPhase()).isNull();
    }
}
