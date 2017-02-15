package is.L42.connected.withSafeOperators.refactor;
import ast.ExpCore.*;
import facade.PData;
import programReduction.Program;

public class Compose {
  public static ClassB compose(PData pData,ClassB l1,ClassB l2){
    Program prg=pData.newP;
    try{while(true){
      System.out.println(sugarVisitors.ToFormattedText.of(prg.top()));
      prg=prg.pop();
    }}
    catch(Program.EmptyProgram ep){}
    return l1;
  }
}
