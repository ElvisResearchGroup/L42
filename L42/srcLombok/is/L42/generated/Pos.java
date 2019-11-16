package is.L42.generated;
import lombok.Value;
import lombok.experimental.Wither;
import java.net.URI;
@Value @Wither public class Pos implements java.io.Serializable{
  URI fileName; int line; int column;
  @Override public String toString(){
    return fileName.toString()+"\nline " + line() + ":" + column() + "\n";
    }}
