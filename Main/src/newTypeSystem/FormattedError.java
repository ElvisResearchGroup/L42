package newTypeSystem;

import ast.Ast;
import ast.Expression;
import facade.L42;
import sugarVisitors.CollapsePositions;

public class FormattedError extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public final TErr err;
    public final ErrorKind kind;
    public static String format(TErr err){
      String res="\n"+err.in+"\n"+err.msg+"\n"+err.kind+"\n"+err._computed;
      if (err.kind==ErrorKind.NotSubtypeClass){
       res+="\nexpected path was :"+err.in.expected.getPath()
        +"\nbut obtained path was :"+err._computed.getPath();
       }
      Ast.Position p=CollapsePositions.of(err.in.e);
      res+="\nin position :"+p;
      return res;
      }
    public FormattedError(TErr err) {
      super(format(err));
      this.err=err;
      this.kind=err.kind;
    }

}
