package is.L42.cache;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.range;
import static is.L42.tools.General.toOneOr;
import static is.L42.tools.General.todo;

import java.util.List;
import java.util.stream.Collectors;

import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.generated.X;
import is.L42.nativeCode.TrustedKind;
import is.L42.platformSpecific.javaTranslation.Resources;
class Box{
  P path;
  Core.L l;
  List<X> xs;
  boolean[] interfaces;
  TrustedKind tk;
  Box(){}
  Program p=Resources.currentP;
  Box(TrustedKind tk){this.tk=tk;}
  Box(L42Cache<?> c,List<C>cs){
    this.path=p.minimize(P.of(cs.size()-1, cs));
    this.l=p._ofCore(path);
    assert l!=null;
    this.xs=l.mwts().stream()
      .filter(m->m._e()==null && m.mh().mdf().isClass())
      .map(m->m.key().xs())
      .distinct()
      .reduce(toOneOr(()->bug())).get();
    interfaces=new boolean[xs.size()];
    for(int i:range(xs)){interfaces[i]=c.rawFieldCache(i)==null;}
    String nk=l.info().nativeKind();
    if(!nk.isEmpty()){tk=TrustedKind._fromString(nk); assert tk!=null;}
    }
    String miniPath(){
      if(!path.isNCs()){return path.toString();}
      var ncs=path.toNCs();
      if(ncs.cs().isEmpty()){return path.toString();}
      C first=ncs.cs().get(0);
      Program pp=p;
      for(@SuppressWarnings("unused") int i:range(ncs.n())){
        if(pp.topCore().inDom(first)){return path.toString();}
        pp=pp.pop();
        }
      return ncs.cs().stream().map(c->c.toString()).collect(Collectors.joining("."));
      }
  }
public class L42StandardToString extends KeyFormatter<Box>{
  private C fromS(String s){
    int colon=s.indexOf(":");
    if(colon==-1){return new C(s,-1);}
    String a=s.substring(0,colon);
    String b=s.substring(colon+2);
    return new C(a,Integer.parseInt(b));
    }
  @Override Box cacheKind(L42Cache<?> ck) {
    if(ck.typename() instanceof String[]){
      String[] cs=(String[])ck.typename();
      return new Box(ck,L(range(cs.length),(c,i)->c.add(fromS(cs[i]))));
      }
    if(ck.typename()instanceof TrustedKind){return new Box((TrustedKind)ck.typename());}
    assert false:ck.typename();
    throw todo();
    }
  @Override String specialS(Box t, int lineN) {
    if(t.tk==null){return null;}
    if(t.tk==TrustedKind.Vector){return null;}
    return k.lines()[lineN][1].toString(); 
    }
  @Override String[] stringParts(boolean isInterface, Box t) {
    String[] res=new String[t.xs.size()+1];
    res[0]=isInterface?t.miniPath():"";
    res[0]+="(";
    if(!t.xs.get(0).equals(X.thatX)){res[0]+=t.xs.get(0)+"=";}
    for(int i:range(1,t.xs.size())){res[i]=", "+t.xs.get(i)+"=";}
    res[res.length-1]=")";
    return res; 
    }
  @Override boolean[] interfaceFields(Box t) {return t.interfaces;}
  @Override String varName(Box t) {
    String name=t.path.toString();
    int i=name.lastIndexOf(".");
    if(i!=-1){name=name.substring(i+1);}
    String first=name.substring(0,1).toLowerCase();
    return first+name.substring(1);
    }
  }