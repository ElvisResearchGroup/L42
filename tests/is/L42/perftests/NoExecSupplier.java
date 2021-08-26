/**
 * 
 */
package is.L42.perftests;

import java.util.function.Supplier;

/**
 * @author Claire
 *
 */
public interface NoExecSupplier<T> extends Supplier<T> {
  
  T supply() throws Exception;
  default T get() {
    try { return this.supply(); } 
    catch(Exception e) { throw new Error(e); }
    }

}
