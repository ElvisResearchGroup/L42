package is.L42.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import is.L42.constraints.FreshNames;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.top.Cache;
import is.L42.top.CachedTop;
import is.L42.top.Init;
import is.L42.top.State;
import is.L42.top.Top;
import is.L42.translationToJava.Loader;

public class TestCachingCases {
@Test
void testAgain(){
  Resources.clearRes();
  String s="{A0={B1={} B2={} B3={}}}";
  Cache res=Top.topCache(Cache.of(),s);
  String exe=Resources.notifiedCompiledNC();
  Resources.clearRes();
  Cache res2=Top.topCache(res,s);
  String exe2=Resources.notifiedCompiledNC();
  assertEquals("B1\nB2\nB3\nA0\n",exe);
  assertEquals("B1\nB2\nB3\nA0\n",exe2);
  assertEquals(exe,exe2);//should be very different
  }
}
