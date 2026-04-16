package de.creditreform.crefoteam.cte.testsupporttool.gui.domain;

import de.creditreform.crefoteam.cte.rest.RestInvokerConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnvironmentTest {

    @Test
    void constructor_setsEnvNameAndToStringMatches() {
        Environment env = new Environment("ENE");
        assertThat(env.getEnvName()).isEqualTo("ENE");
        assertThat(env.toString()).isEqualTo("ENE");
    }

    @Test
    void copyConstructor_copiesOnlyEnvName() {
        Environment src = new Environment("ENE");
        src.setDownloadPath("/tmp/dl");
        src.setRestInvokerConfigDownload(new RestInvokerConfig("http://x", "u", "p"));

        Environment copy = new Environment(src);

        assertThat(copy.getEnvName()).isEqualTo("ENE");
        // Andere Felder werden bewusst NICHT kopiert (Original-Verhalten).
        assertThat(copy.getDownloadPath()).isNull();
        assertThat(copy.getRestInvokerConfigDownload()).isNull();
    }

    @Test
    void settersAndGetters_roundtrip() {
        Environment env = new Environment("ABE");
        env.setStatePropsFileName("state.properties");
        env.setConfigPropsFileName("config.properties");
        env.setSearchCfgFileName("search.cfg");
        env.setSearchResultsPath("/results");
        env.setDownloadPath("/dl");
        env.setGeneratedPath("/gen");
        env.setExportedPath("/exp");
        env.setCollectedPath("/col");
        env.setRegeneratedPath("/regen");
        env.setTemplateRefPath("/tmpl");
        env.setCheckedPath("/chk");
        RestInvokerConfig dl = new RestInvokerConfig("http://dl", "u1", "p1");
        RestInvokerConfig up = new RestInvokerConfig("http://up", "u2", "p2");
        RestInvokerConfig ex = new RestInvokerConfig("http://ex", "u3", "p3");
        env.setRestInvokerConfigDownload(dl);
        env.setRestInvokerConfigUpload(up);
        env.setRestInvokerConfigExports(ex);
        TestCustomer c = new TestCustomer("C01", "C01");
        env.setCustomerTestInfos(List.of(c));

        assertThat(env.getStatePropsFileName()).isEqualTo("state.properties");
        assertThat(env.getConfigPropsFileName()).isEqualTo("config.properties");
        assertThat(env.getSearchCfgFileName()).isEqualTo("search.cfg");
        assertThat(env.getSearchResultsPath()).isEqualTo("/results");
        assertThat(env.getDownloadPath()).isEqualTo("/dl");
        assertThat(env.getGeneratedPath()).isEqualTo("/gen");
        assertThat(env.getExportedPath()).isEqualTo("/exp");
        assertThat(env.getCollectedPath()).isEqualTo("/col");
        assertThat(env.getRegeneratedPath()).isEqualTo("/regen");
        assertThat(env.getTemplateRefPath()).isEqualTo("/tmpl");
        assertThat(env.getCheckedPath()).isEqualTo("/chk");
        assertThat(env.getRestInvokerConfigDownload()).isSameAs(dl);
        assertThat(env.getRestInvokerConfigUpload()).isSameAs(up);
        assertThat(env.getRestInvokerConfigExports()).isSameAs(ex);
        assertThat(env.getCustomerTestInfos()).containsExactly(c);
    }

    @Test
    void equals_sameEnvName_isEqual() {
        assertThat(new Environment("ENE")).isEqualTo(new Environment("ENE"));
        assertThat(new Environment("ENE")).isNotEqualTo(new Environment("ABE"));
    }

    @Test
    void equals_null_returnsFalseInsteadOfThrowing() {
        // Original-Verhalten: explicite null-Pruefung (statt instanceof).
        assertThat(new Environment("ENE").equals(null)).isFalse();
    }

    @Test
    void equals_otherType_throwsClassCastException() {
        // Original-Verhalten: harter Cast auf Environment ohne instanceof — bewusst portiert.
        Environment env = new Environment("ENE");
        assertThatThrownBy(() -> env.equals("nope"))
                .isInstanceOf(ClassCastException.class);
    }

    @Test
    void dumpEnvironmentInfo_default_noTrailingBrace() {
        // "Default"-Spezialfall im Original: KEIN "\n<praefix>}" angehaengt.
        String dump = new Environment("Default").dumpEnvironmentInfo("  ");
        assertThat(dump).isEqualTo("Default");
    }

    @Test
    void dumpEnvironmentInfo_namedEnv_appendsClosingBraceWithPrefix() {
        String dump = new Environment("ENE").dumpEnvironmentInfo("  ");
        assertThat(dump).isEqualTo("ENE\n  }");
    }

    @Test
    void protectedEnvironment_isAssignableToEnvironment() {
        Environment.ProtectedEnvironment pe = new Environment.ProtectedEnvironment("ENE");
        assertThat(pe).isInstanceOf(Environment.class);
        assertThat(pe.getEnvName()).isEqualTo("ENE");
    }
}
