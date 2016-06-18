package helpers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import facade.L42;
import helpers.TestHelper;

@RunWith(Parameterized.class)
abstract public class SeeFile {
  @Parameter(0) public Path p;
  @Parameter(1) public Set<Opt> opts;
  public static List<Object[]> goInner(Opt ...opts){
    Set<Opt>_opts=new HashSet<>(Arrays.asList(opts));
    try {
    Class<?> clazz = Class.forName(new Error().getStackTrace()[1].getClassName());
    return exploreFilesFrom(clazz,_opts);
    }
    catch (Throwable e) { throw new Error(e);}
  }

  public static List<Object[]> goInner(String name,Opt ...opts) throws Throwable{
    List<Object[]> res=goInner(opts).stream()
      .filter(pi->((Path)pi[0]).getFileName().equals(name))
      .collect(Collectors.toList());
    if(res.isEmpty()){throw new Error("File "+name+" unavalable ");}
    return res;
  }
  public static List<Object[]> exploreFilesFrom(Class<?>clazz, Set<Opt>opts) throws Throwable{
    assert clazz!=null;
    Path p0=Paths.get(clazz.getResource("libProject").toURI());
    Path p=Paths.get(clazz.getResource("libTests").toURI());
    System.out.println("Files generation:"+p.toAbsolutePath());
    return Stream.concat(
      Stream.of(p0),StreamSupport
      .stream(Files.newDirectoryStream(p).spliterator(), false)
      )
      .map(pi->new Object[]{pi,opts}).collect(Collectors.toList());
    }

public static enum Opt{
  NoTrust{public void act(){L42.trustPluginsAndFinalProgram=false;}},
  NoAss{public void act(){
    System.out.println("AssertionsDisabled");
    ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(false);
    }},
  DeplAT1{public void act(){}},
  DeplAT2{public void act(){}};
  public abstract void act();
  }
@Test
public void test() throws Throwable{
  L42.trustPluginsAndFinalProgram=true;
  TestHelper.configureForTest();
  System.out.println("start: "+this.p);
  L42.main(new String[]{this.p.toString()});
  System.out.println(this.p);
  TestHelper.check42Fails(L42.record.toString());
  }
}

