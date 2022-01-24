package is.L42.meta;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import is.L42.flyweight.C;
import is.L42.flyweight.CoreL;
import is.L42.generated.Core;
import is.L42.generated.Pos;
import is.L42.visitors.CloneVisitor;

final class PosInfoNormalize extends CloneVisitor {
  private final String fauxFileName;
  private ArrayList<C> cs=new ArrayList<>();
  PosInfoNormalize(String fauxFileName) { this.fauxFileName = fauxFileName; }

  List<Pos>poss(List<Pos>poss){
    return poss.stream()
      .<Pos>map(p->p.withFileName(fileName(p.fileName())))
      .toList();
    }

  URI fileName(URI uri){
    /*String p=uri.getPath();
    int i=p.lastIndexOf("/");
    if(i!=-1) { p=p.substring(i+1); }
    String surj=fauxFileName+"/"+p;*/
    String scs=cs.stream().filter(c->!c.hasUniqueNum()).map(c->c.toString()).collect(Collectors.joining("/"));
    String suri=fauxFileName+(scs.isEmpty()?"":"/"+scs);
    try{ return new URI(suri); }
    catch (URISyntaxException e){throw new Error(e);}
    }

  public Core.EX visitEX(Core.EX x){return super.visitEX(x.withPos(L42£Meta.noPos));}

  public Core.PCastT visitPCastT(Core.PCastT pCastT){
    return super.visitPCastT(pCastT.withPos(L42£Meta.noPos));
    }

  public Core.EVoid visitEVoid(Core.EVoid eVoid){return eVoid.withPos(L42£Meta.noPos);}

  public CoreL visitL(CoreL l){return super.visitL(l.withPoss(poss(l.poss())));}

  public Core.MWT visitMWT(Core.MWT mwt){
    return super.visitMWT(mwt.withPoss(poss(mwt.poss())));
    }

  public Core.NC visitNC(Core.NC nc){
    var pos=poss(nc.poss());
    cs.add(nc.key());
    try{ return super.visitNC(nc.withPoss(pos)); }
    finally{ cs.remove(cs.size()-1); }
    }

  public Core.MCall visitMCall(Core.MCall mCall){
    return super.visitMCall(mCall.withPos(L42£Meta.noPos));
    }

  public Core.Block visitBlock(Core.Block block){
    return super.visitBlock(block.withPos(L42£Meta.noPos));
    }

  public Core.Loop visitLoop(Core.Loop loop){
    return super.visitLoop(loop.withPos(L42£Meta.noPos));
    }

  public Core.Throw visitThrow(Core.Throw thr){
    return super.visitThrow(thr.withPos(L42£Meta.noPos));
    }

  public Core.OpUpdate visitOpUpdate(Core.OpUpdate opUpdate){
    return super.visitOpUpdate(opUpdate.withPos(L42£Meta.noPos));
    }

  @Override public Core.Info visitInfo(Core.Info info){ return info.withTyped(true); }
  }