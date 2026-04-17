package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommandExecutorListenerTest {

    @Test
    void interfaceContract_acceptsLambdaAndForwardsString() {
        // Sanity: CommandExecutorListener ist ein Single-Method-Interface,
        // muss als Lambda implementierbar sein (Functional-Interface-Form).
        List<String> captured = new ArrayList<>();
        CommandExecutorListener listener = captured::add;

        listener.progress("step 1");
        listener.progress("step 2");

        assertThat(captured).containsExactly("step 1", "step 2");
    }
}
