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
  @Test public void fromDouble(){
    //assertEquals(L42£BigRational.from(0d),L42£BigRational.from("0"));
    //assertEquals(L42£BigRational.from(1d),L42£BigRational.from("1"));
    //assertEquals(L42£BigRational.from(2d),L42£BigRational.from("2"));
    //assertEquals(L42£BigRational.from(3d),L42£BigRational.from("3"));
    assertEquals(L42£BigRational.from(4d),L42£BigRational.from("4"));
    assertEquals(L42£BigRational.from(8d),L42£BigRational.from("8"));
    assertEquals(L42£BigRational.from(1.23456789d),L42£BigRational.from("1.23456789"));
    assertEquals(L42£BigRational.from(12d),L42£BigRational.from("12"));
  }
  }
