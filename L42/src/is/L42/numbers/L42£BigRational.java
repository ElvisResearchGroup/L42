package is.L42.numbers;

import static is.L42.tools.General.unreachable;

import java.io.Reader;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import is.L42.cache.L42CacheMap;
import is.L42.cache.nativecache.ValueCache;
import is.L42.platformSpecific.javaTranslation.L42NoFields;

public final class L42£BigRational extends L42NoFields<L42£BigRational> implements Comparable<L42£BigRational>{
  @Override public int hashCode() {
    int result =31 + den.hashCode();
    return 31 * result + num.hashCode();
    }
  @Override public boolean equals(Object obj) {
    if(getClass()!=obj.getClass()){return false;}
    return eq((L42£BigRational) obj);
    }
  public boolean eq(L42£BigRational other){
    if(this==other){return true;}
    if(!den.equals(other.den)){return false;}
    if(!num.equals(other.num)){return false;}
    return true;
    }
  public final BigInteger num;
  public final BigInteger den;
  public final static L42£BigRational ZERO = new L42£BigRational(BigInteger.ZERO,BigInteger.ONE);
  public final static L42£BigRational ONE = new L42£BigRational(BigInteger.ONE,BigInteger.ONE);
  private L42£BigRational(BigInteger num, BigInteger den){this.num=num;this.den=den;}
  public static L42£BigRational from(BigInteger num){return new L42£BigRational(num,BigInteger.ONE);}
  public static L42£BigRational from(BigInteger num,BigInteger den){return normalize(num, den);}
  public static L42£BigRational from(long num,long den){return from(BigInteger.valueOf(num),BigInteger.valueOf(den));}
  public static L42£BigRational from(String s){
    assert s != null;
    assert s.length() != 0;
    assert s.equals(s.trim());
    int i = s.indexOf('/');
    if(i<0){assert i==-1; return noSlash(s);}
    String sn = s.substring(0, i);
    String sd = s.substring(i + 1, s.length());
    if(s.indexOf(".")<0){// both integers
      return from(new BigInteger(sn), new BigInteger(sd));
      }
    return L42£BigRational.noSlash(sn).divide(L42£BigRational.noSlash(sd));
    }
  private static L42£BigRational noSlash(String s) {
    int i = s.indexOf('.');
    if (i < 0) {assert i==-1;return new L42£BigRational(new BigInteger(s),BigInteger.ONE);}
    //we have a . to worry about
    int pow=s.length()-i;
    String sDen="1";
    for(int j=0;j<pow;j++){sDen+="0";}
    BigInteger den=new BigInteger(sDen);
    String sNum=//s.replace(".","");//would tolerate more dots
      s.substring(0, i)+s.substring(i+1,s.length());
    assert sNum.indexOf(".")==-1;
    BigInteger num=new BigInteger(sNum);
    return L42£BigRational.from(num,den);
    }
  @Override public String toString() {
    if (den.equals(BigInteger.ONE)){return num.toString();}
    StringBuffer s = new StringBuffer();
    s.append(num);
    s.append("/");
    s.append(den);
    return s.toString();
    }
  public double doubleValue() {
    double num=this.num.doubleValue();
    double den=this.den.doubleValue();
    return num/den;
    }
 /**
  * Rational number reduction to lowest terms.
  * @param n BigInteger.
  * @param d BigInteger.
  * @return a/b ~ n/d, gcd(a,b) = 1, b > 0.
  */
  public static L42£BigRational normalize(BigInteger n, BigInteger d){
    if(n.equals(BigInteger.ZERO)){return ZERO;}
    if(n.equals(d)){return ONE;}
    BigInteger c = n.gcd(d);
    if (d.signum() ==-1) { n=n.negate(); d=d.negate();}
    assert d.signum()!=-1;
    if (c.equals(BigInteger.ONE)) { return new L42£BigRational(n,d);}//sign
    return new L42£BigRational(n.divide(c), d.divide(c));
    }
  public L42£BigRational abs(){
    if(this.signum()>=0){return this;}
    return this.negate();
    }
  @Override public int compareTo(L42£BigRational s){
    if(this.equals(ZERO)){return -s.signum();}
    if(s.equals(ZERO)){return this.signum();}
    int signum=(num.signum()-s.num.signum())/2; //either -1,0,1
    if(signum!=0){return signum;}
    return num.multiply(s.den).compareTo(den.multiply(s.num));
    }
 /**
  * Rational number difference.
  * @param S BigRational.
  * @return this-S.
  */
  public L42£BigRational subtract(L42£BigRational S){return this.sum(S.negate());}
 /**
  * Rational number inverse.
  * @return 1/this.
  */
  public L42£BigRational inverse(){
    if(num.signum()>=0){return new L42£BigRational(den,num);}
    return new L42£BigRational(den.negate(),num.negate());
    }
 /**
  * Rational number negative.
  * @return -this.
  */
  public L42£BigRational negate(){return new L42£BigRational(num.negate(), den);}
 /**
  * Rational number product.
  * @param s BigRational.
  * @return this*s.
  */
  public L42£BigRational multiply(L42£BigRational s){
    if(this.equals(ZERO)||s.equals(ZERO)){return ZERO;}
    boolean denEq1=den.equals(BigInteger.ONE);
    boolean sDenEq1=s.den.equals(BigInteger.ONE);
    if(denEq1 && sDenEq1){return new L42£BigRational(num.multiply(s.num), BigInteger.ONE);}
    BigInteger div1=num.equals(s.den)?num:num.gcd(s.den);
    if(denEq1){return multiplyAux(div1,this,s);}
    BigInteger div2=s.num.equals(den)?s.num:s.num.gcd(den);
    if(sDenEq1){return multiplyAux(div2,s,this);}
    BigInteger numRes=num.divide(div1).multiply(s.num.divide(div2));
    BigInteger denRes=den.divide(div2).multiply(s.den.divide(div1));
    return new L42£BigRational(numRes,denRes);
    }
  private final static L42£BigRational multiplyAux(BigInteger div,L42£BigRational a,L42£BigRational b){
    return new L42£BigRational(a.num.divide(div).multiply(b.num),b.den.divide(div));    
    }  
 /**
  * Rational number quotient.
  * @param S BigRational.
  * @return this/S.
  */
  public L42£BigRational divide(L42£BigRational S){return multiply(S.inverse());}
 /**
  * Rational number sign.
  */
  public int signum(){return num.signum();}
 /**
  * Rational number sum.
  * @param s BigRational.
  * @return this+S.
  */
  public L42£BigRational sum(L42£BigRational s){
    if(this.equals(ZERO)){return s;}
    if(s.equals(ZERO)){return this;}
    final boolean denEq1=den.equals(BigInteger.ONE);
    final boolean sDenEq1=s.den.equals(BigInteger.ONE);
    if(denEq1 && sDenEq1){return new L42£BigRational(num.add(s.num),BigInteger.ONE);}
    if(denEq1){return new L42£BigRational(num.multiply(s.den).add(s.num), s.den);}
    if(sDenEq1){return new L42£BigRational(den.multiply(s.num).add(num), den);}
    final BigInteger div =den.equals(s.den)?den:den.gcd(s.den);
    if(div.equals(BigInteger.ONE)){
      final BigInteger denRes=den.multiply(s.den);
      final BigInteger numRes=num.multiply(s.den).add(denRes);
      if(numRes.equals(BigInteger.ZERO)){return ZERO;}
      return new L42£BigRational(numRes,denRes);
      }
   final BigInteger mul=s.den.divide(div);
   BigInteger numRes = num.multiply(mul).add(den.divide(div).multiply(s.num));
   if(numRes.equals(BigInteger.ZERO)){return ZERO;}
   final BigInteger E=numRes.equals(div)?div:numRes.gcd(div);
   if(E.equals(BigInteger.ONE)){return new L42£BigRational(numRes,den.multiply(mul));}
   numRes=numRes.divide(E);
   return new L42£BigRational(numRes,den.divide(E).multiply(mul));
   }
   public long bitLength() {
     assert den.signum()==1;
     int toAdd=(num.signum() < 0)?3:2;
     return num.bitLength()+den.bitLength()+toAdd;
     }
  public static final Class<L42£BigRational> _class=L42£BigRational.class;
  public static final MetaCache myCache=new MetaCache();
  static{L42CacheMap.addCachableType_synchronized(L42£BigRational.class,myCache);}
  @Override public MetaCache myCache(){return myCache;}
  @Override public L42£BigRational newInstance(){throw unreachable();}
  }
class MetaCache extends ValueCache<L42£BigRational>{
  @Override public String typename() {return "Meta";}
  @Override protected boolean valueCompare(L42£BigRational t1, L42£BigRational t2) {return t1.eq(t2);}
  }