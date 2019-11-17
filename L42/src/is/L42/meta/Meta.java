package is.L42.meta;

import static is.L42.tools.General.todo;
import static is.L42.tools.General.unreachable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42ClassAny;
import is.L42.platformSpecific.javaTranslation.L42Fwd;
import is.L42.platformSpecific.javaTranslation.L42Library;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.visitors.CloneVisitor;

public class Meta {
  //private Program p;
  //public Meta(Program p){this.p=p;}
  public L42Library simpleRedirect(String innerPath, L42Library l42Lib, L42Any classAny){
    System.out.println(innerPath);
    L42ClassAny cn;
    if(classAny instanceof L42ClassAny){cn=(L42ClassAny)classAny;}
    else{cn=((L42Fwd)classAny).asPath();} 
    System.out.println(cn.unwrap);
    return l42Lib;
    //Resources.currentP
    //Core.L l=l42Lib.unwrap;
    }
  }
