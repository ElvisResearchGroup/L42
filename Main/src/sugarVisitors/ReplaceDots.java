package sugarVisitors;

import java.nio.file.Files;
import java.nio.file.Path;

import facade.L42;
import facade.Parser;
import ast.ErrorMessage;
import ast.Expression;
import ast.Expression.DotDotDot;
import ast.Expression.ClassB.NestedClass;

public class ReplaceDots extends CloneVisitor{
  Path currentFolder;
  NestedClass currentNested;
  public static Expression of(Path currentFolder,Expression e){
    ReplaceDots rd=new ReplaceDots(currentFolder,null);
    return e.accept(rd);
    }
  private ReplaceDots(Path currentFolder,NestedClass currentNested){
    this.currentFolder=currentFolder;
    this.currentNested=currentNested;
    }

  private Error fail(String msg){
    throw new ErrorMessage.DotDotDotCanNotBeResolved(
        msg,this.currentNested,this.currentFolder);
    }
  @Override
  public Expression visit(DotDotDot s) {
    assert this.currentNested!=null;
    Path pathF=pathToFile();
    Path pathD=pathToDirectory();
    if(pathF!=null && pathD!=null){fail("Both file and directory present");}
    if(pathF==null &&pathD==null){fail("File not found");}
    if(pathF!=null){//put in subfunction
      String code=L42.pathToString(pathF);
      Expression e=Parser.parse(pathF.toString(), code);
      auxiliaryGrammar.WellFormedness.checkAll(e);
      return ReplaceDots.of(this.currentFolder,e );
      }
    assert pathD!=null;
    Path pdf=pathD.resolve("Outer0.L42");
    String code=L42.pathToString(pdf);
    Expression e=Parser.parse(pdf.toString(), code);
    return ReplaceDots.of(pathD,e );
  }
  //Decisions: is ok if there is a text file that have no extension?
  //or if the folder have extension? or if there are both? or
  //one is valid and the other one is rubbish?
  public Path pathToFile(){
    Path fileP=this.currentFolder.resolve(this.currentNested.getName()+".L42");
    if(Files.exists(fileP)){
      if(Files.isDirectory(fileP)){fail("File with .L42 extension is a directory");}
      //if(Files.isExecutable(fileP)){fail("File with .L42 extension is executable");}
      return fileP;
      }
    return null;
  }
  public Path pathToDirectory(){
    Path fileP=this.currentFolder.resolve(this.currentNested.getName());
    if(Files.isDirectory(fileP)){
      return fileP;
      }
    if(Files.exists(fileP)){fail("File exists but is not a directory");}
    return null;

  }
  public NestedClass visit(NestedClass nc){
    this.currentNested=nc;
    return super.visit(nc);
    }
}
