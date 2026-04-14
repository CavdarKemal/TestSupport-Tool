package de.creditreform.crefoteam.cte.testsupporttool.env;

import de.creditreform.crefoteam.cte.testsupporttool.config.EnvironmentConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnvironmentConfigTest {

    @Test
    void load_parsesAllPropertiesAndDerivesEnvNameFromFilename(@TempDir Path tmp) throws IOException {
        Path configFile = tmp.resolve("ABE-config.properties");
        Files.writeString(configFile, String.join("\n",
                "TESUN_REST_BASE_URL=http://abe.example/cte-rest",
                "JOB_STATUS_POLLING_MILLIS=500",
                "JOB_TIMEOUT_MILLIS=60000",
                "LOG_OUTPUTS_ROOT=logs"));

        EnvironmentConfig env = EnvironmentConfig.load(configFile.toFile());

        assertThat(env.getCurrentEnvName()).isEqualTo("ABE");
        assertThat(env.getTesunRestBaseUrl()).isEqualTo("http://abe.example/cte-rest");
        assertThat(env.getJobStatusPollingMillis()).isEqualTo(500L);
        assertThat(env.getJobTimeoutMillis()).isEqualTo(60_000L);
        assertThat(env.getLogOutputsRoot()).isEqualTo(tmp.resolve("logs/ABE").toFile());
        assertThat(env.getEnvironmentConfigFile()).isEqualTo(configFile.toFile());
    }

    @Test
    void load_missingRequiredProperty_fails(@TempDir Path tmp) throws IOException {
        Path configFile = tmp.resolve("ENE-config.properties");
        Files.writeString(configFile, "JOB_STATUS_POLLING_MILLIS=100");  // URL fehlt

        assertThatThrownBy(() -> EnvironmentConfig.load(configFile.toFile()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TESUN_REST_BASE_URL");
    }

    @Test
    void load_wrongFilenameSuffix_fails(@TempDir Path tmp) throws IOException {
        Path configFile = tmp.resolve("ENE.properties");
        Files.writeString(configFile, "TESUN_REST_BASE_URL=http://x");

        assertThatThrownBy(() -> EnvironmentConfig.load(configFile.toFile()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("-config.properties");
    }

    @Test
    void getLogOutputsRootForEnv_returnsSiblingDirectory(@TempDir Path tmp) throws IOException {
        Path configFile = tmp.resolve("ENE-config.properties");
        Files.writeString(configFile, "TESUN_REST_BASE_URL=http://x");

        EnvironmentConfig ene = EnvironmentConfig.load(configFile.toFile());

        assertThat(ene.getLogOutputsRootForEnv("ENE")).isEqualTo(ene.getLogOutputsRoot());
        assertThat(ene.getLogOutputsRootForEnv("GEE"))
                .isEqualTo(ene.getLogOutputsRoot().getParentFile().toPath().resolve("GEE").toFile());
    }
}
