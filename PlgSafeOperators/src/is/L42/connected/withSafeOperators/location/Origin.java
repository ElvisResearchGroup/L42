package is.L42.connected.withSafeOperators.location;

import java.nio.file.Path;

import facade.L42;

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
  //public boolean equalequal(Object that){return this.equals(that);}
  public String toS(){
    String name = L42.root.toString();
    String f=this.fileName();
    if(f.startsWith(name)){
      f=f.substring(name.length(), f.length());
      }
    return "Origin:"+f+
            "\nlines " + lineStart +
            " to " + lineEnd +
            "; colums " + columnStart +
            " to " + columnEnd;
    }
  String fileName;
  int lineStart;
  int lineEnd;
  int columnStart;
  int columnEnd;
  //---Generated
  @Override
public int hashCode() {
final int prime = 31;
int result = 1;
result = prime * result + columnEnd;
result = prime * result + columnStart;
result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
result = prime * result + lineEnd;
result = prime * result + lineStart;
return result;
}
@Override
public boolean equals(Object obj) {
if (this == obj)
    return true;
if (obj == null)
    return false;
if (getClass() != obj.getClass())
    return false;
Origin other = (Origin) obj;
if (columnEnd != other.columnEnd)
    return false;
if (columnStart != other.columnStart)
    return false;
if (fileName == null) {
if (other.fileName != null)
    return false;
} else if (!fileName.equals(other.fileName))
    return false;
if (lineEnd != other.lineEnd)
    return false;
if (lineStart != other.lineStart)
    return false;
return true;
}
}
