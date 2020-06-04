package is.L42.tests;

import static is.L42.tools.General.L;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import is.L42.constraints.FreshNames;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.top.CachedTop;
import is.L42.top.Init;
import is.L42.top.State;
import is.L42.top.Top;
import is.L42.translationToJava.Loader;

public class TestCachingCases {
@Test void base(){pass("{A0={B1={} B2={} B3={}}}",
  "B1\nB2\nB3\nA0\n","");}
@Test void baseChange(){pass(
  "{A0={B1={D={}} B2={} B3={}}}",
  "{A0={B1={D={}} B2={C={}} B3={}}}",
  "D\nB1\nB2\nB3\nA0\n",
  "B1\nC\nB2\nB3\nA0\n");}

void pass(String code,String expectedExe1,String expectedExe2){
  pass(code,code,expectedExe1,expectedExe2);
  }
void pass(String code1,String code2,String expectedExe1,String expectedExe2){
  Resources.clearRes();
  var cache1=new CachedTop(L(),L());
  Top.topCache(cache1,code1);
  String exe=Resources.notifiedCompiledNC();
  Resources.clearRes();
  var cache2=cache1.toNextCache();
  Top.topCache(cache2,code2);
  String exe2=Resources.notifiedCompiledNC();
  assertEquals(expectedExe1,exe);
  assertEquals(expectedExe2,exe2);  
  }
}
