package platformSpecific.javaTranslation;

import facade.Configuration;
import facade.ErrorFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import coreVisitors.CollectClassBs0;
import coreVisitors.CollectPaths0;
import platformSpecific.fakeInternet.PluginType;
import sugarVisitors.ToFormattedText;
import tools.Assertions;
import ast.Ast;
import ast.ErrorMessage;
import ast.ErrorMessage.UserLevelError;
import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.Path;
import ast.Ast.SignalKind;
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.EncodingHelper;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;

public class Resources {
  public static final ErrorMessage.PluginActionUndefined notAct=new ErrorMessage.PluginActionUndefined(-2);
  public static final ErrorMessage.PluginActionUndefined actInEnd=new ErrorMessage.PluginActionUndefined(-1);
  private static final HashMap<String,Object> usedRes=new HashMap<>();
  public static String submitRes(Object cb){
    HashSet<String> hs=new HashSet<>(usedRes.keySet());
    String newK=Functions.freshName("key", hs);
    usedRes.put(newK,cb);
    return newK;
    }
  private static Program p;
  public static Program getP(){
    assert p!=null;
    return p;
    }
  public static <T> T withPDo(Program _p,Supplier<T> action){
    if(p!=null){throw new IllegalAccessError();}
    try{
      p=_p;
      return action.get();
      }
    finally{p=null;}
    }
  public static Object getRes(String key){
    Object o=usedRes.get(key);
    if(o==null){throw new Error("Invalid resource"+key+" Valid resources are: "+usedRes.keySet());}
    return o;
    }
  public static void clearRes() {
    usedRes.clear();
  }

  @SuppressWarnings("serial")
  public static class Error extends RuntimeException{
    public String toString() {return "Error["+ unbox +"]";}
    public final Object unbox; public Error(Object u){
      //assert !u.getClass().getName().startsWith("generated.Program42$");
      unbox=u;}
    public static Error multiPartStringError(String kind,String ... map){
      List<ExpCore.ClassB.Member> ms=new ArrayList<>();
      assert map.length%2==0;
      ms.add(new ExpCore.ClassB.NestedClass(Doc.empty(),"Kind", EncodingHelper.wrapStringU(kind)));
      for(int i=0;i<map.length;i+=2){
        ms.add(new ExpCore.ClassB.NestedClass(Doc.empty(),
            map[i], EncodingHelper.wrapStringU(map[i+1])));
      }
      ExpCore.ClassB cb=new ExpCore.ClassB(Doc.empty(), Doc.empty(), false, Collections.emptyList(), ms,Stage.None);
      return new Error(cb);
      }
    }
  @SuppressWarnings("serial")
  public static class Exception extends RuntimeException{
    public String toString() {return "Exception["+ unbox +"]";}
    public final Object unbox; public Exception(Object u){unbox=u;}
    }
  @SuppressWarnings("serial")
  public static class Return extends RuntimeException{
    public String toString() {return "Return["+ unbox +"]";}
    public final Object unbox; public Return(Object u){unbox=u;}
    }
  public static class Void{
    public static final Void instance=new Void();
    public static final Void type=new Void();
    }
  public static class Any{public static final Any type=new Any();}
  public static class Library{public static final Library type=new Library();}
  public static interface PhI<T>{
    public void commit(T t);
    public void addAction(java.util.function.Consumer<T> r);
    }
  public static interface Revertable{
    public static ast.ExpCore doRevert(Object o){
      if (o instanceof Revertable){return ((Revertable)o).revert();}
      return EncodingHelper.wrapResource(o);
    }
    public ast.ExpCore revert();
  }
  public static boolean isValid(Program p,Object res, Object[] xs) {
    ExpCore ec0=Revertable.doRevert(res);
    List<ExpCore> es=new ArrayList<>();
    for(Object o:xs){
      es.add(Revertable.doRevert(o));
    }
    boolean strict=true;
    for(ExpCore ec:es){
      List<ClassB> cbs = CollectClassBs0.of(ec);
      List<Path> ps = CollectPaths0.of(ec);
      for(ClassB cb:cbs){
        ClassB ct1= Configuration.typeSystem.typeExtraction(p,cb);
        if(ct1.getStage()==Stage.Less ||ct1.getStage()==Stage.None  ){strict=false;}
        }
      for(Path path:ps){
        if(path.isPrimitive()){continue;}
        Stage extracted=p.extract(path).getStage();
        if(extracted==Stage.Less || extracted==Stage.None){strict=false;}
        }
      }
    List<ClassB> cbs = CollectClassBs0.of(ec0);
    for(ClassB cb:cbs){
      ClassB ct= Configuration.typeSystem.typeExtraction(p,cb);
      try{Configuration.typeSystem.checkCt( p, ct);}
      catch(ErrorMessage msg){
        UserLevelError err = ErrorFormatter.formatError(msg);
        throw Assertions.codeNotReachable("try to make this happen, is it possible? it should mean bug in plugin code\n"/*+ToFormattedText.of(ct)*/+"\n\n"+err+"\n---------------\n");
      }
      if(strict && (ct.getStage()==Stage.Less || ct.getStage()==Stage.None)){
        throw Assertions.codeNotReachable("try to make this happen, is it possible? it should mean bug in plugin code\n"/*+ToFormattedText.of(ct)*/);
      }
    }
    //confer method under to be in this form; remove cals from method under, be sure that small step use
    //same stuff as compiled step -- dispatching
    return true;
  }
  public static ExpCore validateResult(Program p, ExpCore result,ClassB l1,ClassB l2,Path path) {
    if(!(result instanceof ClassB)){return result;}
    boolean strict=true;
    if(l1!=null){
      ClassB ct1= Configuration.typeSystem.typeExtraction(p,l1);
      if(ct1.getStage()==Stage.Less){strict=false;}
    }
    if(l2!=null){
      ClassB ct2= Configuration.typeSystem.typeExtraction(p,l2);
      if(ct2.getStage()==Stage.Less){strict=false;}
    }
    if(path!=null && path.isCore()){
      if(p.extract(path).getStage()==Stage.Less){strict=false;}
    }
    ClassB ct= Configuration.typeSystem.typeExtraction(p,(ClassB)result);
    try{Configuration.typeSystem.checkCt( p, ct);}
    catch(ErrorMessage msg){
        UserLevelError err = ErrorFormatter.formatError(msg);
      throw Assertions.codeNotReachable("try to make this happen, is it possible? it should mean bug in plugin code\n"/*+ToFormattedText.of(ct)*/+"\n\n"+err+"\n---------------\n");
    }
    if(strict && ct.getStage()==Stage.Less){
      throw Assertions.codeNotReachable("try to make this happen, is it possible? it should mean bug in plugin code\n"/*+ToFormattedText.of(ct)*/);
    }
    return result;
  }

  public static ExecutorService pluginThreads=Executors.newCachedThreadPool();
  public static <T> T block(java.util.function.Supplier<T> p){return p.get();}
  public static platformSpecific.javaTranslation.Resources.Void unused=null;

  public static interface PlgClosure<Pt extends PluginType,T>{
    T apply(Pt plg,Object[] xs);
  }
  /**
   * @param plg plugin instance
   * @param cls plugin executor
   * @param xs parameters
   * @return a safe result, or a safe error, or an non-action exception
   */
  public static <Pt extends PluginType,T> T plgExecuteSafe(Program p,Pt plg,PlgClosure<Pt,T> cls,Object ... xs){
    try{
      T res=cls.apply(plg, xs);
      if(Resources.isValid(p,res,xs)){return res;}
      else{throw Resources.notAct;}
      }
    catch(Resources.Error errF){
      if(Resources.isValid(p,errF.unbox,xs)){throw errF;}
      else{throw Resources.notAct;}
      }
    catch(ErrorMessage.PluginActionUndefined undF){throw undF;}
   //catch(java.lang.Error |RuntimeException msg){//eclipse debugger can not hande it
    catch(AssertionError msg){ throw msg;}
    catch(ErrorMessage msg){
      UserLevelError err = ErrorFormatter.formatError(msg);
      throw Assertions.codeNotReachable("try to make this happen, is it possible? it should mean bug in plugin code\n"+err+"\n---------------\n");
    }
    catch(NullPointerException msg){throw msg;}
    catch(RuntimeException msg){
      //throw Resources.notAct;//will be
      throw Assertions.codeNotReachable("try to make this happen, is it possible? it should mean bug in plugin code\n"+msg+"\n---------------\n");
      }
    catch(java.lang.Error msg){
      //throw Resources.notAct;//will be
      throw Assertions.codeNotReachable("try to make this happen, is it possible? it should mean bug in plugin code\n"+msg+"\n---------------\n");
      }
    catch(Throwable tF){
      //throw Resources.notAct;//will be
      throw new Error(tF);//To debug
      }
    }
  /*public Object bar(Plugin plg,Object e1, Object e2,Callable<Object> conclE){
    return plgExecutor("dbgInfo",null,new Plugin(),
        (plF,xsF)->plF.MsumInt32£xn1£xn2(xsF[0],xsF[1]),
        conclE,e1,e2);
  }*/
  public static <Pt extends PluginType,T> T plgExecutor(String plgCall,Program p,Pt plg,PlgClosure<Pt,T> cls,Callable<T> concl, Object ... es){
    //System.err.println("Executing now::"+plgCall);
    Future<T> exe=null;
    try{//for finally
      while(true){//cycle on another plugin supervision
        try{return plgExecuteSafe(p,plg,cls,es);}//call plg
        catch(ErrorMessage.PluginActionUndefined und){
          int wait=und.getWait();
          if(wait<-1){//not call me again: wait until the end and return the result
            return justGetResult(concl, exe);
            }
          //else, we are supervisionating an expression and plg will be called again
          if(exe==null){exe=pluginThreads.submit(concl);}
          justWait(exe, wait);
          }
        }
    }finally{if(exe!=null){exe.cancel(true);}}
  }
  public static <T> void justWait(Future<T> exe, int wait){
    try{
      if(wait!=-1){//timeout
        try{exe.get(wait, TimeUnit.MILLISECONDS);}
        catch(TimeoutException e){}//loop again
        }
      else {exe.get();}
      }
    catch (InterruptedException ie){
      Thread.currentThread().interrupt();
      throw new Error(ie);
      }
    catch (ExecutionException ee){
      if(ee.getCause() instanceof RuntimeException){
        return;
        //DO Nothing, just wait//throw (RuntimeException)ee.getCause();
        }
      throw new Error(ee);
      }
    }
  public static <T> T justGetResult(Callable<T> concl, Future<T> exe){
    try{
      if(exe!=null){return exe.get();}
      return concl.call();
      }
    catch (InterruptedException ie){
      Thread.currentThread().interrupt();
      throw new Error(ie);
      }
    catch (ExecutionException ee){
      if(ee.getCause() instanceof RuntimeException){
        throw (RuntimeException)ee.getCause();
        }
      throw new Error(ee);
      }
    catch (java.lang.Exception exc){
      if(exc instanceof RuntimeException){
        throw (RuntimeException)exc;
        }
      throw new Error(exc);
      }
    }
}










/*

C:\Users\marco\Desktop\latexHelper\elvisMarcoServetto\L42ProgrammingGuide\L42TestRunApril>
C:\Users\marco\Desktop\latexHelper\elvisMarcoServetto\L42ProgrammingGuide\L42TestRunApril>
C:\Users\marco\Desktop\latexHelper\elvisMarcoServetto\L42ProgrammingGuide\L42TestRunApril>
C:\Users\marco\Desktop\latexHelper\elvisMarcoServetto\L42ProgrammingGuide\L42TestRunApril>
C:\Users\marco\Desktop\latexHelper\elvisMarcoServetto\L42ProgrammingGuide\L42TestRunApril>L42 examples\DeployC
ollections

C:\Users\marco\Desktop\latexHelper\elvisMarcoServetto\L42ProgrammingGuide\L42TestRunApril>C:\Progra~1\Java\jdk
1.8.0_40\bin\java -jar L42_05Main.jar examples\DeployCollections
Compilation Iteration: 1
Compilation Iteration: 2
Compilation Iteration: 3
Exception in thread "main" java.lang.AssertionError: -Code should not be reachable:: try to make this happen,
is it possible? it should mean bug in plugin code


ErrorMessage.UserLevelError(super=ast.ErrorMessage$UserLevelError, kind=TypeError, p=file: ...\VectorStruct.L4
2; from line:10(pos:4) to line:217(pos:127), internal=ErrorMessage.MethodNotPresent(super=ast.ErrorMessage$Met
hodNotPresent: [
[Any.#begin().#add(that:Any.#stringParser(that:{'@stringU
'Elem
}), into:Any.#apply(that:that)).#add(that:Any.#stringParser(that:{'@stringU
'VectorStruct
}), into:Any.#stringParser(that:{'@stringU
'Outer0
})).#end().#left(that:{
Elem:{interface }
Kind:'@private
{
type method 'read/lent/mut/type/capsule/
Outer1::Elem elem() error Outer4::Template::S.#stringParser(that:{'@stringU
'InternalUseOnly
})
type method 'read/lent/mut/type/capsule/
Outer1::Elem elemRead() error Outer4::Template::S.#stringParser(that:{'@stringU
'InternalUseOnly
})}
VectorStruct:{
type method ' size should be computed?
mut Outer0 #apply(mut Outer0::Cell^head'@consistent
, Outer4::Template::N^size'@consistent
)
mut method '@consistent
Void head(mut Outer0::Cell that)
mut method '@consistent
mut Outer0::Cell #head()
read method '@consistent
read Outer0::Cell head()
mut method '@consistent
Void size(Outer4::Template::N that)
mut method '@consistent
Outer4::Template::N #size()
read method '@consistent
Outer4::Template::N size()
Cell:{interface }
CellNext:{<:Outer1::Cell
type method
mut Outer0 #apply(Outer2::Kind::elem() ^val'@consistent
, mut Outer1::Cell^next'@consistent
)
mut method '@consistent
Outer2::Kind::elem() #val()
mut method '@consistent
mut Outer1::Cell #next()
read method '@consistent
read Outer1::Cell next()
read method
Outer2::Kind::elemRead() val() }
CellEnd:{<:Outer1::Cell
type method
mut Outer0 #apply() }
read method
Void reportError(Outer4::Template::N that) error Outer4::Template::S.#stringParser(that:{'@stringU
'IndexOutOfBound: max is
}).#plusplus(that:this.size()).#plusplus(that:Outer4::Template::S.#stringParser(that:{'@stringU
' proposed is
})).#plusplus(that:that)
read method
Void reportUnexpected(Outer4::Template::N that) error Outer4::Template::S.#stringParser(that:{'@stringU
'Unexpected failure like IndexOutOfBound: max is
}).#plusplus(that:this.size()).#plusplus(that:Outer4::Template::S.#stringParser(that:{'@stringU
' proposed is
})).#plusplus(that:that)
read method
Outer1::Kind::elemRead() #apply(Outer4::Template::N that) (
  Void unused=(
    Outer4::Template::N::#rightequal(that ) cond=that.#rightequal(that:this.size())
    (
      Void unused0=cond.#checkTrue()
      catch exception unused1 (
        on Void void
        )
      this.reportError(that:that)
      )
    )
  Outer0.auxGet(that:that, cell:this.head())
  )
type method
Outer1::Kind::elemRead() auxGet(Outer4::Template::N that, read Outer0::Cell cell) (
  Void unused=(
    Void unused0=(
      Void unused2=(
        read Outer0::CellNext cell2=(
          Void unused3=return cell
          catch return casted1 (
            on read Outer0::CellNext casted1

            on read Any exception void
            )
          error {'@stringU
          'CastT-Should be unreachable code
          }
          )
        catch exception unused4 (
          on Void void
          )
        (
          Void unused5=(
            Outer4::Template::N::#equalequal(that ) cond=that.#equalequal(that:Outer4::Template::N.#numberPars
er(that:{'@stringU
            '0
            }))
            (
              Void unused6=cond.#checkTrue()
              catch exception unused7 (
                on Void exception void
                )
              void
              )
            )
          void
          )
        )
      read Outer0::CellNext cell1=(
        Void unused8=return cell
        catch return casted0 (
          on read Outer0::CellNext casted0

          on read Any exception void
          )
        error {'@stringU
        'CastT-Should be unreachable code
        }
        )
      catch exception unused9 (
        on Void (
          read Outer0::CellNext cell0=(
            Void unused10=return cell
            catch return casted (
              on read Outer0::CellNext casted

              on read Any exception void
              )
            error {'@stringU
            'CastT-Should be unreachable code
            }
            )
          catch exception unused11 (
            on Void void
            )
          (
            Void unused12=return Outer0.auxGet(that:that.#less(that:Outer4::Template::N.#numberParser(that:{'@
stringU
            '1
            })), cell:cell0.next())
            void
            )
          )
        )
      (
        Void unused13=return cell1.val()
        void
        )
      )
    Void unused1=error Outer4::Template::S.#stringParser(that:{'@stringU
    'Unreachable
    })
    void
    )
  catch return result (
    on Outer1::Kind::elemRead() result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
mut method
Void #apply(Outer4::Template::N that, Outer1::Kind::elem() val) (
  Void unused=(
    Outer4::Template::N::#rightequal(that ) cond=that.#rightequal(that:this.size())
    (
      Void unused0=cond.#checkTrue()
      catch exception unused1 (
        on Void void
        )
      this.reportError(that:that)
      )
    )
  this.head(that:Outer0.auxSet(that:that, val:val, cell:this.#head()))
  )
type method
mut Outer0::CellNext auxSet(Outer4::Template::N that, Outer1::Kind::elem() val, mut Outer0::Cell cell) (
  Void unused=(
    Void unused0=(
      Void unused2=(
        mut Outer0::CellNext cell2=(
          Void unused3=return cell
          catch return casted1 (
            on mut Outer0::CellNext casted1

            on mut Any exception void
            )
          error {'@stringU
          'CastT-Should be unreachable code
          }
          )
        catch exception unused4 (
          on Void void
          )
        (
          Void unused5=(
            Outer4::Template::N::#equalequal(that ) cond=that.#equalequal(that:Outer4::Template::N.#numberPars
er(that:{'@stringU
            '0
            }))
            (
              Void unused6=cond.#checkTrue()
              catch exception unused7 (
                on Void exception void
                )
              void
              )
            )
          void
          )
        )
      mut Outer0::CellNext cell1=(
        Void unused8=return cell
        catch return casted0 (
          on mut Outer0::CellNext casted0

          on mut Any exception void
          )
        error {'@stringU
        'CastT-Should be unreachable code
        }
        )
      catch exception unused9 (
        on Void (
          mut Outer0::CellNext cell0=(
            Void unused10=return cell
            catch return casted (
              on mut Outer0::CellNext casted

              on mut Any exception void
              )
            error {'@stringU
            'CastT-Should be unreachable code
            }
            )
          catch exception unused11 (
            on Void void
            )
          (
            Void unused12=return Outer0::CellNext.#apply(val:cell0.val(), next:Outer0.auxSet(that:that.#less(t
hat:Outer4::Template::N.#numberParser(that:{'@stringU
            '1
            })), val:val, cell:cell0.#next()))
            void
            )
          )
        )
      (
        Void unused13=return Outer0::CellNext.#apply(val:val, next:cell1.#next())
        void
        )
      )
    Void unused1=error Outer4::Template::S.#stringParser(that:{'@stringU
    'Unreachable
    })
    void
    )
  catch return result (
    on mut Outer0::CellNext result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method
mut Outer0 #apply() Outer0.#apply(head:Outer0::CellEnd.#apply(), size:Outer4::Template::N.#numberParser(that:{
'@stringU
'0
}))
mut method
mut Outer0 add(Outer1::Kind::elem() that) (
  Void unused=this.size(that:this.size().#plus(that:Outer4::Template::N.#numberParser(that:{'@stringU
  '1
  })))
  Void unused0=this.head(that:Outer0.auxAdd(that:that, cell:this.#head()))
  this
  )
type method
mut Outer0::Cell auxAdd(Outer1::Kind::elem() that, mut Outer0::Cell cell) (
  Void unused=(
    Void unused0=(
      mut Outer0::CellEnd cell1=(
        Void unused2=return cell
        catch return casted0 (
          on mut Outer0::CellEnd casted0

          on mut Any exception void
          )
        error {'@stringU
        'CastT-Should be unreachable code
        }
        )
      catch exception unused3 (
        on Void (
          mut Outer0::CellNext cell0=(
            Void unused4=return cell
            catch return casted (
              on mut Outer0::CellNext casted

              on mut Any exception void
              )
            error {'@stringU
            'CastT-Should be unreachable code
            }
            )
          catch exception unused5 (
            on Void void
            )
          (
            Void unused6=return Outer0::CellNext.#apply(val:cell0.val(), next:Outer0.auxAdd(that:that, cell:ce
ll0.#next()))
            void
            )
          )
        )
      (
        Void unused7=return Outer0::CellNext.#apply(val:that, next:cell1)
        void
        )
      )
    Void unused1=error Outer4::Template::S.#stringParser(that:{'@stringU
    'Unreachable
    })
    void
    )
  catch return result (
    on mut Outer0::Cell result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method
mut Outer0 #begin() Outer0.#apply()
mut method
mut Outer0 #end() this
mut method
mut Outer0 #add(Outer1::Kind::elem() that) this.add(that:that)
OptMax:{
type method
Outer0 #new(Outer0::TOpt^that'@consistent
)
mut method '@consistent
Outer0::TOpt #that()
read method '@consistent
Outer0::TOpt that()
TOpt:{interface
method
Outer6::Template::N get() }
TEmpty:{<:Outer1::TOpt
type method
Outer0 #apply()
method get() error Outer6::Template::S.#stringParser(that:{'@stringU
'Value not present
})}
TOf:{<:Outer1::TOpt
type method
Outer0 #apply(Outer6::Template::N^that'@consistent
)
mut method '@consistent
Outer6::Template::N #that()
read method '@consistent
Outer6::Template::N that()
method get() this.that()}
method
Outer5::Template::N #bang() this.that().get()
method
Outer5::Template::Bool isPresent() (
  Void unused=(
    Outer0::#bang() aux=this.#bang()
    Void unused0=return Outer5::Template::Bool.true()
    catch error unused1 (
      on Any return Outer5::Template::Bool.false()
      )
    void
    )
  catch return result (
    on Outer5::Template::Bool result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method
Outer0 #apply() Outer0.#new(that:Outer0::TEmpty.#apply())
type method
Outer0 #apply(Outer5::Template::N that) Outer0.#new(that:Outer0::TOf.#apply(that:that))}
OptFill:{
type method
Outer0 #new(Outer0::TOpt^that'@consistent
)
mut method '@consistent
Outer0::TOpt #that()
read method '@consistent
Outer0::TOpt that()
TOpt:{interface
method
Outer3::Elem get() }
TEmpty:{<:Outer1::TOpt
type method
Outer0 #apply()
method get() error Outer6::Template::S.#stringParser(that:{'@stringU
'Value not present
})}
TOf:{<:Outer1::TOpt
type method
Outer0 #apply(Outer3::Elem^that'@consistent
)
mut method '@consistent
Outer3::Elem #that()
read method '@consistent
Outer3::Elem that()
method get() this.that()}
method
Outer2::Elem #bang() this.that().get()
method
Outer5::Template::Bool isPresent() (
  Void unused=(
    Outer0::#bang() aux=this.#bang()
    Void unused0=return Outer5::Template::Bool.true()
    catch error unused1 (
      on Any return Outer5::Template::Bool.false()
      )
    void
    )
  catch return result (
    on Outer5::Template::Bool result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method
Outer0 #apply() Outer0.#new(that:Outer0::TEmpty.#apply())
type method
Outer0 #apply(Outer2::Elem that) Outer0.#new(that:Outer0::TOf.#apply(that:that))}
read method
lent Outer0::Iterator vals(Outer4::Template::N min, Outer4::Template::N ok, Outer4::Template::N max, Outer1::K
ind::elem() fill) Outer0::Iterator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser(that
:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals(Outer4::Template::N min, Outer4::Template::N ok, Outer4::Template::N max) Outer0::I
terator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
read method
lent Outer0::Iterator vals(Outer4::Template::N min, Outer4::Template::N ok, Outer1::Kind::elem() fill) Outer0:
:Iterator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals(Outer4::Template::N min, Outer4::Template::N ok) Outer0::Iterator.#apply(that:this,
 min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
read method
lent Outer0::Iterator vals(Outer4::Template::N min, Outer4::Template::N max, Outer1::Kind::elem() fill) Outer0
::Iterator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals(Outer4::Template::N min, Outer4::Template::N max) Outer0::Iterator.#apply(that:this
, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
read method
lent Outer0::Iterator vals(Outer4::Template::N min, Outer1::Kind::elem() fill) Outer0::Iterator.#apply(that:th
is, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals(Outer4::Template::N min) Outer0::Iterator.#apply(that:this, min:min.#less(that:Oute
r4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
read method
lent Outer0::Iterator vals(Outer4::Template::N ok, Outer4::Template::N max, Outer1::Kind::elem() fill) Outer0:
:Iterator.#apply(that:this, min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals(Outer4::Template::N ok, Outer4::Template::N max) Outer0::Iterator.#apply(that:this,
 min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
read method
lent Outer0::Iterator vals(Outer4::Template::N ok, Outer1::Kind::elem() fill) Outer0::Iterator.#apply(that:thi
s, min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals(Outer4::Template::N ok) Outer0::Iterator.#apply(that:this, min:Outer4::Template::N.
#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
read method
lent Outer0::Iterator vals(Outer4::Template::N max, Outer1::Kind::elem() fill) Outer0::Iterator.#apply(that:th
is, min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals(Outer4::Template::N max) Outer0::Iterator.#apply(that:this, min:Outer4::Template::N
.#numberParser(that:{'@stringU
'-1
}), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
read method
lent Outer0::Iterator vals(Outer1::Kind::elem() fill) Outer0::Iterator.#apply(that:this, min:Outer4::Template:
:N.#numberParser(that:{'@stringU
'-1
}), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals() Outer0::Iterator.#apply(that:this, min:Outer4::Template::N.#numberParser(that:{'@
stringU
'-1
}), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
Iterator:{
type method
lent Outer0 #apply(read Outer1^that'@consistent
, Outer5::Template::N^min'@consistent
, Outer5::Template::N^ok'@consistent
, Outer1::OptMax^max'@consistent
, Outer1::OptFill^fill'@consistent
)
mut method '@consistent
read Outer1 #that()
read method '@consistent
read Outer1 that()
mut method '@consistent
Void min(Outer5::Template::N that)
mut method '@consistent
Outer5::Template::N #min()
read method '@consistent
Outer5::Template::N min()
mut method '@consistent
Outer5::Template::N #ok()
read method '@consistent
Outer5::Template::N ok()
mut method '@consistent
Outer1::OptMax #max()
read method '@consistent
Outer1::OptMax max()
mut method '@consistent
Outer1::OptFill #fill()
read method '@consistent
Outer1::OptFill fill()
mut method
Void #next() exception Void (
  Void unused=(
    Void unused0=this.min(that:this.min().#plus(that:Outer5::Template::N.#numberParser(that:{'@stringU
    '1
    })))
    Void unused1=(
      Outer0::fill() ::isPresent() cond=this.fill().isPresent()
      (
        Void unused5=cond.#checkTrue()
        catch exception unused6 (
          on Void void
          )
        (
          Void unused7=(
            Outer0::max() ::isPresent() ::#bang() cond0=this.max().isPresent().#bang()
            (
              Void unused9=cond0.#checkTrue()
              catch exception unused10 (
                on Void void
                )
              return void
              )
            )
          Void unused8=(
            Outer0::min() ::#rightequal(that ) cond1=this.min().#rightequal(that:this.max().#bang())
            (
              Void unused11=cond1.#checkTrue()
              catch exception unused12 (
                on Void void
                )
              exception void
              )
            )
          return void
          )
        )
      )
    Void unused2=(
      Outer0::max() ::isPresent() cond2=this.max().isPresent()
      (
        Void unused13=cond2.#checkTrue()
        catch exception unused14 (
          on Void void
          )
        (
          Outer0::min() ::#rightequal(that ) cond3=this.min().#rightequal(that:this.max().#bang())
          (
            Void unused15=cond3.#checkTrue()
            catch exception unused16 (
              on Void void
              )
            exception void
            )
          )
        )
      )
    Void unused3=(
      Outer0::min() ::#rightequal(that ) cond4=this.min().#rightequal(that:this.that().size())
      (
        Void unused17=cond4.#checkTrue()
        catch exception unused18 (
          on Void void
          )
        exception void
        )
      )
    Void unused4=return void
    void
    )
  catch return result (
    on Void result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
read method
Void #checkEnd() (
  Outer0::min() ::#left(that ) cond=this.min().#left(that:this.ok())
  (
    Void unused=cond.#checkTrue()
    catch exception unused0 (
      on Void void
      )
    error Outer5::Template::S.#stringParser(that:{'@stringU
    'Unexpected termination of iteration, reached
    }).#plusplus(that:this.min()).#plusplus(that:Outer5::Template::S.#stringParser(that:{'@stringU
    ' instead of
    })).#plusplus(that:this.ok())
    )
  )
read method
Outer2::Kind::elem() #inner() (
  Void unused=(
    Void unused0=(
      Outer0::fill() ::isPresent() ::#bang() cond=this.fill().isPresent().#bang()
      (
        Void unused3=cond.#checkTrue()
        catch exception unused4 (
          on Void void
          )
        return this.that().#apply(that:this.min())
        )
      )
    Void unused1=(
      Outer0::min() ::#left(that ) cond0=this.min().#left(that:this.that().size())
      (
        Void unused5=cond0.#checkTrue()
        catch exception unused6 (
          on Void void
          )
        return this.that().#apply(that:this.min())
        )
      )
    Void unused2=return this.fill().#bang()
    void
    )
  catch return result (
    on Outer2::Kind::elem() result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
read method
Void #close() void}
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min, Outer4::Template::N ok, Outer4::Template::N max, Outer1:
:Kind::elem() fill) Outer0::VarIterator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser
(that:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min, Outer4::Template::N ok, Outer4::Template::N max) Outer0:
:VarIterator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min, Outer4::Template::N ok, Outer1::Kind::elem() fill) Outer
0::VarIterator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min, Outer4::Template::N ok) Outer0::VarIterator.#apply(that:
this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min, Outer4::Template::N max, Outer1::Kind::elem() fill) Oute
r0::VarIterator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min, Outer4::Template::N max) Outer0::VarIterator.#apply(that
:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min, Outer1::Kind::elem() fill) Outer0::VarIterator.#apply(th
at:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min) Outer0::VarIterator.#apply(that:this, min:min.#less(that
:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
mut method
mut Outer0::VarIterator vars(Outer4::Template::N ok, Outer4::Template::N max, Outer1::Kind::elem() fill) Outer
0::VarIterator.#apply(that:this, min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars(Outer4::Template::N ok, Outer4::Template::N max) Outer0::VarIterator.#apply(that:
this, min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
mut method
mut Outer0::VarIterator vars(Outer4::Template::N ok, Outer1::Kind::elem() fill) Outer0::VarIterator.#apply(tha
t:this, min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars(Outer4::Template::N ok) Outer0::VarIterator.#apply(that:this, min:Outer4::Templat
e::N.#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
mut method
mut Outer0::VarIterator vars(Outer4::Template::N max, Outer1::Kind::elem() fill) Outer0::VarIterator.#apply(th
at:this, min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars(Outer4::Template::N max) Outer0::VarIterator.#apply(that:this, min:Outer4::Templa
te::N.#numberParser(that:{'@stringU
'-1
}), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
mut method
mut Outer0::VarIterator vars(Outer1::Kind::elem() fill) Outer0::VarIterator.#apply(that:this, min:Outer4::Temp
late::N.#numberParser(that:{'@stringU
'-1
}), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars() Outer0::VarIterator.#apply(that:this, min:Outer4::Template::N.#numberParser(tha
t:{'@stringU
'-1
}), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
VarIterator:{
type method
mut Outer0 #apply(mut Outer1^that'@consistent
, Outer5::Template::N^min'@consistent
, Outer5::Template::N^ok'@consistent
, Outer1::OptMax^max'@consistent
, Outer1::OptFill^fill'@consistent
)
mut method '@consistent
mut Outer1 #that()
read method '@consistent
read Outer1 that()
mut method '@consistent
Void min(Outer5::Template::N that)
mut method '@consistent
Outer5::Template::N #min()
read method '@consistent
Outer5::Template::N min()
mut method '@consistent
Outer5::Template::N #ok()
read method '@consistent
Outer5::Template::N ok()
mut method '@consistent
Outer1::OptMax #max()
read method '@consistent
Outer1::OptMax max()
mut method '@consistent
Outer1::OptFill #fill()
read method '@consistent
Outer1::OptFill fill()
mut method
Void inner(Outer2::Kind::elem() that) this.#that().#apply(that:this.min(), val:that)
mut method
Void #next() exception Void (
  Void unused=(
    Void unused0=this.min(that:this.min().#plus(that:Outer5::Template::N.#numberParser(that:{'@stringU
    '1
    })))
    Void unused1=(
      Outer0::fill() ::isPresent() cond=this.fill().isPresent()
      (
        Void unused5=cond.#checkTrue()
        catch exception unused6 (
          on Void void
          )
        (
          Void unused7=(
            Outer0::max() ::isPresent() ::#bang() cond0=this.max().isPresent().#bang()
            (
              Void unused9=cond0.#checkTrue()
              catch exception unused10 (
                on Void void
                )
              return void
              )
            )
          Void unused8=(
            Outer0::min() ::#rightequal(that ) cond1=this.min().#rightequal(that:this.max().#bang())
            (
              Void unused11=cond1.#checkTrue()
              catch exception unused12 (
                on Void void
                )
              exception void
              )
            )
          return void
          )
        )
      )
    Void unused2=(
      Outer0::max() ::isPresent() cond2=this.max().isPresent()
      (
        Void unused13=cond2.#checkTrue()
        catch exception unused14 (
          on Void void
          )
        (
          Outer0::min() ::#rightequal(that ) cond3=this.min().#rightequal(that:this.max().#bang())
          (
            Void unused15=cond3.#checkTrue()
            catch exception unused16 (
              on Void void
              )
            exception void
            )
          )
        )
      )
    Void unused3=(
      Outer0::min() ::#rightequal(that ) cond4=this.min().#rightequal(that:this.that().size())
      (
        Void unused17=cond4.#checkTrue()
        catch exception unused18 (
          on Void void
          )
        exception void
        )
      )
    Void unused4=return void
    void
    )
  catch return result (
    on Void result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
read method
Void #checkEnd() (
  Outer0::min() ::#left(that ) cond=this.min().#left(that:this.ok())
  (
    Void unused=cond.#checkTrue()
    catch exception unused0 (
      on Void void
      )
    error Outer5::Template::S.#stringParser(that:{'@stringU
    'Unexpected termination of iteration, reached
    }).#plusplus(that:this.min()).#plusplus(that:Outer5::Template::S.#stringParser(that:{'@stringU
    ' instead of
    })).#plusplus(that:this.ok())
    )
  )
read method
Outer2::Kind::elem() #inner() (
  Void unused=(
    Void unused0=(
      Outer0::fill() ::isPresent() ::#bang() cond=this.fill().isPresent().#bang()
      (
        Void unused3=cond.#checkTrue()
        catch exception unused4 (
          on Void void
          )
        return this.that().#apply(that:this.min())
        )
      )
    Void unused1=(
      Outer0::min() ::#left(that ) cond0=this.min().#left(that:this.that().size())
      (
        Void unused5=cond0.#checkTrue()
        catch exception unused6 (
          on Void void
          )
        return this.that().#apply(that:this.min())
        )
      )
    Void unused2=return this.fill().#bang()
    void
    )
  catch return result (
    on Outer2::Kind::elem() result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
read method
Void #close() void}}})]], path=Ast.Path(rowData=[Any]), ms=#begin, p=[{
type method
Library vector(type Any that) Any.#begin().#add(that:Any.#stringParser(that:{'@stringU
'Elem
}), into:Any.#apply(that:that)).#add(that:Any.#stringParser(that:{'@stringU
'VectorStruct
}), into:Any.#stringParser(that:{'@stringU
'Outer0
})).#end().#left(that:{
Elem:{interface }
Kind:'@private
{
type method 'read/lent/mut/type/capsule/
Outer1::Elem elem() error Outer4::Template::S.#stringParser(that:{'@stringU
'InternalUseOnly
})
type method 'read/lent/mut/type/capsule/
Outer1::Elem elemRead() error Outer4::Template::S.#stringParser(that:{'@stringU
'InternalUseOnly
})}
VectorStruct:{
type method ' size should be computed?
mut Outer0 #apply(mut Outer0::Cell^head'@consistent
, Outer4::Template::N^size'@consistent
)
mut method '@consistent
Void head(mut Outer0::Cell that)
mut method '@consistent
mut Outer0::Cell #head()
read method '@consistent
read Outer0::Cell head()
mut method '@consistent
Void size(Outer4::Template::N that)
mut method '@consistent
Outer4::Template::N #size()
read method '@consistent
Outer4::Template::N size()
Cell:{interface }
CellNext:{<:Outer1::Cell
type method
mut Outer0 #apply(Outer2::Kind::elem() ^val'@consistent
, mut Outer1::Cell^next'@consistent
)
mut method '@consistent
Outer2::Kind::elem() #val()
mut method '@consistent
mut Outer1::Cell #next()
read method '@consistent
read Outer1::Cell next()
read method
Outer2::Kind::elemRead() val() }
CellEnd:{<:Outer1::Cell
type method
mut Outer0 #apply() }
read method
Void reportError(Outer4::Template::N that) error Outer4::Template::S.#stringParser(that:{'@stringU
'IndexOutOfBound: max is
}).#plusplus(that:this.size()).#plusplus(that:Outer4::Template::S.#stringParser(that:{'@stringU
' proposed is
})).#plusplus(that:that)
read method
Void reportUnexpected(Outer4::Template::N that) error Outer4::Template::S.#stringParser(that:{'@stringU
'Unexpected failure like IndexOutOfBound: max is
}).#plusplus(that:this.size()).#plusplus(that:Outer4::Template::S.#stringParser(that:{'@stringU
' proposed is
})).#plusplus(that:that)
read method
Outer1::Kind::elemRead() #apply(Outer4::Template::N that) (
  Void unused=(
    Outer4::Template::N::#rightequal(that ) cond=that.#rightequal(that:this.size())
    (
      Void unused0=cond.#checkTrue()
      catch exception unused1 (
        on Void void
        )
      this.reportError(that:that)
      )
    )
  Outer0.auxGet(that:that, cell:this.head())
  )
type method
Outer1::Kind::elemRead() auxGet(Outer4::Template::N that, read Outer0::Cell cell) (
  Void unused=(
    Void unused0=(
      Void unused2=(
        read Outer0::CellNext cell2=(
          Void unused3=return cell
          catch return casted1 (
            on read Outer0::CellNext casted1

            on read Any exception void
            )
          error {'@stringU
          'CastT-Should be unreachable code
          }
          )
        catch exception unused4 (
          on Void void
          )
        (
          Void unused5=(
            Outer4::Template::N::#equalequal(that ) cond=that.#equalequal(that:Outer4::Template::N.#numberPars
er(that:{'@stringU
            '0
            }))
            (
              Void unused6=cond.#checkTrue()
              catch exception unused7 (
                on Void exception void
                )
              void
              )
            )
          void
          )
        )
      read Outer0::CellNext cell1=(
        Void unused8=return cell
        catch return casted0 (
          on read Outer0::CellNext casted0

          on read Any exception void
          )
        error {'@stringU
        'CastT-Should be unreachable code
        }
        )
      catch exception unused9 (
        on Void (
          read Outer0::CellNext cell0=(
            Void unused10=return cell
            catch return casted (
              on read Outer0::CellNext casted

              on read Any exception void
              )
            error {'@stringU
            'CastT-Should be unreachable code
            }
            )
          catch exception unused11 (
            on Void void
            )
          (
            Void unused12=return Outer0.auxGet(that:that.#less(that:Outer4::Template::N.#numberParser(that:{'@
stringU
            '1
            })), cell:cell0.next())
            void
            )
          )
        )
      (
        Void unused13=return cell1.val()
        void
        )
      )
    Void unused1=error Outer4::Template::S.#stringParser(that:{'@stringU
    'Unreachable
    })
    void
    )
  catch return result (
    on Outer1::Kind::elemRead() result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
mut method
Void #apply(Outer4::Template::N that, Outer1::Kind::elem() val) (
  Void unused=(
    Outer4::Template::N::#rightequal(that ) cond=that.#rightequal(that:this.size())
    (
      Void unused0=cond.#checkTrue()
      catch exception unused1 (
        on Void void
        )
      this.reportError(that:that)
      )
    )
  this.head(that:Outer0.auxSet(that:that, val:val, cell:this.#head()))
  )
type method
mut Outer0::CellNext auxSet(Outer4::Template::N that, Outer1::Kind::elem() val, mut Outer0::Cell cell) (
  Void unused=(
    Void unused0=(
      Void unused2=(
        mut Outer0::CellNext cell2=(
          Void unused3=return cell
          catch return casted1 (
            on mut Outer0::CellNext casted1

            on mut Any exception void
            )
          error {'@stringU
          'CastT-Should be unreachable code
          }
          )
        catch exception unused4 (
          on Void void
          )
        (
          Void unused5=(
            Outer4::Template::N::#equalequal(that ) cond=that.#equalequal(that:Outer4::Template::N.#numberPars
er(that:{'@stringU
            '0
            }))
            (
              Void unused6=cond.#checkTrue()
              catch exception unused7 (
                on Void exception void
                )
              void
              )
            )
          void
          )
        )
      mut Outer0::CellNext cell1=(
        Void unused8=return cell
        catch return casted0 (
          on mut Outer0::CellNext casted0

          on mut Any exception void
          )
        error {'@stringU
        'CastT-Should be unreachable code
        }
        )
      catch exception unused9 (
        on Void (
          mut Outer0::CellNext cell0=(
            Void unused10=return cell
            catch return casted (
              on mut Outer0::CellNext casted

              on mut Any exception void
              )
            error {'@stringU
            'CastT-Should be unreachable code
            }
            )
          catch exception unused11 (
            on Void void
            )
          (
            Void unused12=return Outer0::CellNext.#apply(val:cell0.val(), next:Outer0.auxSet(that:that.#less(t
hat:Outer4::Template::N.#numberParser(that:{'@stringU
            '1
            })), val:val, cell:cell0.#next()))
            void
            )
          )
        )
      (
        Void unused13=return Outer0::CellNext.#apply(val:val, next:cell1.#next())
        void
        )
      )
    Void unused1=error Outer4::Template::S.#stringParser(that:{'@stringU
    'Unreachable
    })
    void
    )
  catch return result (
    on mut Outer0::CellNext result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method
mut Outer0 #apply() Outer0.#apply(head:Outer0::CellEnd.#apply(), size:Outer4::Template::N.#numberParser(that:{
'@stringU
'0
}))
mut method
mut Outer0 add(Outer1::Kind::elem() that) (
  Void unused=this.size(that:this.size().#plus(that:Outer4::Template::N.#numberParser(that:{'@stringU
  '1
  })))
  Void unused0=this.head(that:Outer0.auxAdd(that:that, cell:this.#head()))
  this
  )
type method
mut Outer0::Cell auxAdd(Outer1::Kind::elem() that, mut Outer0::Cell cell) (
  Void unused=(
    Void unused0=(
      mut Outer0::CellEnd cell1=(
        Void unused2=return cell
        catch return casted0 (
          on mut Outer0::CellEnd casted0

          on mut Any exception void
          )
        error {'@stringU
        'CastT-Should be unreachable code
        }
        )
      catch exception unused3 (
        on Void (
          mut Outer0::CellNext cell0=(
            Void unused4=return cell
            catch return casted (
              on mut Outer0::CellNext casted

              on mut Any exception void
              )
            error {'@stringU
            'CastT-Should be unreachable code
            }
            )
          catch exception unused5 (
            on Void void
            )
          (
            Void unused6=return Outer0::CellNext.#apply(val:cell0.val(), next:Outer0.auxAdd(that:that, cell:ce
ll0.#next()))
            void
            )
          )
        )
      (
        Void unused7=return Outer0::CellNext.#apply(val:that, next:cell1)
        void
        )
      )
    Void unused1=error Outer4::Template::S.#stringParser(that:{'@stringU
    'Unreachable
    })
    void
    )
  catch return result (
    on mut Outer0::Cell result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method
mut Outer0 #begin() Outer0.#apply()
mut method
mut Outer0 #end() this
mut method
mut Outer0 #add(Outer1::Kind::elem() that) this.add(that:that)
OptMax:{
type method
Outer0 #new(Outer0::TOpt^that'@consistent
)
mut method '@consistent
Outer0::TOpt #that()
read method '@consistent
Outer0::TOpt that()
TOpt:{interface
method
Outer6::Template::N get() }
TEmpty:{<:Outer1::TOpt
type method
Outer0 #apply()
method get() error Outer6::Template::S.#stringParser(that:{'@stringU
'Value not present
})}
TOf:{<:Outer1::TOpt
type method
Outer0 #apply(Outer6::Template::N^that'@consistent
)
mut method '@consistent
Outer6::Template::N #that()
read method '@consistent
Outer6::Template::N that()
method get() this.that()}
method
Outer5::Template::N #bang() this.that().get()
method
Outer5::Template::Bool isPresent() (
  Void unused=(
    Outer0::#bang() aux=this.#bang()
    Void unused0=return Outer5::Template::Bool.true()
    catch error unused1 (
      on Any return Outer5::Template::Bool.false()
      )
    void
    )
  catch return result (
    on Outer5::Template::Bool result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method
Outer0 #apply() Outer0.#new(that:Outer0::TEmpty.#apply())
type method
Outer0 #apply(Outer5::Template::N that) Outer0.#new(that:Outer0::TOf.#apply(that:that))}
OptFill:{
type method
Outer0 #new(Outer0::TOpt^that'@consistent
)
mut method '@consistent
Outer0::TOpt #that()
read method '@consistent
Outer0::TOpt that()
TOpt:{interface
method
Outer3::Elem get() }
TEmpty:{<:Outer1::TOpt
type method
Outer0 #apply()
method get() error Outer6::Template::S.#stringParser(that:{'@stringU
'Value not present
})}
TOf:{<:Outer1::TOpt
type method
Outer0 #apply(Outer3::Elem^that'@consistent
)
mut method '@consistent
Outer3::Elem #that()
read method '@consistent
Outer3::Elem that()
method get() this.that()}
method
Outer2::Elem #bang() this.that().get()
method
Outer5::Template::Bool isPresent() (
  Void unused=(
    Outer0::#bang() aux=this.#bang()
    Void unused0=return Outer5::Template::Bool.true()
    catch error unused1 (
      on Any return Outer5::Template::Bool.false()
      )
    void
    )
  catch return result (
    on Outer5::Template::Bool result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method
Outer0 #apply() Outer0.#new(that:Outer0::TEmpty.#apply())
type method
Outer0 #apply(Outer2::Elem that) Outer0.#new(that:Outer0::TOf.#apply(that:that))}
read method
lent Outer0::Iterator vals(Outer4::Template::N min, Outer4::Template::N ok, Outer4::Template::N max, Outer1::K
ind::elem() fill) Outer0::Iterator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser(that
:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals(Outer4::Template::N min, Outer4::Template::N ok, Outer4::Template::N max) Outer0::I
terator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
read method
lent Outer0::Iterator vals(Outer4::Template::N min, Outer4::Template::N ok, Outer1::Kind::elem() fill) Outer0:
:Iterator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals(Outer4::Template::N min, Outer4::Template::N ok) Outer0::Iterator.#apply(that:this,
 min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
read method
lent Outer0::Iterator vals(Outer4::Template::N min, Outer4::Template::N max, Outer1::Kind::elem() fill) Outer0
::Iterator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals(Outer4::Template::N min, Outer4::Template::N max) Outer0::Iterator.#apply(that:this
, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
read method
lent Outer0::Iterator vals(Outer4::Template::N min, Outer1::Kind::elem() fill) Outer0::Iterator.#apply(that:th
is, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals(Outer4::Template::N min) Outer0::Iterator.#apply(that:this, min:min.#less(that:Oute
r4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
read method
lent Outer0::Iterator vals(Outer4::Template::N ok, Outer4::Template::N max, Outer1::Kind::elem() fill) Outer0:
:Iterator.#apply(that:this, min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals(Outer4::Template::N ok, Outer4::Template::N max) Outer0::Iterator.#apply(that:this,
 min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
read method
lent Outer0::Iterator vals(Outer4::Template::N ok, Outer1::Kind::elem() fill) Outer0::Iterator.#apply(that:thi
s, min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals(Outer4::Template::N ok) Outer0::Iterator.#apply(that:this, min:Outer4::Template::N.
#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
read method
lent Outer0::Iterator vals(Outer4::Template::N max, Outer1::Kind::elem() fill) Outer0::Iterator.#apply(that:th
is, min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals(Outer4::Template::N max) Outer0::Iterator.#apply(that:this, min:Outer4::Template::N
.#numberParser(that:{'@stringU
'-1
}), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
read method
lent Outer0::Iterator vals(Outer1::Kind::elem() fill) Outer0::Iterator.#apply(that:this, min:Outer4::Template:
:N.#numberParser(that:{'@stringU
'-1
}), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
read method
lent Outer0::Iterator vals() Outer0::Iterator.#apply(that:this, min:Outer4::Template::N.#numberParser(that:{'@
stringU
'-1
}), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
Iterator:{
type method
lent Outer0 #apply(read Outer1^that'@consistent
, Outer5::Template::N^min'@consistent
, Outer5::Template::N^ok'@consistent
, Outer1::OptMax^max'@consistent
, Outer1::OptFill^fill'@consistent
)
mut method '@consistent
read Outer1 #that()
read method '@consistent
read Outer1 that()
mut method '@consistent
Void min(Outer5::Template::N that)
mut method '@consistent
Outer5::Template::N #min()
read method '@consistent
Outer5::Template::N min()
mut method '@consistent
Outer5::Template::N #ok()
read method '@consistent
Outer5::Template::N ok()
mut method '@consistent
Outer1::OptMax #max()
read method '@consistent
Outer1::OptMax max()
mut method '@consistent
Outer1::OptFill #fill()
read method '@consistent
Outer1::OptFill fill()
mut method
Void #next() exception Void (
  Void unused=(
    Void unused0=this.min(that:this.min().#plus(that:Outer5::Template::N.#numberParser(that:{'@stringU
    '1
    })))
    Void unused1=(
      Outer0::fill() ::isPresent() cond=this.fill().isPresent()
      (
        Void unused5=cond.#checkTrue()
        catch exception unused6 (
          on Void void
          )
        (
          Void unused7=(
            Outer0::max() ::isPresent() ::#bang() cond0=this.max().isPresent().#bang()
            (
              Void unused9=cond0.#checkTrue()
              catch exception unused10 (
                on Void void
                )
              return void
              )
            )
          Void unused8=(
            Outer0::min() ::#rightequal(that ) cond1=this.min().#rightequal(that:this.max().#bang())
            (
              Void unused11=cond1.#checkTrue()
              catch exception unused12 (
                on Void void
                )
              exception void
              )
            )
          return void
          )
        )
      )
    Void unused2=(
      Outer0::max() ::isPresent() cond2=this.max().isPresent()
      (
        Void unused13=cond2.#checkTrue()
        catch exception unused14 (
          on Void void
          )
        (
          Outer0::min() ::#rightequal(that ) cond3=this.min().#rightequal(that:this.max().#bang())
          (
            Void unused15=cond3.#checkTrue()
            catch exception unused16 (
              on Void void
              )
            exception void
            )
          )
        )
      )
    Void unused3=(
      Outer0::min() ::#rightequal(that ) cond4=this.min().#rightequal(that:this.that().size())
      (
        Void unused17=cond4.#checkTrue()
        catch exception unused18 (
          on Void void
          )
        exception void
        )
      )
    Void unused4=return void
    void
    )
  catch return result (
    on Void result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
read method
Void #checkEnd() (
  Outer0::min() ::#left(that ) cond=this.min().#left(that:this.ok())
  (
    Void unused=cond.#checkTrue()
    catch exception unused0 (
      on Void void
      )
    error Outer5::Template::S.#stringParser(that:{'@stringU
    'Unexpected termination of iteration, reached
    }).#plusplus(that:this.min()).#plusplus(that:Outer5::Template::S.#stringParser(that:{'@stringU
    ' instead of
    })).#plusplus(that:this.ok())
    )
  )
read method
Outer2::Kind::elem() #inner() (
  Void unused=(
    Void unused0=(
      Outer0::fill() ::isPresent() ::#bang() cond=this.fill().isPresent().#bang()
      (
        Void unused3=cond.#checkTrue()
        catch exception unused4 (
          on Void void
          )
        return this.that().#apply(that:this.min())
        )
      )
    Void unused1=(
      Outer0::min() ::#left(that ) cond0=this.min().#left(that:this.that().size())
      (
        Void unused5=cond0.#checkTrue()
        catch exception unused6 (
          on Void void
          )
        return this.that().#apply(that:this.min())
        )
      )
    Void unused2=return this.fill().#bang()
    void
    )
  catch return result (
    on Outer2::Kind::elem() result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
read method
Void #close() void}
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min, Outer4::Template::N ok, Outer4::Template::N max, Outer1:
:Kind::elem() fill) Outer0::VarIterator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser
(that:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min, Outer4::Template::N ok, Outer4::Template::N max) Outer0:
:VarIterator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min, Outer4::Template::N ok, Outer1::Kind::elem() fill) Outer
0::VarIterator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min, Outer4::Template::N ok) Outer0::VarIterator.#apply(that:
this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min, Outer4::Template::N max, Outer1::Kind::elem() fill) Oute
r0::VarIterator.#apply(that:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min, Outer4::Template::N max) Outer0::VarIterator.#apply(that
:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min, Outer1::Kind::elem() fill) Outer0::VarIterator.#apply(th
at:this, min:min.#less(that:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars(Outer4::Template::N min) Outer0::VarIterator.#apply(that:this, min:min.#less(that
:Outer4::Template::N.#numberParser(that:{'@stringU
'1
})), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
mut method
mut Outer0::VarIterator vars(Outer4::Template::N ok, Outer4::Template::N max, Outer1::Kind::elem() fill) Outer
0::VarIterator.#apply(that:this, min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars(Outer4::Template::N ok, Outer4::Template::N max) Outer0::VarIterator.#apply(that:
this, min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
mut method
mut Outer0::VarIterator vars(Outer4::Template::N ok, Outer1::Kind::elem() fill) Outer0::VarIterator.#apply(tha
t:this, min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars(Outer4::Template::N ok) Outer0::VarIterator.#apply(that:this, min:Outer4::Templat
e::N.#numberParser(that:{'@stringU
'-1
}), ok:ok, max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
mut method
mut Outer0::VarIterator vars(Outer4::Template::N max, Outer1::Kind::elem() fill) Outer0::VarIterator.#apply(th
at:this, min:Outer4::Template::N.#numberParser(that:{'@stringU
'-1
}), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars(Outer4::Template::N max) Outer0::VarIterator.#apply(that:this, min:Outer4::Templa
te::N.#numberParser(that:{'@stringU
'-1
}), ok:max, max:Outer0::OptMax.#apply(that:max), fill:Outer0::OptFill.#apply())
mut method
mut Outer0::VarIterator vars(Outer1::Kind::elem() fill) Outer0::VarIterator.#apply(that:this, min:Outer4::Temp
late::N.#numberParser(that:{'@stringU
'-1
}), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply(that:fill))
mut method
mut Outer0::VarIterator vars() Outer0::VarIterator.#apply(that:this, min:Outer4::Template::N.#numberParser(tha
t:{'@stringU
'-1
}), ok:this.size(), max:Outer0::OptMax.#apply(), fill:Outer0::OptFill.#apply())
VarIterator:{
type method
mut Outer0 #apply(mut Outer1^that'@consistent
, Outer5::Template::N^min'@consistent
, Outer5::Template::N^ok'@consistent
, Outer1::OptMax^max'@consistent
, Outer1::OptFill^fill'@consistent
)
mut method '@consistent
mut Outer1 #that()
read method '@consistent
read Outer1 that()
mut method '@consistent
Void min(Outer5::Template::N that)
mut method '@consistent
Outer5::Template::N #min()
read method '@consistent
Outer5::Template::N min()
mut method '@consistent
Outer5::Template::N #ok()
read method '@consistent
Outer5::Template::N ok()
mut method '@consistent
Outer1::OptMax #max()
read method '@consistent
Outer1::OptMax max()
mut method '@consistent
Outer1::OptFill #fill()
read method '@consistent
Outer1::OptFill fill()
mut method
Void inner(Outer2::Kind::elem() that) this.#that().#apply(that:this.min(), val:that)
mut method
Void #next() exception Void (
  Void unused=(
    Void unused0=this.min(that:this.min().#plus(that:Outer5::Template::N.#numberParser(that:{'@stringU
    '1
    })))
    Void unused1=(
      Outer0::fill() ::isPresent() cond=this.fill().isPresent()
      (
        Void unused5=cond.#checkTrue()
        catch exception unused6 (
          on Void void
          )
        (
          Void unused7=(
            Outer0::max() ::isPresent() ::#bang() cond0=this.max().isPresent().#bang()
            (
              Void unused9=cond0.#checkTrue()
              catch exception unused10 (
                on Void void
                )
              return void
              )
            )
          Void unused8=(
            Outer0::min() ::#rightequal(that ) cond1=this.min().#rightequal(that:this.max().#bang())
            (
              Void unused11=cond1.#checkTrue()
              catch exception unused12 (
                on Void void
                )
              exception void
              )
            )
          return void
          )
        )
      )
    Void unused2=(
      Outer0::max() ::isPresent() cond2=this.max().isPresent()
      (
        Void unused13=cond2.#checkTrue()
        catch exception unused14 (
          on Void void
          )
        (
          Outer0::min() ::#rightequal(that ) cond3=this.min().#rightequal(that:this.max().#bang())
          (
            Void unused15=cond3.#checkTrue()
            catch exception unused16 (
              on Void void
              )
            exception void
            )
          )
        )
      )
    Void unused3=(
      Outer0::min() ::#rightequal(that ) cond4=this.min().#rightequal(that:this.that().size())
      (
        Void unused17=cond4.#checkTrue()
        catch exception unused18 (
          on Void void
          )
        exception void
        )
      )
    Void unused4=return void
    void
    )
  catch return result (
    on Void result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
read method
Void #checkEnd() (
  Outer0::min() ::#left(that ) cond=this.min().#left(that:this.ok())
  (
    Void unused=cond.#checkTrue()
    catch exception unused0 (
      on Void void
      )
    error Outer5::Template::S.#stringParser(that:{'@stringU
    'Unexpected termination of iteration, reached
    }).#plusplus(that:this.min()).#plusplus(that:Outer5::Template::S.#stringParser(that:{'@stringU
    ' instead of
    })).#plusplus(that:this.ok())
    )
  )
read method
Outer2::Kind::elem() #inner() (
  Void unused=(
    Void unused0=(
      Outer0::fill() ::isPresent() ::#bang() cond=this.fill().isPresent().#bang()
      (
        Void unused3=cond.#checkTrue()
        catch exception unused4 (
          on Void void
          )
        return this.that().#apply(that:this.min())
        )
      )
    Void unused1=(
      Outer0::min() ::#left(that ) cond0=this.min().#left(that:this.that().size())
      (
        Void unused5=cond0.#checkTrue()
        catch exception unused6 (
          on Void void
          )
        return this.that().#apply(that:this.min())
        )
      )
    Void unused2=return this.fill().#bang()
    void
    )
  catch return result (
    on Outer2::Kind::elem() result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
read method
Void #close() void}}})}##star ^##, {
Exported:##walkBy}##star ^##, {
Bool:{<:Outer1::S::ToS
method
Outer1::S toS() (
  Void unused=(
    Void unused0=(
      Void unused2=this.#checkTrue()
      catch exception unused3 (
        on Void void
        )
      return Outer1::S.#stringParser(that:{'@stringU
      'true
      })
      )
    Void unused1=return Outer1::S.#stringParser(that:{'@stringU
    'false
    })
    void
    )
  catch return result (
    on Outer0::toS() result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
method
Void #checkTrue() exception Void using Outer1::Alu check ifInt32EqualDo(n1:this.binaryRepr5(), n2:Outer1::N.#n
umberParser(that:{'@stringU
'0
}).binaryRepr()) exception void
type method
Outer0 true() Outer0.#apply4(binaryRepr5:Outer1::N.#numberParser(that:{'@stringU
'1
}).binaryRepr())
type method
Outer0 false() Outer0.#apply4(binaryRepr5:Outer1::N.#numberParser(that:{'@stringU
'0
}).binaryRepr())
method
Outer0 #and(Outer0 that) (
  Void unused=(
    Void unused0=(
      Void unused2=this.#checkTrue()
      catch exception unused3 (
        on Void void
        )
      return that
      )
    Void unused1=return this
    void
    )
  catch return result (
    on Outer0 result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
method
Outer0 #or(Outer0 that) (
  Void unused=(
    Void unused0=(
      Void unused2=this.#checkTrue()
      catch exception unused3 (
        on Void void
        )
      return this
      )
    Void unused1=return that
    void
    )
  catch return result (
    on Outer0 result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
method
Outer0 #bang() (
  Void unused=(
    Void unused0=(
      Void unused2=this.#checkTrue()
      catch exception unused3 (
        on Void void
        )
      return Outer0.false()
      )
    Void unused1=return Outer0.true()
    void
    )
  catch return result (
    on Outer0 result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method '@private
Outer0 #apply4(Library^binaryRepr5'@consistent
)
mut method '@consistent
Library #binaryRepr5()
read method '@consistent
Library binaryRepr5() }##star ^##
ExitCode:{
type method
Library normal() {'@exitStatus
'0
}
type method
Library failure() {'@exitStatus
'42000
}}##star ^##
Alu:{'@plugin
'L42.is/connected/withAlu

type method '@private
Outer0 _private18() }##star ^##
N:{<:Outer1::S::ToS
method
Outer1::S toS() Outer1::S.#stringParser(that:using Outer1::Alu check int32ToString(that:this.binaryRepr()) err
or void)
type method
Outer0 #apply(Library^binaryRepr'@consistent
)
mut method '@consistent
Library #binaryRepr()
read method '@consistent
Library binaryRepr()
type method
Outer1::N #numberParser(Library that) Outer1::N.#apply(binaryRepr:using Outer1::Alu check stringToInt32(that:t
hat) error void)
method
Outer1::N #plus(Outer1::N that) Outer1::N.#apply(binaryRepr:using Outer1::Alu check sumInt32(n1:this.binaryRep
r(), n2:that.binaryRepr()) error void)
method
Outer1::N #less(Outer1::N that) Outer1::N.#apply(binaryRepr:using Outer1::Alu check subInt32(n1:this.binaryRep
r(), n2:that.binaryRepr()) error void)
method
Outer1::N #times(Outer1::N that) Outer1::N.#apply(binaryRepr:using Outer1::Alu check mulInt32(n1:this.binaryRe
pr(), n2:that.binaryRepr()) error void)
method
Outer1::N #divide(Outer1::N that) Outer1::N.#apply(binaryRepr:using Outer1::Alu check divInt32(n1:this.binaryR
epr(), n2:that.binaryRepr()) error void)
method
Outer1::Bool #equalequal(Outer1::N that) (
  Void unused=(
    Void unused0=using Outer1::Alu check ifInt32EqualDo(n1:this.binaryRepr(), n2:that.binaryRepr()) return Out
er1::Bool.true()
    Void unused1=return Outer1::Bool.false()
    void
    )
  catch return result (
    on Outer1::Bool result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
method
Outer1::Bool #left(Outer1::N that) (
  Void unused=(
    Void unused0=using Outer1::Alu check ifInt32GrtDo(n1:this.binaryRepr(), n2:that.binaryRepr()) return Outer
1::Bool.true()
    Void unused1=return Outer1::Bool.false()
    void
    )
  catch return result (
    on Outer1::Bool result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
method
Outer1::Bool #leftequal(Outer1::N that) (
  Void unused=(
    Void unused0=using Outer1::Alu check ifInt32GEqDo(n1:this.binaryRepr(), n2:that.binaryRepr()) return Outer
1::Bool.true()
    Void unused1=return Outer1::Bool.false()
    void
    )
  catch return result (
    on Outer1::Bool result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
method
Outer1::Bool #bangequal(Outer1::N that) this.#equalequal(that:that).#bang()
method
Outer1::Bool #right(Outer1::N that) this.#leftequal(that:that).#bang()
method
Outer1::Bool #rightequal(Outer1::N that) this.#left(that:that).#bang()}##star ^##
S:{<:Outer0::ToS
method
Outer1::S toS() this
Varresult0:{
type method
mut Outer0 #apply(Outer2::S^inner'@consistent
)
mut method '@consistent
Void inner(Outer2::S that)
mut method '@consistent
Outer2::S #inner()
read method '@consistent
Outer2::S inner() }##star ^##
Vari0:{
type method
mut Outer0 #apply(Outer2::N^inner'@consistent
)
mut method '@consistent
Void inner(Outer2::N that)
mut method '@consistent
Outer2::N #inner()
read method '@consistent
Outer2::N inner() }##star ^##
type method
Outer0 #stringParser(Library that) Outer0._private19(_binaryRepr5:that)
method
Library binaryRepr() this._binaryRepr5()
ToS:{interface
method
Outer2::S toS() }##star ^##
method
Outer1::S #plusplus(Outer0::ToS that) Outer1::S.#stringParser(that:using Outer1::Alu check stringConcat(s1:thi
s.binaryRepr(), s2:that.toS().binaryRepr()) error void)
method
Outer1::Bool #bangequal(Outer1::S that) this.#equalequal(that:that).#bang()
method
Outer1::Bool #equalequal(Outer1::S that) (
  Void unused=(
    Void unused0=using Outer1::Alu check ifStringEqualDo(s1:this.binaryRepr(), s2:that.binaryRepr()) return Ou
ter1::Bool.true()
    Void unused1=return Outer1::Bool.false()
    void
    )
  catch return result (
    on Outer1::Bool result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
method
Outer1::S #apply(Outer1::N that) Outer1::S.#stringParser(that:using Outer1::Alu check stringCharAt(that:this.b
inaryRepr(), pos:that.binaryRepr()) error void)
method
Outer1::N size() Outer1::N.#apply(binaryRepr:using Outer1::Alu check stringSize(that:this.binaryRepr()) error
void)
type method
Outer1::S doubleQuote() Outer1::S.#stringParser(that:{'@stringU
'"\u000a
}).#apply(that:Outer1::N.#numberParser(that:{'@stringU
'0
}))
method
Outer1::S replace(Outer1::S that, Outer1::S into) (
  Void unused=('that must be of size 1

    Outer1::N i=Outer1::N.#numberParser(that:{'@stringU
    '0
    })
    Outer1::S result=Outer1::S.#stringParser(that:{'@stringU
    })
    mut Outer0::Vari0 vari=Outer0::Vari0.#apply(inner:i)
    mut Outer0::Varresult0 varresult=Outer0::Varresult0.#apply(inner:result)
    Void unused0=(
      Void unused2=loop (
        Void unused3=vari.#inner().#left(that:this.size()).#checkTrue()
        (
          Void unused4=(
            Outer0::#apply(that ) ::#equalequal(that ) cond=this.#apply(that:vari.#inner()).#equalequal(that:t
hat)
            (
              Void unused5=cond.#checkTrue()
              catch exception unused6 (
                on Void varresult.inner(that:varresult.#inner().#plusplus(that:this.#apply(that:vari.#inner())
))
                )
              varresult.inner(that:varresult.#inner().#plusplus(that:into))
              )
            )
          vari.inner(that:vari.#inner().#plus(that:Outer1::N.#numberParser(that:{'@stringU
          '1
          })))
          )
        )
      catch exception unused7 (
        on Void void
        )
      void
      )
    Void unused1=return varresult.#inner()
    void
    )
  catch return result0 (
    on Outer1::S result0
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method '@private
Outer0 _private19(Library^_binaryRepr5'@consistent
)
mut method '@consistent
Library #_binaryRepr5()
read method '@consistent
Library _binaryRepr5() }##star ^##
Debug:{
type method
Void #apply(Outer1::S that) using Outer1::Alu check stringDebug(that:that.binaryRepr()) void
type method
Void #apply(Outer1::S fileName, Outer1::S content) using Outer1::Alu check fileDebug(fileName:fileName.binaryR
epr(), content:content.binaryRepr()) void
type method '@private
Outer0 _private20() }##star ^##
IntrospectionPlugin:{'@plugin
'L42.is/connected/withItself

type method
Library sumLib(Library l1, Library l2) using Outer0 check sumLib(l1:l1, l2:l2) error {
type method
Outer0 sumLib() }
type method
Library adaptLib(Library l1, Library l2) using Outer0 check adaptLib(l1:l1, l2:l2) error {
type method
Outer0 adaptLib() }
type method
Library nameToAdapter(Library that) using Outer0 check nameToAdapter(that:that) error {
type method
Outer0 nameToAdapter() }
type method
Library typeNameToAdapter(type Any that) using Outer0 check typeNameToAdapter(that:that) error {
type method
Outer0 typeNameToAdapter() }
type method
Library getFreshName(Library that) using Outer0 check getFreshName(that:that) error {
type method
Outer0 getFreshName() }
type method
Library adapter(type Any that, Outer1::S name) (
  Void unused=(
    Outer1::IntrospectionPlugin::typeNameToAdapter(that ) name1=Outer1::IntrospectionPlugin.typeNameToAdapter(
that:that)
    Outer1::IntrospectionPlugin::nameToAdapter(that ) name2=Outer1::IntrospectionPlugin.nameToAdapter(that:nam
e.binaryRepr())
    Void unused0=return Outer1::IntrospectionPlugin.adaptLib(l1:name1, l2:name2)
    void
    )
  catch return result (
    on Library result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method
Library adapter(Outer1::S nameDest, Outer1::S nameSrc) (
  Void unused=(
    Outer1::IntrospectionPlugin::nameToAdapter(that ) name1=Outer1::IntrospectionPlugin.nameToAdapter(that:nam
eDest.binaryRepr())
    Outer1::IntrospectionPlugin::nameToAdapter(that ) name2=Outer1::IntrospectionPlugin.nameToAdapter(that:nam
eSrc.binaryRepr())
    Void unused0=return Outer1::IntrospectionPlugin.adaptLib(l1:name1, l2:name2)
    void
    )
  catch return result (
    on Library result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method '@private
Outer0 _private21() }##star ^##
Generalize:{
Varmap:{
type method
mut Outer0 #apply(Outer2::IntrospectionPlugin::sumLib(l1 l2 ) ^inner'@consistent
)
mut method '@consistent
Void inner(Outer2::IntrospectionPlugin::sumLib(l1 l2 ) that)
mut method '@consistent
Outer2::IntrospectionPlugin::sumLib(l1 l2 ) #inner() }##star ^##
type method
type Outer0 #begin() Outer0
type method
type Outer0 #end() Outer0
type method
Library #left(Library that) (
  Void unused=('if Bool.true() (return that)

    Outer1::IntrospectionPlugin::adapter(that name ) boolA=Outer1::IntrospectionPlugin.adapter(that:Outer1::Te
mplate::Bool, name:Outer1::S.#stringParser(that:{'@stringU
    'Bool
    }))
    Outer1::IntrospectionPlugin::adapter(that name ) nA=Outer1::IntrospectionPlugin.adapter(that:Outer1::Templ
ate::N, name:Outer1::S.#stringParser(that:{'@stringU
    'N
    }))
    Outer1::IntrospectionPlugin::adapter(that name ) sA=Outer1::IntrospectionPlugin.adapter(that:Outer1::Templ
ate::S, name:Outer1::S.#stringParser(that:{'@stringU
    'S
    }))
    Outer1::IntrospectionPlugin::adapter(that name ) toSA=Outer1::IntrospectionPlugin.adapter(that:Outer1::Tem
plate::S::ToS, name:Outer1::S.#stringParser(that:{'@stringU
    'S::ToS
    }))
    Outer1::IntrospectionPlugin::adapter(that name ) debugA=Outer1::IntrospectionPlugin.adapter(that:Outer1::T
emplate::Debug, name:Outer1::S.#stringParser(that:{'@stringU
    'Debug
    }))
    Outer1::IntrospectionPlugin::sumLib(l1 l2 ) map=Outer1::IntrospectionPlugin.sumLib(l1:boolA, l2:nA)
    mut Outer0::Varmap varmap=Outer0::Varmap.#apply(inner:map)
    Void unused0=varmap.inner(that:Outer1::IntrospectionPlugin.sumLib(l1:varmap.#inner(), l2:sA))
    Void unused1=varmap.inner(that:Outer1::IntrospectionPlugin.sumLib(l1:varmap.#inner(), l2:toSA))
    Void unused2=varmap.inner(that:Outer1::IntrospectionPlugin.sumLib(l1:varmap.#inner(), l2:debugA))
    Void unused3=varmap.inner(that:Outer1::IntrospectionPlugin.sumLib(l1:varmap.#inner(), l2:Outer1::Introspec
tionPlugin.adapter(that:Any, name:Outer1::S.#stringParser(that:{'@stringU
    'ExitCode
    }))))
    Void unused4=varmap.inner(that:Outer1::IntrospectionPlugin.sumLib(l1:varmap.#inner(), l2:Outer1::Introspec
tionPlugin.adapter(that:Any, name:Outer1::S.#stringParser(that:{'@stringU
    'Alu
    }))))
    Void unused5=varmap.inner(that:Outer1::IntrospectionPlugin.sumLib(l1:varmap.#inner(), l2:Outer1::Introspec
tionPlugin.adapter(that:Any, name:Outer1::S.#stringParser(that:{'@stringU
    'IntrospectionPlugin
    }))))
    Void unused6=varmap.inner(that:Outer1::IntrospectionPlugin.sumLib(l1:varmap.#inner(), l2:Outer1::Introspec
tionPlugin.adapter(that:Any, name:Outer1::S.#stringParser(that:{'@stringU
    'Collections
    }))))
    Void unused7=varmap.inner(that:Outer1::IntrospectionPlugin.sumLib(l1:varmap.#inner(), l2:Outer1::Introspec
tionPlugin.adapter(that:Any, name:Outer1::S.#stringParser(that:{'@stringU
    'Name
    }))))
    Void unused8=varmap.inner(that:Outer1::IntrospectionPlugin.sumLib(l1:varmap.#inner(), l2:Outer1::Introspec
tionPlugin.adapter(that:Any, name:Outer1::S.#stringParser(that:{'@stringU
    'Introspection
    }))))
    Void unused9=varmap.inner(that:Outer1::IntrospectionPlugin.sumLib(l1:varmap.#inner(), l2:Outer1::Introspec
tionPlugin.adapter(that:Any, name:Outer1::S.#stringParser(that:{'@stringU
    'Load
    }))))
    Void unused10=varmap.inner(that:Outer1::IntrospectionPlugin.sumLib(l1:varmap.#inner(), l2:Outer1::Introspe
ctionPlugin.adapter(that:Any, name:Outer1::S.#stringParser(that:{'@stringU
    'Opt
    }))))
    Void unused11=varmap.inner(that:Outer1::IntrospectionPlugin.sumLib(l1:varmap.#inner(), l2:Outer1::Introspe
ctionPlugin.adapter(that:Any, name:Outer1::S.#stringParser(that:{'@stringU
    'Use
    }))))
    Void unused12=varmap.inner(that:Outer1::IntrospectionPlugin.sumLib(l1:varmap.#inner(), l2:Outer1::Introspe
ctionPlugin.adapter(that:Any, name:Outer1::S.#stringParser(that:{'@stringU
    'Adapt
    }))))
    Void unused13=varmap.inner(that:Outer1::IntrospectionPlugin.sumLib(l1:varmap.#inner(), l2:Outer1::Introspe
ctionPlugin.adapter(that:Any, name:Outer1::S.#stringParser(that:{'@stringU
    'Data
    }))))
    Void unused14=return Outer1::IntrospectionPlugin.adaptLib(l1:that, l2:varmap.#inner())
    void
    )
  catch return result (
    on Library result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )}##star ^##
Template:{
Bool:{<:Outer1::S::ToS
method
Outer1::S toS()
method
Void #checkTrue() exception Void
type method
Outer0 true()
type method
Outer0 false()
method
Outer0 #and(Outer0 that)
method
Outer0 #or(Outer0 that)
method
Outer0 #bang() }##plus ^##
N:{<:Outer1::S::ToS
method
Outer1::S toS()
type method
Outer1::N #numberParser(Library that)
method
Outer1::N #plus(Outer1::N that)
method
Outer1::N #less(Outer1::N that)
method
Outer1::N #times(Outer1::N that)
method
Outer1::N #divide(Outer1::N that)
method
Outer1::Bool #equalequal(Outer1::N that)
method
Outer1::Bool #left(Outer1::N that)
method
Outer1::Bool #leftequal(Outer1::N that)
method
Outer1::Bool #bangequal(Outer1::N that)
method
Outer1::Bool #right(Outer1::N that)
method
Outer1::Bool #rightequal(Outer1::N that)
method
Library binaryRepr() }##plus ^##
S:{<:Outer0::ToS
method
Outer1::S toS()
type method
Outer0 #stringParser(Library that)
ToS:{interface
method
Outer2::S toS() }##plus ^##
method
Outer1::S #plusplus(Outer0::ToS that)
method
Outer1::Bool #bangequal(Outer1::S that)
method
Outer1::Bool #equalequal(Outer1::S that)
method
Outer1::S #apply(Outer1::N that)
method
Outer1::N size()
type method
Outer1::S doubleQuote()
method
Outer1::S replace(Outer1::S that, Outer1::S into)
method
Library binaryRepr() }##plus ^##
Debug:{
type method
Void #apply(Outer1::S that) }##plus ^##}##plus ^##
Top:##walkBy}##less ^##, {
Bool:{<:Outer1::S::ToS
method
Outer1::S toS() (
  Void unused=(
    Void unused0=(
      Void unused2=this.#checkTrue()
      catch exception unused3 (
        on Void void
        )
      return Outer1::S.#stringParser(that:{'@stringU
      'true
      })
      )
    Void unused1=return Outer1::S.#stringParser(that:{'@stringU
    'false
    })
    void
    )
  catch return result (
    on Outer0::toS() result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
method
Void #checkTrue() exception Void using Outer1::Alu check ifInt32EqualDo(n1:this.binaryRepr2(), n2:Outer1::N.#n
umberParser(that:{'@stringU
'0
}).binaryRepr()) exception void
type method
Outer0 true() Outer0.#apply2(binaryRepr2:Outer1::N.#numberParser(that:{'@stringU
'1
}).binaryRepr())
type method
Outer0 false() Outer0.#apply2(binaryRepr2:Outer1::N.#numberParser(that:{'@stringU
'0
}).binaryRepr())
method
Outer0 #and(Outer0 that) (
  Void unused=(
    Void unused0=(
      Void unused2=this.#checkTrue()
      catch exception unused3 (
        on Void void
        )
      return that
      )
    Void unused1=return this
    void
    )
  catch return result (
    on Outer0 result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
method
Outer0 #or(Outer0 that) (
  Void unused=(
    Void unused0=(
      Void unused2=this.#checkTrue()
      catch exception unused3 (
        on Void void
        )
      return this
      )
    Void unused1=return that
    void
    )
  catch return result (
    on Outer0 result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
method
Outer0 #bang() (
  Void unused=(
    Void unused0=(
      Void unused2=this.#checkTrue()
      catch exception unused3 (
        on Void void
        )
      return Outer0.false()
      )
    Void unused1=return Outer0.true()
    void
    )
  catch return result (
    on Outer0 result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method '@private
Outer0 #apply2(Library^binaryRepr2'@consistent
)
mut method '@consistent
Library #binaryRepr2()
read method '@consistent
Library binaryRepr2() }##star ^##
ExitCode:{
type method
Library normal() {'@exitStatus
'0
}
type method
Library failure() {'@exitStatus
'42000
}}##star ^##
Alu:{'@plugin
'L42.is/connected/withAlu

type method '@private
Outer0 _private8() }##star ^##
N:{<:Outer1::S::ToS
method
Outer1::S toS() Outer1::S.#stringParser(that:using Outer1::Alu check int32ToString(that:this.binaryRepr()) err
or void)
type method
Outer0 #apply(Library^binaryRepr'@consistent
)
mut method '@consistent
Library #binaryRepr()
read method '@consistent
Library binaryRepr()
type method
Outer1::N #numberParser(Library that) Outer1::N.#apply(binaryRepr:using Outer1::Alu check stringToInt32(that:t
hat) error void)
method
Outer1::N #plus(Outer1::N that) Outer1::N.#apply(binaryRepr:using Outer1::Alu check sumInt32(n1:this.binaryRep
r(), n2:that.binaryRepr()) error void)
method
Outer1::N #less(Outer1::N that) Outer1::N.#apply(binaryRepr:using Outer1::Alu check subInt32(n1:this.binaryRep
r(), n2:that.binaryRepr()) error void)
method
Outer1::N #times(Outer1::N that) Outer1::N.#apply(binaryRepr:using Outer1::Alu check mulInt32(n1:this.binaryRe
pr(), n2:that.binaryRepr()) error void)
method
Outer1::N #divide(Outer1::N that) Outer1::N.#apply(binaryRepr:using Outer1::Alu check divInt32(n1:this.binaryR
epr(), n2:that.binaryRepr()) error void)
method
Outer1::Bool #equalequal(Outer1::N that) (
  Void unused=(
    Void unused0=using Outer1::Alu check ifInt32EqualDo(n1:this.binaryRepr(), n2:that.binaryRepr()) return Out
er1::Bool.true()
    Void unused1=return Outer1::Bool.false()
    void
    )
  catch return result (
    on Outer1::Bool result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
method
Outer1::Bool #left(Outer1::N that) (
  Void unused=(
    Void unused0=using Outer1::Alu check ifInt32GrtDo(n1:this.binaryRepr(), n2:that.binaryRepr()) return Outer
1::Bool.true()
    Void unused1=return Outer1::Bool.false()
    void
    )
  catch return result (
    on Outer1::Bool result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
method
Outer1::Bool #leftequal(Outer1::N that) (
  Void unused=(
    Void unused0=using Outer1::Alu check ifInt32GEqDo(n1:this.binaryRepr(), n2:that.binaryRepr()) return Outer
1::Bool.true()
    Void unused1=return Outer1::Bool.false()
    void
    )
  catch return result (
    on Outer1::Bool result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
method
Outer1::Bool #bangequal(Outer1::N that) this.#equalequal(that:that).#bang()
method
Outer1::Bool #right(Outer1::N that) this.#leftequal(that:that).#bang()
method
Outer1::Bool #rightequal(Outer1::N that) this.#left(that:that).#bang()}##star ^##
S:{<:Outer0::ToS
method
Outer1::S toS() this
Varresult:{
type method
mut Outer0 #apply(Outer2::S^inner'@consistent
)
mut method '@consistent
Void inner(Outer2::S that)
mut method '@consistent
Outer2::S #inner()
read method '@consistent
Outer2::S inner() }##star ^##
Vari:{
type method
mut Outer0 #apply(Outer2::N^inner'@consistent
)
mut method '@consistent
Void inner(Outer2::N that)
mut method '@consistent
Outer2::N #inner()
read method '@consistent
Outer2::N inner() }##star ^##
type method
Outer0 #stringParser(Library that) Outer0._private9(_binaryRepr2:that)
method
Library binaryRepr() this._binaryRepr2()
ToS:{interface
method
Outer2::S toS() }##star ^##
method
Outer1::S #plusplus(Outer0::ToS that) Outer1::S.#stringParser(that:using Outer1::Alu check stringConcat(s1:thi
s.binaryRepr(), s2:that.toS().binaryRepr()) error void)
method
Outer1::Bool #bangequal(Outer1::S that) this.#equalequal(that:that).#bang()
method
Outer1::Bool #equalequal(Outer1::S that) (
  Void unused=(
    Void unused0=using Outer1::Alu check ifStringEqualDo(s1:this.binaryRepr(), s2:that.binaryRepr()) return Ou
ter1::Bool.true()
    Void unused1=return Outer1::Bool.false()
    void
    )
  catch return result (
    on Outer1::Bool result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
method
Outer1::S #apply(Outer1::N that) Outer1::S.#stringParser(that:using Outer1::Alu check stringCharAt(that:this.b
inaryRepr(), pos:that.binaryRepr()) error void)
method
Outer1::N size() Outer1::N.#apply(binaryRepr:using Outer1::Alu check stringSize(that:this.binaryRepr()) error
void)
type method
Outer1::S doubleQuote() Outer1::S.#stringParser(that:{'@stringU
'"\u000a
}).#apply(that:Outer1::N.#numberParser(that:{'@stringU
'0
}))
method
Outer1::S replace(Outer1::S that, Outer1::S into) (
  Void unused=('that must be of size 1

    Outer1::N i=Outer1::N.#numberParser(that:{'@stringU
    '0
    })
    Outer1::S result=Outer1::S.#stringParser(that:{'@stringU
    })
    mut Outer0::Vari vari=Outer0::Vari.#apply(inner:i)
    mut Outer0::Varresult varresult=Outer0::Varresult.#apply(inner:result)
    Void unused0=(
      Void unused2=loop (
        Void unused3=vari.#inner().#left(that:this.size()).#checkTrue()
        (
          Void unused4=(
            Outer0::#apply(that ) ::#equalequal(that ) cond=this.#apply(that:vari.#inner()).#equalequal(that:t
hat)
            (
              Void unused5=cond.#checkTrue()
              catch exception unused6 (
                on Void varresult.inner(that:varresult.#inner().#plusplus(that:this.#apply(that:vari.#inner())
))
                )
              varresult.inner(that:varresult.#inner().#plusplus(that:into))
              )
            )
          vari.inner(that:vari.#inner().#plus(that:Outer1::N.#numberParser(that:{'@stringU
          '1
          })))
          )
        )
      catch exception unused7 (
        on Void void
        )
      void
      )
    Void unused1=return varresult.#inner()
    void
    )
  catch return result0 (
    on Outer1::S result0
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method '@private
Outer0 _private9(Library^_binaryRepr2'@consistent
)
mut method '@consistent
Library #_binaryRepr2()
read method '@consistent
Library _binaryRepr2() }##star ^##
Debug:{
type method
Void #apply(Outer1::S that) using Outer1::Alu check stringDebug(that:that.binaryRepr()) void
type method
Void #apply(Outer1::S fileName, Outer1::S content) using Outer1::Alu check fileDebug(fileName:fileName.binaryR
epr(), content:content.binaryRepr()) void
type method '@private
Outer0 _private10() }##star ^##
IntrospectionPlugin:{'@plugin
'L42.is/connected/withItself

type method
Library sumLib(Library l1, Library l2) using Outer0 check sumLib(l1:l1, l2:l2) error {
type method
Outer0 sumLib() }
type method
Library adaptLib(Library l1, Library l2) using Outer0 check adaptLib(l1:l1, l2:l2) error {
type method
Outer0 adaptLib() }
type method
Library nameToAdapter(Library that) using Outer0 check nameToAdapter(that:that) error {
type method
Outer0 nameToAdapter() }
type method
Library typeNameToAdapter(type Any that) using Outer0 check typeNameToAdapter(that:that) error {
type method
Outer0 typeNameToAdapter() }
type method
Library getFreshName(Library that) using Outer0 check getFreshName(that:that) error {
type method
Outer0 getFreshName() }
type method
Library adapter(type Any that, Outer1::S name) (
  Void unused=(
    Outer1::IntrospectionPlugin::typeNameToAdapter(that ) name1=Outer1::IntrospectionPlugin.typeNameToAdapter(
that:that)
    Outer1::IntrospectionPlugin::nameToAdapter(that ) name2=Outer1::IntrospectionPlugin.nameToAdapter(that:nam
e.binaryRepr())
    Void unused0=return Outer1::IntrospectionPlugin.adaptLib(l1:name1, l2:name2)
    void
    )
  catch return result (
    on Library result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method
Library adapter(Outer1::S nameDest, Outer1::S nameSrc) (
  Void unused=(
    Outer1::IntrospectionPlugin::nameToAdapter(that ) name1=Outer1::IntrospectionPlugin.nameToAdapter(that:nam
eDest.binaryRepr())
    Outer1::IntrospectionPlugin::nameToAdapter(that ) name2=Outer1::IntrospectionPlugin.nameToAdapter(that:nam
eSrc.binaryRepr())
    Void unused0=return Outer1::IntrospectionPlugin.adaptLib(l1:name1, l2:name2)
    void
    )
  catch return result (
    on Library result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method '@private
Outer0 _private11() }##star ^##
Deploy:{
type method
Outer0 #apply(Outer1::S^node'@consistent
, Outer1::S^fileName'@consistent
)
mut method '@consistent
Outer1::S #node()
read method '@consistent
Outer1::S node()
mut method '@consistent
Outer1::S #fileName()
read method '@consistent
Outer1::S fileName()
type method
type Outer0 #begin() Outer0
type method
Outer0 #add(Outer1::S that, Outer1::S fileName) Outer0.#apply(node:that, fileName:fileName)
method
Outer0 #end() this
method
Library #left(Library that) (
  Void unused=(
    Outer1::IntrospectionPlugin::adapter(nameDest nameSrc ) adapter=Outer1::IntrospectionPlugin.adapter(nameDe
st:this.node(), nameSrc:Outer1::S.#stringParser(that:{'@stringU
    'Template
    }))
    Outer1::IntrospectionPlugin::adaptLib(l1 l2 ) code=Outer1::IntrospectionPlugin.adaptLib(l1:that, l2:adapte
r)
    Void unused0=Outer0.#apply(fileName:this.fileName(), code:code, node:this.node())
    Void unused1=return Outer1::ExitCode.normal()
    void
    )
  catch return result (
    on Library result
    )
  error {'@stringU
  'CurlyBlock-Should be unreachable code
  }
  )
type method
Outer1::S _get(Library that, Outer1::S node) Outer1::S.#stringParser(that:using Outer1::IntrospectionPlugin ch
eck get(that:that, node:node.binaryRepr()) error {
type method
Outer0 getThatNode() })
type method
Void #apply(Outer1::S fileName, Library code, Outer1::S node) Outer1::Debug.#apply(fileName:fileName, content:
Outer0._get(that:code, node:node))}##star ^##
Main:##walkBy}]), errorTxt=




*/