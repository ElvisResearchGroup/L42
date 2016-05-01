package is.L42.connected.withNumbers;

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



public final class BigRational implements Comparable<BigRational>{
    @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((den == null) ? 0 : den.hashCode());
    result = prime * result + ((num == null) ? 0 : num.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BigRational other = (BigRational) obj;
    if (den == null) {
      if (other.den != null)
        return false;
    } else if (!den.equals(other.den))
      return false;
    if (num == null) {
      if (other.num != null)
        return false;
    } else if (!num.equals(other.num))
      return false;
    return true;
  }




    public final BigInteger num;
    public final BigInteger den;
    public final static BigRational ZERO = new BigRational(BigInteger.ZERO,BigInteger.ONE);
    public final static BigRational ONE = new BigRational(BigInteger.ONE,BigInteger.ONE);
    //public final static BigRational POSINFINITE = new BigRational(BigInteger.ONE,BigInteger.ZERO);
    //public final static BigRational NEGINFINITE = new BigRational(BigInteger.ONE.negate(),BigInteger.ZERO);
    //they should never happen
    private BigRational(BigInteger num, BigInteger den) {     this.num = num; this.den = den;   }

    public static BigRational from(BigInteger num) {   return new BigRational(num,BigInteger.ONE); }

    public static BigRational from (BigInteger num, BigInteger den) {   return normalize(num, den);    }

    public static BigRational from (long num, long den) {
        return from(BigInteger.valueOf(num),BigInteger.valueOf(den));
     }

    /**
     * @param s String.
     * @throws NumberFormatException
     */
    public static BigRational from(String s){
        assert s != null;
        assert s.length() != 0;
        assert s.equals(s.trim());
        int i = s.indexOf('/');
        if (i < 0) { assert i==-1; return noSlash(s);}
       String sn = s.substring(0, i);
       String sd = s.substring(i + 1, s.length());
       if (s.indexOf(".") < 0) { // both integers
         return from(new BigInteger(sn), new BigInteger(sd));
       }
       return BigRational.noSlash(sn).divide(BigRational.noSlash(sd));
    }
   private static BigRational noSlash(String s) {
      int i = s.indexOf('.');
      if (i < 0) {assert i==-1;
        return new BigRational(new BigInteger(s),BigInteger.ONE);
      }
      //we have a . to worry about
      int pow=s.length()-i;
      String sDen="1";
      for(int j=0;j<pow;j++){ sDen+="0";}
      BigInteger den=new BigInteger(sDen);
      String sNum=//s.replace(".","");//would tollerate more dots
      s.substring(0, i)+s.substring(i+1,s.length());
      assert sNum.indexOf(".")==-1;
      BigInteger num=new BigInteger(sNum);
      return BigRational.from(num,den);
    }

    /**
     * Get the string representation.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
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

    public static BigRational normalize(BigInteger n, BigInteger d) {
        if (n.equals(BigInteger.ZERO)) { return ZERO;}
        if (n.equals(d)) { return ONE;}
        BigInteger c = n.gcd(d);
        if (d.signum() ==-1) { n=n.negate(); d=d.negate();}
        assert d.signum()!=-1;
        if (c.equals(BigInteger.ONE)) { return new BigRational(n,d);}//sign
        return new BigRational(n.divide(c), d.divide(c));
    }


    public BigRational abs() {
        if (this.signum() >= 0) {
            return this;
        }
        return this.negate();
    }

    @Override
    public int compareTo(BigRational S) {
        BigInteger J2Y;
        BigInteger J3Y;
        BigInteger R1;
        BigInteger R2;
        BigInteger S1;
        BigInteger S2;
        int J1Y;
        int SL;
        int TL;
        int RL;
        if (this.equals(ZERO)) {
            return -S.signum();
        }
        if (S.equals(ZERO)) {
            return this.signum();
        }
        R1 = num; //this.numerator();
        R2 = den; //this.denominator();
        S1 = S.num;
        S2 = S.den;
        RL = R1.signum();
        SL = S1.signum();
        J1Y = (RL - SL);
        TL = (J1Y / 2);
        if (TL != 0) {
            return TL;
        }
        J3Y = R1.multiply(S2);
        J2Y = R2.multiply(S1);
        TL = J3Y.compareTo(J2Y);
        return TL;
    }


    /**
     * Rational number difference.
     * @param S BigRational.
     * @return this-S.
     */
    public BigRational subtract(BigRational S) {
        return this.sum(S.negate());
    }


    /**
     * Rational number inverse.
     * @return 1/this.
     * @see edu.jas.structure.RingElem#inverse()
     */
    public BigRational inverse() {
        BigInteger R1 = num;
        BigInteger R2 = den;
        BigInteger S1;
        BigInteger S2;
        if (R1.signum() >= 0) {
            S1 = R2;
            S2 = R1;
        } else {
            S1 = R2.negate();
            S2 = R1.negate();
        }
        return new BigRational(S1, S2);
    }



    /**
     * Rational number negative.
     * @return -this.
     * @see edu.jas.structure.RingElem#negate()
     */
    public BigRational negate() {
        BigInteger n = num.negate();
        return new BigRational(n, den);
    }


    /**
     * Rational number product.
     * @param S BigRational.
     * @return this*S.
     */
    public BigRational multiply(BigRational S) {
        BigInteger D1 = null;
        BigInteger D2 = null;
        BigInteger R1 = null;
        BigInteger R2 = null;
        BigInteger RB1 = null;
        BigInteger RB2 = null;
        BigInteger S1 = null;
        BigInteger S2 = null;
        BigInteger SB1 = null;
        BigInteger SB2 = null;
        BigRational T;
        BigInteger T1;
        BigInteger T2;
        if (this.equals(ZERO) || S.equals(ZERO)) {
            T = ZERO;
            return T;
        }
        R1 = num; //this.numerator();
        R2 = den; //this.denominator();
        S1 = S.num;
        S2 = S.den;
        if (R2.equals(BigInteger.ONE) && S2.equals(BigInteger.ONE)) {
            T1 = R1.multiply(S1);
            T = new BigRational(T1, BigInteger.ONE);
            return T;
        }
        if (R2.equals(BigInteger.ONE)) {
            if (R1.equals(S2)) {
                D1 = R1;
            } else {
                D1 = R1.gcd(S2);
            }
            RB1 = R1.divide(D1);
            SB2 = S2.divide(D1);
            T1 = RB1.multiply(S1);
            T = new BigRational(T1, SB2);
            return T;
        }
        if (S2.equals(BigInteger.ONE)) {
            if (S1.equals(R2)) {
                D2 = S1;
            } else {
                D2 = S1.gcd(R2);
            }
            SB1 = S1.divide(D2);
            RB2 = R2.divide(D2);
            T1 = SB1.multiply(R1);
            T = new BigRational(T1, RB2);
            return T;
        }
        if (R1.equals(S2)) {
            D1 = R1;
        } else {
            D1 = R1.gcd(S2);
        }
        RB1 = R1.divide(D1);
        SB2 = S2.divide(D1);
        if (S1.equals(R2)) {
            D2 = S1;
        } else {
            D2 = S1.gcd(R2);
        }
        SB1 = S1.divide(D2);
        RB2 = R2.divide(D2);
        T1 = RB1.multiply(SB1);
        T2 = RB2.multiply(SB2);
        T = new BigRational(T1, T2);
        return T;
    }



    /**
     * Rational number quotient.
     * @param S BigRational.
     * @return this/S.
     */
    public BigRational divide(BigRational S) {
        return multiply(S.inverse());
    }




    /**
     * Rational number sign.
     * @see edu.jas.structure.RingElem#signum()
     */
    public int signum() {
        return num.signum();
    }


    /**
     * Rational number sum.
     * @param S BigRational.
     * @return this+S.
     */
    public BigRational sum(BigRational S) {
        BigInteger D = null;
        BigInteger E, J1Y, J2Y;
        BigRational T;
        BigInteger R1 = null;
        BigInteger R2 = null;
        BigInteger RB2 = null;
        BigInteger S1 = null;
        BigInteger S2 = null;
        BigInteger SB2 = null;
        BigInteger T1;
        BigInteger T2;
        if (this.equals(ZERO)) {
            return S;
        }
        if (S.equals(ZERO)) {
            return this;
        }
        R1 = num; //this.numerator();
        R2 = den; //this.denominator();
        S1 = S.num;
        S2 = S.den;
        if (R2.equals(BigInteger.ONE) && S2.equals(BigInteger.ONE)) {
            T1 = R1.add(S1);
            T = new BigRational(T1, BigInteger.ONE);
            return T;
        }
        if (R2.equals(BigInteger.ONE)) {
            T1 = R1.multiply(S2);
            T1 = T1.add(S1);
            T = new BigRational(T1, S2);
            return T;
        }
        if (S2.equals(BigInteger.ONE)) {
            T1 = R2.multiply(S1);
            T1 = T1.add(R1);
            T = new BigRational(T1, R2);
            return T;
        }
        if (R2.equals(S2)) {
            D = R2;
        } else {
            D = R2.gcd(S2);
        }
        if (D.equals(BigInteger.ONE)) {
            RB2 = R2;
            SB2 = S2;
        } else {
            RB2 = R2.divide(D);
            SB2 = S2.divide(D);
        }
        J1Y = R1.multiply(SB2);
        J2Y = RB2.multiply(S1);
        T1 = J1Y.add(J2Y);
        if (T1.equals(BigInteger.ZERO)) {
            return ZERO;
        }
        if (!D.equals(BigInteger.ONE)) {
            if (T1.equals(D)) {
                E = D;
            } else {
                E = T1.gcd(D);
            }
            if (!E.equals(BigInteger.ONE)) {
                T1 = T1.divide(E);
                R2 = R2.divide(E);
            }
        }
        T2 = R2.multiply(SB2);
        T = new BigRational(T1, T2);
        return T;
    }




    /**
     * Returns the number of bits in the representation of this BigRational,
     * including a sign bit. For positive BigRational, this is equivalent to
     * {@code num.bitLength()+den.bitLength()}.)
     * @return number of bits in the representation of this BigRational,
     *         including a sign bit.
     */
    public long bitLength() {
        long n = num.bitLength();
        if (num.signum() < 0) {
            n++;
        }
        n++;
        n += den.bitLength();
        assert den.signum() ==1;
        n++;
        return n;
    }
}