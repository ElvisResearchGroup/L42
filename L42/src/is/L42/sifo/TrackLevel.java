package is.L42.sifo;

public class TrackLevel<T> {

  private T level;
  private boolean visited;
  private boolean atLeastOnceVisited;

  public TrackLevel(T level, boolean visited) {
    this.level = level;
    this.visited = visited;
    this.setAtLeastOnceVisited(false);
  }
  
  public TrackLevel(T level) {
    this.level = level;
    this.visited = false;
    this.setAtLeastOnceVisited(false);
  }
  
  public boolean isVisited() {
    return this.visited;
  }

  public void setVisited(boolean visited) {
    this.visited = visited;
  }


  public T getLevel() {
    return this.level;
  }

  public boolean isAtLeastOnceVisited() {
    return atLeastOnceVisited;
  }

  public void setAtLeastOnceVisited(boolean atLeastOnceVisited) {
    this.atLeastOnceVisited = atLeastOnceVisited;
  }
}
