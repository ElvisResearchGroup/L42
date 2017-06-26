package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import ast.Ast.Path;
import ast.ExpCore.ClassB;
import helpers.TestHelper;

public class ExtractInfoTest {
  
  @RunWith(Parameterized.class)
  public static class TestUsageCount {
    @Parameter(0) public String _classInput;
    @Parameter(1) public String _path;
    @Parameter(2) public Integer count;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
      {"{}","B",0
    },{"{()}","B",0
    },{"{}","This0",0
    },{"{()}","This0",1//for desugaring of ()
    },{"{B:{(This0 a)}}","This0",0
    },{"{B:{(This1 a)}}","This0",1//ctx, getter, exposer
    },{"{B:{(This0 a)}}","This0.B",1//ctx*2, getter,exposer
    },{"{B:{(This0 a, This1.B b)}}","This0.B",1//ctx*3, getter*2,exposer*2
  }});}
  @Test  public void test() {
    ClassB classInput=getClassB(false,null,_classInput);
    Path path=Path.sugarParse(_path);
    if(!path.isCore()){
      path=Path.outer(0,path.sugarNames());//normalize, put This0 on top.
      }
    ExtractInfo.IsUsed iu=new ExtractInfo.IsUsed(path);
    classInput.accept(iu);
    assertEquals(iu.whereUsed.size(),(int)count);//cast to avoid overloading ambiguity :(
    }
  }


  
}
