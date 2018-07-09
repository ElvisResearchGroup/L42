package programReduction;

import ast.Ast.Type;
import ast.ErrorMessage;
import ast.Ast.Path;
import ast.Ast.Position;

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
import auxiliaryGrammar.Functions;
import coreVisitors.CloneVisitor;
import coreVisitors.CollectPaths0;
import coreVisitors.PropagatorVisitor;
import tools.Assertions;
import tools.Map;
import ast.Ast;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Type;
public class Norm {

  static ExpCore.ClassB.MethodWithType select(Ast.MethodSelector ms, List<ExpCore.ClassB.MethodWithType> list){
    for(MethodWithType mwt:list){if(mwt.getMs().equals(ms)){return mwt;}}
    return null;
  }

  public static boolean subsetEq(Program p,List<Type> all, List<Type> some) {
    //check all.containsAll(some)
    for(Type tAll:all)out:{
      for(Type tSome:some){
        if (p.equiv(tAll,tSome)){break out;}
        }
      return false;
      }
    return true;
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
      assert Norm.subsetEq(p, l.getSupertypes(),Methods.collect(p,l.getSupertypes()));
      return l;
      }
    //Ps'=collect(p,Ps)
    List<Type> _ps1 = Methods.collect(p,l.getSupertypes());
    List<Type> ps1=new ArrayList<>(l.getSupertypes());
    for(Type t:_ps1){
      boolean none=l.getSupertypes().stream().noneMatch(ti->p.equiv(ti.getPath(),t.getPath()));
      if(none){ps1.add(t);}
      }
    //Ms'=methods(p,This0), {C:e in Ms} //norm now put all the nested classes in the back.
    List<MethodWithType> this0Ms=p.methods(Path.outer(0));

    List<ClassB.Member> msOld=new ArrayList<>();
    List<ClassB.Member> msNc=new ArrayList<>();
    for(ClassB.Member mi:l.getMs()){
      if (mi instanceof ClassB.NestedClass){msNc.add(norm(p,mi));}
      else {msOld.add(mi);}
      }
    List<ClassB.Member> ms1=new ArrayList<>();
    for(MethodWithType mi:this0Ms){
      MethodSelector msi=mi.getMs();
      out:{int j=-1;
        for(ClassB.Member mj:msOld){j+=1;
          MethodSelector msj;
          if(mj instanceof ClassB.MethodImplemented){msj=((ClassB.MethodImplemented)mj).getS();}
          else{msj=((MethodWithType)mj).getMs();}
          if(!msj.equals(msi)){continue;}
          msOld.set(j,norm(p,mi));//found it!
          break out;
          }
        //not found
        ms1.add(norm(p,mi));
        }
      }
    ms1.addAll(msOld);
    ms1.addAll(msNc);
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
    if(mwt.get_inner()==null){return mwt;}
    ExpCore e=mwt.get_inner().accept(new CloneVisitor(){
      @Override public ExpCore visit(ClassB cb){
        return norm(p.evilPush(cb));
        }});
    for(Path pi: CollectPaths0.of(mwt)){
      if(!UsedPaths.alive(p, pi)){
        throw new ErrorMessage.PathMetaOrNonExistant(false, pi.getCBar(), p.top(), mwt.getP(), Position.noInfo);
        }
      }
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

