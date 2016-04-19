package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import helpers.TestHelper;
import platformSpecific.javaTranslation.Resources;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import ast.Ast.Path;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Program;
public class TestIntrospection {

@RunWith(Parameterized.class)
public static class TestGiveInfoType {
  @Parameter(0) public String _cb;
  @Parameter(1) public String _path;
  @Parameter(2) public int methNum;
  @Parameter(3) public int typeNum;
  @Parameter(4) public String _expected;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {"{B:{method Void m()}}","B",1,0,
    "{Kind:{//@stringU\n//TypeReport\n}"
    +"TypeKind:{//@stringU\n//Normal\n}"
    +"Mdf:{//@stringU\n//Immutable\n}"
//+"ResolvedMdf:{//@stringU\n//Immutable\n}"
    +"Path:{//@Void\n}"
    +"ResolvedPath:{//@Void\n}"
    +"Ph:{//@stringU\n//false\n}"
    +"ResolvedPh:{//@stringU\n//false\n}"
    +"Suffix:{//@stringU\n//\n}"
//+"ParName:{//@stringU\n//\n}"
    +"Doc:{}"
    +"AllAsString:{//@stringU\n//Void\n}}"
    //------------------------
  },{"{B:{method Void m(Any x)}}","B",1,1,
    "{ Kind:{//@stringU\n//TypeReport\n}"
    +"TypeKind:{//@stringU\n//Normal\n}"
  +"Mdf:{//@stringU\n//Immutable\n}"
//+"ResolvedMdf:{//@stringU\n//Immutable\n}"
  +"Path:{//@Any\n}"
+"ResolvedPath:{//@Any\n}"
+"Ph:{//@stringU\n//false\n}"
+"ResolvedPh:{//@stringU\n//false\n}"
+"Suffix:{//@stringU\n//\n}"
//+"ParName:{//@stringU\n//x\n}"
+"Doc:{}"
+"AllAsString:{//@stringU\n//Any\n}}"
}});}
@Test  public void test() {
  ClassB cb=getClassB(_cb);
  Path path=Path.parse(_path);
  ClassB expected=getClassB(_expected);
  ClassB result=Introspection.giveInfoType(null,Program.empty(),cb,path.getCBar(),methNum,typeNum);
  TestHelper.assertEqualExp(expected,result);
  }
}
}