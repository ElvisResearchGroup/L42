package helpers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
  
  public static List<Path[]> goInner() throws Throwable{
    Class<?> clazz=Class.forName(new Error().getStackTrace()[1].getClassName());
    return exploreFilesFrom(clazz);
  }
  public static List<Path[]> exploreFilesFrom(Class<?>clazz) throws Throwable{
    assert clazz!=null;
    Path p0=Paths.get(clazz.getResource("libProject").toURI());
    Path p=Paths.get(clazz.getResource("libTests").toURI());
    System.out.println("Files generation:"+p.toAbsolutePath());
    return Stream.concat(
      Stream.of(p0),StreamSupport
      .stream(Files.newDirectoryStream(p).spliterator(), false)
      )
      .map(pi->new Path[]{pi}).collect(Collectors.toList());
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

