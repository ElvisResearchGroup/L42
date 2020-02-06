package is.L42.cache;

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
			  if(o instanceof KeyVarID || o instanceof String || o instanceof Number || o instanceof Character || o instanceof Boolean) {
			    hc += i++ * o.hashCode();
			    } else {
			    hc += i++ * System.identityHashCode(o);  
			    }
			  }
		  }
		return hc;
		}
	
	public boolean equals(Object o) {
		if(o instanceof KeyNorm2D) {
			KeyNorm2D key = (KeyNorm2D) o;
			if(key.lines.length == this.lines.length) {
				for(int i = 0; i < lines.length; i++) {
				  if(lines[i].length != key.lines[i].length) { return false; }
				  L42Cache<?> cache = (L42Cache<?>) lines[i][0];
				  if(cache != key.lines[i][0]) { return false; }
					for(int j = 1; j < lines[i].length; j++) {
					  if(lines[i][j] instanceof KeyVarID) {
					    if(!lines[i][j].equals(key.lines[i][j])) {
					      return false;
					      }
					  } else if(!cache.fieldCache(lines[i][j], j - 1).identityEquals(lines[i][j], key.lines[i][j])) {
					    return false;
					    }
					  }
					}
				return true;
				}
			}
		return false;
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
	      builder.append(line[j].toString().replace('\n', ' '));
	    }
	    builder.append('\n');
	    }
	  builder.delete(builder.length() - 1, builder.length());
	  return builder.toString();
	  }
	
}
