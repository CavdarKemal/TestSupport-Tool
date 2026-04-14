package de.creditreform.crefoteam.cte.tesun.util;

/**
 * Schalen-Port aus {@code testsupport_client.tesun_util}. Nur die API,
 * die von {@link TestCustomer}/{@link TestScenario} aufgerufen wird. Volle
 * Logik kommt später, wenn die Handler über den Spike hinaus aktiviert werden.
 */
public class TestCrefo {

    private long itsqTestCrefoNr;
    private boolean activated = true;
    private boolean shouldBeExported = true;

    public TestCrefo() { }

    public TestCrefo(long itsqTestCrefoNr) {
        this.itsqTestCrefoNr = itsqTestCrefoNr;
    }

    public long getItsqTestCrefoNr() { return itsqTestCrefoNr; }

    public void setItsqTestCrefoNr(long itsqTestCrefoNr) { this.itsqTestCrefoNr = itsqTestCrefoNr; }

    public boolean isActivated() { return activated; }

    public void setActivated(boolean activated) { this.activated = activated; }

    public boolean isShouldBeExported() { return shouldBeExported; }

    public void setShouldBeExported(boolean shouldBeExported) { this.shouldBeExported = shouldBeExported; }
}
