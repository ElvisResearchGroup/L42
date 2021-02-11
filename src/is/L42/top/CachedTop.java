package is.L42.top;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.pushL;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.introspection.L42£Nested;


public class CachedTop implements Serializable{
  final List<G> cached;
  final List<R>cachedR;
  public CachedTop(List<G> cached,List<R>cachedR){
    this.cached=cached;
    this.cachedR=cachedR;
    }
  public void fakeRunWithNoChange() {
    performed.addAll(cached);
    performedR.addAll(cachedR);
    }
  public Optional<Core.L> lastTopL(){
    return cachedR.stream().flatMap(this::_lastTopL).reduce((a, b)->b);
    }
  private Stream<Core.L> _lastTopL(R r){
    if(r.isErr()) {return Stream.of();}
    if(!(r._obj instanceof Program)) {return Stream.of();}
    Program p=(Program)r._obj;
    p=p.pop(p.dept());
    //System.out.println(p.topCore().domNC());
    return Stream.of(p.topCore());
    //var le=(LayerE)r._g.layer();
    //System.out.println(L42£Nested.fromLibrary((Core.L)r._obj).toString());
    /*if(le.layerL()==LayerL.empty()){return Stream.of((Core.L)r._obj);} 
    var p=le.layerL().p();
    System.out.println("OUTER P");
    System.out.println(L42£Nested.fromLibrary(p.topCore()).toString());
    var i=le.layerL().index();
    var c=le.layerL().ncs().get(i).key();
    System.out.println(c);
    System.out.println("######################");
    p=p.push(c, (Core.L)r._obj);
    //partially correct, if the e had more then one l and it terminated with error
    //it would return the last solved one.*/
    //p=p.pop(p.dept());
    //System.out.println(p.topCore().domNC());
    //return Stream.of(p.topCore());
    }
  public CachedTop toNextCache(){return new CachedTop(performed,performedR);}
  final ArrayList<G> performed=new ArrayList<>();
  final ArrayList<R> performedR=new ArrayList<>();
  boolean ok=true;
  G _getCached(){
    assert consistent();
    if(!ok){return null;}
    if(performed.size()>=cached.size()){return null;}
    return cached.get(performed.size());
    }
  R _getCachedR(){
    if(!ok){return null;}
    if(performed.size()>=cached.size()){return null;}
    return cachedR.get(performed.size());
    }
  void addPerformed(G g,R r){
    assert consistent();
    performed.add(g);
    performedR.add(r);
    assert consistent();
    }  
  boolean consistent(){
    for(var e:cached){
      assert cached.stream().noneMatch(e0->e0!=e && e0.state==e.state);
      assert performed.stream().noneMatch(e0->e0.state==e.state);
      }
    for(var e:performed){
      assert cached.stream().noneMatch(e0->e0.state==e.state);
      assert performed.stream().noneMatch(e0->e0!=e && e0.state==e.state);
      }
    return true;
    }  
  Program top(G g){
    R r=openClose(g);
    if(r.isErr()){throw r._err;}
    return (Program)r._obj;
    }
  void dbgPrint(G g,G gc){
    String str=gc==null?"null":gc.getClass().getSimpleName();
    System.err.println("Cache comparing "+g.getClass().getSimpleName()+" "+str);
    Object l=g.layer();
    LayerL p=null;
    if(l instanceof LayerL){p=(LayerL)l;}
    if(l instanceof LayerE){p=((LayerE)l).layerL();}
    if(p!=null && p!=LayerL.empty()){System.err.println("New Pos Is:"+p.p().path());}
    if(gc==null){return;}
    Object lc=gc.layer();
    LayerL pc=null;
    if(lc instanceof LayerL){pc=(LayerL)lc;}   
    if(lc instanceof LayerE){pc=((LayerE)lc).layerL();}
    if(pc!=null && pc!=LayerL.empty()){System.err.println("Cached Pos Is:"+pc.p().path());}
    }
  R openClose(G g0){
    //TestCachingCases.timeNow("open "+g0.getClass().getSimpleName());
    G cg0=_getCached();
    R cr0=_getCachedR();
    dbgPrint(g0,cg0);
    R r0=g0.open(cg0,cr0);
    addPerformed(g0,r0);
    if(r0.isErr()){return r0;}
    R r1=openCloseNested(r0);
    if(r1.isErr()){return r1;}
    G cg1=_getCached();
    R cr1=_getCachedR();
    dbgPrint(r1._g,cg1);
    R res=r1._g.close(cg1,cr1);
    addPerformed(r1._g,res);
    //TestCachingCases.timeNow("close "+r1._g.getClass().getSimpleName());
    return res;
    }
  R openCloseNested(R r0){
    assert !r0.isErr();
    while(true){
      if(!r0._g.needOpen()){return r0;}
      R r1=openClose(r0._g);
      if(r1.isErr()){return r1;}
      r0=r1;
      }
    }
  public static CachedTop loadCache(Path path){
    path=path.toAbsolutePath();
    try(
      var file=new FileInputStream(path.resolve("cache.L42Bytes").toFile()); 
      var out=new ObjectInputStream(file);
      ){return (CachedTop)out.readObject();}
    catch(FileNotFoundException e){return new CachedTop(L(),L());}
    catch(ClassNotFoundException e){throw bug();}
    catch(java.io.InvalidClassException e){
      System.err.println("LOG: saved cache was made of outdated bytecode");
      return new CachedTop(L(),L());
      }
    catch(java.io.EOFException e){
      System.err.print("Log: cache file was corrupted\n\n"+e);
      return new CachedTop(L(),L());
      }
    catch(IOException e){throw new Error(e);}
    }
  public void saveCache(Path path){
    path=path.toAbsolutePath();
    try(
      var file=new FileOutputStream(path.resolve("cache.L42Bytes").toFile()); 
      var out=new ObjectOutputStream(file);
      ){out.writeObject(this);}
    catch (FileNotFoundException e) {throw new Error(e);}
    catch (IOException e) {throw new Error(e);}
    }
  }