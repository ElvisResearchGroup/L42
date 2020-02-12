package is.L42.cache.tests;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import is.L42.cache.L42Cachable;
import is.L42.cache.L42Cache;
import is.L42.cache.L42StandardCache;

public class TestEarlyCache {
  public static void main(String[]arg){
    SBox s1=new SBox("Hello");
    //SBox s2=new SBox("Hello ");
    //s2=s2.myCache().normalize(s2);
    //assert s2!=s1;
    System.out.println(s1.methName1());
    //System.out.println(s1.methName1());
    }
  }
class SBox implements L42Cachable<SBox>{
  String s;SBox(String s){this.s=s;}
  String _methName1(){return this.s+this.s+this.methName2();}
  String _methName2(){return this.s+this.s+this.methName1();}
  //generated to be normalizable
  static final Class<SBox> _class=SBox.class;
  private static final L42StandardCache<SBox> myCache=new L42StandardCache<>("SBox",SBox._class);
  static{myCache.lateInitialize(String.class);}
  private SBox norm;
  boolean started=false; //only if there is any early cache
  public SBox normed(){return norm!=null?norm:myCache.normalize(this);}
  @Override public int numFields(){return 1;}
  @Override public Object[] allFields() {return new Object[]{s};}
  @Override public void setField(int i, Object o) { this.s=(String)o;}
  @Override public L42Cache<SBox> myCache() {return myCache;}
  @Override public Object getField(int i){return s;}
  @Override public SBox myNorm() {return this.norm;}
  @Override public SBox newInstance() { return new SBox(""); }
  @Override public void setNorm(SBox t) {
    assert norm==null;
    System.err.println(t.s+" Normed with hashcode "+t.hashCode()+" over "+this.hashCode());
    //only if there is any early cache, else just "this.norm=t;"
    if(t!=this){this.norm=t;return;}    
    if(started){throw new StackOverflowError(s);}
    started=true;
    methName1=new CompletableFuture<String>();
    methName2=new CompletableFuture<String>();
    CompletableFuture.supplyAsync(t::_methName1).thenAccept(methName1::complete);
    CompletableFuture.supplyAsync(t::_methName2).thenAccept(methName2::complete);
    //... more lines, one for each early cache 
    CompletableFuture.allOf(methName1,methName2).thenAccept(a->this.norm=t);
    }
  //generated for caching method methName1
  CompletableFuture<String> methName1;  
  String methName1(){return Util.join(normed().methName1);}
  //generated for caching method methName2
  CompletableFuture<String> methName2;  
  String methName2(){return Util.join(normed().methName2);}
  }
class Util{
  public static <T>T join(CompletableFuture<T> t){
    try{return t.join();}
    catch(CompletionException ce){
      if(ce.getCause() instanceof RuntimeException){throw (RuntimeException)ce.getCause();}
      if(ce.getCause() instanceof Error){throw (Error)ce.getCause();}
      throw ce;
      }
    }
  }