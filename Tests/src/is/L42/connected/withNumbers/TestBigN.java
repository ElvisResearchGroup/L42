package is.L42.connected.withNumbers;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class TestBigN {
@Test
  public void fromSBigInteger(){
    assertEquals("12",new BigInteger("12").toString());
    assertEquals("-12",new BigInteger("-12").toString());
    }

    @Test
  public void fromS(){
    assertEquals("12",BigRational.from("12").toString());
    assertEquals("-12",BigRational.from("-12").toString());
    assertEquals("-4",BigRational.from("-12/3").toString());
    assertEquals("-11/3",BigRational.from("-11/3").toString());
    assertEquals("-11/3",BigRational.from("-11.0/3.0").toString());
    assertEquals("-111/31",BigRational.from("-11.1/3.1").toString());
    assertEquals("111",BigRational.from("111").toString());
    assertEquals(BigRational.from("123456"),BigRational.from("123456"));
    assertFalse(BigRational.from("123456").equals(BigRational.from("123455")));
    }

}
