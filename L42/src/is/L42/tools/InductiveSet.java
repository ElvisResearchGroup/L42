package is.L42.tools;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
public class InductiveSet<K,R> implements BiConsumer<K,InductiveSet.IRule<K,R>>{
  public interface BRule<K,R>{ void op(K k, Consumer<R> set,BiConsumer<K,IRule<K,R>>register);}
  public interface IRule<K,R>{void op(R r);}
  BRule<K,R> bRule;
  public InductiveSet(BRule<K,R> bRule){this.bRule=bRule;}
  public Set<R> compute(K k){
    Set<R> mRes=memoizedMap.get(k);
    if(mRes!=null){return mRes;}
    assert !isComputing;//need to be used single-threaded
    isComputing=true;
    assert progressMap.isEmpty();
    var resRec=new Record<K,R>();
    progressMap.put(k,resRec);
    bRule.op(k,resRec,this);
    var res=progressMap.get(k);
    assert res!=null;
    for(var novel:progressMap.entrySet()){
      assert !memoizedMap.containsKey(novel.getKey());
      memoizedMap.put(novel.getKey(),novel.getValue().res);
      }
    return res.res;
    } 
  private static class Record<K,R> implements Consumer<R>{
    Set<R> res=new HashSet<>();
    List<IRule<K,R>> rules=new ArrayList<>();
    @Override public void accept(R r) {
      boolean added=res.add(r);
      if(added){
        for(var rule:rules){rule.op(r);}
        }
      }
    public void register(IRule<K,R>iRule){
      for(R r:res){iRule.op(r);}
      rules.add(iRule);
      }    
    }
  boolean isComputing=false;
  Map<K,Record<K,R>> progressMap=new HashMap<>();
  Map<K,Set<R>> memoizedMap=new HashMap<>();
  @Override public void accept(K ki, IRule<K, R> iR) {
    var res0=new Record<K,R>();
    var resi=progressMap.putIfAbsent(ki,res0);
    if(resi==null){
      resi=res0;
      bRule.op(ki,resi,this);
      }
    resi.register(iR);
    }
  }
/*  
TENTATIVE DESIGN:  
    
user provides the following functions:
  base(x)->xz
  f1(xz)->xz'  g1(x)->xz
  ..
  fn(xz)->xz'  gn(x)->xz

---------------base(x)=xz
  xz in op(x)
  
  
  xz1 in op(x1) .. xzn in op(xn)
--------------------------------- gi(x)=x1..xn  
  xz in op(x)                     xz=fi(xz1..xzn)
  

For example, here is a conventional multiply recursive inductive definition of sets
 {i} in mod(i) if i in {0,1}
 {} in mod(i) otherwise 
 i+1 in mod(i) if i in mod((i+1)%2) and i<1000

mod(0) is all the even numbers less then 1000,
mod(1) is all the odd numbers less then 1000 and
mod(i) for i>1 is always an empty set.

We can encode such definition as following:

base:1->{1},0->{0}
inductive:
  f({i})->{i+1} if i<1000
  f({i})->{}    if i>=1000
  g(i)->(i+1)%2

As you can see, the idea is that we need to build at the same time the set of
even and odd numbers less then 1000, no matter if we start asking
for the set of evens or the set of odds.

INFORMAL TEXT:
I'm trying to build a general algorithm to compute an inductive set from a set of rules, but I can not figure out a concrete algorithm. It is getting to be quite of a puzzle!

Notation: x is an 'element' and xz is a set of elements.

The idea is that the user provides the following functions:
  base(x)->xz //given an element, produce a set of elements
  f1(xz)->xz'  g1(x)->xz //pair of functions f and g
  ..
  fn(xz)->xz'  gn(x)->xz

The input is interpreted according to the following inductive set definition:
(baseCase)
    xz in op(x)
      where base(x) = xz

(inductiveCase)
    xz in op(x)
      where gi(x) = x1..xn,   xz1 in op(x1) .. xzn in op(xn) and  xz = fi(xz1..xzn)

I'm trying to create a (possibly slow but very general) algorithm that can take an set of functions/lambdas/closures and assuming they are deterministic, it would create the resulting xz set from a starting input x.
If those lambdas are fileds for a 'compute' object, such object could also store a cache of former requests so that it is faster for the next "identical" call. This may be already needed by the algorithm since we would need to keep some form of "visited" structure.

Lets make a concrete example:

For example, here is a conventional multiply recursive inductive definition of sets
 {i} in mod(i) if i in {0,1}
 {} in mod(i) otherwise
 i+1 in mod(i) if i in mod((i+1)%2) and i<1000

mod(0) is all the even numbers less then 1000,
mod(1) is all the odd numbers less then 1000 and
mod(i) for i>1 is always an empty set.

We can encode such definition as following:

base:1->{1},0->{0}
inductive:
  f({i})->{i+1} if i<1000
  f({i})->{}    if i>=1000
  g(i)->(i+1)%2

The idea is that we need to build at the same time the set of
even and odd numbers less then 1000, no matter if we start asking
for the set of evens or the set of odds.
For other set of rules we may have to build at the same time hundreds of
different sets, and memorize the ones that are still under construction and
the ones that are now completed.
------------------------------------
Start with x:
compute base x =xz
for all rules: apply the rule on a xi in xz;
the rule can either return some xz or open a new base x=xz.
for all the rules, for all the open sets, 
  a rule can return: noAction or action.
  a rule can ask if an element see: opened sets, completed sets
  a rule can do: open a new set, add new elements to a set.
  We can assume: rules are deterministic and do not lie:
    if the do not modification, they say noAction. Otherwise they say Action.
when for an open set, all rules say noAction, then the set is promoted to complete.

   can it still be ill-founded? add foo if bars is complete; add bar if foos is complete.
  
following all the gi(x) open all the op(x) needed for this 
 
 i+2 in like(0)

f(data) in op(data,key)
f(data,e) in op(data,key)
  if e in op(data,key)

*/