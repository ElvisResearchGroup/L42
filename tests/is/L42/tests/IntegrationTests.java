package is.L42.tests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.stream.Stream;

import is.L42.main.Main;
import is.L42.tools.TestL42Bridge;

public class IntegrationTests extends TestL42Bridge {
  public static String name(String name)throws IOException, URISyntaxException{
    var res=IntegrationTests.class.getResource(name);
    assert res!=null:"The resource "+name+" is invalid";
    var path=Path.of(res.toURI());
    return name(path);
    }
  public static Stream<L42Test> test() throws IOException, URISyntaxException {
    var currentRepoveVersion=Main.l42IsRepoVersion;
    try{
      Main.l42IsRepoVersion=Main.testingRepoVersion;
      return fromStream(integrationTests()); 
      }
    finally { Main.l42IsRepoVersion=currentRepoveVersion; }
    }
  public static Stream<String> integrationTests() throws IOException, URISyntaxException {    
    Stream<String> adamTowel=Stream.of(
      name("L42Source/AdamsTowel")
      );
    Stream<String> deployed=DeployAllLibraries.deployAllLibraries();
    //Stream<String> deployed=Stream.of();
    Stream<String> integrationTests=Stream.of(      
      name("L42Source/TestAST"),
      name("L42Source/TestCacheCall"),
      name("L42Source/TestCoherent"),
      name("L42Source/TestData"),
      name("L42Source/TestDecoratorsErrors"),
      name("L42Source/TestEagerCache"),
      name("L42Source/TestEnum"),
      name("L42Source/TestForkJoin"),
      name("L42Source/TestFullMergePattern"),
      name("L42Source/TestIntrospection"),
      name("L42Source/TestJavaSlaveDirect"),
      name("L42Source/TestLazyCache"),
      name("L42Source/TestList"),
      name("L42Source/TestMap"),
      name("L42Source/TestNonDeterministicError"),
      name("L42Source/TestOpt"),
      name("L42Source/TestOrganize"),
      name("L42Source/TestResetDocs"),
      name("L42Source/TestSelfCollection"),
      name("L42Source/TestSUtils"),
      name("L42Source/TestTutorialExercises"),
      name("L42Source/TestTutorialJavaSlave"),
      name("L42Source/TestView"),
      name("L42Source/TestDecoratorDecorator"),
      name("L42Source/TestSifoExamples"),
      name("L42Source/BattleShip")
      //name("L42Source/TestAdventOfCode2021")
      );
    return Stream.concat(adamTowel,Stream.concat(deployed,integrationTests));
    }
  }