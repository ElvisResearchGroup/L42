package is.L42.cache;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The key/representation used to identify structurally equal objects.
 * Format:
 * it is a wrapper over an array of arrays of objects.
 * each line represents an object in a circular graph.
 * the first column is the cache of an object, the other columns are the fields of such object
 * Objects pointing outside of the circular graph are directly referenced.
 * Objects inside the circular graph are represented by a KeyVarID.
 * 
 * Thus, an object that is not circular is just a single line with its cache and its fields.
 * Note, the object is not present
 * 
 * @author Claire
 */
public class KeyNorm2D {  
  private final Object[][] lines;  
  public KeyNorm2D(Object[][] lines) {
    this.lines = Objects.requireNonNull(lines);
    }
  
  Object[][] lines(){return lines;}
  
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
        else{if(!L42CacheMap.identityEquals(l,kl)){return false;}}
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