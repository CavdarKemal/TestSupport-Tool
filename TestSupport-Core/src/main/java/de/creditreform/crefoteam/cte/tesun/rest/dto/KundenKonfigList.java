package de.creditreform.crefoteam.cte.tesun.rest.dto;

import java.util.Collections;
import java.util.List;

public class KundenKonfigList {
    private final List<KundenKonfig> konfigs;

    public KundenKonfigList(List<KundenKonfig> konfigs) {
        this.konfigs = konfigs == null ? Collections.emptyList() : konfigs;
    }

    public List<KundenKonfig> getKonfigs() { return konfigs; }
}
