package programReduction;

import java.util.List;

import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import coreVisitors.IsCompiled;
import tools.Assertions;

public class CtxL {
  ExpCore.ClassB origin;
  int pos;
  CtxC ctx;
  CtxL(ExpCore.ClassB origin,int pos,CtxC ctx){
    this.origin=origin;
    this.pos=pos;
    this.ctx=ctx;
    }
  public ExpCore.ClassB fillHole(ExpCore hole){
    Member m=origin.getMs().get(pos);
    m=m.withInner(ctx.fillHole(hole));
    return origin.withMember(m);
  }
  public ExpCore originalHole(){return ctx.originalHole();}
  
  public CtxL divide(ExpCore.ClassB all){
    Member m=all.getMs().get(pos);//already checking there are enough members
    CtxC divided=ctx.divide(m.getInner());
    return new CtxL(all,pos,divided);    
  }
  public Member originalCtxM(){return origin.getMs().get(pos);}
  
  public String nameWhereThereisTheHole(){
    Member m = this.origin.getMs().get(this.pos);
    return m.match(
      nc->nc.getName(),
      mi->mi.getS().toString(),
      mwt->mwt.getMs().toString());
    }  
  public String toString() {return "CtxL["+sugarVisitors.ToFormattedText.of(this.fillHole(new ExpCore.X("_HOLE_")))+",originalHole:"+sugarVisitors.ToFormattedText.of(this.originalHole())+"]";}
  public int hashCode() {return this.fillHole(new ExpCore.WalkBy()).hashCode();}
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    CtxL other = (CtxL) obj;
    ExpCore oE=other.fillHole(new ExpCore.WalkBy());
    ExpCore thisE=this.fillHole(new ExpCore.WalkBy());
    return thisE.equals(oE);
    }
  public static CtxL _split (ExpCore.ClassB l){
    int pos=firstNotCompiled(l.getMs());
    if(pos==l.getMs().size()){return null;}
    CtxC innerSplit=CtxC.split(l.getMs().get(pos).getInner());
    return new CtxL(l,pos,innerSplit);
    }
  public static CtxL split (ExpCore.ClassB l,String c){
    int pos=findC(l,c);
    CtxC ctx=CtxC.hole(l.getMs().get(pos).getInner());
    return new CtxL(l,pos,ctx);
    }
  private static int findC(ExpCore.ClassB l,String c){
    int i=0;
    for(Member m:l.getMs()){
      if (m instanceof ExpCore.ClassB.NestedClass && ((ExpCore.ClassB.NestedClass)m).getName().equals(c)){
        return i;
        }
      i++;
    }
    throw Assertions.codeNotReachable();
    }
  private static int firstNotCompiled(List<Member> ms) {
    for(int i=0;i<ms.size();i++){
      if(ms.get(i) instanceof ClassB.MethodWithType 
        && !((ClassB.MethodWithType)ms.get(i)).get_inner().isPresent()){continue;}
      if(!IsCompiled.of(ms.get(i).getInner())){return i;}
      }
    return ms.size();
    }
  }
