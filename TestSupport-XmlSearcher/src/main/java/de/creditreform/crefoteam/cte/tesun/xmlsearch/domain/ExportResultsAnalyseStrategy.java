package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import java.util.regex.Pattern;

public enum ExportResultsAnalyseStrategy {
    EH_LOESCHSAETZE("EH-Löschsätze", new AnalyseStrategyEHMehrfacheLoeschsaetze(Pattern.compile("loeschsatz"))),
    BVD_LOESCHSAETZE("BVD-Scheiben", new AnalyseStrategyBvDBlabla());

    private final String strategyName;
    private final AnalyseStrategyIF analyseStrategyImplementor;

    ExportResultsAnalyseStrategy(String strategyName, AnalyseStrategyIF analyseStrategyImplementor) {
        this.strategyName = strategyName;
        this.analyseStrategyImplementor = analyseStrategyImplementor;
    }

    @Override
    public String toString() {
        return strategyName;
    }

    public DuplicateExportInfo doAnalyse(SearchSpecification searchSpecification) throws Exception {
        return analyseStrategyImplementor.doAnalyse(searchSpecification);
    }
}
