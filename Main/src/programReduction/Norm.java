package programReduction;

import ast.Ast.Type;
import ast.Ast.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;

import coreVisitors.CloneVisitor;
import tools.Assertions;
import tools.Map;
import ast.Ast;
import ast.Ast.MethodType;
import ast.Ast.Type;
public class Norm {

  static Type nextT(Ast.Path p,MethodWithType mwt,Ast.MethodSelectorX msx){
    String x=msx.getX();
    if(x.isEmpty()){return mwt.getMt().getReturnType();}
    if(x.equals("this")){
      return new Type(mwt.getMt().getMdf(),p,mwt.getDoc());
      }
    int i=mwt.getMs().getNames().indexOf(x);
    assert i!=-1:
      mwt.getMs()+" but searched for "+ x+" in "+p;
    return mwt.getMt().getTs().get(i);
    }
  static ExpCore.ClassB.MethodWithType select(Ast.MethodSelector ms, List<ExpCore.ClassB.MethodWithType> list){
    for(MethodWithType mwt:list){if(mwt.getMs().equals(ms)){return mwt;}}
    return null;
  }


  ExpCore norm(Program p,ExpCore e){
    return e.accept(new CloneVisitor(){
      public ExpCore visit(ClassB s) {
        //TODO: enable when there is new TS if(s.getPhase()!=Phase.None){return s;}
        return norm(p.evilPush(s));
        }});
    }
  public ExpCore.ClassB norm(Program p){
    //-norm(p)={interface? implements Ps' norm(p,Ms') }
    //p.top()={interface? implements Ps Ms} //Ms is free var and is ok
    ClassB l=p.top();
    //Ps'=collect(p,Ps)
    List<Path> ps1 = Methods.collect(p,l.getSuperPaths());
    //Ms'=methods(p,This0), {C:e in Ms} //norm now put all the nested classes in the back.
    List<ClassB.Member> ms1 = Stream.concat(
      p.methods(Path.outer(0)).stream(),
      l.getMs().stream().filter(m->m instanceof ClassB.NestedClass)
      ).map(m->norm(p,m)).collect(Collectors.toList());
    //return l.withSupertypes(ps1).withMs(ms1).withUniqueId(p.getFreshId()).withPhase(Phase.Norm);
    return new ClassB(l.getDoc1(),l.isInterface(),Map.of(pi->pi.toImmNT(),ps1),ms1,l.getP(),Phase.Norm,p.getFreshId());
    }
  @SuppressWarnings("unchecked")
  <T extends ExpCore.ClassB.Member> T norm(Program p,T m){
    return m.match(
      nc->(T)normNC(p,nc),
      mi->(T)Assertions.codeNotReachable(),
      mwt->(T)normMwt(p,mwt)
      );
    }
  protected NestedClass normNC(Program p,NestedClass nc){
    return nc.withE(norm(p,nc.getE()));
    //modify here to decrese performance but reduce evilpushes, by doing push(C) in case e is L
  }
  protected MethodWithType normMwt(Program p,MethodWithType mwt){
    Optional<ExpCore> e=mwt.get_inner().map(e1->e1.accept(new CloneVisitor(){
      @Override public ExpCore visit(ClassB cb){
        return norm(p.evilPush(cb));
        }}));
    return mwt.with_inner(e);
    }
      

  static Program auxMultiNorm(Program p, List<List<Ast.C>>topPaths){
    ClassB lTop=p.top();
    for(List<Ast.C> csi:topPaths){
//  pi = p.navigate(Csi)
//  Li = norm(pi)//norming the top
//  L = p.top()[Cs1=L1..Csn=Ln] //replace the nested classes in paths Csi with libraries Li.
      Program pi=p.navigate(csi);
      ClassB li=new Norm().norm(pi);
      lTop=lTop.onClassNavigateToPathAndDo(csi, _l->li);
      }
    return p.updateTop(lTop);
    }
  
  static Program multiNorm(Program p, Paths paths){
//- multiNorm(p,empty) = p
      if(paths.isEmpty()){return p;}
//- multiNorm(p, Cs1..Csn)/*a single Css*/= p.update(L)
      Paths popped=paths.pop();
      if(popped.isEmpty()){
        return auxMultiNorm(p,paths.top());
        }
//- multiNorm(p, Css,Csss) =multiNorm(p',Css) 
//  p'=p.growFellow(multiNorm(p.pop(), Csss))
      Program rec=multiNorm(p.pop(),popped);
      Program p1=p.growFellow(rec);
      return auxMultiNorm(p1,paths.current);
      }
    }

