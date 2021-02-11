package is.L42.tests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.stream.Stream;
import is.L42.platformSpecific.javaTranslation.Resources;

public class TestEagerCache extends is.L42.tools.TestL42Bridge{
  public static Stream<L42Test> test() throws IOException, URISyntaxException {
    Resources.clearResKeepReuse();
    var res=TestEagerCache.class.getResource("L42Source/TestEagerCache");
    is.L42.main.Main.main(Paths.get(res.toURI()).toString());
    return fromString(Resources.tests());
    }
  }