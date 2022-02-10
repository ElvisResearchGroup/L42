package is.L42.tests;


import org.junit.jupiter.api.Test;

import is.L42.common.Constants;
import is.L42.common.Err;
import is.L42.common.Parse;
import is.L42.main.Settings;

public class TestSettings {
  void parseOk(String s,String expected){
    Settings ss=Parse.sureSettings(Constants.dummy, s);
   Err.strCmp(ss.toString().replace(" ",""), expected.replace(" ","").replace("\n",""));
    }
  @Test public void empty0(){parseOk("""
    ""","""
    Settings[options=-Xss128M -Xms256M -Xmx2G, permissions={}]
    """);}
  @Test public void empty1(){parseOk("""
      //nothing
      ""","""
      Settings[options=-Xss128M -Xms256M -Xmx2G, permissions={}]
      """);}
  @Test public void empty2(){parseOk("""
      /*nothing*/,,
      ""","""
      Settings[options=-Xss128M -Xms256M -Xmx2G, permissions={}]
      """);}
  @Test public void opt1(){parseOk("""
      maxStackSize = 10M
      ""","""
      Settings[options=-Xss10M -Xms256M -Xmx2G, permissions={}]
      """);}
  @Test public void opt2(){parseOk("""
      maxMemorySize = 20G
      ""","""
      Settings[options=-Xss128M -Xms256M -Xmx20G, permissions={}]
      """);}
  @Test public void opt3(){parseOk("""
      initialMemorySize = 300M
      ""","""
      Settings[options=-Xss128M -Xms300M -Xmx2G, permissions={}]
      """);}
  @Test public void opt4(){parseOk("""
      maxStackSize = 10M
      maxMemorySize = 20G
      initialMemorySize = 300M
      ""","""
      Settings[options=-Xss10M -Xms300M -Xmx20G, permissions={}]
      """);}
  @Test public void opt5(){parseOk("""
      initialMemorySize = 300M
      maxStackSize = 10M
      maxMemorySize = 20G
      ""","""
      Settings[options=-Xss10M -Xms300M -Xmx20G, permissions={}]
      """);}
  @Test public void sec1(){parseOk("""
      Foo.Bar = [L42.is/FileSystem] [L42.is/bb]
      ""","""
      Settings[options=-Xss128M-Xms256M-Xmx2G,permissions={
        Foo.Bar=[L42.is/FileSystem,L42.is/bb]}]
      """);}
  @Test public void sec2(){parseOk("""
      Foo.Bar::2 = [L42.is/FileSystem] [L42.is/bb]
      maxMemorySize = 20G
      Foo.Bar::3 = [L42.is/FileSystem]
      ""","""
      Settings[options=-Xss128M-Xms256M-Xmx20G,permissions={
        Foo.Bar::2=[L42.is/FileSystem,L42.is/bb];
        Foo.Bar::3=[L42.is/FileSystem]
        }]
      """);}
  }