package is.L42.sifo;

import java.util.ArrayList;
import java.util.List;

import is.L42.generated.P;

public class LoopTest {

  public Lattice lattice;
  public List<TrackLevel> trackedLevels = new ArrayList<TrackLevel>();

  public LoopTest(Lattice lattice) {
    this.lattice = lattice;
    TrackLevel trackedLevel = null;

    for (P level : lattice.getAllLevels()) {
      trackedLevel = new TrackLevel(level, false);
      trackedLevels.add(trackedLevel);
    }
  }

  public boolean TestForLoop() {
    P bottom = lattice.getBottom();
    if (bottom == null) {
      System.out.println("Error finding bottom");
      return false;
    }
    TrackLevel tracked = getTrackedLevelFromP(bottom);
    tracked.setVisited(true);
    return traverseUpwards(tracked);
  }

  public boolean traverseUpwards(TrackLevel lowerTrackLevel) {
    boolean result = false;
    for (P higherLevelP : lattice.getHigherLevels(lowerTrackLevel.getLevel())) {
      TrackLevel higherLevel = getTrackedLevelFromP(higherLevelP);
      if (higherLevel.isVisited()) {
        result = true;
        return true;
      } else {
        higherLevel.setVisited(true);
        result = traverseUpwards(higherLevel);
        higherLevel.setVisited(false);
      }
    }
    lowerTrackLevel.setVisited(false);
    return result;
  }

  private TrackLevel getTrackedLevelFromP(P level) {
    for (TrackLevel trackedLevel : trackedLevels) {
      if (trackedLevel.getLevel() == level) {
        return trackedLevel;
      }
    }
    return null;
  }
}
