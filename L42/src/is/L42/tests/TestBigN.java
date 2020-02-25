package is.L42.tests;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

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
  }
