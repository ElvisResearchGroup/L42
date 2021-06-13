package is.L42.tests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import is.L42.numbers.L42£BigRational;

public class TestBigN {
  @Test public void fromSBigInteger(){
    assertEquals("12",new BigInteger("12").toString());
    assertEquals("-12",new BigInteger("-12").toString());
    }

  @Test public void fromS(){
    //need to test sum and multiplication
    assertEquals("12",L42£BigRational.from("12").toString());
    assertEquals("-12",L42£BigRational.from("-12").toString());
    assertEquals("-4",L42£BigRational.from("-12/3").toString());
    assertEquals("-11/3",L42£BigRational.from("-11/3").toString());
    assertEquals("-11/3",L42£BigRational.from("-11.0/3.0").toString());
    assertEquals("-111/31",L42£BigRational.from("-11.1/3.1").toString());
    assertEquals("111",L42£BigRational.from("111").toString());
    assertEquals(L42£BigRational.from("123456"),L42£BigRational.from("123456"));
    assertFalse(L42£BigRational.from("123456").equals(L42£BigRational.from("123455")));
    }
  @Test public void fromDouble(){
    IntStream.range(0,10000).forEach(i->fromDouble(i));
    assertEquals(L42£BigRational.from(100_000_000_000_000_000d),L42£BigRational.from("100000000000000000"));
    assertEquals(L42£BigRational.from(100000000000000005366162204393472d),L42£BigRational.from("100000000000000005366162204393472"));
    assertEquals(L42£BigRational.from(0.0009765625d),L42£BigRational.from("0.0009765625"));
//    assertEquals(L42£BigRational.from(0.0000095735d),L42£BigRational.from("0.0000095735"));
//    assertEquals(L42£BigRational.from(0.00000762939d),L42£BigRational.from("0.00000762939"));
    assertEquals(L42£BigRational.from(
      0.0000076293900000000003182895440267952125168449128977954387664794921875d),
      L42£BigRational.from(
      "0.0000076293900000000003182895440267952125168449128977954387664794921875"
      ));        
    }
  public void fromDouble(int i){
    List<Double>exacts=List.of(0d,0.5d,0.25d,0.75d,0.125,0.875);//0.225d,0.775d);//fails with 0.275 too
    for(double d1:exacts){
      for(double d2:exacts){
        double dip1=((double)i)+d1+d2;
        double dim1=((double)-i)+d1+d2;
        double dip2=((double)i)+d1-d2;
        double dim2=((double)-i)+d1-d2;
        assertEquals(L42£BigRational.from(dip1),L42£BigRational.from(dip1+""));
        assertEquals(L42£BigRational.from(dim1),L42£BigRational.from(dim1+""));
        assertEquals(L42£BigRational.from(dip2),L42£BigRational.from(dip2+""));
        assertEquals(L42£BigRational.from(dim2),L42£BigRational.from(dim2+""));
        }
      }
    }
  }
