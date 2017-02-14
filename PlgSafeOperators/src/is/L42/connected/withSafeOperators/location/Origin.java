package is.L42.connected.withSafeOperators.location;

public class Origin {
  public String fileName() {
    return fileName;
    }
  public void fileName(String fileName) {
    this.fileName = fileName;
    }
  public int lineStart() {
    return lineStart;
    }
  public void lineStart(int line) {
    this.lineStart = line;
    }
  public int columnStart() {
    return columnStart;
    }
  public void columnStart(int column) {
    this.columnStart = column;
    }
  public int lineEnd() {
    return lineEnd;
    }
  public void lineEnd(int line) {
    this.lineEnd = line;
    }
  public int columnEnd() {
    return columnEnd;
    }
  public void columnEnd(int column) {
    this.columnEnd = column;
    }

  public Origin(String fileName, int lineStart, int lineEnd, int columnStart, int columnEnd) {
    super();
    this.fileName = fileName;
    this.lineStart = lineStart;
    this.lineEnd = lineEnd;
    this.columnStart = columnStart;
    this.columnEnd = columnEnd;
    }
  String fileName;
  int lineStart;
  int lineEnd;
  int columnStart;
  int columnEnd;
}
