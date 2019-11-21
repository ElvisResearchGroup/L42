package is.L42.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import is.L42.tools.InductiveSet;

class TestInductiveSet {

  @Test void test() {
    var is=new InductiveSet<Integer,Integer>(){
      @Override public void rule(Integer k,Consumer<Integer> s){
        if(k==0){
          install(1,i->{if(i<1000){s.accept(i+1);}});
          }
        if(k==1){
          install(0,i->{if(i<1000){s.accept(i+1);}});
        }
        s.accept(k);
        }
      };
    var res0=is.compute(0);
    assertEquals(res0.size(),501);
    assertTrue(res0.containsAll(List.of(0,2,4,8,10,100,500,1000)));
    assertTrue(is.isCached(0));
    assertTrue(is.isCached(1));
    var res1=is.compute(1);
    assertEquals(res1.size(),500);
    assertTrue(res1.containsAll(List.of(1,3,5,9,11,101,499,501,999)));
    assertTrue(res0==is.compute(0));
    assertTrue(res1==is.compute(1));
  }
  //e::= x| L | e e 
  //L::= (\x. e)
  //x::= a|b
  enum Term{E,X,L}
  @Test void testLambda() {
    int limit=20;
    var is=new InductiveSet<Term,String>(){
      @Override public void rule(Term k,Consumer<String> s){
        if(k.equals(Term.X)){s.accept("a");s.accept("b");}
        if(k.equals(Term.L)){
          install(Term.X,x->install(Term.E,e->s.accept("(\\"+x+"."+e+")")));
          }
        if(k.equals(Term.E)){
          install(Term.X,x->s.accept(x));
          install(Term.L,l->{
            if(l.length()>limit){return;}
            s.accept(l);
            });
          install(Term.E,e1->install(Term.E,e2->{
            var res="("+e1+" "+e2+")";
            if(res.length()>limit){return;}
            s.accept(res);
            }));
          }
        }
      };
    var res0=is.compute(Term.E);
    assertEquals(res0.size(),3258);
  }
  //e_xs::= x_xs| L_xs | e_xs e_xs 
  //L_xs::= (\x_ab. e_xs,x_ab)
  //x_xs::= a|b //if in xs  
  String group(String g,String tail){
    return g+tail.substring(1);
    }
  @Test void testLambdaNoFV() {
    String x="x",L="L",e="e",a="a",b="b";
    int limit=20;
    var is=new InductiveSet<String,String>(){
      @Override public void rule(String k,Consumer<String> s){
        if(k.startsWith(x)){
          if(k.contains(a)){s.accept(a);}
          if(k.contains(b)){s.accept(b);}
          }
        if(k.startsWith(L)){
          install(group("xab",k),x0->{
            var tail=k.substring(1);
            if(!tail.contains(x0)){tail+=x0;}
            install(e+tail,e0->s.accept("(\\"+x0+"."+e0+")"));
            });
          }
        if(k.startsWith(e)){
          install(group(x,k),x0->s.accept(x0));
          install(group(L,k),l->{
            if(l.length()>limit){return;}
            s.accept(l);
            });
          install(group(e,k),e1->install(group(e,k),e2->{
            var res="("+e1+" "+e2+")";
            if(res.length()>limit){return;}
            s.accept(res);
            }));
          }
        }
      };
    var res0=is.compute(e);
    assertEquals(res0.size(),304);
  }
}
