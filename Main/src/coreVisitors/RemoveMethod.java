package coreVisitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import tools.Map;
import ast.Ast;
import ast.ExpCore;
import ast.Ast.FieldDec;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.WalkBy;
import ast.ExpCore.ClassB.Member;
import auxiliaryGrammar.Program;

public class RemoveMethod extends CloneVisitor{
  private List<String> path;
  private MethodSelector ms;
  private MethodSelector msDestOnlyForConstr;
  public RemoveMethod(Path path, MethodSelector ms, MethodSelector msDestOnlyForConstr) {
    this.path = new ArrayList<>(path.getCBar());this.ms=ms;this.msDestOnlyForConstr=msDestOnlyForConstr;
    }  
  public List<Member> liftMembers(List<Member> s) {
    if(!path.isEmpty()){return Map.of(this::liftM,s);}
    List<Member> result=new ArrayList<>(s);
    Optional<Member> mOpt = Program.getIfInDom(s, this.ms);
    if( mOpt.isPresent()){
      Member m=mOpt.get();
      result.remove(m);
      }
    return result;
  }
  
  /*public ExpCore visit(ClassB s){
    if(!path.isEmpty()){ return super.visit(s);}
    if(!(s.getH() instanceof Ast.ConcreteHeader)){ return super.visit(s);}
    Ast.ConcreteHeader h=(Ast.ConcreteHeader)s.getH();
    List<String>names=new ArrayList<>();
   for(FieldDec f:h.getFs()){  names.add(f.getName()); }
   MethodSelector msK=new MethodSelector(h.getName(),names);
   if(msK.equals(this.ms)){
     List<FieldDec> fs =new ArrayList<>();
     {int i=-1;for(FieldDec fd: h.getFs()){i+=1;
       fs.add(fd.withName(msDestOnlyForConstr.getNames().get(i)));
       }}
     Ast.ConcreteHeader h2=h.withName(msDestOnlyForConstr.getName()).withFs(fs);
     return super.visit(s.withH(h2));
     }
    return super.visit(s);
  }*/
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    if(!path.get(0).equals(nc.getName())){
      return nc;
      }
    List<String> aux=new ArrayList<String>(path);
    path.remove(0);
    try{return super.visit(nc);}
    finally{path=aux;}
    }
  public static ClassB of(ClassB s,Path path, MethodSelector ms, MethodSelector msDest) {
    RemoveMethod rm=new RemoveMethod(path, ms,msDest);
    assert path.outerNumber()==0;
    return (ClassB)s.accept(rm);
  }
}