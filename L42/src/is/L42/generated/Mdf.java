package is.L42.generated;
import java.util.List;
import java.util.Arrays;
import static is.L42.tools.General.*;

public enum Mdf {
  Immutable("imm"),
  Mutable("mut"),
  Readable("read"),
  Lent("lent"),
  Capsule("capsule"),
  Class("class"),
  ImmutableFwd("fwd imm"),
  ImmutablePFwd("fwd%imm"),
  MutableFwd("fwd mut"),
  MutablePFwd("fwd%mut");
  public final String inner;
  Mdf(String inner) {this.inner = inner;}
  public boolean isImm() {return this==Mdf.Immutable;}
  public boolean isMut() {return this==Mdf.Mutable;}
  public boolean isRead() {return this==Mdf.Readable;}
  public boolean isLent() {return this==Mdf.Lent;}
  public boolean isCapsule() {return this==Mdf.Capsule;}
  public boolean isClass() {return this==Mdf.Class;}
  public boolean isFwdImm() {return this==Mdf.ImmutableFwd;}
  public boolean isFwdPImm() {return this==Mdf.ImmutablePFwd;}
  public boolean isFwdMut() {return this==Mdf.MutableFwd;}
  public boolean isFwdPMut() {return this==Mdf.MutablePFwd;}
  public boolean isIn(Mdf a,Mdf b) {return this==a || this==b;}
  public boolean isIn(Mdf a,Mdf b,Mdf c) {return this==a || this==b ||this==c;}
  public boolean isIn(Mdf a,Mdf b,Mdf c,Mdf d) {return this==a || this==b ||this==c || this==d;}
  public static Mdf fromString(String s) {
   for (Mdf mdf : Mdf.values()) {if (mdf.inner.equals(s))return mdf;}
   throw bug();
    }
  public static List<Mdf> muts=Arrays.asList(Mdf.Mutable,Mdf.MutablePFwd,Mdf.MutableFwd);
  public static List<Mdf> imms=Arrays.asList(Mdf.Immutable,Mdf.ImmutablePFwd,Mdf.ImmutableFwd);
  }