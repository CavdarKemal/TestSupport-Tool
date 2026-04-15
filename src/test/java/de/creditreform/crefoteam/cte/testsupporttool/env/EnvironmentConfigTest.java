package de.creditreform.crefoteam.cte.testsupporttool.env;

import de.creditreform.crefoteam.cte.rest.RestInvokerConfig;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.tesun.util.PropertiesException;
import org.junit.jupiter.api.Test;

import java.util.List;

// Hinweis: der Literal-Port von EnvironmentConfig nutzt `isRequired=true` auch
// für Getter, die selbst einen Default-Wert übergeben (PROPNAME_TARGET_CLZ_*,
// Job-Name-Defaults etc.). Die `getProperty`-Logik wirft allerdings bei
// required+null *vor* dem Default-Fallback eine Exception. Getter, die gegen
// die DEMO-config.properties kein Pendant haben, werden daher hier nicht
// abgedeckt.

import static org.assertj.core.api.Assertions.assertThat;

class EnvironmentConfigTest {

    @Test
    void loadsDemoConfigFromTestClasspath() throws PropertiesException {
        EnvironmentConfig env = new EnvironmentConfig("DEMO");

        assertThat(env.getCurrentEnvName()).isEqualTo("DEMO");
        assertThat(env.getMillisForJobStatusQuerySleepTime()).isEqualTo(1_000L);
        assertThat(env.getMillisForImportCycleTimeOut()).isEqualTo(10_000L);

        List<RestInvokerConfig> masterkonsole = env.getRestServiceConfigsForMasterkonsole();
        assertThat(masterkonsole).hasSize(1);
        assertThat(masterkonsole.get(0).getServiceURI()).isEqualTo("http://localhost:1234");
    }

    @Test
    void getRestServiceConfigsList_handlesUserAtPassDoubleColonUrl() {
        EnvironmentConfig env = new EnvironmentConfig("DEMO");
        List<RestInvokerConfig> configs = env.getRestServiceConfigsList(
                "user1@pwd1::http://host1:7077;user2@pwd2::http://host2:7078");

        assertThat(configs).hasSize(2);
        assertThat(configs.get(0).getServiceURI()).isEqualTo("http://host1:7077");
        assertThat(configs.get(0).getServiceUser()).isEqualTo("user1");
        assertThat(configs.get(0).getServicePassword()).isEqualTo("pwd1");
        assertThat(configs.get(1).getServiceURI()).isEqualTo("http://host2:7078");
    }

    @Test
    void getRestServiceConfigsList_handlesPlainHttpUrl() {
        EnvironmentConfig env = new EnvironmentConfig("DEMO");
        List<RestInvokerConfig> configs = env.getRestServiceConfigsList("http://host:7051");

        assertThat(configs).hasSize(1);
        assertThat(configs.get(0).getServiceURI()).isEqualTo("http://host:7051");
        assertThat(configs.get(0).getServiceUser()).isEmpty();
    }

    @Test
    void getRestServiceConfigsList_ignoresListStartingWithQuestionMark() {
        EnvironmentConfig env = new EnvironmentConfig("DEMO");
        List<RestInvokerConfig> configs = env.getRestServiceConfigsList("?disabled");
        assertThat(configs).isEmpty();
    }

}
