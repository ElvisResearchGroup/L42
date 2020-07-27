package is.L42.sifo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import is.L42.generated.P;

public class Lattice {
  // all the invariants: for example, no circles
  // make sure 1 top,
  // bottom marker?P p=P.pAny;
  // constructor takes in input the top
  // lub and subtype
  // look to the varius tests, like testSifo, and make a test suit for Lattice
  
  /**
   * Key: some Level
   * Value: All direct higher Levels
   */
  Map<P, List<P>> inner;

  public Lattice(P top) {
    Map<P, List<P>> lattice = new HashMap<>();
    lattice.put(top, List.of());
    this.inner = lattice;
    traverseInterfaceHierarchy(top);
    
  }

  private void traverseInterfaceHierarchy(P top) {
    for (P nextLevel : lowerLevels(top)) {
      addAdditionalElementToKey(nextLevel, top);
      traverseInterfaceHierarchy(nextLevel);
    }
  }

  /**
   * return the direct lower levels of p
   * @param p the higher node
   * @return list of lower nodes
   */
  private List<P> lowerLevels(P p) {
    return null;
  }
  
  void addElement(P morePublic, P moreSecret) {
    inner.put(morePublic, new ArrayList<>(List.of(moreSecret)));
  }
  
  void addElement(P morePublic, List<P> moreSecret) {
    inner.put(morePublic, moreSecret);
  }
  
  private void addAdditionalElementToKey(P morePublic, P moreSecret) {
    if (inner.containsKey(morePublic)) {
      inner.get(morePublic).add(moreSecret);
    } else {
      addElement(morePublic, moreSecret);
    }
  }
  
  Set<P> getAllLevels() {
    return inner.keySet();
  }
  
  List<P> getHigherLevels(P level) {
    return inner.get(level);
  }

  P getBottom() {
    return P.pAny;
  }

  public P leastUpperBound(List<P> levels) {
    if (levels.size() == 1) {
      return levels.get(0);
    } else if (levels.size() > 1) {
      P compareLevel = levels.get(0);
      for (int i = 1; i < levels.size(); i++) {
        P secondLevel = levels.get(i);
        if (compareLevel.equals(secondLevel)) {
          continue;
        }
        Map<P, Integer> upper1 = getUpper(compareLevel);
        Map<P, Integer> upper2 = getUpper(secondLevel);
        P leastUpperNode = calculateLeast(upper1, upper2);
        compareLevel = leastUpperNode;
      }
      return compareLevel;
    }
    return null;
  }

  private Map<P, Integer> getUpper(P level) {
    Map<P, Integer> uppers = new HashMap<P, Integer>();
    uppers.put(level, 0);
    for (P upperLevel : inner.get(level)) {
      uppers.put(upperLevel, 1);
      getUpper(uppers, upperLevel, 1);
    }
    return uppers;
  }
  
  private void getUpper(Map<P, Integer> uppers, P level, int i) {
    for (P upperLevel : inner.get(level)) {
      uppers.put(upperLevel, i + 1);
      getUpper(uppers, upperLevel, i + 1);
    }
  }

  public boolean SecondHigherThanFirst(P level1, P level2) {
    Map<P, Integer> uppers = getUpper(level1);
    return uppers.keySet().contains(level2);
  }

  private P calculateLeast(Map<P, Integer> upper1, Map<P, Integer> upper2) {
    P least = null;
    int distance = Integer.MAX_VALUE;
    for (P level1 : upper1.keySet()) {
      if (upper2.containsKey(level1)) {
        int newDistance = upper1.get(level1) + upper2.get(level1);
        if (newDistance < distance) {
          least = level1;
          distance = newDistance;
        } else if (newDistance == distance) {
          System.out.println("ERROR SAME DISTANCE -> NO LATTICE: " + "current level: " + level1 + " has same distance as: " + least);
        }
      }
    }
    return least;
  }
}
