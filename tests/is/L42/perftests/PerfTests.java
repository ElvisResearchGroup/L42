package is.L42.perftests;

import static is.L42.tools.General.L;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import is.L42.common.Parse;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tests.DeployAdamTowel;
import is.L42.top.CachedTop;
import is.L42.top.Init;

public class PerfTests {
	
	public static final PrintStream out, err;
	
	private static final boolean SAVE_RESULTS = false;
	
	private static final File programOutput;
	
	static {
		try {
			programOutput = File.createTempFile("l42testoutput", ".log");
			PrintStream stream = new PrintStream(new FileOutputStream(programOutput));
			System.setOut(stream);
			System.setErr(stream);
			out = new PrintStream(new FileOutputStream(FileDescriptor.out));
			err = new PrintStream(new FileOutputStream(FileDescriptor.err));
			} catch (Exception e) {
			throw new Error(e);
			}
		}
	
	@AfterAll
	public static void showLog()
	{
		System.setOut(out);
		System.setErr(err);
		System.out.println("L42 Program output logged to: " + programOutput.getAbsolutePath());
	}
	
//	@Test 
//	public void testAdamsTowel() throws IOException, URISyntaxException, ClassNotFoundException {
//		testRun("L42Source/AdamTowel", "adamstowel");
//		}
//
	@Test 
	public void testUnit() throws IOException, URISyntaxException, ClassNotFoundException {
		testRun("L42Source/TestUnit", "unit");
		}
//	
//	@Test 
//	public void testLoad() throws IOException, URISyntaxException, ClassNotFoundException {
//		testRun("L42Source/TestLoad", "load");
//		}
	
//	@Test 
//	public void testHelloWorld() throws IOException, URISyntaxException, ClassNotFoundException {
//		testRun("L42Source/HelloWorld2", "helloworld");
//		}
//	
//	@Test 
//	public void testCacheSaving() throws IOException, URISyntaxException, ClassNotFoundException
//	{
//		PerfResult prevres = PerfResult.getResult("cachesaveload");
//		PerfResult prevres2 = PerfResult.getResult("unit.memcache");
//		PerfResult prevres3 = PerfResult.getResult("unit.filecache");
//		File tmp = new File("tmp" + Math.random()); tmp.mkdirs();
//		var url=Paths.get(DeployAdamTowel.class.getResource("L42Source/TestUnit").toURI()).resolve("This.L42");
//		var code=Parse.codeFromPath(url);
//		CachedTop memcache = new CachedTop(L(),L());
//		Resources.clearRes();
//	    Init.topCache(memcache, url, code);
//	    Resources.clearResKeepReuse();
//	    final CachedTop finalvar = memcache;
//		Pair<PerfResult,CachedTop> res = PerfMonitor.test(() -> {
//			try {
//				finalvar.toNextCache().saveCache(tmp.toPath());
//				return CachedTop.loadCache(tmp.toPath());
//				} catch(Exception e) { throw new Error(e); }
//			}, 5, true);
//		CachedTop filecache = res.getSecond();
//		PerfResult res2 = PerfMonitor.test(() -> {
//			try {
//				Resources.clearResKeepReuse();
//				Init.topCache(finalvar.toNextCache(), url, code);
//				Resources.clearResKeepReuse();
//			    return null;
//				} catch(Exception e) { throw new Error(e); }
//			}, 5, true).getFirst();
//		PerfResult res3 = PerfMonitor.test(() -> {
//			try {
//				Resources.clearResKeepReuse();
//				Init.topCache(filecache, url, code);
//				Resources.clearResKeepReuse();
//			    return null;
//				} catch(Exception e) { throw new Error(e); }
//			}, 1, true).getFirst();
//		System.out.println("Cache saving and loading: " + res.getFirst().toString());
//		System.out.println("Unit with in-memory cache: " + res2.toString());
//		System.out.println("Unit with file cache: " + res3.toString());
//		if(SAVE_RESULTS) {
//			PerfResult.saveResult("cachesaveload", res.getFirst());
//			PerfResult.saveResult("unit.memcache", res2);
//			PerfResult.saveResult("unit.filecache", res3);
//			} else {
//			if(prevres != null) {
//				assertTrue(prevres.compareTo(res.getFirst()) <= 0);
//				}
//			if(prevres2 != null) {
//				assertTrue(prevres.compareTo(res2) <= 0);
//				}
//			if(prevres3 != null) {
//				assertTrue(prevres.compareTo(res3) <= 0);
//				}
//			}
//		}
	
	public void testRun(String path, String name) throws IOException, URISyntaxException, ClassNotFoundException {
		PerfResult prevres = PerfResult.getResult(name + ".nocache");
		PerfResult prevres2 = PerfResult.getResult(name + ".cache");
		out.println("Testing " + name);
		out.println("Previous results:");
		out.println(prevres);
		out.println(prevres2);
		out.println("New results:");
		var url=Paths.get(DeployAdamTowel.class.getResource(path).toURI()).resolve("This.L42");
		var code=Parse.codeFromPath(url);
		Pair<PerfResult,CachedTop> res = PerfMonitor.test(() -> {
			try {
				System.out.println("--------------");
				CachedTop top = new CachedTop(L(),L());
				Resources.clearRes();
			    Init.topCache(top, url, code);
			    Resources.clearResKeepReuse();
			    System.out.println("--------------");
			    return top;
				} catch(Exception e) { throw new Error(e); }
			}, 5, true);
		PerfResult res2 = PerfMonitor.test(() -> {
			try {
				Resources.clearResKeepReuse();
				Init.topCache(res.getSecond().toNextCache(), url, code);
				Resources.clearResKeepReuse();
			    return null;
				} catch(Exception e) { throw new Error(e); }
			}, 5, true).getFirst();
		out.println(name + ", no cache: " + res.getFirst().toString());
		out.println(name + ", cache: " + res2.toString());
		if(SAVE_RESULTS) {
			PerfResult.saveResult(name + ".nocache", res.getFirst());
			PerfResult.saveResult(name + ".cache", res2);
			} else {
			if(prevres != null) {
				assertTrue(prevres.compareTo(res.getFirst()) <= 0);
				}
			if(prevres2 != null) {
				assertTrue(prevres.compareTo(res2) <= 0);
				}
			}
		}
	
}
