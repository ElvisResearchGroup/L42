package is.L42.common;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Pos;
import is.L42.visitors.ToSVisitor;
import is.L42.visitors.Visitable;
import is.L42.visitors.WellFormedness;

public class Constants{
  static{
//    try{assert false; throw new Error("assertions disabled");}
//    catch(AssertionError e){}
    }
  public static Function<Visitable<?>,String> toS=ToSVisitor::of;
  public static Predicate<Visitable<?>> wf=WellFormedness::of;
  public static Function<PTails,Full.L> readFolder=ReadFolder::of;
  public static BiFunction<String,List<Pos>,Core.L> readURL=ReadURL::of;
  private static final HashMap<String,Program> fwPrograms=new HashMap<>();
  public static void refresh(){fwPrograms.clear();}
  public static Path localhost=Paths.get("localhost");
  public static Path dummy=localhost.resolve("dummy.txt");
  public static Path temp=localhost.resolve("temp.txt");
  private static boolean updatePopChecks=true;
  public static boolean updatePopChecks(){return updatePopChecks;}
  public static void testWithNoUpdatePopChecks(Runnable r){
    try{updatePopChecks=false;r.run();}
    finally{updatePopChecks=true;}
    }
  /*public static boolean newFwProgram(Program p){
    String s=p.toString();
    assert !fwPrograms.containsKey(s):s+"\n"+fwPrograms.keySet();
    fwPrograms.put(s,p);
    return true;
    }*/

  }