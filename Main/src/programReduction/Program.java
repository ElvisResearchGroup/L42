package programReduction;

import java.util.Collections;
import java.util.List;

import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Phase;
import coreVisitors.From;
import facade.PData;
import tools.Assertions;

public interface Program {
  @SuppressWarnings("serial")
  public static class EmptyProgram extends RuntimeException{}
  
  Program pop();//(L,ctxL,ctxLs).pop()=ctxL[L],ctxLs

  Program push(CtxL ctx, ExpCore.ClassB l);//(ctxL[L],ctxLs).push(ctxL,L)=L,ctxL,ctxLs
  
  //well, it is a primitive but always has the same implementation
  default Program evilPush(ExpCore.ClassB l){return new EvilPushed(l,this);}
  
  ExpCore.ClassB top();//(L,_).top()=L
  
  Program updateTop(ExpCore.ClassB l);
  //this is the only one that can detect evilPush
  //(L,ctxLs).update(L')=L',ctxLs

  Path _reducePath(Path p);//return null in case of failure
    //ok also this one can be messed up by evilPush
  Program growFellow(Program fellow);
  //(L,ctxL,_).growFellow(p)==p.push(p.top()/ctxL)
  int getFreshId();//good for debugging and needed for reduction 

  List<ExpCore.ClassB.MethodWithType> methods(Ast.Path p);
  
  //program derived operations:
  default boolean subtypeEq(Ast.Path p,Ast.Path p1){
    if(p1.equals(Path.Any())){return true;}
    if(equiv(p,p1)){return true;}
    if(p.isPrimitive()){return false;}
    ClassB cb=this.extractClassB(p);
    for(Path pi:cb.getSuperPaths()){
      if(equiv(From.fromP(pi,p), p1)){return true;}
      }
    return false;
    }
  //hint for testing: p.equiv(p0,p1)==p.navigate(p0).equals(p.navigate(p1))
  //provided both navigates do not throw evilPush errors
  //and p0, p1 already exists (not still to metaprogram their outers)
  default boolean equiv(Ast.Path p, Ast.Path p1){
    if (p.equals(p1)){return true;}
    if (p.isPrimitive() ||p1.isPrimitive()){return false;}
    if(p.outerNumber()==p1.outerNumber()){return false;}
    if(p.outerNumber()<p1.outerNumber()){
      Path aux=p1; p1=p; p=aux;//swap
      }
    assert p.outerNumber()>p1.outerNumber();
    Path reduced=p;
    while(true){
      reduced=this._reducePath(reduced);
      if(reduced==null){return false;}
      if(reduced.outerNumber()==p1.outerNumber()){
        return reduced.equals(p1);
        }
      }
    }
  default boolean equiv(Ast.NormType t, Ast.NormType t1){
    return t.getMdf()==t1.getMdf() && equiv(t.getPath(),t1.getPath());
    }
  
  default ExpCore.ClassB get(int n){
    assert n>=0;
    if(n==0){return this.top();}
    else return this.pop().get(n-1);
    }

  default Program push(Ast.C c) {
    CtxL splitted=CtxL.split(this.top(), c);
    return new PushedProgram((ClassB)splitted.originalHole(),splitted,this); 
    }

  /*mah -push(L)//non determinism is not relevant if update is not used
  p.push(L)=p.push(ctxL,L)//an evilPush can exist in implementation
  with ctxL[L]=p.top()
  //with p'=p.evilPush(L)   p'.top()==L, p'.pop()==p, p'.update(..) error! 
  */

  default Program navigate(List<Ast.C>cs){
    Program res=this;
    for(Ast.C c:cs){res=res.push(c);}
    return res;
    }

  default Program navigate(Ast.Path p){
    Program res=this;
    for(int i=0;i<p.outerNumber();i++){res=res.pop();}
    return res.navigate(p.getCBar());
  }
  default ExpCore.ClassB extractClassB(Ast.Path p){
    Program res1=this;
    for(int i=0;i<p.outerNumber();i++){res1=res1.pop();}
    ExpCore.ClassB top=res1.top();
    try{return top.getClassB(p.getCBar());}
    catch(ErrorMessage.PathNonExistant pne){
      throw pne.withListOfNodeNames(p.getCBar()).withCb(top);
      }
    }
  default Path _reducePath(Path p,CtxL ctx){
    if(p.isPrimitive()){return null;}
    if(p.outerNumber()==0){return null;}
    if(p.outerNumber()>1){
      Path p1=p.setNewOuter(p.outerNumber()-1);
      Path p2=this.pop()._reducePath(p1);
      if (p2==null){return null;}
      assert p2.outerNumber()==p1.outerNumber()-1;
      return p2.setNewOuter(p2.outerNumber()+1);
      }
    assert p.outerNumber()==1;
    List<Ast.C> cs = p.getCBar();
    if(cs.isEmpty()){return null;}
    ClassB.Member m=ctx.originalCtxM();
    if(!(m instanceof ClassB.NestedClass)){return null;}
    Ast.C ncName=((ClassB.NestedClass)m).getName();
    if(!ncName.equals(cs.get(0))){return null;}
    return Path.outer(0, cs.subList(1, cs.size()));
    }
  default PData reprAsPData(){
    PData res=new PData(this);
    return res;
    }
  static Program emptyLibraryProgram(){return EmptyProgramHolder.cached;}
  }
class EmptyProgramHolder{
  static final Program cached=new FlatProgram(new ClassB(Doc.empty(), false,Collections.emptyList(),Collections.emptyList(), Position.noInfo, Phase.Coherent, 0));
  }

class FlatProgram extends Methods{
  int freshIds=1;
  ExpCore.ClassB l;
  FlatProgram(ExpCore.ClassB l){this.l=l;}
    
  public Program pop() { throw new Program.EmptyProgram();}
  
  public Program push(CtxL ctx, ClassB l) {
    return new PushedProgram(l,ctx,this);
    }
    
  public ClassB top() {return l;}

  public Program updateTop(ClassB l) {
    FlatProgram fp= new FlatProgram(l);
    fp.freshIds=this.freshIds;
    return fp;
    }

  public Path _reducePath(Path p){return null;}
    
  public boolean equiv(Path p, Path p1) {return p.equals(p1);}

  public Program growFellow(Program fellow) {throw new Program.EmptyProgram();}

  public int getFreshId(){return freshIds++;}
  }
class PushedProgram extends Methods{
  ClassB newTop;
  CtxL splitPoint;
  Program former;
  public PushedProgram(ClassB newTop, CtxL splitPoint, Program former) {
    this.newTop=newTop;
    this.splitPoint=splitPoint;
    this.former=former;
    assert (!this.getClass().equals(PushedProgram.class)) || splitPoint.fillHole(newTop).equals(former.top()):
    "";
  }
  public Program pop() { return former;}

  public Program push(CtxL ctx, ClassB l) {
    return new PushedProgram(l,ctx,this);
    }

  public ClassB top() {return newTop;}
   
  public Program updateTop(ClassB l) {return new UpdatedProgram(l,this.splitPoint,this.former);}
   
  public Path _reducePath(Path p){return _reducePath(p,this.splitPoint);}
    
  public Program growFellow(Program fellow) {
    CtxL ctx=this.splitPoint.divide(fellow.top());
    return fellow.push(ctx,(ClassB)ctx.originalHole());
    //(L,ctxL,_).growFellow(p)==p.push(p.top()/ctxL)
    }
  public int getFreshId(){
    int popped=this.pop().getFreshId();
    return popped;//+"."+this.splitPoint.nameWhereThereisTheHole();
    }
  }
class UpdatedProgram extends PushedProgram{
  public UpdatedProgram(ClassB newTop, CtxL splitPoint, Program former) {
    super(newTop, splitPoint, former);}
  
  public Program pop() {
    return former.updateTop(splitPoint.fillHole(newTop));
    }
  }

class EvilPushed extends Methods{
  ClassB newTop; Program former;
  public EvilPushed(ClassB newTop, Program former) {
    this.newTop=newTop; this.former=former;
    }

  public Program pop() {return former;}
  //TODO: may be next line need to not throw, if allowed, then updateTop need to check if there is an evil in the tail, and throw on update
  public Program push(CtxL ctx, ClassB l) {throw Assertions.codeNotReachable();}

  public ClassB top() {return newTop;}

  public Program updateTop(ClassB l) {return new EvilPushed(l,former);}

  public Path _reducePath(Path p) {return null;}

  public Program growFellow(Program fellow) {throw Assertions.codeNotReachable();}
  
  public int getFreshId(){
    int popped=this.pop().getFreshId();
    return popped;//+".<EvilPushed>";
    }
  }