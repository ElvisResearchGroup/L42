package is.L42.tests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.constraints.FreshNames;
import is.L42.common.EndError.InvalidImplements;
import is.L42.common.EndError.PathNotExistent;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.main.Main;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.AtomicTest;
import is.L42.tools.TestL42Bridge;
import is.L42.top.Init;
import is.L42.top.Top;
import is.L42.translationToJava.Loader;
import is.L42.visitors.FullL42Visitor;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;
import static org.junit.jupiter.api.Assertions.*;

public class TestLazyCache extends TestL42Bridge {
  public static Stream<L42Test> test() throws IOException, URISyntaxException {
    Resources.clearRes();
    var res=TestLazyCache.class.getResource("L42Source/TestLazyCache");
    Main.main(Paths.get(res.toURI()).toString());
    return fromString(Resources.tests());
    }
  }