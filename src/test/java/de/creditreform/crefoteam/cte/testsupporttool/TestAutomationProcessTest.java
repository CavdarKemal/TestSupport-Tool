package de.creditreform.crefoteam.cte.testsupporttool;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import de.creditreform.crefoteam.cte.statemachine.ProcessDefinition;
import de.creditreform.crefoteam.cte.statemachine.ProcessOutcome;
import de.creditreform.crefoteam.cte.tesun.TesunClientJobListener;
import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import de.creditreform.crefoteam.cte.testsupporttool.process.TestAutomationProcess;
import de.creditreform.crefoteam.cte.testsupporttool.rest.TesunRestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.assertj.core.api.Assertions.assertThat;

class TestAutomationProcessTest {

    private WireMockServer wireMockServer;
    private EnvironmentConfig env;
    private TesunRestService rest;

    @BeforeEach
    void startServer() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        String baseUrl = "http://localhost:" + wireMockServer.port();
        env = EnvironmentConfig.forDemo(baseUrl, 50L, 5_000L);
        rest = new TesunRestService(baseUrl);
    }

    @AfterEach
    void stopServer() {
        wireMockServer.stop();
    }

    @Test
    void demoMode_runsProcessToCompletionWithoutHittingRest() {
        Map<String, Object> vars = new HashMap<>();
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE, "PHASE_2");
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_TYPE, TestAutomationProcess.TEST_TYPE_PHASE1_AND_PHASE2);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE, Boolean.TRUE);

        ProcessDefinition definition = TestAutomationProcess.build(env, rest);
        ProcessOutcome outcome = new ProcessRunner().run(definition, vars);

        assertThat(outcome).isEqualTo(ProcessOutcome.COMPLETED);
        assertThat(wireMockServer.getAllServeEvents()).isEmpty();
    }

    @Test
    void liveMode_pollsRestUntilJobCompleted() {
        wireMockServer.stubFor(post(urlMatching("/jvms/.*/jobs/.*/start"))
                .willReturn(aResponse().withStatus(200)));
        wireMockServer.stubFor(get(urlMatching("/jobs/FROM_STAGING_INTO_CTE/last-execution"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"jobStatus\":\"COMPLETED\","
                                + "\"lastStartDate\":\"2099-01-01T00:00:00Z\","
                                + "\"lastCompletionDate\":\"2099-01-01T00:00:00Z\"}")));

        Map<String, Object> vars = new HashMap<>();
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_PHASE, "PHASE_2");
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_TYPE, TestAutomationProcess.TEST_TYPE_PHASE1_AND_PHASE2);
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE, Boolean.FALSE);

        ProcessDefinition definition = TestAutomationProcess.build(env, rest);
        ProcessOutcome outcome = new ProcessRunner().run(definition, vars);

        assertThat(outcome).isEqualTo(ProcessOutcome.COMPLETED);
    }

    @Test
    void wrongTestType_takesFalseBranch_failsDownstream_endsInFailedOutcome() {
        Map<String, Object> vars = new HashMap<>();
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_TEST_TYPE, "ANYTHING_ELSE");
        vars.put(TesunClientJobListener.UT_TASK_PARAM_NAME_DEMO_MODE, Boolean.FALSE);

        ProcessDefinition definition = TestAutomationProcess.build(env, rest);
        ProcessOutcome outcome = new ProcessRunner().run(definition, vars);

        assertThat(outcome).isEqualTo(ProcessOutcome.FAILED);
    }
}
