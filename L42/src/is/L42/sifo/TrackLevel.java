package is.L42.sifo;

import is.L42.generated.P;

public class TrackLevel {

  private P level;
  private boolean visited;

  public TrackLevel(P level, boolean visited) {
    this.level = level;
    this.visited = visited;
  }
  
  public boolean isVisited() {
    return this.visited;
  }

  public void setVisited(boolean visited) {
    this.visited = visited;
  }


  public P getLevel() {
    return this.level;
  }
}
