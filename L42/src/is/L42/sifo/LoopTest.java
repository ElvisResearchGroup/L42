package is.L42.sifo;

import java.util.ArrayList;
import java.util.List;

import is.L42.tools.General;

public class LoopTest<T> {

  public Lattice<T> lattice;
  public List<TrackLevel<T>> trackedLevels = new ArrayList<TrackLevel<T>>();

  public LoopTest(Lattice<T> lattice) {
    this.lattice = lattice;

    for (T level : lattice.getAllLevels()) {
      TrackLevel<T> trackedLevel = new TrackLevel<>(level);
      trackedLevels.add(trackedLevel);
    }
  }

  public boolean testForLoop() {
    T bottom = lattice.getBottom();
    if (bottom == null) {
      throw new Error("Error finding bottom");
    }
    TrackLevel<T> tracked = getTrackedLevelFromP(bottom);
    tracked.setVisited(true);
    tracked.setAtLeastOnceVisited(true);
    boolean hasLoop = traverseUpwards(tracked);
    return moreThanOneLattice() || hasLoop;
  }

  private boolean moreThanOneLattice() {
    for (TrackLevel<T> trackLevel : trackedLevels) {
      if (trackLevel.isAtLeastOnceVisited() == false) {
        return true;
      }
    }
    return false;
  }

  public boolean traverseUpwards(TrackLevel<T> lowerTrackLevel) {
    boolean result = false;
    for (T higherLevelP : lattice.getHigherLevels(lowerTrackLevel.getLevel())) {
      TrackLevel<T> higherLevel = getTrackedLevelFromP(higherLevelP);
      if (higherLevel.isVisited()) {
        return true;
      } else {
        higherLevel.setVisited(true);
        higherLevel.setAtLeastOnceVisited(true);
        result = traverseUpwards(higherLevel);
        higherLevel.setVisited(false);
      }
    }
    lowerTrackLevel.setVisited(false);
    return result;
  }

  private TrackLevel<T> getTrackedLevelFromP(T level) {
    for (TrackLevel<T> trackedLevel : trackedLevels) {
      if (trackedLevel.getLevel().equals(level)) {
        return trackedLevel;
      }
    }
    throw General.unreachable();
  }
}
