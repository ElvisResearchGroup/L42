package newTypeSystem;

import java.util.ArrayList;
import java.util.List;

import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;
import programReduction.Norm;
import programReduction.Program;
import tools.Assertions;

public class TypeLibrary {

    public static TOut type(TIn in) {
    // TODO Auto-generated method stub
    return null;
    }
    
    public static TOut libraryShallowNorm(TIn in) {
    //(library shallow norm)
    //Norm  |- p ~> norm(p)  //remember: norm ignores meth bodies
    ////assert forall P in norm(p).Ps p(P).Phase>=Norm
    ClassB normP = normTopL(in);
    return new TOk(in,normP,Path.Library().toImmNT());
    }

    private static ClassB normTopL(TIn in) throws Error {
    ClassB normP;
    try{normP=new Norm().norm(in.p);}
    catch(RuntimeException exc){
      throw Assertions.codeNotReachable("not implemented yet");
      //TODO: try catch and wrap norm errors
    }
    assert normP.getSupertypes().stream().allMatch(
      t->{
        Phase phase=in.p.extractClassB(t.getNT().getPath()).getPhase();
        return phase==Phase.Norm ||
                phase==Phase.Typed;});
    return normP;
    }
    
    public static TOut libraryWellTyped(TIn in) {
//   (library well typed)
//   Phase |- p ~> L' //In implementation, if p.top().Phase>=Phase, L'=p.Top()
     ClassB top=in.p.top();
     assert top.getPhase().subtypeEq(Phase.Typed);//   Phase in {Typed,Coherent}
     if(top.getPhase().subtypeEq(in.phase)){ 
       return new TOk(in,top,Path.Library().toImmNT());
       }
//   L0={interface? implements Ps M1..Mn Phase'}=norm(p)
//   L'={interface? implements Ps M1'..Mn' max(Phase',Phase)}
     ClassB L0 = normTopL(in);
     List<MethodWithType> mwts = L0.mwts();
     List<MethodWithType> newMwts = new ArrayList<>();
     List<NestedClass> ns = L0.ns();
     List<NestedClass> newNs = new ArrayList<>();
     //   forall i in 1..n
//     Phase| p| Ps |- Mi ~> Mi'
     for(MethodWithType mwt:mwts){
       TOutM out=memberMethod(in,L0.getSupertypes(),mwt);
       if(!out.isOk()){return out.toError();}
       newMwts.add((MethodWithType)out.toOkM().inner);
       }
     for(NestedClass nt:ns){
       TOutM out=memberNested(in,nt);
       if(!out.isOk()){return out.toError();}
       newNs.add((NestedClass)out.toOkM().inner);
     }
     Phase maxPhase=L0.getPhase();
     if(in.phase.subtypeEq(maxPhase)){maxPhase=in.phase;}
     ClassB L1=new ClassB(L0.getDoc1(),L0.isInterface(),L0.getSupertypes(),newMwts,newNs,L0.getP(),L0.getStage(),maxPhase,L0.getUniqueId());
     if(in.phase==Phase.Coherent){
       boolean isCoh=coherent(in.p.updateTop(L1));
       if(!isCoh){throw Assertions.codeNotReachable("not implemented yet");}
       }
//   if Phase=Coherent then coherent(p.updateTop(L'))
//   //or error not coherent set of abstr. methods:list
    return null;
    }

    private static boolean coherent(Program updateTop) {
    // TODO Auto-generated method stub
    return false;
    }

    private static TOutM memberNested(newTypeSystem.TIn in, NestedClass nc) {
    //(member nested)
    //Phase| p| Ps |-C:L ~>  C:L'
    //   where
    //   Phase |-p.push(C) ~> L'    return null;
    Program p1=in.p.push(nc.getName());
    TOut res=type(in.withP(p1));
    if(!res.isOk()){return res.toError();}
    return new TOkM(nc.withInner(res.toOk().annotated));
    }

    private static TOutM memberMethod(TIn in, List<Type> supertypes, MethodWithType mwt) {
    // TODO Auto-generated method stub
    return null;
    }

}
