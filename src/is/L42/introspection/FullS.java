package is.L42.introspection;

import static is.L42.tools.General.L;

import java.util.List;
import java.util.function.Predicate;

import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.generated.Core.Info;
import is.L42.generated.Core.MWT;
import is.L42.generated.Core.NC;
import is.L42.generated.Core.T;
import is.L42.visitors.ToSVisitor;

public class FullS extends ToSVisitor{
public void visitMWT(MWT mwt){
  if(mwt.key().hasUniqueNum()){return;}
  super.visitMWT(mwt.with_e(null).withNativeUrl(""));
  if(!mwt.nativeUrl().isEmpty()){c("native{..}");}
  if(mwt._e()!=null){c("=(..)");}
  }
public void visitNC(NC nc){
  if(!nc.key().hasUniqueNum()){super.visitNC(nc);}
  }
public void visitInfo(Info info){
  //TODO: do we need to print "has private implements"?
  //I used to think it was a (virtual) info, but may be "close" is sufficient?
  //in this case, we may have to remove the method hasHiddenImplements.
  separeFromChar();
  if(info.isTyped()){c("#typed{");}
  else {c("#norm{");}
  Predicate<P.NCs> pp=p->p.hasUniqueNum();
  infoItem("typeDep",r(info.typeDep(),pp));
  infoItem("coherentDep",r(info.coherentDep(),pp));
  infoItem("metaCoherentDep",r(info.metaCoherentDep(),pp));
  infoItem("watched",info.watched());
  infoItem("usedMethods",info.usedMethods());
  infoItem("hiddenSupertypes",info.hiddenSupertypes());
  infoItem("refined",info.refined());
  boolInfoItem(info.close(),"close");
  infoElem("nativeKind",info.nativeKind(),"");
  infoItem("nativePar",info.nativePar());
  //infoElem("uniqueId",info._uniqueId(),-1);
  c("}");
  }
public void visitP(P p){
  if(!p.hasUniqueNum()){super.visitP(p);return;}
  c("<private>");
  }
@Override public void exceptionImplements(List<T> ts){
  super.exceptionImplements(r(ts,t->!t.p().hasUniqueNum()));
  }
private <E> List<E> r(List<E> es,Predicate<E>p){return L(es.stream().filter(p));}
}