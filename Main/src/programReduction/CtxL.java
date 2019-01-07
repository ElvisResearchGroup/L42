package programReduction;

import java.util.List;

import ast.Ast;
import ast.Ast.Position;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import coreVisitors.IsCompiled;
import tools.Assertions;

public class CtxL {
  ExpCore.ClassB origin;
  int pos;
  CtxC ctx;
  Object nameOfWayInside() {
    Member m = origin.getMs().get(pos);
    return m.match(
      nC->nC.getName(),
      mi->mi.getS(),
      mt->mt.getMs());
    }
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
      nc->nc.getName().toString(),
      mi->mi.getS().toString(),
      mwt->mwt.getMs().toString());
    }
  public String toString() {return "CtxL["+sugarVisitors.ToFormattedText.of(this.fillHole(new ExpCore.X(Position.noInfo,"_HOLE_")))+",originalHole:"+sugarVisitors.ToFormattedText.of(this.originalHole())+"]";}
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
    Member m=l.getMs().get(pos);
    ExpCore inner=m.getInner();
    boolean metaReady=m instanceof NestedClass && IsCompiled.of(m.getInner());
    CtxC innerSplit;
    if(metaReady){innerSplit=CtxC.hole(inner);}
    else {innerSplit=CtxC.split(inner);}
    return new CtxL(l,pos,innerSplit);
    }
  public static CtxL split (ExpCore.ClassB l,Ast.C c){
    int pos=findC(l,c);
    CtxC ctx=CtxC.hole(l.getMs().get(pos).getInner());
    return new CtxL(l,pos,ctx);
    }
  private static int findC(ExpCore.ClassB l,Ast.C c){
    int i=0;
    for(Member m:l.getMs()){
      if (m instanceof ExpCore.ClassB.NestedClass && ((ExpCore.ClassB.NestedClass)m).getName().equals(c)){
        return i;
        }
      i++;
    }
    throw new ErrorMessage.CtxExtractImpossible(null,null);
    }
  private static int firstNotCompiled(List<Member> ms) {
    for(int i=0;i<ms.size();i++){
      if (!IsCompiled.of(ms.get(i))){return i;}
      }
    return ms.size();
    }
  }
