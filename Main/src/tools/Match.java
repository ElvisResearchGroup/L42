package tools;
import java.util.ArrayList;
import java.util.function.Function;

public class Match<T2> {
   public static class Pattern<T1,T2> {
     public Pattern(Class<T1> clazz, Function<T1, T2> function) {
      this.clazz = clazz;this.function = function;}
    Class<T1> clazz; Function<T1, T2> function;}
  Object elem; private Match(Object elem){this.elem=elem;}
  public static <T> Match<T> of(Object elem){return new Match<T>(elem);}
  private ArrayList<Pattern<?,T2>> patterns=new ArrayList<Pattern<?,T2>>();
  @SuppressWarnings("unchecked")
  private <T1,T> T apply(Pattern<T1,T>p){
    return p.function.apply((T1)this.elem);
  }
  public T2 end() {
    return end(null);
  }
  public T2 end(RuntimeException e) {
    for (Pattern<?,T2> pattern : patterns){
      if (!pattern.clazz.isInstance(this.elem)){continue;}
      return apply(pattern);
    }
    if(e!=null){throw e;}
    throw new IllegalArgumentException("Cannot match "/* + this.elem*/);
  }
  public <T1>Match<T2> add(Class<T1>t,Function<T1, T2> f){this.patterns.add(new Pattern<T1,T2>(t,f));return this;}
}
  
  
