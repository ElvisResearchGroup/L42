package programReduction;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import ast.Ast;
public class Paths {
  //@SuppressWarnings("serial")
  //public static class EmptyPath extends RuntimeException{}
  final Paths next;
  final List<List<String>> current;
  private Paths(Paths next, List<List<String>> current){
    this.next=next; this.current=current;
    }
  private static final Paths empty=new Paths(null,Collections.emptyList());
  public static Paths empty(){return empty;}
  public boolean isEmpty(){return this==empty;}
  
  public List<List<String>> top(){return current;}
  public Paths pop(){
    if (this==empty){return empty;}
    return this.next;
    }
  public Paths push(List<List<String>> css){
    return new Paths(this,minimize(css));
    }
  public Paths union(Paths other){
    assert other!=null:
    "";
    if (this==empty){return other;}
    if (other==empty){return this;}
    Paths rec=this.pop().union(other.pop());
    List<List<String>> css = new ArrayList<>(this.top());
    css.addAll(other.top());
    return rec.push(css);
  }
  public Paths prefix(List<String>cs){
    if(this==empty){return empty;}
    if(cs.isEmpty()){return this;}
    if(this.pop()==empty){
      List<List<String>> res=new ArrayList<>();
      for(List<String>csi:this.top()){
        List<String>cscsi=new ArrayList<>(cs);
        cscsi.addAll(csi);
        res.add(cscsi);
        }
      return new Paths(empty,res);
      }
    assert this.pop()!=empty;
    Paths path=this.pop();
    List<String> csPopped=cs.subList(0, cs.size()-1);
    Paths paths0=path.prefix(csPopped);
    assert paths0!=empty;
    Paths paths1=new Paths(empty,this.top()).prefix(cs);
    assert paths1!=empty;
    return paths0.union(paths1);
    }
  public static Paths reorganize(List<Ast.Path> ps){
    if(ps.isEmpty()){return empty;}
    return reorganize(0,ps);
  }
  private static Paths reorganize(int level,List<Ast.Path> ps){
  if(ps.isEmpty()){return empty;}
  List<List<String>> csi=new ArrayList<>();
  List<Ast.Path> nextPs=new ArrayList<>();
  for(Ast.Path pi:ps){
    if(pi.isPrimitive()){continue;}
    if(pi.outerNumber()==level){csi.add(pi.getCBar());continue;}
    nextPs.add(pi);
    }
  return reorganize(level+1,nextPs).push(csi);
  }
  public static List<List<String>> minimize(List<List<String>> css){
    Set<List<String>> res=new LinkedHashSet<>();
    for(List<String> csCandidate: css) outCandidate:{
      for(List<String> csTest: css){
        if(isPrefix(csTest,csCandidate)){break outCandidate;}
      }
      res.add(csCandidate);
    }
    return new ArrayList<>(res);
    }
  private static boolean isPrefix(List<String>csShort,List<String>csLong){
    if(csShort.size()>=csLong.size()){return false;}
    for(int i=0;i<csShort.size();i++){
      if (!csShort.get(i).equals(csLong.get(i))){return false;}
      }
    //with cs1 in csShort.vals(), cs2 in csLong.vals(maxTo:csShort.size()) ( if cs1!=cs2 (return Bool.false()))
    return true;
    }
  public String toString(){
    return toString(0);
    }
  public String toString(int n){
    String result="";
    if(this.isEmpty()){return "<Empty Paths>";}
    for(List<String> cs:this.top()){
      result+=Ast.Path.outer(n, cs)+" ";
      }
    if(this.pop()!=empty){result+=this.pop().toString(n+1);}
    return result;
    }
  
  public boolean contains(ast.Ast.Path s) {
    Paths popped=this;
    for(int i=0;i<s.outerNumber();i++){
      popped=popped.pop();
      }
    return popped.current.contains(s.getCBar());
    }
  }
