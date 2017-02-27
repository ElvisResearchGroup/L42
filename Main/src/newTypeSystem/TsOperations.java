package newTypeSystem;

import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.Using;
import ast.ExpCore.X;
import ast.ExpCore._void;
import newTypeSystem.TypeSystem.TIn;
import newTypeSystem.TypeSystem.TOut;
import newTypeSystem.TypeSystem.TOk;
import newTypeSystem.TypeSystem.TErr;

public class TsOperations {

    public static TOut tsPath(TIn in, Path s) {
    //D |- P~>P:class P <= T | emptyTr
    //D.p|-class P <= T
    NormType t=new NormType(Mdf.Class,s,Doc.empty());
    if(TypeSystem.subtype(in.p, t, in.expected)){
      return new TOk(in,s,t);
      }
    TErr out=new TErr(in,"----",t);
    return out;
    }

    public static TOut tsX(TIn in, X s) {
    // TODO Auto-generated method stub
    return null;
    }

    public static TOut tsVoid(TIn in, _void s) {
    //D |- void~> void:imm Void <= T | emptyTr
    //D.p|-imm Void <= T
    NormType t=Path.Void().toImmNT();
    if(TypeSystem.subtype(in.p, t, in.expected)){
      return new TOk(in,s,t);
      }
    TErr out=new TErr(in,"misplaced void constant",t);
    return out;
    }

    public static TOut tsUsing(TIn in, Using s) {
    // TODO Auto-generated method stub
    return null;
    }

    public static TOut tsSignal(TIn in, Signal s) {
    // TODO Auto-generated method stub
    return null;
    }

    public static TOut tsMCall(TIn in, MCall s) {
    // TODO Auto-generated method stub
    return null;
    }

    public static TOut tsBlock(TIn in, Block s) {
    // TODO Auto-generated method stub
    return null;
    }

    public static TOut tsClassB(TIn in, ClassB s) {
    //D |- L ~> L' : imm Library <= T | emptyTr
    //D.p|-imm Library <= T
    //D.Phase  |- D.p.evilPush(L) ~> L'
    NormType t=Path.Library().toImmNT();
    if(!TypeSystem.subtype(in.p, t, in.expected)){
      TErr out=new TErr(in,"-----------",t);
      return out;  
      }
    TOut out=TypeLibrary.type(in.phase,in.withP(in.p.evilPush(s)));
    if(out.isOk()){return new TOk(in,s,t);}
    return out.toError().enrich(in);
    }

    public static TOut tsLoop(TIn in, Loop s) {
    // TODO Auto-generated method stub
    return null;
    }

}
