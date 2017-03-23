package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ast.Ast;
import ast.Ast.C;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.Ast.NormType;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.*;
import auxiliaryGrammar.Functions;
import programReduction.Program;
import tools.Map;

public class MakeMethod {
public static ClassB addMethod(ClassB _lib, List<Ast.C> path, MethodSelector ms, String mdfs,int excNumber){
  Errors42.checkExistsPathMethod(_lib, path, Optional.empty());
  ClassB innerLib=_lib.getClassB(path);
  String[] _mdfs = mdfs.split(" ");
  assert _mdfs.length==ms.getNames().size()+2;
  List<String> nc=new ArrayList<>();
  Type retT=new NormType(Mdf.valueOf(_mdfs[1]),Path.outer(0,Arrays.asList(C.of("$0"))),Doc.empty());
  nc.add("$0");
  List<Type> ts=new ArrayList<>();
  List<Doc> docs=new ArrayList<>();
  int count=1;
  for(String n: ms.getNames()){
    String cn="$"+count;
    ts.add(new NormType(Mdf.valueOf(_mdfs[count+1]),Path.outer(0,Arrays.asList(C.of(cn))),Doc.empty()));
    nc.add(cn);
    count++;
    }
  List<Path> exceptions=new ArrayList<>();
  for(int i=0;i<excNumber;i++){
    String cn="$"+count++;
    exceptions.add(Path.outer(0,Arrays.asList(C.of(cn))));
    nc.add(cn);
    }
  MethodType mt = new MethodType(false,Mdf.valueOf(_mdfs[0]),ts, retT,Map.of(pi->pi.toImmNT(),exceptions));
  MethodWithType mwt=new MethodWithType(Doc.empty(),ms,mt,Optional.empty(),innerLib.getP());
  Optional<Member> optM = Functions.getIfInDom(innerLib.getMs(),ms);
  if(optM.isPresent()){
    throw Errors42.errorMethodClash(path, mwt, optM.get(), false, Collections.emptyList(), false, false,false);
    }
  ClassB emptyCb=ClassB.membersClass(Collections.emptyList(),innerLib.getP(),innerLib.getPhase());
  return _lib.onClassNavigateToPathAndDo(path,cbi->{
    List<Member> mem = new ArrayList<>(cbi.getMs());
    mem.add(mwt);
    for(String s:nc){
      mem.add(new NestedClass(Doc.empty(),C.of(s),emptyCb,cbi.getP()));
      }
    return cbi.withMs(mem);
    });
  }
}
