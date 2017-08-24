package l42FVisitors;

import ast.MiniJ;
import ast.MiniJ.B;
import ast.MiniJ.Break;
import ast.MiniJ.E;
import ast.MiniJ.If;
import ast.MiniJ.K;
import ast.MiniJ.MCall;
import ast.MiniJ.Return;
import ast.MiniJ.S;
import ast.MiniJ.Throw;
import ast.MiniJ.Try;
import ast.MiniJ.UseCall;
import ast.MiniJ.VarAss;
import ast.MiniJ.VarDec;
import ast.MiniJ.WhileTrue;
import ast.MiniJ.X;

public class JCloneVisitor implements JVisitor<MiniJ.S>{

    @Override
    public MiniJ.B visit(B s) {
    return new B(liftLabel(s.getLabel()),tools.Map.of(si->si.accept(this),s.getSs()));
    }

    protected String liftLabel(String label) {
      return label;
      }

    @Override
    public MiniJ.S visit(Break s) {
      return new Break(liftLabel(s.getLabel()));
    }


    @Override
    public MiniJ.S visit(If s) {
      return new If(liftX(s.getCond()),this.visit(s.getThen()),this.visit(s.get_else()));
    }


    protected String liftX(String x) {
      return x;
    }

    @Override
    public MiniJ.E visit(MCall s) {
      return new MCall(liftCn(s.getCn()),liftMn(s.getMName()),tools.Map.of(this::liftX, s.getXs()));
    }

    protected String liftCn(String cn) {
      return cn;
    }

    protected String liftMn(String mName) {
      return mName;
    }

    protected K liftK(K s) {
      //t is the "kind" not the exception type
      return new K(s.getT(),liftX(s.getX()),this.visit(s.getB()));
    }
    @Override
    public MiniJ.S visit(Return s) {
      return new Return(liftX(s.getX()));
    }

    @Override
    public MiniJ.S visit(Throw s) {
      return new Throw(liftCn(s.getCn()),liftX(s.getX()));
    }

    @Override
    public MiniJ.S visit(Try s) {
      return new Try(this.visit(s.getB()),tools.Map.of(this::liftK, s.getKs()));
    }

    @Override
    public MiniJ.E visit(UseCall s) {
      return new UseCall(tools.Map.of(this::liftX,s.getXs()));
    }

    @Override
    public MiniJ.S visit(VarAss s) {
    String x=liftX(s.getX());
    E e=(E) s.getE().accept(this);
    return new VarAss(x,e);
    }

    @Override
    public MiniJ.S visit(VarDec s) {
      return new VarDec(liftCn(s.getCn()),liftX(s.getX()));
    }

    @Override
    public MiniJ.S visit(WhileTrue s) {
      return new WhileTrue(this.visit(s.getB()));
    }

    @Override
    public MiniJ.E visit(X s) {
      return new X(liftX(s.getInner()));
    }

}
