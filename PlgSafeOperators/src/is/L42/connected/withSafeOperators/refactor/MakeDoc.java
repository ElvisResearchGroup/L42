package is.L42.connected.withSafeOperators.refactor;
import ast.ExpCore.ClassB;
import ast.Ast.Doc;
public class MakeDoc {
/*  public static ClassB makeDoc(Object o){
    if(o instanceof is.L42.connected.withSafeOperators.location.Doc) {
      is.L42.connected.withSafeOperators.location.Doc
      docL=(is.L42.connected.withSafeOperators.location.Doc)o;
      return makeDocJ(docL.toS());
      }
    if(o instanceof ast.Ast.Doc) {
      ast.Ast.Doc doc=(ast.Ast.Doc)o;
      return makeDocJ(doc.toCodeFormattedString());
    }
    String s=EncodingHelper.ensureExtractStringU(o);
    return makeDocJ(s);
    }
*/
  public static ClassB makeDoc(String s){
    return ClassB.docClass(Doc.factory(true, s));
    }
}