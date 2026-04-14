package de.creditreform.crefoteam.cte.testsupporttool;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CliArgsTest {

    @Test
    void onlyEnv_defaultsToRealMode() {
        CliArgs cli = CliArgs.parse(new String[]{"e:ENE"});

        assertThat(cli.getEnvName()).isEqualTo("ENE");
        assertThat(cli.isDemoMode()).isFalse();
    }

    @Test
    void envAndDemoTrue_runsHandlersInDemoMode() {
        CliArgs cli = CliArgs.parse(new String[]{"e:ENE", "-Demo:true"});

        assertThat(cli.getEnvName()).isEqualTo("ENE");
        assertThat(cli.isDemoMode()).isTrue();
        assertThat(cli.isDemoExplicit()).isTrue();
    }

    @Test
    void envAndDemoFalse_runsRealMode() {
        CliArgs cli = CliArgs.parse(new String[]{"e:ENE", "Demo:false"});

        assertThat(cli.isDemoMode()).isFalse();
        assertThat(cli.isDemoExplicit()).isTrue();
    }

    @Test
    void aliases_envAndEnvironment() {
        assertThat(CliArgs.parse(new String[]{"env:GEE"}).getEnvName()).isEqualTo("GEE");
        assertThat(CliArgs.parse(new String[]{"-environment:GEE"}).getEnvName()).isEqualTo("GEE");
    }

    @Test
    void aliases_demoAndDemoMode() {
        assertThat(CliArgs.parse(new String[]{"e:ENE", "demo:true"}).isDemoMode()).isTrue();
        assertThat(CliArgs.parse(new String[]{"e:ENE", "-DemoMode:true"}).isDemoMode()).isTrue();
    }

    @Test
    void boolValueAliases_jaUndNein() {
        assertThat(CliArgs.parse(new String[]{"e:ENE", "Demo:ja"}).isDemoMode()).isTrue();
        assertThat(CliArgs.parse(new String[]{"e:ENE", "Demo:nein"}).isDemoMode()).isFalse();
        assertThat(CliArgs.parse(new String[]{"e:ENE", "Demo:1"}).isDemoMode()).isTrue();
        assertThat(CliArgs.parse(new String[]{"e:ENE", "Demo:0"}).isDemoMode()).isFalse();
    }

    @Test
    void requireValid_failsWhenEnvMissing() {
        CliArgs cli = CliArgs.parse(new String[]{"Demo:true"});
        assertThatThrownBy(cli::requireValid)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pflicht-Argument fehlt: e:<envName>");
    }

    @Test
    void requireValid_failsForEmptyArgs() {
        CliArgs cli = CliArgs.parse(new String[0]);
        assertThatThrownBy(cli::requireValid)
                .isInstanceOf(IllegalArgumentException.class);
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
        assertThatThrownBy(() -> CliArgs.parse(new String[]{"e:ENE", "Demo:vielleicht"}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Boolean-Wert");
    }
}
