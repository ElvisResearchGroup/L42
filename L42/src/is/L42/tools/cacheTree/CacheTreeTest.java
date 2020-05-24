package is.L42.tools.cacheTree;
/*
import java.util.List;

import is.L42.tools.cacheTree.CacheTree.C;
import is.L42.tools.cacheTree.CacheTree.D;
import is.L42.tools.cacheTree.CacheTree.Fun;
import is.L42.tools.cacheTree.CacheTree.I;
import is.L42.tools.cacheTree.CacheTree.M;
import is.L42.tools.cacheTree.CacheTree.O;
import is.L42.tools.cacheTree.CacheTree.R;
import static is.L42.tools.cacheTree.CacheTreeTest.num;

class N implements CacheTree.I,CacheTree.M,CacheTree.O{
  public final int n;N(int n){this.n=n;}
  @Override public Boolean mayBeEq(M m){return this.n==num(m);}
  @Override public Boolean mayBeEq(I i){return this.n==num(i);}
  @Override public boolean eq(O f){return this.n==num(f);}
  }
class Node implements D{
  N i; N m; List<Node> nodes;
  Node(N i, N m, List<Node> nodes){this.i=i;this.m=m;this.nodes=nodes;}
  @Override public N i() {return i;}
  @Override public List<Node> ds() { return nodes;}
  @Override public N m() {return m;}
  }
class Algo0 implements Fun{
  @Override public Fun complexEq(I a, I b, boolean[] eq){
    eq[0]=num(a)==num(b);
    return this;
    }
  @Override public Fun complexEq(M a, M b, boolean[] eq){
    eq[0]=num(a)==num(b);
    return this;
    }
  @Override public boolean eq(Fun f) {return this.getClass()==f.getClass();}
  @Override public Fun apply(I i) {
    return new Algo1(this,num(i));
    }
  @Override public R apply(M m, List<O> os) {throw new Error();}
  }
class Algo1 implements Fun{
  @Override public Fun complexEq(I a, I b, boolean[] eq){
    eq[0]=num(a)==num(b);
    return this;
    }
  @Override public Fun complexEq(M a, M b, boolean[] eq){
    eq[0]=num(a)==num(b);
    return this;
    }
  Fun former;
  int acc;
  Algo1(Fun former,int n){this.former=former;acc=n;}
  @Override public boolean eq(Fun f) {return this.getClass()==f.getClass() && acc==((Algo1)f).acc;}//TODO: should also check former is the same?
  @Override public Fun apply(I i) {return new Algo1(this,num(i));}
  @Override public R apply(M m, List<O> os) {
    int res=this.acc+os.stream().mapToInt(oi->num(oi)).sum();
    return new R(this.former,new N(res/((N)m).n),null);
    }
  }

public class CacheTreeTest {
  static int num(M m){return ((N)m).n;}
  static int num(I m){return ((N)m).n;}
  static int num(O m){return ((N)m).n;}
  private static Node a(int i,List<Node> ns,int m){
    return new Node(new N(i),new N(m),ns);
    }
  public static void main(String[] args) {
    Node _3=a(3,List.of(),1);
    Node node=a(5,List.of(_3,_3,_3),2);
    {C c=new Algo0().apply(null,node);
    System.out.println( ((N)c.r._o).n);}
    Node combo=a(1000,List.of(node,node,_3,_3),2);
    {C c=new Algo0().apply(null,combo);
    System.out.println( ((N)c.r._o).n);}
    }
  }

  /*
  C= Cache, I input M medium O out
  D= ?node?
  
  
  */