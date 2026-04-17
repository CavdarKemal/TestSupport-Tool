package de.creditreform.crefoteam.cte.tesun.xmlsearch.handler;

import de.creditreform.crefoteam.cte.tesun.xmlsearch.schnittstellen.IGroupByRow;

import java.util.*;

/**
 * Default-Implementierung für {@link IGroupByRow}
 */
public class GroupByRowDefaultImpl
implements IGroupByRow {

   private final List<String> componentsOfKey;
   private final int hashCode;

   public GroupByRowDefaultImpl(String... componentsOfKeyArray) {
      this((componentsOfKeyArray==null) ? null : Arrays.asList(componentsOfKeyArray));
   }

   public GroupByRowDefaultImpl(List<String> componentsOfKeyList) {
      if (componentsOfKeyList==null) {
         this.componentsOfKey = Collections.emptyList();
      }
      else {
         this.componentsOfKey = Collections.unmodifiableList(new ArrayList<>(componentsOfKeyList));
      }
      // Der HashCode wird immer benötigt und sofort berechnet...
      this.hashCode = this.componentsOfKey.hashCode();
   }

   @Override
   public List<String> getComponentsOfKey() {
      return componentsOfKey;
   }

   @Override
   public int compareTo(IGroupByRow o) {
      // Vorbereitungen...
      final int loopLength; // Länge der Vergleichs-Schleife
      final int resultAtLoopExit; // Ergebnis, wenn die Schleife keinen Unterschied feststellt
      int mySize = size();
      int otherSize = o.size();
      if (otherSize == mySize) {
         // Listen gleich lang...
         loopLength = mySize;
         resultAtLoopExit = 0;
      }
      else if (otherSize > mySize) {
         // die andere Liste ist länger...
         loopLength = mySize;
         resultAtLoopExit = -1;
      }
      else {
         // diese Liste ist länger...
         loopLength = otherSize;
         resultAtLoopExit = 1;
      }
      // Vergleichs-Schleife...
      for (int i=0; i<loopLength; i++) {
         String myComponent = getAt(i);
         String otherComponent = o.getAt(i);
         if (myComponent==null) {
            if (otherComponent!=null) {
               return -1;
            }
            // beide null (continue)
         }
         else if (otherComponent==null) {
            return 1;
         }
         else {
            int cmp = myComponent.compareTo(otherComponent);
            if (cmp!=0) {
               return cmp;
            }
         }
      }
      return resultAtLoopExit;
   }

   @Override
   public int size() {
      return componentsOfKey.size();
   }

   @Override
   public String getAt(int index) {
      return componentsOfKey.get(index);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof IGroupByRow)) return false;
      IGroupByRow that = (IGroupByRow) o;
      if (hashCode!=that.hashCode()) return false;
      return componentsOfKey.equals(that.getComponentsOfKey());
   }

   @Override
   public int hashCode() {
      return hashCode;
   }

   @Override
   public String toString() {
      return componentsOfKey.toString();
   }

}
