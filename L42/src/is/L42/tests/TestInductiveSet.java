package is.L42.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import is.L42.tools.InductiveSet;

class TestInductiveSet {

  @Test void test() {
    var is=new InductiveSet<Integer,Integer>((k,s,installer)->{
      if(k==0){
        s.accept(0);
        installer.accept(1,i->{if(i<1000){s.accept(i+1);}});
        }
      if(k==1){
        s.accept(1);
        installer.accept(0,i->{if(i<1000){s.accept(i+1);}});
        }
      });
    System.out.println(is.compute(0));
  }

}
