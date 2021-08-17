package is.L42.top;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.generated.Pos;
import is.L42.tools.General;

class CheckFileSystemPortable{
  Path top;
  List<Path> okPaths=new ArrayList<>();
  List<Path> badPaths=new ArrayList<>();
  List<Path> repeatedPaths=new ArrayList<>();
  public void makeError(List<Pos> poss) {
    if(badPaths.isEmpty() && repeatedPaths.isEmpty()){return;}
    throw new EndError.InvalidImplements(poss,ErrMsg.dotDotDotSouceNotPortable(top,badPaths,repeatedPaths));
  }
  CheckFileSystemPortable(Path top){
    assert Files.isDirectory(top);
    this.top=top;
    try (Stream<Path> walk = Files.walk(top)) {//the first one is guaranteed to be the root
      walk.skip(1).forEach(this::checkSystemIndependentPath);
      }
    catch (IOException e) {throw General.unreachable();}
    for(var p:okPaths) {checkRepeatedPaths(p);}
    okPaths.removeAll(repeatedPaths);
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
    "$BadClus", "$Secure", "$Upcase", "$Extend", "$Quota", "$ObjId", "$Reparse"
    );
  private void checkSystemIndependentPath(Path path){
    String lastName=path.getName(path.getNameCount()-1).toString();
    String[] parts=lastName.split("\\.");
    var ko=forbiddenWin.stream().filter(
      f->Stream.of(parts).anyMatch(p->p.equalsIgnoreCase(f))
      ).count();
    if(ko!=0) {badPaths.add(path);}
    else {okPaths.add(path);}
    }
  }