package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.Ast.NormType;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.*;
import ast.Util.CachedStage;
import auxiliaryGrammar.Program;

public class MakeMethod {
public static ClassB addMethod(ClassB _lib, List<String> path, MethodSelector ms, String mdfs,int excNumber){
  Errors42.checkExistsPathMethod(_lib, path, Optional.empty());
  ClassB innerLib=Program.extractCBar(path, _lib);
  String[] _mdfs = mdfs.split(" ");
  assert _mdfs.length==ms.getNames().size()+2;
  List<String> nc=new ArrayList<>();
  Type retT=new NormType(Mdf.valueOf(_mdfs[1]),Path.outer(0,Arrays.asList("$0")),Ph.None);
  nc.add("$0");
  List<Type> ts=new ArrayList<>();
  List<Doc> docs=new ArrayList<>();
  int count=1;
  for(String n: ms.getNames()){
    String cn="$"+count;
    ts.add(new NormType(Mdf.valueOf(_mdfs[count+1]),Path.outer(0,Arrays.asList(cn)),Ph.None));
    docs.add(Doc.empty());
    nc.add(cn);
    count++;
    }
  List<Path> exceptions=new ArrayList<>();
  for(int i=0;i<excNumber;i++){
    String cn="$"+count++;
    exceptions.add(Path.outer(0,Arrays.asList(cn)));
    nc.add(cn);
    }
  MethodType mt = new MethodType(false,Doc.empty(),Mdf.valueOf(_mdfs[0]),ts,docs, retT,exceptions);
  MethodWithType mwt=new MethodWithType(Doc.empty(),ms,mt,Optional.empty(),innerLib.getP());
  Optional<Member> optM = Program.getIfInDom(innerLib.getMs(),ms);
  if(optM.isPresent()){
    throw Errors42.errorMethodClash(path, mwt, optM.get(), false, Collections.emptyList(), false, false,false);
    }
  CachedStage cs=new CachedStage();
  cs.setStage(Stage.Star);
  ClassB emptyCb=new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),Collections.emptyList(),innerLib.getP(),cs);
  return _lib.onClassNavigateToPathAndDo(path,cbi->{
    List<Member> mem = new ArrayList<>(cbi.getMs());
    mem.add(mwt);
    for(String s:nc){
      mem.add(new NestedClass(Doc.empty(),s,emptyCb,cbi.getP()));
      }
    return cbi.withMs(mem);
    });
  }
}
