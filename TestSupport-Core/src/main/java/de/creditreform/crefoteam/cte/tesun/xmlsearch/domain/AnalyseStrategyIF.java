package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

public interface AnalyseStrategyIF {

    DuplicateExportInfo doAnalyse(SearchSpecification searchSpecification) throws Exception;
}
