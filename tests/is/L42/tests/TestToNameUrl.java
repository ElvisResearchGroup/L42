package is.L42.tests;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.ToNameUrl;
public class TestToNameUrl {
  void check(String in,String out) {
    Err.strCmp(ToNameUrl.of(in,List.of())+"",out);
    }
  void checkErr(String in) {
    try{ToNameUrl.of(in,List.of());fail();}
    catch(EndError.UrlNotExistent une) {}
    }
  @Test public void testBase(){check("a",
      "[###]localhost\\a.L42, fullPath=[###]/localhost/a.L42]");}
  @Test public void testEmpty(){checkErr(""
      );}
  @Test public void testSpaces1(){checkErr(" "
      );}
  @Test public void testSpaces2(){checkErr("foo bar"
      );}
  @Test public void testFile1(){check("a/b.L42",
      "[###]localhost"+File.separator+"a"+File.separator+"b.L42, fullPath=[###]/localhost/a/b.L42]");}
  @Test public void testFile2(){check("a/b",
      "[###]localhost"+File.separator+"a"+File.separator+"b.L42, fullPath=[###]/localhost/a/b.L42]");}
  @Test public void testFile3(){check("file://a/b",
      "[###]localhost"+File.separator+"a"+File.separator+"b.L42, fullPath=[###]/localhost/a/b.L42]");}

  @Test public void testUrl1(){check("www.google.com/nope/b.L42",
      "NameUrl[fullName=https://www.google.com/nope/b.L42, fullPath=https://www.google.com/nope/b.L42]");}
  @Test public void testUrl2(){check("http://www.google.com/nope/b/",
      "NameUrl[fullName=https://www.google.com/nope/b.L42, fullPath=https://www.google.com/nope/b.L42]");}
  @Test public void testUrl3(){check("https://www.google.com/nope/b",
      "NameUrl[fullName=https://www.google.com/nope/b.L42, fullPath=https://www.google.com/nope/b.L42]");}

  //    https://github.com/example42gdrive/Example1/blob/main/FileSystem.L42?raw=true
  @Test public void testGitUrl1(){check("github.com/name/repo/b.L42",
      "NameUrl[fullName=https://github.com/name/repo/blob/main/b.L42?raw=true, fullPath=https://github.com/name/repo/blob/main/b.L42?raw=true]");}
  @Test public void testGitUrl2(){check("https://github.com/name/repo/blob/main/b",
      "NameUrl[fullName=https://github.com/name/repo/blob/main/b.L42?raw=true, fullPath=https://github.com/name/repo/blob/main/b.L42?raw=true]");}
  @Test public void testGitUrl3(){check("https://github.com/name/repo/blob/dfe3324/b",
      "NameUrl[fullName=https://github.com/name/repo/blob/dfe3324/b.L42?raw=true, fullPath=https://github.com/name/repo/blob/dfe3324/b.L42?raw=true]");}

  @Test public void test42Url1(){check("L42.is/AdamsTowel",
      "NameUrl[fullName=https://github.com/Language42/is/blob/main/AdamsTowel.L42?raw=true, fullPath=https://github.com/Language42/is/blob/main/AdamsTowel.L42?raw=true]");}
  }