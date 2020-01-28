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
		for(Object[] line : lines)
			hc += Arrays.hashCode(line);
		return hc;
		}
	
	@SuppressWarnings("unchecked") 
	public boolean equals(Object o) {
		if(o instanceof KeyNorm2D) {
			KeyNorm2D key = (KeyNorm2D) o;
			if(key.lines.length == this.lines.length) {
				for(int i = 0; i < lines.length; i++) {
				  if(lines[i].length != key.lines[i].length) { return false; }
				  Cache<?> cache = (Cache<?>) lines[i][0];
					for(int j = 1; j < lines[i].length; j++) {
					  if(lines[i][j] instanceof £KeyVarID) {
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
	
}
