package newReduction;

import java.util.ArrayList;
import java.util.List;

import ast.Ast.MethodSelector;
import ast.L42F;
import ast.MiniJ;
import ast.L42F.Body;
import ast.L42F.CD;
import ast.L42F.E;
import ast.L42F.M;
import ast.L42F.SimpleBody;
import ast.MiniJ.S;
import ast.MiniJ.RawJ;
import l42FVisitors.BodyVisitor;

public class L42FToMiniJTop {
  public static MiniJ.CD of(ClassTable ct,L42F.CD cd){
    String name=cd.l42ClassName();
    boolean interf=cd.getKind()==L42F.SimpleKind.Interface;
    List<String> cs=tools.Map.of(i->ct.get(i).cd.l42ClassName(),cd.getCns());
    List<MiniJ.M>ms=new ArrayList<>();
    for(L42F.M m:cd.getMs()){
    String retT=ct.get(m.getReturnType().getCn()).cd.className();
    List<String> ts=tools.Map.of(tx->ct.get(tx.getT().getCn()).cd.className(),m.getTxs());
    List<String> xs=tools.Map.of(tx->tx.getX(),m.getTxs());
    MiniJ.S body=m.getBody().accept(new VB(cd,m));
    ms.add(new MiniJ.M(retT, liftMs(m.getSelector()), ts, xs, body));
    }
  return new MiniJ.CD(interf, name, cs, ms);
  }

  private static class VB implements BodyVisitor<MiniJ.S>{
    public VB(CD cd, M m) {
      this.cd = cd;
      this.m = m;
      }
    L42F.CD cd;
    L42F.M m;
  
    @Override
    public S visitEmpty(SimpleBody s) {
      return new RawJ("");
      }

    @Override
    public S visitSetter(SimpleBody s) {
      return new RawJ("this."+liftMs(m.getSelector())+"=that;");
      }

    @Override
    public S visitGetter(SimpleBody s) {
      return new RawJ("return this."+liftMs(m.getSelector())+";");
      }

    @Override
    public S visitNew(SimpleBody s) {
    // TODO Auto-generated method stub
    return null;
    }

    @Override
    public S visitNewWithFwd(SimpleBody s) {
    // TODO Auto-generated method stub
    return null;
    }

    @Override
    public S visitNewFwd(SimpleBody s) {
    // TODO Auto-generated method stub
    return null;
    }

    @Override
    public S visitNativeIntSum(SimpleBody s) {
    // TODO Auto-generated method stub
    return null;
    }

    @Override
    public S visitE(E s) {
    // TODO Auto-generated method stub
    return null;
    }
  
  }

public static String liftMs(MethodSelector ms) {
String res=ms.nameToS();
for(String xi:ms.getNames()){res+="£X"+xi;}
return res;
}
}
