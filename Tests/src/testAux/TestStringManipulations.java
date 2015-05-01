package testAux;

import org.testng.Assert;
import org.testng.annotations.Test;

import auxiliaryGrammar.EncodingHelper;

public class TestStringManipulations {
  @Test
  public void parseStringUnicode() {
    Assert.assertEquals(EncodingHelper.parseStringUnicode("fuffa\\u2222bar"),"fuffa\u2222bar");
    
    Assert.assertEquals(EncodingHelper.produceStringUnicode("fuffa\u2222bar"),"fuffa\\u2222bar");
  }
}
