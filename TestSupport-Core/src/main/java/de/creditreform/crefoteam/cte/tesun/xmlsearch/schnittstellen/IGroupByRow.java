package de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen;

import java.util.List;

/**
 * Beschreibung eines Teil-Ergebnisses aus einem Group-By. Implementierungen dieser Schnittstelle dienen als
 * Key einer Map und sind über {@link Comparable#compareTo(Object)} miteinander vergleichbar. Die Methoden
 * {@link Object#equals(Object)} und {@link Object#hashCode()} sind ebenfalls 'sinnvoll' implementiert.
 * Implementierungen dieser Schnittstelle sind immutable und damit Thread-safe.
 */
public interface IGroupByRow
extends Comparable<IGroupByRow> {

   /**
    * Lese die (unveränderbare) Liste der gefundenen Werte in der Reihenfolge ihrer Definition. Einzelne
    * oder alle Elemente in der Liste können null sein, nicht aber die gesamte Liste.
    */
   List<String> getComponentsOfKey();

   /**
    * Lese die Anzahl der Komponenten in der internen Liste
    */
   int size();

   /**
    * Lese die einzelne Komponente an der angegebenen Position
    * @param index position für den Zugriff
    */
   String getAt(int index);

}
