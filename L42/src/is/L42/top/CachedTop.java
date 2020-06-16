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

import is.L42.common.Program;
import is.L42.tests.TestCachingCases;

public class CachedTop implements Serializable{
  final List<G> cached;
  final List<R>cachedR;
  public CachedTop(List<G> cached,List<R>cachedR){
    this.cached=cached;
    this.cachedR=cachedR;
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
  R openClose(G g0){
    TestCachingCases.timeNow("open");
    G cg0=_getCached();
    R cr0=_getCachedR();
    R r0=g0.open(cg0,cr0);
    addPerformed(g0,r0);
    if(r0.isErr()){return r0;}
    R r1=openCloseNested(r0);
    if(r1.isErr()){return r1;}
    G cg1=_getCached();
    R cr1=_getCachedR();
    R res=r1._g.close(cg1,cr1);
    addPerformed(r1._g,res);
    TestCachingCases.timeNow("close");
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