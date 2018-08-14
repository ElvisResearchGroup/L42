package is.L42.connected.withFileSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import platformSpecific.fakeInternet.ActionType;
import platformSpecific.fakeInternet.PluginType;
import platformSpecific.javaTranslation.Resources;
import static auxiliaryGrammar.EncodingHelper.*;

public class Plugin implements PluginType.WellKnown {
  // This is non-deterministic since it may throw an IOException
  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library,ActionType.NormType.Library})
  public Resources.Void MfileDebug£xfileName£xcontent(Object _fName,Object _content){
    String fName=ensureExtractStringU(_fName);
    String content=ensureExtractStringU(_content);
    java.nio.file.Path p=Paths.get(fName);
    try {Files.write(p, content.getBytes());}
    catch (IOException e) {throw new Error(e);}
    return Resources.Void.instance;
  }

  // Reading a file is obviously non-deterministic
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library})
  public Object MfileReadDebug£xfileName(Object _fName){
    String fName=ensureExtractStringU(_fName);
    java.nio.file.Path p=Paths.get(fName);
    try {
      byte[] res = Files.readAllBytes(p);
      return new String(res);
      }
    catch (IOException e) {throw new Error(e);}
  }

  // Allegedly Path.toAbsolutePath may throw an IOError...
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library})
  public Object MlocalToAbsolute£xfileName(Object _fName){
    String fName=ensureExtractStringU(_fName);
    java.nio.file.Path p= Paths.get(fName);
    String s = p.toAbsolutePath().normalize().toString();
    return s;
  }
}
