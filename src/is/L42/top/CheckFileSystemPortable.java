package is.L42.top;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.generated.Pos;
import is.L42.tools.General;

public class CheckFileSystemPortable{
  Path top;
  List<Path> okPaths=new ArrayList<>();
  List<Path> badPaths=new ArrayList<>();
  List<Path> repeatedPaths=new ArrayList<>();
  public void makeError(List<Pos> poss) {
    if(badPaths.isEmpty() && repeatedPaths.isEmpty()){return;}
    throw new EndError.InvalidImplements(poss,ErrMsg.dotDotDotSouceNotPortable(top,badPaths,repeatedPaths));
  }
  public boolean isDirectory(Path top){ return Files.isDirectory(top); }
  public CheckFileSystemPortable(Path top){
    assert isDirectory(top);
    this.top=top;
    walkIn1(top);
    for(var p:okPaths) {checkRepeatedPaths(p);}
    okPaths.removeAll(repeatedPaths);
    }
  HashSet<Path> visited=new HashSet<>();
  public void walkIn1(Path path) {
    assert !visited.contains(path):
      path;
    visited.add(path);
    try (Stream<Path> walk = Files.walk(path,1)) {//the first one is guaranteed to be the root
      walk.skip(1).forEach(this::checkSystemIndependentPath);
      }
    catch (IOException e) {throw General.unreachable();}
    }
  private void checkRepeatedPaths(Path p){
    var s=p.toString();
    for(var pi:okPaths){
      if (pi!=p && pi.toString().equalsIgnoreCase(s)) {repeatedPaths.add(pi);}
      }
    }
  private static final List<String>forbiddenWin=List.of(
    "CON", "PRN", "AUX", "CLOCK$", "NUL",
    "COM0", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
    "LPT0", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9",
    "LST", "KEYBD$", "SCREEN$", "$IDLE$", "CONFIG$", 
    "$Mft", "$MftMirr", "$LogFile", "$Volume", "$AttrDef", "$Bitmap", "$Boot",
    "$BadClus", "$Secure", "$Upcase", "$Extend", "$Quota", "$ObjId", "$Reparse",
    ""
    );
  static final Pattern regex = Pattern.compile("[a-zA-Z0-9\\_\\-\\$]*");
  public void checkSystemIndependentPath(Path path){
    String lastName=path.getName(path.getNameCount()-1).toString();
    if(lastName.endsWith(".")) { badPaths.add(path); return; }
    boolean skip=path.toFile().isHidden() || lastName.startsWith(".");
    if(skip){ return; }
    if(lastName.length()>248) { badPaths.add(path); return; }
    //path.toFile().isSystem()//system files??
    String[] parts=lastName.split("\\.");
    for(var p:parts) { 
      if(!regex.matcher(p).matches()) { badPaths.add(path); return; }
      }
    var ko=forbiddenWin.stream().filter(
      f->Stream.of(parts).anyMatch(p->p.equalsIgnoreCase(f))
      ).count();
    if(ko!=0){ badPaths.add(path); return; }
    okPaths.add(path);
    walkIn1(path);//recursive exploration
    }
  public String toString() { return
      "okPaths=" + okPaths.toString().replace(""+File.separatorChar,"|") +
    "\nbadPaths=" + badPaths.toString().replace(""+File.separatorChar,"|") +
    "\nrepeatedPaths=" + repeatedPaths.toString().replace(""+File.separatorChar,"|") + "\n";
    }
  }