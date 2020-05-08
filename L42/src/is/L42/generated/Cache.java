// Generated by delombok at Tue Mar 24 20:42:48 NZDT 2020
package is.L42.generated;

import static is.L42.tools.General.todo;
import static is.L42.tools.General.unreachable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import is.L42.common.CTz;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader.SClassFile;
import is.L42.platformSpecific.javaTranslation.L42£Library;

/*public class Cache implements Serializable {
  ArrayList<L42£Library> allLibs;
  ArrayList<SClassFile> allByteCode;
  CTop _top;


  public static class CTop implements Serializable {
    InOut in;
    InOut out;
    ArrayList<CTopNC1> ncs;
    boolean hasHDDeep;
    Core.L sortedHeader;
    int nHByteCode;
    int nHlibs;

    @java.lang.SuppressWarnings("all")
    public InOut in() {
      return this.in;
    }

    @java.lang.SuppressWarnings("all")
    public InOut out() {
      return this.out;
    }

    @java.lang.SuppressWarnings("all")
    public ArrayList<CTopNC1> ncs() {
      return this.ncs;
    }

    @java.lang.SuppressWarnings("all")
    public boolean hasHDDeep() {
      return this.hasHDDeep;
    }

    @java.lang.SuppressWarnings("all")
    public Core.L sortedHeader() {
      return this.sortedHeader;
    }

    @java.lang.SuppressWarnings("all")
    public int nHByteCode() {
      return this.nHByteCode;
    }

    @java.lang.SuppressWarnings("all")
    public int nHlibs() {
      return this.nHlibs;
    }

    @java.lang.SuppressWarnings("all")
    public CTop in(final InOut in) {
      this.in = in;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public CTop out(final InOut out) {
      this.out = out;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public CTop ncs(final ArrayList<CTopNC1> ncs) {
      this.ncs = ncs;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public CTop hasHDDeep(final boolean hasHDDeep) {
      this.hasHDDeep = hasHDDeep;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public CTop sortedHeader(final Core.L sortedHeader) {
      this.sortedHeader = sortedHeader;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public CTop nHByteCode(final int nHByteCode) {
      this.nHByteCode = nHByteCode;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public CTop nHlibs(final int nHlibs) {
      this.nHlibs = nHlibs;
      return this;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof Cache.CTop)) return false;
      final Cache.CTop other = (Cache.CTop) o;
      if (!other.canEqual((java.lang.Object) this)) return false;
      final java.lang.Object this$in = this.in();
      final java.lang.Object other$in = other.in();
      if (this$in == null ? other$in != null : !this$in.equals(other$in)) return false;
      final java.lang.Object this$out = this.out();
      final java.lang.Object other$out = other.out();
      if (this$out == null ? other$out != null : !this$out.equals(other$out)) return false;
      final java.lang.Object this$ncs = this.ncs();
      final java.lang.Object other$ncs = other.ncs();
      if (this$ncs == null ? other$ncs != null : !this$ncs.equals(other$ncs)) return false;
      if (this.hasHDDeep() != other.hasHDDeep()) return false;
      final java.lang.Object this$sortedHeader = this.sortedHeader();
      final java.lang.Object other$sortedHeader = other.sortedHeader();
      if (this$sortedHeader == null ? other$sortedHeader != null : !this$sortedHeader.equals(other$sortedHeader)) return false;
      if (this.nHByteCode() != other.nHByteCode()) return false;
      if (this.nHlibs() != other.nHlibs()) return false;
      return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
      return other instanceof Cache.CTop;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final java.lang.Object $in = this.in();
      result = result * PRIME + ($in == null ? 43 : $in.hashCode());
      final java.lang.Object $out = this.out();
      result = result * PRIME + ($out == null ? 43 : $out.hashCode());
      final java.lang.Object $ncs = this.ncs();
      result = result * PRIME + ($ncs == null ? 43 : $ncs.hashCode());
      result = result * PRIME + (this.hasHDDeep() ? 79 : 97);
      final java.lang.Object $sortedHeader = this.sortedHeader();
      result = result * PRIME + ($sortedHeader == null ? 43 : $sortedHeader.hashCode());
      result = result * PRIME + this.nHByteCode();
      result = result * PRIME + this.nHlibs();
      return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
      return "Cache.CTop(in=" + this.in() + ", out=" + this.out() + ", ncs=" + this.ncs() + ", hasHDDeep=" + this.hasHDDeep() + ", sortedHeader=" + this.sortedHeader() + ", nHByteCode=" + this.nHByteCode() + ", nHlibs=" + this.nHlibs() + ")";
    }

    @java.lang.SuppressWarnings("all")
    public CTop(final InOut in, final InOut out, final ArrayList<CTopNC1> ncs, final boolean hasHDDeep, final Core.L sortedHeader, final int nHByteCode, final int nHlibs) {
      this.in = in;
      this.out = out;
      this.ncs = ncs;
      this.hasHDDeep = hasHDDeep;
      this.sortedHeader = sortedHeader;
      this.nHByteCode = nHByteCode;
      this.nHlibs = nHlibs;
    }
  }


  public static class CTopNC1 implements Serializable {
    ArrayList<CTop> tops;
    InOut in;
    InOut out;
    boolean hasHDE;
    boolean hasHDL;
    Full.L.NC ncIn;
    Core.E coreE;
    Core.L lOut;

    @java.lang.SuppressWarnings("all")
    public ArrayList<CTop> tops() {
      return this.tops;
    }

    @java.lang.SuppressWarnings("all")
    public InOut in() {
      return this.in;
    }

    @java.lang.SuppressWarnings("all")
    public InOut out() {
      return this.out;
    }

    @java.lang.SuppressWarnings("all")
    public boolean hasHDE() {
      return this.hasHDE;
    }

    @java.lang.SuppressWarnings("all")
    public boolean hasHDL() {
      return this.hasHDL;
    }

    @java.lang.SuppressWarnings("all")
    public Full.L.NC ncIn() {
      return this.ncIn;
    }

    @java.lang.SuppressWarnings("all")
    public Core.E coreE() {
      return this.coreE;
    }

    @java.lang.SuppressWarnings("all")
    public Core.L lOut() {
      return this.lOut;
    }

    @java.lang.SuppressWarnings("all")
    public CTopNC1 tops(final ArrayList<CTop> tops) {
      this.tops = tops;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public CTopNC1 in(final InOut in) {
      this.in = in;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public CTopNC1 out(final InOut out) {
      this.out = out;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public CTopNC1 hasHDE(final boolean hasHDE) {
      this.hasHDE = hasHDE;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public CTopNC1 hasHDL(final boolean hasHDL) {
      this.hasHDL = hasHDL;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public CTopNC1 ncIn(final Full.L.NC ncIn) {
      this.ncIn = ncIn;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public CTopNC1 coreE(final Core.E coreE) {
      this.coreE = coreE;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public CTopNC1 lOut(final Core.L lOut) {
      this.lOut = lOut;
      return this;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof Cache.CTopNC1)) return false;
      final Cache.CTopNC1 other = (Cache.CTopNC1) o;
      if (!other.canEqual((java.lang.Object) this)) return false;
      final java.lang.Object this$tops = this.tops();
      final java.lang.Object other$tops = other.tops();
      if (this$tops == null ? other$tops != null : !this$tops.equals(other$tops)) return false;
      final java.lang.Object this$in = this.in();
      final java.lang.Object other$in = other.in();
      if (this$in == null ? other$in != null : !this$in.equals(other$in)) return false;
      final java.lang.Object this$out = this.out();
      final java.lang.Object other$out = other.out();
      if (this$out == null ? other$out != null : !this$out.equals(other$out)) return false;
      if (this.hasHDE() != other.hasHDE()) return false;
      if (this.hasHDL() != other.hasHDL()) return false;
      final java.lang.Object this$ncIn = this.ncIn();
      final java.lang.Object other$ncIn = other.ncIn();
      if (this$ncIn == null ? other$ncIn != null : !this$ncIn.equals(other$ncIn)) return false;
      final java.lang.Object this$coreE = this.coreE();
      final java.lang.Object other$coreE = other.coreE();
      if (this$coreE == null ? other$coreE != null : !this$coreE.equals(other$coreE)) return false;
      final java.lang.Object this$lOut = this.lOut();
      final java.lang.Object other$lOut = other.lOut();
      if (this$lOut == null ? other$lOut != null : !this$lOut.equals(other$lOut)) return false;
      return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
      return other instanceof Cache.CTopNC1;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final java.lang.Object $tops = this.tops();
      result = result * PRIME + ($tops == null ? 43 : $tops.hashCode());
      final java.lang.Object $in = this.in();
      result = result * PRIME + ($in == null ? 43 : $in.hashCode());
      final java.lang.Object $out = this.out();
      result = result * PRIME + ($out == null ? 43 : $out.hashCode());
      result = result * PRIME + (this.hasHDE() ? 79 : 97);
      result = result * PRIME + (this.hasHDL() ? 79 : 97);
      final java.lang.Object $ncIn = this.ncIn();
      result = result * PRIME + ($ncIn == null ? 43 : $ncIn.hashCode());
      final java.lang.Object $coreE = this.coreE();
      result = result * PRIME + ($coreE == null ? 43 : $coreE.hashCode());
      final java.lang.Object $lOut = this.lOut();
      result = result * PRIME + ($lOut == null ? 43 : $lOut.hashCode());
      return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
      return "Cache.CTopNC1(tops=" + this.tops() + ", in=" + this.in() + ", out=" + this.out() + ", hasHDE=" + this.hasHDE() + ", hasHDL=" + this.hasHDL() + ", ncIn=" + this.ncIn() + ", coreE=" + this.coreE() + ", lOut=" + this.lOut() + ")";
    }

    @java.lang.SuppressWarnings("all")
    public CTopNC1(final ArrayList<CTop> tops, final InOut in, final InOut out, final boolean hasHDE, final boolean hasHDL, final Full.L.NC ncIn, final Core.E coreE, final Core.L lOut) {
      this.tops = tops;
      this.in = in;
      this.out = out;
      this.hasHDE = hasHDE;
      this.hasHDL = hasHDL;
      this.ncIn = ncIn;
      this.coreE = coreE;
      this.lOut = lOut;
    }
  }


  public static class InOut implements Serializable {
    int nByteCode;
    int nLibs;
    CTz ctz;
    List<Set<List<C>>> coherentList;
    Program p;

    @java.lang.SuppressWarnings("all")
    public int nByteCode() {
      return this.nByteCode;
    }

    @java.lang.SuppressWarnings("all")
    public int nLibs() {
      return this.nLibs;
    }

    @java.lang.SuppressWarnings("all")
    public CTz ctz() {
      return this.ctz;
    }

    @java.lang.SuppressWarnings("all")
    public List<Set<List<C>>> coherentList() {
      return this.coherentList;
    }

    @java.lang.SuppressWarnings("all")
    public Program p() {
      return this.p;
    }

    @java.lang.SuppressWarnings("all")
    public InOut nByteCode(final int nByteCode) {
      this.nByteCode = nByteCode;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public InOut nLibs(final int nLibs) {
      this.nLibs = nLibs;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public InOut ctz(final CTz ctz) {
      this.ctz = ctz;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public InOut coherentList(final List<Set<List<C>>> coherentList) {
      this.coherentList = coherentList;
      return this;
    }

    @java.lang.SuppressWarnings("all")
    public InOut p(final Program p) {
      this.p = p;
      return this;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof Cache.InOut)) return false;
      final Cache.InOut other = (Cache.InOut) o;
      if (!other.canEqual((java.lang.Object) this)) return false;
      if (this.nByteCode() != other.nByteCode()) return false;
      if (this.nLibs() != other.nLibs()) return false;
      final java.lang.Object this$ctz = this.ctz();
      final java.lang.Object other$ctz = other.ctz();
      if (this$ctz == null ? other$ctz != null : !this$ctz.equals(other$ctz)) return false;
      final java.lang.Object this$coherentList = this.coherentList();
      final java.lang.Object other$coherentList = other.coherentList();
      if (this$coherentList == null ? other$coherentList != null : !this$coherentList.equals(other$coherentList)) return false;
      final java.lang.Object this$p = this.p();
      final java.lang.Object other$p = other.p();
      if (this$p == null ? other$p != null : !this$p.equals(other$p)) return false;
      return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
      return other instanceof Cache.InOut;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      result = result * PRIME + this.nByteCode();
      result = result * PRIME + this.nLibs();
      final java.lang.Object $ctz = this.ctz();
      result = result * PRIME + ($ctz == null ? 43 : $ctz.hashCode());
      final java.lang.Object $coherentList = this.coherentList();
      result = result * PRIME + ($coherentList == null ? 43 : $coherentList.hashCode());
      final java.lang.Object $p = this.p();
      result = result * PRIME + ($p == null ? 43 : $p.hashCode());
      return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
      return "Cache.InOut(nByteCode=" + this.nByteCode() + ", nLibs=" + this.nLibs() + ", ctz=" + this.ctz() + ", coherentList=" + this.coherentList() + ", p=" + this.p() + ")";
    }

    @java.lang.SuppressWarnings("all")
    public InOut(final int nByteCode, final int nLibs, final CTz ctz, final List<Set<List<C>>> coherentList, final Program p) {
      this.nByteCode = nByteCode;
      this.nLibs = nLibs;
      this.ctz = ctz;
      this.coherentList = coherentList;
      this.p = p;
    }
  }

  public static Cache loadCache(Path path) {
    if (path.endsWith("localhost")) {
      //TODO: good just for testing
      return new Cache(new ArrayList<>(), new ArrayList<>(), null);
    }
    try (
      var file = new FileInputStream(path.resolve("cache.L42Bytes").toFile());
      var out = new ObjectInputStream(file)) {
      return (Cache) out.readObject();
    } catch (FileNotFoundException e) {
      return new Cache(new ArrayList<>(), new ArrayList<>(), null);
    } catch (ClassNotFoundException e) {
      throw unreachable();
    } catch (IOException e) {
      throw new Error(e);
    }
  }

  public static void saveCache(Path path, Cache cache) {
    if (path.endsWith("localhost")) {
      return;
    }//TODO: good just for testing
    try (
      var file = new FileOutputStream(path.resolve("cache.L42Bytes").toFile());
      var out = new ObjectOutputStream(file)) {
      out.writeObject(cache);
    } catch (FileNotFoundException e) {
      throw new Error(e);
    } catch (IOException e) {
      e.printStackTrace();
      throw todo();
    }
  }

  @java.lang.SuppressWarnings("all")
  public ArrayList<L42£Library> allLibs() {
    return this.allLibs;
  }

  @java.lang.SuppressWarnings("all")
  public ArrayList<SClassFile> allByteCode() {
    return this.allByteCode;
  }

  @java.lang.SuppressWarnings("all")
  public CTop _top() {
    return this._top;
  }

  @java.lang.SuppressWarnings("all")
  public Cache allLibs(final ArrayList<L42£Library> allLibs) {
    this.allLibs = allLibs;
    return this;
  }

  @java.lang.SuppressWarnings("all")
  public Cache allByteCode(final ArrayList<SClassFile> allByteCode) {
    this.allByteCode = allByteCode;
    return this;
  }

  @java.lang.SuppressWarnings("all")
  public Cache _top(final CTop _top) {
    this._top = _top;
    return this;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof Cache)) return false;
    final Cache other = (Cache) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    final java.lang.Object this$allLibs = this.allLibs();
    final java.lang.Object other$allLibs = other.allLibs();
    if (this$allLibs == null ? other$allLibs != null : !this$allLibs.equals(other$allLibs)) return false;
    final java.lang.Object this$allByteCode = this.allByteCode();
    final java.lang.Object other$allByteCode = other.allByteCode();
    if (this$allByteCode == null ? other$allByteCode != null : !this$allByteCode.equals(other$allByteCode)) return false;
    final java.lang.Object this$_top = this._top();
    final java.lang.Object other$_top = other._top();
    if (this$_top == null ? other$_top != null : !this$_top.equals(other$_top)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof Cache;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final java.lang.Object $allLibs = this.allLibs();
    result = result * PRIME + ($allLibs == null ? 43 : $allLibs.hashCode());
    final java.lang.Object $allByteCode = this.allByteCode();
    result = result * PRIME + ($allByteCode == null ? 43 : $allByteCode.hashCode());
    final java.lang.Object $_top = this._top();
    result = result * PRIME + ($_top == null ? 43 : $_top.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "Cache(allLibs=" + this.allLibs() + ", allByteCode=" + this.allByteCode() + ", _top=" + this._top() + ")";
  }

  @java.lang.SuppressWarnings("all")
  public Cache(final ArrayList<L42£Library> allLibs, final ArrayList<SClassFile> allByteCode, final CTop _top) {
    this.allLibs = allLibs;
    this.allByteCode = allByteCode;
    this._top = _top;
  }
}
*/