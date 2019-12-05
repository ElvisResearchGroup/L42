package is.L42.platformSpecific.javaTranslation;

import static is.L42.tools.General.todo;
import static is.L42.tools.General.unreachable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.stream.Collectors;

import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Pos;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.visitors.CloneVisitor;

public class L42TrustedIO {
  public L42Void strDebug(String s){
    Resources.out(s);
    return L42Void.instance;
    }
    
  public L42Void testActualExpected(L42Library hasPos,String name, String actual, String expected,String message){
    boolean cond=actual.equals(expected);
    Pos pos=hasPos.unwrap.pos();
    assert !name.contains("\n");
    assert !pos.fileName().toString().contains("\n");
    StringBuilder sb=new StringBuilder("###################\n");
    if(cond){sb.append("#Pass    "+name+"\n");}
    else{sb.append("#Fail    "+name+"\n");}
    sb.append("#line: "+pos.line());
    sb.append("    ");
    sb.append(pos.fileName().toString()+"\n");
    if(!cond){
      sb.append("#StrCompare\n");
      sb.append("#Actual\n");
      actual=actual.lines().map(l->"#|"+l).collect(Collectors.joining());
      sb.append(actual);
      sb.append("\n#Expected\n");
      expected=expected.lines().map(l->"#|"+l).collect(Collectors.joining());
      sb.append(expected);
      }  
    Resources.tests(sb.toString());


    return L42Void.instance;
    }
    
  public L42Void testCondition(L42Library hasPos,String name,boolean cond,String message){
    Pos pos=hasPos.unwrap.pos();
    assert !name.contains("\n");
    assert !pos.fileName().toString().contains("\n");
    StringBuilder sb=new StringBuilder("###################\n");
    if(cond){sb.append("#Pass    "+name+"\n");}
    else{sb.append("#Fail    "+name+"\n");}
    sb.append("#line: "+pos.line());
    sb.append("    ");
    sb.append(pos.fileName().toString()+"\n");
    if(!cond){
      message=message.lines().map(l->"#|"+l).collect(Collectors.joining());
      sb.append(message);
      if(!message.isEmpty() && !message.endsWith("\n")){sb.append("\n");}
      }  
    Resources.tests(sb.toString());
    return L42Void.instance;
    }
  public L42Void deployLibrary(String s, L42Library l42Lib){
    Core.L l=l42Lib.unwrap;
    ProgramTypeSystem.type(true, Program.flat(l));
    //TODO: wrap an EndError above as a L42 exception
    l=l.accept(new CloneVisitor(){
      @Override public Core.L.Info visitInfo(Core.L.Info info){
        return info.withTyped(true);
        }});
    try(
      var file=new FileOutputStream("localhost"+File.separator+s+".L42"); 
      var out=new ObjectOutputStream(file);
      ){
      out.writeObject(l);
      }
    catch (FileNotFoundException e) {throw unreachable();}
    catch (IOException e) {
      e.printStackTrace();
      throw todo();
      }
    //TODO: should throw a non deterministic exception as for 
    //memory overflow/stack overflow. It should be error S,
    //the same type of the String
    return L42Void.instance;
    }
  }
