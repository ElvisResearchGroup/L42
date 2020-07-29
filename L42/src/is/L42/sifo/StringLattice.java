package is.L42.sifo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import is.L42.tools.General;

public class StringLattice extends Lattice<String> {

  @Override
  protected ArrayList<String> lowerLevels(String p) {
    return null;
  }

  @Override
  public String getBottom() {
    List<String> copiedKeys = new ArrayList<String>(inner.keySet());
    for (String level : inner.keySet()) {
      for (String upperLevel : inner.get(level)) {
        if (copiedKeys.contains(upperLevel)) {
          copiedKeys.remove(upperLevel);
        }
      }
    }
    if (copiedKeys.size() == 1) {
      return copiedKeys.get(0);
    }
    if (copiedKeys.size() > 1) {
      throw new Error("Lattice has more than two bottoms.");
    } else {
      throw new Error("Lattice has no bottom.");
    }
  }

  /**
   * Read Lattice of the form A->B,C,D;B->E;C->E;D->E;
   * 
   * @param inputLattice
   */
  public void readLattice(String inputLattice) {
    String[] lines = inputLattice.split(";");
    for (String line : lines) {
      String[] tokens = line.split("->");
      if (tokens.length > 1) {
        String[] upperLevels = tokens[1].split(",");
        putElements(tokens[0], new ArrayList<>(Arrays.asList(upperLevels)));
      } else {
        putElements(tokens[0], new ArrayList<>());
      }
    }
  }

}
