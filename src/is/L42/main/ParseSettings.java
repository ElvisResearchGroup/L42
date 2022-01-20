package is.L42.main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import is.L42.common.Parse;

public class ParseSettings {
  public static final String defaultArgs=Settings.defaultOptions.toString(); 
  public static void main(String...arg) throws IOException {
    if(arg.length==0){ System.out.println(defaultArgs); return;}
    String name = arg[0];
    assert name != null;
    Path path=Paths.get(name);
    boolean exists=Files.exists(path);
    boolean isDir=exists && Files.isDirectory(path);
    if(!exists || !isDir){ System.out.println(defaultArgs); return;}
    Path thisFile=path.resolve("Setti.ngs");
    if(!Files.exists(thisFile)){ System.out.println(defaultArgs); return ;}
    String code=Files.readString(path,StandardCharsets.US_ASCII);
    code=code.replace("\r","");
    var o=Settings.defaultOptions;
    try{o=Parse.sureSettings(thisFile, code).options();}
    catch(Throwable t){
      System.err.println("better printout for"+t);
      //what happens if there is error but not out?
      return;
      }
    System.out.println(o.toString());
    }
}