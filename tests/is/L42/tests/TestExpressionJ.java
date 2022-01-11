package is.L42.tests;

import static is.L42.tools.General.L;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import is.L42.common.Err;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.tools.AtomicTest;
import is.L42.translationToJava.J;

public class TestExpressionJ
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
  je("Any<:class Any","L42Any.pathInstance")
  ),new AtomicTest(()->
  je("Library<:class Any","L42£Library.pathInstance")
  ),new AtomicTest(()->
  je("Void<:class Any","L42£Void.pathInstance")
  ),new AtomicTest(()->
  je("This0.A<:class This0.A","£cA£i1.pathInstance")
  ),new AtomicTest(()->
  je("This0.B<:class This0.B","£cB£i1.pathInstance")
  ),new AtomicTest(()->
  je("This0.A<:class Any","£cA£i1.pathInstance")
  ),new AtomicTest(()->
  je("This0.B<:class Any","£cB£i1.pathInstance")
  ),new AtomicTest(()->
  je("void","L42£Void.instance")
  ),new AtomicTest(()->
  je("{#norm{}}","Resources.ofLib(0)")
    ),new AtomicTest(()->
  je("{#typed{}}","Resources.ofLib(0)")
    ),new AtomicTest(()->
  je("This0.A<:class This0.A.ma(a=This0.A<:class This0.A.of())","£cA£i1.£mma£xa(£cA£i1.pathInstance,£cA£i1.£mof(£cA£i1.pathInstance))")
    ),new AtomicTest(()->
  je("This0.B<:class This0.B.mb(b=This0.B<:class This0.B.of())","£cB£i1.£mmb£xb(£cB£i1.pathInstance,£cB£i1.£mof(£cB£i1.pathInstance))")
    ),new AtomicTest(()->
  je("loop void","switch(0){default->{if(false)yield Resources.throwE(null);while(true){Object loopVar1=L42£Void.instance;}}}")
    ),new AtomicTest(()->
  je("return void","Resources.throwE(new L42Return(L42£Void.instance))")
  //op update needs blocks
  ),new AtomicTest(()->
  je("(void)","L42£Void.instance")
  ),new AtomicTest(()->
  je("(This0.A a=This0.A<:class This0.A.of() a)","""
    switch(0){default->{
      £cA£i1 £xa=null;
      £xa=£cA£i1.£mof(£cA£i1.pathInstance);
      yield £xa;
      }}
    """)
  ),new AtomicTest(()->
  je("(var This0.A a=This0.A<:class This0.A.of() a:=a)","""
    switch(0){default->{
      £cA£i1 £xa=null;
      £xa=£cA£i1.£mof(£cA£i1.pathInstance);
      yield Resources.toVoid(£xa=£xa);
      }}
    """)
  ),new AtomicTest(()->//template, next tests will be similar
  je("""
   (var This0.A a=This0.A<:class This0.A.of()
    This0.A a0=a
    a.ma(a=a)
    )""","""
    switch(0){default->{
      £cA£i1 £xa=null;
      £cA£i1 £xa0=null;
      £xa=£cA£i1.£mof(£cA£i1.pathInstance);
      £xa0=£xa;
      yield £cA£i1.£mma£xa(£xa,£xa);
      }}
    """)
  ),new AtomicTest(()->//using Any for a0
  je("""
   (var This0.A a=This0.A<:class This0.A.of()
    Any a0=a
    a.ma(a=a)
    )""","""
    switch(0){default->{
      £cA£i1 £xa=null;
      L42Any £xa0=null;
      £xa=£cA£i1.£mof(£cA£i1.pathInstance);
      £xa0=£xa;
      yield £cA£i1.£mma£xa(£xa,£xa);
      }}
    """)
  ),new AtomicTest(()->//using B instead of A
  je("""
   (var This0.B b=This0.B<:class This0.B.of()
    This0.B b0=b
    b.mb(b=b)
    )""","""
    switch(0){default->{
      String £xb=null;
      String £xb0=null;
      £xb=£cB£i1.£mof(£cB£i1.pathInstance);
      £xb0=£xb;
      yield £cB£i1.£mmb£xb(£xb,£xb);
      }}
    """)
  ),new AtomicTest(()->//B instead of A, Any for b0
  je("""
   (var This0.B b=This0.B<:class This0.B.of()
    Any b0=b
    b.mb(b=b)
    )""","""
    switch(0){default->{
      String £xb=null;
      L42Any £xb0=null;
      £xb=£cB£i1.£mof(£cB£i1.pathInstance);
      £xb0=£cB£i1.wrap(£xb);
      yield £cB£i1.£mmb£xb(£xb,£xb);
      }}
    """)
  ),new AtomicTest(()->//throwing a B and an A a Void
  je("""
   (This0.A a=This0.A<:class This0.A.of()
    This0.B b=This0.B<:class This0.B.of()
    Void v0=error a
    Void v1=error b
    Void v2=error void
    void
    )""","""
    switch(0){default->{
      £cA£i1 £xa=null;
      String £xb=null;
      L42£Void £xv0=null;
      L42£Void £xv1=null;
      L42£Void £xv2=null;
      £xa=£cA£i1.£mof(£cA£i1.pathInstance);
      £xb=£cB£i1.£mof(£cB£i1.pathInstance);
      £xv0=Resources.throwE(new L42Error(£xa));
      £xv1=Resources.throwE(new L42Error(£cB£i1.wrap(£xb)));
      £xv2=Resources.throwE(new L42Error(L42£Void.instance));
      yield L42£Void.instance;
      }}
    """)

  ),new AtomicTest(()->//using fwds
  je("""
   (This0.A a=This0.A<:class This0.A.of()
    This0.A a0=a.ma(a=a0)
    a0
    )""","""
    switch(0){default->{
      £cA£i1 £xa=null;
      £cA£i1 £xa0=null;
      £cA£i1 £xa0£fwd=£cA£i1.NewFwd();
      £xa=£cA£i1.£mof(£cA£i1.pathInstance);
      £xa0=£cA£i1.£mma£xa(£xa,£xa0£fwd);
      ((L42Fwd)£xa0£fwd).fix(£xa0);
      yield £xa0;
      }}
    """)
  ),new AtomicTest(()->//using fwds
  je("""
   (This0.B b=This0.B<:class This0.B.of()
    This0.B b0=b.mb(b=b0)
    b0
    )""","""
    switch(0){default->{
      String £xb=null;
      String £xb0=null;
      Object £xb0£fwd=£cB£i1.NewFwd();
      £xb=£cB£i1.£mof(£cB£i1.pathInstance);
      £xb0=£cB£i1.£mmb£xb(£xb,£xb0£fwd);
      ((L42Fwd)£xb0£fwd).fix(£xb0);
      yield £xb0;
      }}
    """)

  ),new AtomicTest(()->//catch
  je("""
   (This0.B b=This0.B<:class This0.B.of()
    This0.B b0=b.mb(b=b0)
    catch error This0.B x x
    b0
    )""","""
    switch(0){default->{
      String £xb=null;
      String £xb0=null;
      Object £xb0£fwd=£cB£i1.NewFwd();
      try{
        £xb=£cB£i1.£mof(£cB£i1.pathInstance);
        £xb0=£cB£i1.£mmb£xb(£xb,£xb0£fwd);
        ((L42Fwd)£xb0£fwd).fix(£xb0);
        }
      catch(L42Error catchVar0){
        if(catchVar0.obj42() instanceof £cB£i1){
          String £xx=((£cB£i1)catchVar0.obj42()).unwrap;
          yield £xx;
          }
        throw catchVar0;
        }
      yield £xb0;
      }}
    """
    //eclipse bug: if I put umbalanced "}" in the code literal, the parser may fails. It also fails if the same multiline string is embedded in /* */
    )


//jc
  ),new AtomicTest(()->
  jc("""
     """,
     """
     """,
     """
     public class £cN£i1 implements L42Any,L42Cachable<£cN£i1>{
       [###]
       public static int £mof(£cN£i1 £xthis){
         return 0;
         }
       public static int £msum£xthat(int £xthis, int £xthat){
         return £xthis + £xthat;
         }
       public static £cN£i1 NewFwd(){return new _Fwd();}
       public static class _Fwd extends £cN£i1 implements L42Fwd{
         private List<Object> os=new ArrayList<>();
         private List<BiConsumer<Object,Object>> fs=new ArrayList<>();
         public List<Object> os(){return os;}
         public List<BiConsumer<Object,Object>> fs(){return fs;}
         public L42ClassAny asPath(){return Resources.ofPath(0);}
         @Override public L42Cache<£cN£i1> myCache() {return mySCache;}}
       public static final £cN£i1 pathInstance=new _Fwd();
       static final L42Cache<£cN£i1> mySCache=[###]
       public int unwrap;
       public static £cN£i1 wrap(int that){£cN£i1 res=new £cN£i1();res.unwrap=that;return res;}
       }
     ""","""
     public class £cA£i1 implements L42Any,L42Cachable<£cA£i1>{
       [###]
       int £xn;
       public static BiConsumer<Object,Object> FieldAssFor_n=(f,o)->{((£cA£i1)o).£xn=(int)f;};
       public static £cA£i1 £mof£xn(£cA£i1 £xthis, int £xn){
         £cA£i1 Res=new £cA£i1();
         Res.£xn=£xn;
         return Res;
         }
       public static int £mn(£cA£i1 £xthis){
         return £xthis.£xn;
         }
       public static L42£Void £mn£xthat(£cA£i1 £xthis, int £xthat){
         £xthis.£xn=£xthat;return L42£Void.instance;
         }
       public static £cA£i1 NewFwd(){return new _Fwd();}
       public static class _Fwd extends £cA£i1 implements L42Fwd{
         private List<Object> os=new ArrayList<>();
         private List<BiConsumer<Object,Object>> fs=new ArrayList<>();
         public List<Object> os(){return os;}
         public List<BiConsumer<Object,Object>> fs(){return fs;}
         public L42ClassAny asPath(){return Resources.ofPath(0);}
         @Override public L42Cache<£cA£i1> myCache() {return mySCache;}}
       public static final £cA£i1 pathInstance=new _Fwd();
       static final L42Cache<£cA£i1> mySCache=[###]
       }
     """)
  ),new AtomicTest(()->
  jc("""
     C={ #norm{}}
     """,
     """
     public class £cC£i1 extends L42NoFields<£cC£i1> implements L42Any{
       static final Class<£cC£i1> _class=£cC£i1.class;
       public static final L42Cache<£cC£i1> myCache=[###]
       @Override public L42Cache<£cC£i1> myCache(){return myCache;}
       public static £cC£i1 NewFwd(){return new _Fwd();}
       public static class _Fwd extends £cC£i1 implements L42Fwd{
         private List<Object> os=new ArrayList<>();
         private List<BiConsumer<Object,Object>> fs=new ArrayList<>();
         public List<Object> os(){return os;}
         public List<BiConsumer<Object,Object>> fs(){return fs;}
         public L42ClassAny asPath(){return Resources.ofPath(0);}
         @Override public L42Cache<£cC£i1> myCache() {return mySCache;}}
       public static final £cC£i1 pathInstance=new _Fwd();
       static final L42Cache<£cC£i1> mySCache=[###]
       }
     ""","","","")
  ),new AtomicTest(()->
  jc("""
     C={ 
       class method mut This0 of(fwd imm This1.N n)
       method This1.N n()
       #norm{typeDep=This This1.N}}
     ""","""
     public class £cC£i1 implements L42Any,L42Cachable<£cC£i1>{
       [###]
       int £xn;
       public static BiConsumer<Object,Object> FieldAssFor_n=(f,o)->{((£cC£i1)o).£xn=(int)f;};
       public static £cC£i1 £mof£xn(£cC£i1 £xthis, Object £xn){
         £cC£i1 Res=new £cC£i1();
         if(£xn instanceof L42Fwd && !(((Object)£xn ) instanceof L42NoFields<?>)){((L42Fwd)£xn).rememberAssign(Res,£cC£i1.FieldAssFor_n);}else{Res.£xn=(int)£xn;}
         return Res;
         }
       public static int £mn(£cC£i1 £xthis){
         return £xthis.£xn;
         }
       public static £cC£i1 NewFwd(){return new _Fwd();}
       public static class _Fwd extends £cC£i1 implements L42Fwd{
         private List<Object> os=new ArrayList<>();
         private List<BiConsumer<Object,Object>> fs=new ArrayList<>();
         public List<Object> os(){return os;}
         public List<BiConsumer<Object,Object>> fs(){return fs;}
         public L42ClassAny asPath(){return Resources.ofPath(0);}
         @Override public L42Cache<£cC£i1> myCache() {return mySCache;}}
       public static final £cC£i1 pathInstance=new _Fwd();
       static final L42Cache<£cC£i1> mySCache=[###]
       }
     ""","","","")

  ));}
public static void je(String e,String out){
  String l="{ method Void m()="+e+" "+"""
    A={
      class method This0 of()
      method Void ma(This0 a)
      #norm{typeDep=This}
      }
    B={
      class method This0 of()
      method Void mb(This0 b)
      #norm{nativeKind=String typeDep=This,This1.PE,watched=This1.PE, nativePar=This1.PE, coherentDep=This1.PE}
      }
    PE={#norm{nativeKind=LazyMessage}}
    #norm{typeDep=This This.A This.B coherentDep=This.A This.B uniqueId=id1
      usedMethods=This0.A.of(),This0.A.ma(a),This0.B.of(),This0.B.mb(b)}}
    """;
  var p=Program.parse(l);
  J j=new J(p,G.empty(), new ArrayList<>(),false);
  j.visitE(p.topCore().mwts().get(0)._e());
  String res=j.result().toString();
  Err.strCmp(res,out);
  }
public static void jc(String e,String ...out){
  String l="{ "+e+
  """
  PE={#norm{nativeKind=LazyMessage}}
  N={
    class method This0 of()
    method This0 sum(This0 that)=native{trusted:OP+} error void
    #norm{nativeKind=Int typeDep=This,This1.PE coherentDep=This,This1.PE, nativePar=This1.PE}}
  A={
    class method mut This0 of(This1.N n)
    method This1.N n()
    mut method Void n(This1.N that)
    #norm{typeDep=This This1.N}} 
  #norm{uniqueId=id1}}
  """;
  var p=Program.parse(l);
  List<String> res=L(p.topCore().ncs(),(c,nc)->{
    var pi=p.push(nc.key());
    J j=new J(pi,G.empty(),new ArrayList<>(),false);
    j.mkClass();
    c.add(j.result().toString());
    });
  L(res,List.of(out),(c,r,o)->c.add(o.isEmpty() || Err.strCmp(r,o)));
  }
}