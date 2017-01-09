package typeSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import coreVisitors.FreeVariables;
import ast.Ast;
import ast.Ast.FreeType;
import ast.Ast.HistoricType;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.MCall;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;

public class TypeCheckMethod {
  public static List<HashMap<String,NormType>> splitAllVarEnvForMethod(ExpCore e,List<ExpCore> es, HashMap<String,NormType>varEnv){
    List<ExpCore> es2=new ArrayList<ExpCore>();
    es2.add(e);
    es2.addAll(es);
    List<HashMap<String,NormType>> result=new ArrayList<HashMap<String,NormType>>();
    for( ExpCore ei:es2){result.add(TypeSystem.splitVarEnv(ei,varEnv));}
    TypeSystem.checkDisjointCapsule(es2, result);
    assert TypeSystem.noFreeVar(es2,result,varEnv);
    return result;
  }

  public static boolean checkExceptions1(Program p,Path path1,HashSet<Path> exceptions2) {
    for(Path path2:exceptions2){
      if(Functions.isSubtype(p, path1,path2)){return true;}
    }
    return false;
  }

  public static void checkExceptions(Program p,MCall s,List<Path> exceptions1,HashSet<Path> exceptions2) {
  for(Path path1:exceptions1){
    if(checkExceptions1(p,path1,exceptions2)){continue;}
    throw new ErrorMessage.ExceptionThrownNotCaptured(s,path1,exceptions2,s.getP());
    }
  }

  public static Type methCallT(TypeSystem that, List<HashMap<String, NormType>> varEnvs,MCall s,NormType recExpected, MethodWithType mwt) {
    //check exceptions subtype
    checkExceptions(that.p,s,tools.Map.of(ti->ti.getNT().getPath(),mwt.getMt().getExceptions()),that.throwEnv.exceptions);
    List<NormType> tsExp=new ArrayList<NormType>();
    List<Type> ts=new ArrayList<Type>();
    List<ExpCore> es = new ArrayList<ExpCore>();
    ArrayList<Set<String>> fvs=new ArrayList<>();
    es.add(s.getInner());
    es.addAll(s.getEs());
    {int i=0;
    tsExp.add(recExpected);
    fvs.add(FreeVariables.of(es.get(i)));
    for(ExpCore ei:s.getEs()){
      i+=1;//correct to start from 1
      NormType expectedi=Functions.forceNormType(that.p,ei, mwt.getMt().getTs().get(i-1));
      assert i==tsExp.size();
      tsExp.add(expectedi);
      fvs.add(FreeVariables.of(es.get(i)));
    }}
    {int i=-1;for(ExpCore ei:es){i+=1;
      Set<String>fvToAdd=fvLessi(fvs,i);
      SealEnv sealEnvi=new SealEnv(that.sealEnv);
      sealEnvi.addToAllXxssK(fvToAdd);
      ts.add(TypeSystem.typecheckSure(false,that.p,varEnvs.get(i),sealEnvi, that.throwEnv, tsExp.get(i),ei));
    }}
    //here ts is all the actual types, and tsExp all the expected ones
    //from typecheck with suggested, we know they are subtypes.
    Type rtOpt=mwt.getMt().getReturnType();
    if(that.p.executablePlus() && rtOpt instanceof Ast.HistoricType){return rtOpt;}
    NormType rt=Functions.forceNormType(that.p,s, rtOpt);
    //TODO:for now force all the types to be norm anyway, open to relax for Stage.Less
    boolean ph=false;
    assert ts.size()==es.size():ts.size()+" "+es.size();
    {int j=-1;for(Type t:ts){j+=1;
      if(t instanceof Ast.FreeType){continue;}
      NormType nt=Functions.forceNormType(that.p,es.get(j), t);
      if(nt.getPh()!=Ph.None){ph=true;}
    }}
    if(ph){return Functions.toPartial(rt);}
    return rt;
  }

  private static Set<String> fvLessi(ArrayList<Set<String>> fvs, int i) {
    Set<String> result=new HashSet<String>();
    {int j=-1;for(Set<String> si:fvs){j+=1;
      if(j!=i){continue;}
      result.addAll(si);
    }}
    return result;
  }

}
