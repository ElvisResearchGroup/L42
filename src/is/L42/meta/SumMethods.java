package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;

import java.util.ArrayList;
import java.util.List;

import is.L42.generated.Core.L.MWT;

//92 wither too
/* summing methods for classes (not interfaces)
 * Used in some meta operations;
 * Sum uses a more general algorithms to cover also interfaces
 */
public record SumMethods(MetaError err){
  //as first,bs last or merged with an element in as
  List<MWT> sum(List<MWT>as, List<MWT>bs){
    var oldMWTs=new ArrayList<MWT>();
    var newMWTs=new ArrayList<MWT>();
    for(MWT bi:bs){
      MWT ai=_elem(as,bi.key());
      if(ai==null){newMWTs.add(bi);}
      else{
        oldMWTs.add(sum(ai,bi));
        }
      }
    List<MWT> mwts=L(c->{
      for(MWT ai:as){
        MWT overridden=_elem(oldMWTs,ai.key());
        if(overridden==null){c.add(ai);}
        else{c.add(overridden);}
        }
      c.addAll(newMWTs);
      });
    return mwts;
    }
  //TODO: we may want to add a 'weak b' option, where b implementation may be ignored/auto-overridden
  private MWT sum(MWT ai, MWT bi){//Warning: this only make sense if the methods are part of classes/not interfaces
    boolean eqMH=Utils.equalMH(ai.mh(),bi.mh());
    boolean abs1=ai._e()==null;
    boolean abs2=bi._e()==null;
    if(!abs1 && !abs2){
      if(true)throw new NullPointerException();
      err.throwErr(ai,Sum.Plus.errConflict);}
    if(!eqMH){err.throwErr(ai,"The methods have different signatures:\n"+err.intro(bi,false));}
    if(abs2){return Utils.accDoc(ai,bi);} //this keeps ai body
    return Utils.accDoc(ai,bi).with_e(bi._e()).withNativeUrl(bi.nativeUrl());
    }  
  }