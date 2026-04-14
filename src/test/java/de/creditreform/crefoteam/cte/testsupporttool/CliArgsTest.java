package de.creditreform.crefoteam.cte.testsupporttool;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CliArgsTest {

    @Test
    void noArgs_defaultsToDemoModeWithoutEnv() {
        CliArgs cli = CliArgs.parse(new String[0]);

        assertThat(cli.getEnvName()).isNull();
        assertThat(cli.isDemoMode()).isTrue();
        assertThat(cli.isDemoExplicit()).isFalse();
    }

    @Test
    void onlyEnv_defaultsToRealMode() {
        CliArgs cli = CliArgs.parse(new String[]{"e:ENE"});

        assertThat(cli.getEnvName()).isEqualTo("ENE");
        assertThat(cli.isDemoMode()).isFalse();
    }

    @Test
    void envAndDemoTrue_loadsEnvButRunsInDemoMode() {
        CliArgs cli = CliArgs.parse(new String[]{"e:ENE", "-Demo:true"});

        assertThat(cli.getEnvName()).isEqualTo("ENE");
        assertThat(cli.isDemoMode()).isTrue();
        assertThat(cli.isDemoExplicit()).isTrue();
    }

    @Test
    void aliases_envAndEnvironment() {
        assertThat(CliArgs.parse(new String[]{"env:GEE"}).getEnvName()).isEqualTo("GEE");
        assertThat(CliArgs.parse(new String[]{"-environment:GEE"}).getEnvName()).isEqualTo("GEE");
    }

    @Test
    void aliases_demoAndDemoMode() {
        assertThat(CliArgs.parse(new String[]{"demo:false"}).isDemoMode()).isFalse();
        assertThat(CliArgs.parse(new String[]{"-DemoMode:false"}).isDemoMode()).isFalse();
    }

    @Test
    void boolValueAliases_jaUndNein() {
        assertThat(CliArgs.parse(new String[]{"Demo:ja"}).isDemoMode()).isTrue();
        assertThat(CliArgs.parse(new String[]{"Demo:nein"}).isDemoMode()).isFalse();
        assertThat(CliArgs.parse(new String[]{"Demo:1"}).isDemoMode()).isTrue();
        assertThat(CliArgs.parse(new String[]{"Demo:0"}).isDemoMode()).isFalse();
    }

    @Test
    void unknownKey_fails() {
        assertThatThrownBy(() -> CliArgs.parse(new String[]{"x:1"}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unbekanntes Argument");
    }

    @Test
    void missingColon_fails() {
        assertThatThrownBy(() -> CliArgs.parse(new String[]{"ENE"}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("falsche Format");
    }

    @Test
    void invalidBoolValue_fails() {
        assertThatThrownBy(() -> CliArgs.parse(new String[]{"Demo:vielleicht"}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Boolean-Wert");
    }
}
