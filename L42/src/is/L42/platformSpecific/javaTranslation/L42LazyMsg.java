package is.L42.platformSpecific.javaTranslation;

import static is.L42.tools.General.todo;
import static is.L42.tools.General.unreachable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Pos;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.visitors.CloneVisitor;

public class L42LazyMsg {
  public L42LazyMsg(){this.lazy=()->"";}
  public String getMsg(){
    if(msg!=null){return msg;}
    msg=lazy.get();
    return msg;
    }
  public void setMsg(String msg){this.msg=msg;}
  private String msg=null;
  private final Supplier<String> lazy;
  public L42LazyMsg(Supplier<String>lazy){this.lazy=lazy;}
  public L42LazyMsg(String msg){this.msg=msg;this.lazy=()->"";}
  }
