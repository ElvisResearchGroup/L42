package is.L42.tests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.stream.Stream;
import is.L42.platformSpecific.javaTranslation.Resources;

public class TestCache extends is.L42.tools.TestL42Bridge{
  public static Stream<L42Test> test() throws IOException, URISyntaxException {
    Resources.clearRes();
    var res=TestCache.class.getResource("L42Source/TestCache");
    is.L42.main.Main.main(Paths.get(res.toURI()).toString());
    return fromString(Resources.tests());
    }
  }