package is.L42.sifo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import is.L42.tools.General;

public abstract class Lattice<T> {
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
  Map<T, ArrayList<T>> inner;

  public Lattice(T top) {
    Map<T, ArrayList<T>> lattice = new HashMap<>();
    lattice.put(top, new ArrayList<>());
    this.inner = lattice;
  }
  
  public Lattice() {
    this.inner = new HashMap<>();
  }

  public void traverseInterfaceHierarchy(T top) {
    for (T nextLevel : lowerLevels(top)) {
      addElementToKey(nextLevel, top);
      traverseInterfaceHierarchy(nextLevel);
    }
  }

  /**
   * return the direct lower levels of p
   * @param p the higher node
   * @return list of lower nodes
   */
  protected abstract ArrayList<T> lowerLevels(T p);
  
  protected void putElement(T morePublic, T moreSecret) {
    inner.put(morePublic, new ArrayList<>(List.of(moreSecret)));
  }
  
  protected void putElements(T morePublic, ArrayList<T> moreSecret) {
    inner.put(morePublic, moreSecret);
  }
  
  public void addElementToKey(T morePublic, T moreSecret) {
    if (inner.containsKey(morePublic)) {
      inner.get(morePublic).add(moreSecret);
    } else {
      putElement(morePublic, moreSecret);
    }
  }
  
  public Set<T> getAllLevels() {
    return inner.keySet();
  }
  
  public ArrayList<T> getHigherLevels(T level) {
    return inner.get(level);
  }

  public abstract T getBottom();

  public T leastUpperBound(List<T> levels) {
    if (levels.size() == 1) {
      return levels.get(0);
    }
    if (levels.size() < 1) {throw General.bug();}
      
    T compareLevel = levels.get(0);
    for (int i = 1; i < levels.size(); i++) {
      T secondLevel = levels.get(i);
      if (compareLevel.equals(secondLevel)) {
        continue;
      }
      Map<T, Integer> upper1 = getUpper(compareLevel);
      Map<T, Integer> upper2 = getUpper(secondLevel);
      compareLevel = calculateLeast(upper1, upper2);
    }
    return compareLevel;
  }

  protected Map<T, Integer> getUpper(T level) {
    Map<T, Integer> uppers = new HashMap<T, Integer>();
    uppers.put(level, 0);
    for (T upperLevel : inner.get(level)) {
      uppers.put(upperLevel, 1);
      getUpper(uppers, upperLevel, 1);
    }
    return uppers;
  }
  
  protected void getUpper(Map<T, Integer> uppers, T level, int i) {
    for (T upperLevel : inner.get(level)) {
      uppers.put(upperLevel, i + 1);
      getUpper(uppers, upperLevel, i + 1);
    }
  }

  public boolean secondHigherThanFirst(T level1, T level2) {
    Map<T, Integer> uppers = getUpper(level1);
    return uppers.keySet().contains(level2);
  }

  protected T calculateLeast(Map<T, Integer> upper1, Map<T, Integer> upper2) {
    T least = null;
    int distance = Integer.MAX_VALUE;
    for (T level1 : upper1.keySet()) {
      if (upper2.containsKey(level1)) {
        int newDistance = upper1.get(level1) + upper2.get(level1);
        if (newDistance < distance) {
          least = level1;
          distance = newDistance;
        } else if (newDistance == distance) {
          throw new Error("ERROR SAME DISTANCE -> NO LATTICE: " + "current level: " + level1 + " has same distance as: " + least);
        }
      }
    }
    return least;
  }
}
