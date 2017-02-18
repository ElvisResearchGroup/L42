package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ast.Ast;
import ast.Ast.MethodSelector;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Util.InfoAboutMs;
import auxiliaryGrammar.Locator;
import auxiliaryGrammar.Program;
import auxiliaryGrammar.Locator.Kind;
import tools.Assertions;

class RenameAlsoDefinition extends RenameUsage{
  public RenameAlsoDefinition(ClassB visitStart,CollectedLocatorsMap maps,Program p) { super(visitStart,maps,p);}

  public List<Member> liftMembers(List<Member> s) {
    List<Member>result1=super.liftMembers(s);
    List<Member>result2=new ArrayList<>();
    for(Member m:result1){
      Optional<Member> optM = Program.getIfInDom(result2,m);
      if (!optM.isPresent()){result2.add(m);continue;}
      Member m2=optM.get();
      result2.remove(m2);
      Sum.doubleSimmetricalMatch(null/*boh, null program, does it breaks?*/,
          visitStart,visitStart,result2,this.getLocator().getClassNamesPath(),m,m2);
    //remove clashes here
    }
    return result2;
    }

  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    //System.out.println("visitMethodImplemented "+mi.getS());
    return potentiallyRenameMethodImplementedHeader(super.visit(mi));
  }
  private MethodImplemented potentiallyRenameMethodImplementedHeader(MethodImplemented mi) {
    ClassB currentCb=this.getLocator().getLastCb();
    Program ep=Program.getExtendedProgram(p, this.getLocator().getCbs());
    //List<Path> supers = Program.getAllSupertypes(ep, currentCb);
    InfoAboutMs info = Program.getMT(ep, mi.getS(),currentCb);
    assert !info.getAllSuper().isEmpty();
    Locator original=this.getLocator().copy();
    boolean isOut=original.moveInPath(info.getOriginal());
    if(isOut){return mi;}
    for(Locator pMx :maps.selectors){
      assert pMx.kind()==Kind.Method;
      MethodSelector s=pMx.getLastMember().match(
          nc->{throw Assertions.codeNotReachable();},
          mimpl->mimpl.getS(),
          mt->mt.getMs());
      if(!mi.getS().equals(s)){continue;}
      Locator renamed =pMx.copy();
      renamed.toFormerNodeLocator();
      if(!original.equals(renamed)){return mi;}
      MethodSelector ms2=(MethodSelector) pMx.getAnnotation();
      return mi.withS(ms2);
      }
    return mi;
  }
  
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    //System.out.println("visitNestedClass "+nc.getName());
    Locator current=this.getLocator().copy();
    current.pushMember(nc);
    for(Locator nl:maps.nesteds){
      if(!(nl.getAnnotation() instanceof Ast.C)){continue;}
      if(!nl.equals(current)){continue;}
      assert nl.getAnnotation() instanceof Ast.C:nl.getAnnotation();
      return super.visit(nc.withName((Ast.C)nl.getAnnotation()));
    }
    return super.visit(nc);
  }
}