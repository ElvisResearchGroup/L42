package programReduction;

import ast.Ast.Type;
import ast.Ast.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;

import coreVisitors.CloneVisitor;
import coreVisitors.PropagatorVisitor;
import tools.Assertions;
import tools.Map;
import ast.Ast;
import ast.Ast.MethodType;
import ast.Ast.Type;
public class Norm {

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
    if(l.getPhase()!=Phase.None) {
      //TODO: logging an error instead of failing to keep stuff going (just) for now
      List<Type> ps1 = Methods.collect(p,l.getSupertypes());
      if(!ps1.equals(l.getSupertypes())) {System.err.println("LoggingError: "+ps1+" "+l.getSupertypes());}
      return l;
      }

    //Ps'=collect(p,Ps)
    List<Type> ps1 = Methods.collect(p,l.getSupertypes());
    //Ms'=methods(p,This0), {C:e in Ms} //norm now put all the nested classes in the back.
    List<MethodWithType> this0Ms=p.methods(Path.outer(0));
    List<ClassB.Member> ms1 = Stream.concat(
      this0Ms.stream(),
      l.getMs().stream().filter(m->m instanceof ClassB.NestedClass)
      ).map(m->norm(p,m)).collect(Collectors.toList());
    ClassB newL=new ClassB(l.getDoc1(),l.isInterface(),ps1,ms1,l.getP(),Phase.Norm,p.getFreshId());
    return newL;
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
    assert nc.getE() instanceof ClassB;
    return nc.withE(norm(p.push(nc.getName())));
    //faster but buggy return nc.withE(norm(p,nc.getE()));
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

