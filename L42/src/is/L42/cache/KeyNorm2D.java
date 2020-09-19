package is.L42.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * The key/representation used to identify structurally equal objects.
 * 
 * @author Claire
 */
public class KeyNorm2D {
  
  private final Object[][] lines;
  
  public KeyNorm2D(Object[][] lines) {
    this.lines = Objects.requireNonNull(lines);
    }
  
  Object[][] lines() {
    return lines;
    }
  
  public int hashCode() {
    int hc = 0;
    int i = 1;
    for(Object[] line : lines) {
      for(Object o : line) {
        if(o==null){i++; continue;}
        if(o instanceof ArrayList){hc += i++ * System.identityHashCode(o);}
        else{hc += i++ * o.hashCode();}
        }
      }
    return hc;
    }
  
  public boolean equals(Object o) {
    if(!(o instanceof KeyNorm2D)){return false;}
    KeyNorm2D key = (KeyNorm2D) o;
    if(key.lines.length != lines.length) {return false;}
    for(int i = 0; i < lines.length; i++){
      int length=lines[i].length;
      int klength=key.lines[i].length;
      if(length != klength){return false;}
      L42Cache<?> cache = (L42Cache<?>) lines[i][0];
      if(!cache.equals(key.lines[i][0])){return false;}
      for(int j = 1; j < length; j++){
        var l=lines[i][j];
        var kl=key.lines[i][j];
        if(l instanceof KeyVarID){if(!l.equals(kl)){return false;}}
        else{if(!cache.fieldCache(l,j-1).identityEquals(l,kl)){return false;}}
        }
      }
    return true;
    }
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for(int i = 0; i < lines.length; i++) {
      Object[] line = lines[i];
      builder.append('v');
      builder.append(i);
      builder.append(" : ");
      builder.append(((L42Cache<?>) line[0]).typename());
      for(int j = 1; j < line.length; j++) {
        builder.append(' ');
        builder.append(line[j] == null ? "null" : line[j].toString().replace('\n', ' '));
      }
      builder.append('\n');
      }
    builder.delete(builder.length() - 1, builder.length());
    return builder.toString();
    }
  
}
