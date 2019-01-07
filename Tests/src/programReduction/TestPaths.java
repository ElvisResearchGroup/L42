package programReduction;

import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import ast.Ast;
import ast.Ast.Path;
import ast.ExpCore;
import ast.PathAux;
import helpers.TestHelper;

public class TestPaths {
@RunWith(Parameterized.class)
public static class TestReorganize {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _paths;
  @Parameter(2) public String _expected;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"This0.Foo", "This0.Foo "
  },{lineNumber(),"This0.Foo This0.Boo", "This0.Foo This0.Boo "
  },{lineNumber(),"This3.Foo This0.Boo", "This0.Boo This3.Foo "
  },{lineNumber(),"This3.Foo This2.Foo.Bar This2.Foo This2.Foo.Ber.Beer This0.Boo", "This0.Boo This2.Foo This3.Foo "
  },{lineNumber(),"This0.Foo This0.Foo This0.Foo", "This0.Foo "

  }});}
@Test  public void test() {
  String[] ss=_paths.split(" ");
  List<Ast.Path> ps=new ArrayList<>();
  for( String s:ss){ ps.add(Path.parse(s));}
  Paths paths=Paths.reorganize(ps);
  assertEquals(_expected,paths.toString());
  }
  }

@RunWith(Parameterized.class)
public static class TestPrefix{
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _paths;
  @Parameter(2) public String _prefix;
  @Parameter(3) public String _expected;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"This0.Foo","A.B", "This0.A.B.Foo "
  },{lineNumber(),"This0.A This1.B This2.C This3.D", "W.E",
      "This0.C This0.W.B This0.W.E.A This1.D "

 }});}
@Test  public void test() {
  String[] ss=_paths.split(" ");
  List<Ast.Path> ps=new ArrayList<>();
  List<Ast.C>prefix=PathAux.parseValidCs(_prefix);
  for( String s:ss){ ps.add(Path.parse(s));}
  Paths paths=Paths.reorganize(ps);
  paths=paths.prefix(prefix);
  assertEquals(_expected,paths.toString());
  }
  }

}
