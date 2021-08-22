package is.L42.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import java.util.*;

public class TestCheckFileSystemPortable {
 static void test(Path path,Map<Path,List<Path>> paths, String expected){
   var testObj= new is.L42.top.CheckFileSystemPortable(path){
     public void walkIn1(Path path){paths.getOrDefault(path,List.of()).forEach(this::checkSystemIndependentPath); }
     public boolean isDirectory(Path top){ return true;}
     };
   assertEquals(expected,testObj.toString());
   }
 public static final Path a=Path.of("a");
 public static final Path a_b=Path.of("a","b");
 public static final Path a_d=Path.of("a","d");
 public static final Path a_B=Path.of("a","B");
 public static final Path a_D=Path.of("a","D"); 
 public static final Path a_b_c=Path.of("a","b","c");
 public static final Path a_b_=Path.of("a","b","");
 public static final Path a_b_dot=Path.of("a","b",".");
 public static final Path a_b_dotDot=Path.of("a","b","..");
 public static final Path a_b_dotFoo=Path.of("a","b",".foo");
 @Test void t1(){test(a_b,Map.of(a_b,List.of()),"""
     okPaths=[]
     badPaths=[]
     repeatedPaths=[]
     """);}
 @Test void t2(){test(a,Map.of(
       a,List.of(a_b,a_d),
       a_b,List.of(a_b_c)
       ),"""
     okPaths=[a|b, a|b|c, a|d]
     badPaths=[]
     repeatedPaths=[]
     """);}
 @Test
 void t3(){
   assertEquals(a_b,a_b_);//so, how do we specify the empty string? correctly avoided by java?
   assertThrows(StackOverflowError.class,
     ()->test(a_b,Map.of(a_b,List.of(a_b_)),""));
   }
 @Test void t4(){test(a_b,Map.of(a_b,List.of(a_b_dot)),"""
     okPaths=[]
     badPaths=[a|b|.]
     repeatedPaths=[]
     """);}
 @Test void t5(){test(a_b,Map.of(a_b,List.of(a_b_dotDot)),"""
     okPaths=[]
     badPaths=[a|b|..]
     repeatedPaths=[]
     """);}
 @Test void t6(){test(a_b,Map.of(a_b,List.of(a_b_dotFoo)),"""
     okPaths=[]
     badPaths=[]
     repeatedPaths=[]
     """);}
 public static final Path a_spaces1=Path.of("a","b c.txt");
 public static final Path a_spaces2=Path.of("a","b .txt");
 public static final Path a_spaces3=Path.of("a"," b.txt");
 @Test void tSpaces1(){test(a,Map.of(a,List.of(a_spaces1)),"""
     okPaths=[]
     badPaths=[a|b c.txt]
     repeatedPaths=[]
     """);}
 @Test void tSpaces2(){test(a,Map.of(a,List.of(a_spaces2)),"""
     okPaths=[]
     badPaths=[a|b .txt]
     repeatedPaths=[]
     """);}
 @Test void tSpaces3(){test(a,Map.of(a,List.of(a_spaces3)),"""
     okPaths=[]
     badPaths=[a| b.txt]
     repeatedPaths=[]
     """);}
 public static final Path a_tilde1=Path.of("a","a~b.txt");
 @Test void tTilde1(){test(a,Map.of(a,List.of(a_tilde1)),"""
     okPaths=[]
     badPaths=[a|a~b.txt]
     repeatedPaths=[]
     """);}
 public static final Path a_tilde2=Path.of("a","foo.a~");
 @Test void tTilde2(){test(a,Map.of(a,List.of(a_tilde2)),"""
     okPaths=[]
     badPaths=[a|foo.a~]
     repeatedPaths=[]
     """);}
 @Test void tRepeat1(){test(a,Map.of(a,List.of(a_b,a_B,a_D,a_d)),"""
     okPaths=[]
     badPaths=[]
     repeatedPaths=[a|B, a|b, a|d, a|D]
     """);}
}
