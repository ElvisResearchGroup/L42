package helpers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import facade.L42;
import helpers.TestHelper;

@RunWith(Parameterized.class)
public class TestRunner {
  @Parameter(0) public Path p;
  @Parameter(1) public List<Opt> opts;
  public static List<Object[]> goInner(Opt ...opts){
    List<Opt>_opts=Arrays.asList(opts);
    try {
    Class<?> clazz = Class.forName(findCaller());
    return exploreFilesFrom(clazz,_opts);
    }
    catch (Throwable e) { throw handleThrowable(e);}
  }

  private static String findCaller() {
    StackTraceElement[] stes = new Error().getStackTrace();
    for(StackTraceElement ste:stes){
      if(!ste.getClassName().equals(TestRunner.class.getName())){return ste.getClassName();}
      }
      throw new Error("caller?");
  }

  public static List<Object[]> goInner(String name,Opt ...opts){
    try{
      List<Object[]> res=goInner(opts).stream()
        .filter(pi->((Path)pi[0]).getFileName().endsWith(name))
        .collect(Collectors.toList());
      if(res.isEmpty()){throw new Error("File "+name+" unavalable ");}
      return res;
    }
    catch (Throwable t){throw handleThrowable(t);}
  }
  public static List<Object[]> exploreFilesFrom(Class<?>clazz, List<Opt>opts) throws Throwable{
    assert clazz!=null;
    Path p=Paths.get(clazz.getResource("libTests").toURI());
    L42.trustPluginsAndFinalProgram=true;
    for(Opt o:opts)o.act();
    if(!opts.contains(Opt.NoDeplCurrent)){ testDeploy(clazz);}
    return StreamSupport.stream(Files.newDirectoryStream(p).spliterator(), false)
      .map(pi->new Object[]{pi,opts}).collect(Collectors.toList());
    }

public static enum Opt{
  NoTrust{public void act(){L42.trustPluginsAndFinalProgram=false;}},
  NoAss{public void act(){
    System.out.println("AssertionsDisabled");
    ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(false);
    }},
  DeplAT1{public void act(){assert false:"add call to deploy adamt1";}},
  DeplAT2{public void act(){assert false:"add call to deploy adamt2";}},
  NoDeplCurrent{public void act(){};};
  public abstract void act();
  }

public static void testDeploy(Class<?> clazz){
  try{
    Path p=Paths.get(clazz.getResource("libProject").toURI());
    System.out.println("start: "+p);
   TestHelper.configureForTest();
    L42.main(new String[]{p.toString()});
  }
  catch(Throwable t){handleThrowable(t);}
  }
@Test
public void test() throws Throwable{
  System.out.println("start: "+this.p);
  TestHelper.configureForTest();
  L42.main(new String[]{this.p.toString()});
  TestHelper.check42Fails(L42.record.toString());
  }

  public static Error handleThrowable(Throwable t){
    if (t instanceof RuntimeException) throw (RuntimeException)t;
    if (t instanceof Error) throw (Error)t;
    throw new Error(t);
    }
}

