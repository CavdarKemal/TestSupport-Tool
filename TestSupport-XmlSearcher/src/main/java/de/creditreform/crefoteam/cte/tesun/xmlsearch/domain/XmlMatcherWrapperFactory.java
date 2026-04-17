package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IGroupByRow;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.xmlmatcher.XmlMatcher;

import java.util.Map;

public interface XmlMatcherWrapperFactory {
    /**
     * Verpacke die {@link XmlMatcher} aus der übergebenen Instanz von
     * ZipSearchData entsprechend des Anwendungs-Gebietes. In der aktuellen
     * Implementierung werden die Matcher um zusätzliche Kriterien für das
     * Durchsuchen von Crefo-Exporten ergänzt.
     */
    XmlMatcher wrapXmlMatcher(String crefoNrTagName, XmlMatcher plainMatcher);

    /**
     * Erzeuge einen String zur eindeutigen Identifikation des durchsuchten
     * XML. Mit Hilfe dieser Methode wird (nach dem Durchsuchen) aus den
     * Inhalten eines XML-Dokumentes ein das Dokument beschreibender Text
     * ermittelt. </br>
     * Der Aufrufer garantiert, dass die als Parameter übergebene Instanz
     * von {@link XmlMatcher} vorher durch die selbe Factory erzeugt wurde.
     * Die Factory kann daher eventuell benötigte Zustandasinformationen
     * innerhalb der selbst erzeugten Instanz von XmlMatcher speichern.
     * @param xmlMatcher Instanz, die zum Durchsuchen eines Dokumentes verwendet wurde
     */
    String getResultIdentification(XmlMatcher xmlMatcher);

    /**
     * Lese die Ergebnisse des Group-By
     * Der Aufrufer garantiert, dass die als Parameter übergebene Instanz
     * von {@link XmlMatcher} vorher durch die selbe Factory erzeugt wurde.
     * Die Factory kann daher eventuell benötigte Zustandasinformationen
     * innerhalb der selbst erzeugten Instanz von XmlMatcher speichern.
     * @param xmlMatcher Instanz, die zum Durchsuchen eines Dokumentes verwendet wurde
     */
    Map<IGroupByRow, Integer> getGroupByResults(XmlMatcher xmlMatcher);

}
