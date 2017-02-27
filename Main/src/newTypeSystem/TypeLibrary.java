package newTypeSystem;

import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Phase;
import newTypeSystem.TypeSystem.TOut;
import newTypeSystem.TypeSystem.TOk;
import newTypeSystem.TypeSystem.TIn;
import programReduction.Norm;
import programReduction.Program;

public class TypeLibrary {

    public static TOut type(Phase phase, TIn in) {
    // TODO Auto-generated method stub
    return null;
    }
    
    public static TOut libraryShallowNorm(TIn in) {
    //(library shallow norm)
    //Norm  |- p ~> norm(p)  //remember: norm ignores meth bodies
    ////assert forall P in norm(p).Ps p(P).Phase>=Norm
    ClassB normP=new Norm().norm(in.p);
    assert normP.getSupertypes().stream().allMatch(
      t->{
        Phase phase=in.p.extractClassB(t.getNT().getPath()).getPhase();
        return phase==Phase.Norm ||
                phase==Phase.Typed;});
    return new TOk(in,normP,Path.Library().toImmNT());
    }
    /*
    (library well typed)
    Phase |- p ~> L' //In implementation, if p.top().Phase>=Phase, L'=p.Top()
       where
       Phase in {Typed,Coherent}
       L0={interface? implements Ps M1..Mn Phase'}=norm(p)
       L'={interface? implements Ps M1'..Mn' max(Phase',Phase)}
       //assert forall P in Ps p(P).Phase>=Norm
       forall i in 1..n
         Phase| p| Ps |- Mi ~> Mi'
       if Phase=Coherent then coherent(p.updateTop(L'))
*/

}
