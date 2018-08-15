// Generated by delombok at Wed Aug 15 16:44:10 NZST 2018
package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.Member;

public class Util {

  public static final class CsMx {
    @NonNull
    private final java.util.List<Ast.C> cs;
    @NonNull
    private final MethodSelector ms;

    public String toString() {
      String prefix = PathAux.as42Path(cs);
      return prefix + "::" + ms;
    }

    @java.lang.SuppressWarnings("all")
    public CsMx(@NonNull final java.util.List<Ast.C> cs, @NonNull final MethodSelector ms) {
      if (cs == null) {
        throw new java.lang.NullPointerException("cs is marked @NonNull but is null");
      }
      if (ms == null) {
        throw new java.lang.NullPointerException("ms is marked @NonNull but is null");
      }
      this.cs = cs;
      this.ms = ms;
    }

    @NonNull
    @java.lang.SuppressWarnings("all")
    public java.util.List<Ast.C> getCs() {
      return this.cs;
    }

    @NonNull
    @java.lang.SuppressWarnings("all")
    public MethodSelector getMs() {
      return this.ms;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof Util.CsMx)) return false;
      final Util.CsMx other = (Util.CsMx) o;
      final java.lang.Object this$cs = this.getCs();
      final java.lang.Object other$cs = other.getCs();
      if (this$cs == null ? other$cs != null : !this$cs.equals(other$cs)) return false;
      final java.lang.Object this$ms = this.getMs();
      final java.lang.Object other$ms = other.getMs();
      if (this$ms == null ? other$ms != null : !this$ms.equals(other$ms)) return false;
      return true;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final java.lang.Object $cs = this.getCs();
      result = result * PRIME + ($cs == null ? 43 : $cs.hashCode());
      final java.lang.Object $ms = this.getMs();
      result = result * PRIME + ($ms == null ? 43 : $ms.hashCode());
      return result;
    }

    @java.lang.SuppressWarnings("all")
    public CsMx withCs(@NonNull final java.util.List<Ast.C> cs) {
      if (cs == null) {
        throw new java.lang.NullPointerException("cs is marked @NonNull but is null");
      }
      return this.cs == cs ? this : new CsMx(cs, this.ms);
    }

    @java.lang.SuppressWarnings("all")
    public CsMx withMs(@NonNull final MethodSelector ms) {
      if (ms == null) {
        throw new java.lang.NullPointerException("ms is marked @NonNull but is null");
      }
      return this.ms == ms ? this : new CsMx(this.cs, ms);
    }
  }


  public static final class CsPath {
    @NonNull
    private final java.util.List<Ast.C> cs;
    @NonNull
    private final Path path;

    public String toString() {
      String prefix = PathAux.as42Path(cs);
      return prefix + "->" + path;
    }

    @java.lang.SuppressWarnings("all")
    public CsPath(@NonNull final java.util.List<Ast.C> cs, @NonNull final Path path) {
      if (cs == null) {
        throw new java.lang.NullPointerException("cs is marked @NonNull but is null");
      }
      if (path == null) {
        throw new java.lang.NullPointerException("path is marked @NonNull but is null");
      }
      this.cs = cs;
      this.path = path;
    }

    @NonNull
    @java.lang.SuppressWarnings("all")
    public java.util.List<Ast.C> getCs() {
      return this.cs;
    }

    @NonNull
    @java.lang.SuppressWarnings("all")
    public Path getPath() {
      return this.path;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof Util.CsPath)) return false;
      final Util.CsPath other = (Util.CsPath) o;
      final java.lang.Object this$cs = this.getCs();
      final java.lang.Object other$cs = other.getCs();
      if (this$cs == null ? other$cs != null : !this$cs.equals(other$cs)) return false;
      final java.lang.Object this$path = this.getPath();
      final java.lang.Object other$path = other.getPath();
      if (this$path == null ? other$path != null : !this$path.equals(other$path)) return false;
      return true;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final java.lang.Object $cs = this.getCs();
      result = result * PRIME + ($cs == null ? 43 : $cs.hashCode());
      final java.lang.Object $path = this.getPath();
      result = result * PRIME + ($path == null ? 43 : $path.hashCode());
      return result;
    }

    @java.lang.SuppressWarnings("all")
    public CsPath withCs(@NonNull final java.util.List<Ast.C> cs) {
      if (cs == null) {
        throw new java.lang.NullPointerException("cs is marked @NonNull but is null");
      }
      return this.cs == cs ? this : new CsPath(cs, this.path);
    }

    @java.lang.SuppressWarnings("all")
    public CsPath withPath(@NonNull final Path path) {
      if (path == null) {
        throw new java.lang.NullPointerException("path is marked @NonNull but is null");
      }
      return this.path == path ? this : new CsPath(this.cs, path);
    }
  }


  public static final class CsMxMx {
    @NonNull
    private final java.util.List<Ast.C> cs;
    private final boolean flag;
    private final MethodSelector ms1;
    private final MethodSelector ms2;

    public String toString() {
      String prefix = PathAux.as42Path(cs);
      return prefix + "[" + flag + "]" + ms1 + "->" + ms2;
    }

    @java.lang.SuppressWarnings("all")
    public CsMxMx(@NonNull final java.util.List<Ast.C> cs, final boolean flag, final MethodSelector ms1, final MethodSelector ms2) {
      if (cs == null) {
        throw new java.lang.NullPointerException("cs is marked @NonNull but is null");
      }
      this.cs = cs;
      this.flag = flag;
      this.ms1 = ms1;
      this.ms2 = ms2;
    }

    @NonNull
    @java.lang.SuppressWarnings("all")
    public java.util.List<Ast.C> getCs() {
      return this.cs;
    }

    @java.lang.SuppressWarnings("all")
    public boolean isFlag() {
      return this.flag;
    }

    @java.lang.SuppressWarnings("all")
    public MethodSelector getMs1() {
      return this.ms1;
    }

    @java.lang.SuppressWarnings("all")
    public MethodSelector getMs2() {
      return this.ms2;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof Util.CsMxMx)) return false;
      final Util.CsMxMx other = (Util.CsMxMx) o;
      final java.lang.Object this$cs = this.getCs();
      final java.lang.Object other$cs = other.getCs();
      if (this$cs == null ? other$cs != null : !this$cs.equals(other$cs)) return false;
      if (this.isFlag() != other.isFlag()) return false;
      final java.lang.Object this$ms1 = this.getMs1();
      final java.lang.Object other$ms1 = other.getMs1();
      if (this$ms1 == null ? other$ms1 != null : !this$ms1.equals(other$ms1)) return false;
      final java.lang.Object this$ms2 = this.getMs2();
      final java.lang.Object other$ms2 = other.getMs2();
      if (this$ms2 == null ? other$ms2 != null : !this$ms2.equals(other$ms2)) return false;
      return true;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final java.lang.Object $cs = this.getCs();
      result = result * PRIME + ($cs == null ? 43 : $cs.hashCode());
      result = result * PRIME + (this.isFlag() ? 79 : 97);
      final java.lang.Object $ms1 = this.getMs1();
      result = result * PRIME + ($ms1 == null ? 43 : $ms1.hashCode());
      final java.lang.Object $ms2 = this.getMs2();
      result = result * PRIME + ($ms2 == null ? 43 : $ms2.hashCode());
      return result;
    }

    @java.lang.SuppressWarnings("all")
    public CsMxMx withCs(@NonNull final java.util.List<Ast.C> cs) {
      if (cs == null) {
        throw new java.lang.NullPointerException("cs is marked @NonNull but is null");
      }
      return this.cs == cs ? this : new CsMxMx(cs, this.flag, this.ms1, this.ms2);
    }

    @java.lang.SuppressWarnings("all")
    public CsMxMx withFlag(final boolean flag) {
      return this.flag == flag ? this : new CsMxMx(this.cs, flag, this.ms1, this.ms2);
    }

    @java.lang.SuppressWarnings("all")
    public CsMxMx withMs1(final MethodSelector ms1) {
      return this.ms1 == ms1 ? this : new CsMxMx(this.cs, this.flag, ms1, this.ms2);
    }

    @java.lang.SuppressWarnings("all")
    public CsMxMx withMs2(final MethodSelector ms2) {
      return this.ms2 == ms2 ? this : new CsMxMx(this.cs, this.flag, this.ms1, ms2);
    }
  }


  public static class CsSPath {
    @NonNull
    List<Ast.C> cs;
    @NonNull
    java.util.Set<Path> pathsSet;

    public String toString() {
      return "" + cs + "->" + pathsSet;
    }

    @java.lang.SuppressWarnings("all")
    public CsSPath(@NonNull final List<Ast.C> cs, @NonNull final java.util.Set<Path> pathsSet) {
      if (cs == null) {
        throw new java.lang.NullPointerException("cs is marked @NonNull but is null");
      }
      if (pathsSet == null) {
        throw new java.lang.NullPointerException("pathsSet is marked @NonNull but is null");
      }
      this.cs = cs;
      this.pathsSet = pathsSet;
    }

    @NonNull
    @java.lang.SuppressWarnings("all")
    public List<Ast.C> getCs() {
      return this.cs;
    }

    @NonNull
    @java.lang.SuppressWarnings("all")
    public java.util.Set<Path> getPathsSet() {
      return this.pathsSet;
    }

    @java.lang.SuppressWarnings("all")
    public void setCs(@NonNull final List<Ast.C> cs) {
      if (cs == null) {
        throw new java.lang.NullPointerException("cs is marked @NonNull but is null");
      }
      this.cs = cs;
    }

    @java.lang.SuppressWarnings("all")
    public void setPathsSet(@NonNull final java.util.Set<Path> pathsSet) {
      if (pathsSet == null) {
        throw new java.lang.NullPointerException("pathsSet is marked @NonNull but is null");
      }
      this.pathsSet = pathsSet;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof Util.CsSPath)) return false;
      final Util.CsSPath other = (Util.CsSPath) o;
      if (!other.canEqual((java.lang.Object) this)) return false;
      final java.lang.Object this$cs = this.getCs();
      final java.lang.Object other$cs = other.getCs();
      if (this$cs == null ? other$cs != null : !this$cs.equals(other$cs)) return false;
      final java.lang.Object this$pathsSet = this.getPathsSet();
      final java.lang.Object other$pathsSet = other.getPathsSet();
      if (this$pathsSet == null ? other$pathsSet != null : !this$pathsSet.equals(other$pathsSet)) return false;
      return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
      return other instanceof Util.CsSPath;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final java.lang.Object $cs = this.getCs();
      result = result * PRIME + ($cs == null ? 43 : $cs.hashCode());
      final java.lang.Object $pathsSet = this.getPathsSet();
      result = result * PRIME + ($pathsSet == null ? 43 : $pathsSet.hashCode());
      return result;
    }

    @java.lang.SuppressWarnings("all")
    public CsSPath withCs(@NonNull final List<Ast.C> cs) {
      if (cs == null) {
        throw new java.lang.NullPointerException("cs is marked @NonNull but is null");
      }
      return this.cs == cs ? this : new CsSPath(cs, this.pathsSet);
    }

    @java.lang.SuppressWarnings("all")
    public CsSPath withPathsSet(@NonNull final java.util.Set<Path> pathsSet) {
      if (pathsSet == null) {
        throw new java.lang.NullPointerException("pathsSet is marked @NonNull but is null");
      }
      return this.pathsSet == pathsSet ? this : new CsSPath(this.cs, pathsSet);
    }
  }


  public static class CsMwtPMwt {
    @NonNull
    List<Ast.C> src1;
    @NonNull
    ClassB.MethodWithType mwt1;
    @NonNull
    Path src2;
    @NonNull
    ClassB.MethodWithType mwt2;

    @java.lang.SuppressWarnings("all")
    public CsMwtPMwt(@NonNull final List<Ast.C> src1, @NonNull final ClassB.MethodWithType mwt1, @NonNull final Path src2, @NonNull final ClassB.MethodWithType mwt2) {
      if (src1 == null) {
        throw new java.lang.NullPointerException("src1 is marked @NonNull but is null");
      }
      if (mwt1 == null) {
        throw new java.lang.NullPointerException("mwt1 is marked @NonNull but is null");
      }
      if (src2 == null) {
        throw new java.lang.NullPointerException("src2 is marked @NonNull but is null");
      }
      if (mwt2 == null) {
        throw new java.lang.NullPointerException("mwt2 is marked @NonNull but is null");
      }
      this.src1 = src1;
      this.mwt1 = mwt1;
      this.src2 = src2;
      this.mwt2 = mwt2;
    }

    @NonNull
    @java.lang.SuppressWarnings("all")
    public List<Ast.C> getSrc1() {
      return this.src1;
    }

    @NonNull
    @java.lang.SuppressWarnings("all")
    public ClassB.MethodWithType getMwt1() {
      return this.mwt1;
    }

    @NonNull
    @java.lang.SuppressWarnings("all")
    public Path getSrc2() {
      return this.src2;
    }

    @NonNull
    @java.lang.SuppressWarnings("all")
    public ClassB.MethodWithType getMwt2() {
      return this.mwt2;
    }

    @java.lang.SuppressWarnings("all")
    public void setSrc1(@NonNull final List<Ast.C> src1) {
      if (src1 == null) {
        throw new java.lang.NullPointerException("src1 is marked @NonNull but is null");
      }
      this.src1 = src1;
    }

    @java.lang.SuppressWarnings("all")
    public void setMwt1(@NonNull final ClassB.MethodWithType mwt1) {
      if (mwt1 == null) {
        throw new java.lang.NullPointerException("mwt1 is marked @NonNull but is null");
      }
      this.mwt1 = mwt1;
    }

    @java.lang.SuppressWarnings("all")
    public void setSrc2(@NonNull final Path src2) {
      if (src2 == null) {
        throw new java.lang.NullPointerException("src2 is marked @NonNull but is null");
      }
      this.src2 = src2;
    }

    @java.lang.SuppressWarnings("all")
    public void setMwt2(@NonNull final ClassB.MethodWithType mwt2) {
      if (mwt2 == null) {
        throw new java.lang.NullPointerException("mwt2 is marked @NonNull but is null");
      }
      this.mwt2 = mwt2;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof Util.CsMwtPMwt)) return false;
      final Util.CsMwtPMwt other = (Util.CsMwtPMwt) o;
      if (!other.canEqual((java.lang.Object) this)) return false;
      final java.lang.Object this$src1 = this.getSrc1();
      final java.lang.Object other$src1 = other.getSrc1();
      if (this$src1 == null ? other$src1 != null : !this$src1.equals(other$src1)) return false;
      final java.lang.Object this$mwt1 = this.getMwt1();
      final java.lang.Object other$mwt1 = other.getMwt1();
      if (this$mwt1 == null ? other$mwt1 != null : !this$mwt1.equals(other$mwt1)) return false;
      final java.lang.Object this$src2 = this.getSrc2();
      final java.lang.Object other$src2 = other.getSrc2();
      if (this$src2 == null ? other$src2 != null : !this$src2.equals(other$src2)) return false;
      final java.lang.Object this$mwt2 = this.getMwt2();
      final java.lang.Object other$mwt2 = other.getMwt2();
      if (this$mwt2 == null ? other$mwt2 != null : !this$mwt2.equals(other$mwt2)) return false;
      return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
      return other instanceof Util.CsMwtPMwt;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final java.lang.Object $src1 = this.getSrc1();
      result = result * PRIME + ($src1 == null ? 43 : $src1.hashCode());
      final java.lang.Object $mwt1 = this.getMwt1();
      result = result * PRIME + ($mwt1 == null ? 43 : $mwt1.hashCode());
      final java.lang.Object $src2 = this.getSrc2();
      result = result * PRIME + ($src2 == null ? 43 : $src2.hashCode());
      final java.lang.Object $mwt2 = this.getMwt2();
      result = result * PRIME + ($mwt2 == null ? 43 : $mwt2.hashCode());
      return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
      return "Util.CsMwtPMwt(src1=" + this.getSrc1() + ", mwt1=" + this.getMwt1() + ", src2=" + this.getSrc2() + ", mwt2=" + this.getMwt2() + ")";
    }

    @java.lang.SuppressWarnings("all")
    public CsMwtPMwt withSrc1(@NonNull final List<Ast.C> src1) {
      if (src1 == null) {
        throw new java.lang.NullPointerException("src1 is marked @NonNull but is null");
      }
      return this.src1 == src1 ? this : new CsMwtPMwt(src1, this.mwt1, this.src2, this.mwt2);
    }

    @java.lang.SuppressWarnings("all")
    public CsMwtPMwt withMwt1(@NonNull final ClassB.MethodWithType mwt1) {
      if (mwt1 == null) {
        throw new java.lang.NullPointerException("mwt1 is marked @NonNull but is null");
      }
      return this.mwt1 == mwt1 ? this : new CsMwtPMwt(this.src1, mwt1, this.src2, this.mwt2);
    }

    @java.lang.SuppressWarnings("all")
    public CsMwtPMwt withSrc2(@NonNull final Path src2) {
      if (src2 == null) {
        throw new java.lang.NullPointerException("src2 is marked @NonNull but is null");
      }
      return this.src2 == src2 ? this : new CsMwtPMwt(this.src1, this.mwt1, src2, this.mwt2);
    }

    @java.lang.SuppressWarnings("all")
    public CsMwtPMwt withMwt2(@NonNull final ClassB.MethodWithType mwt2) {
      if (mwt2 == null) {
        throw new java.lang.NullPointerException("mwt2 is marked @NonNull but is null");
      }
      return this.mwt2 == mwt2 ? this : new CsMwtPMwt(this.src1, this.mwt1, this.src2, mwt2);
    }
    //@NonNull List<Path> paths1; @NonNull List<Path> paths2;
    //public String toString(){return ""+paths1+"->"+paths2;}
  }
}
