package is.L42.meta;

import java.util.List;
import java.util.stream.Collectors;

import is.L42.generated.C;
import is.L42.generated.P;
import is.L42.generated.S;

public final class Arrow{
  public Arrow(List<C> cs, S _s) {this(cs,_s,false,false,null,null,null);}
  public Arrow(List<C> cs, S _s, boolean full,boolean star, P _path, List<C> _cs, S _sOut) {
    this.cs = cs;
    this._s = _s;
    this.full = full;
    this.star=star;
    this._path = _path;
    this._cs = _cs;
    this._sOut = _sOut;
    assert _path==null || _cs==null;
    assert _sOut==null || _cs!=null;
    }
  public Arrow withStar(){
    if(_s!=null) {return this;}
    return new Arrow(cs,_s,full,true,_path,_cs,_sOut);
    }
  public Arrow withNoStar(){
    return new Arrow(cs,_s,full,false,_path,_cs,_sOut);    
    }
  public Arrow copy(){
    return new Arrow(cs,_s,full,star,_path,_cs,_sOut);    
    }
  @Override public String toString() {
    String res=cs.toString();
    if(_s!=null){res+="."+_s;}
    if(full){res+="=>";}
    else{res+="->";}
    if(star){res+="*";}
    if(_cs!=null){res+=_cs;}
    if(_path!=null){res+=_path;}
    if(_sOut!=null){res+="."+_sOut;}
    return res;
    }
  public String toStringCs(List<C> cs){
    if(cs==null){return "";}
    if(cs.stream().anyMatch(c->c.hasUniqueNum())){return "<empty>";}
    if(cs.isEmpty()){return "This";}
    return cs.stream().map(c->c.toString()).collect(Collectors.joining("."));
    }
  public String toStringErr() {
    String res=toStringCs(cs);
    if(_s!=null){res+="."+_s;}
    if(full){res+="=>";}
    else{res+="->";}
    if(star){res+="*";}
    if(isEmpty()){res+="<empty>";}
    if(_sOut!=null && _sOut.hasUniqueNum()){res+="<empty>";}
    else{
      res+=toStringCs(_cs);
      if(_sOut!=null){res+="."+_sOut;}
      }
    if(_path!=null){res+=_path;}
    return res;
    }
  public String toStringKey() {
    String res=toStringCs(cs);
    if(_s!=null){res+="."+_s;}
    return res;
    }
  boolean isP(){return _path!=null;}
  boolean isMeth(){return _sOut!=null;}
  boolean isCs(){return _cs!=null && _sOut==null;}
  boolean isEmpty(){return _cs==null &&_path==null;}
  @Override public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_cs == null) ? 0 : _cs.hashCode());
    result = prime * result + ((_path == null) ? 0 : _path.hashCode());
    result = prime * result + ((_s == null) ? 0 : _s.hashCode());
    result = prime * result + ((_sOut == null) ? 0 : _sOut.hashCode());
    result = prime * result + cs.hashCode();
    result = prime * result + (full ? 1231 : 1237);
    result = prime * result + (star ? 1231 : 1237);
    return result;
    }
  @Override public boolean equals(Object obj) {
    if(this == obj){return true;}
    Arrow other = (Arrow) obj;
    if(_cs == null){if(other._cs != null){return false;}}
    else if (!_cs.equals(other._cs)){return false;}
    if(_path == null){if(other._path != null){return false;}}
    else if(!_path.equals(other._path)){return false;}
    if(_s == null){if (other._s != null){return false;}}
    else if(!_s.equals(other._s)){return false;}
    if(_sOut == null){if(other._sOut != null){return false;}}
    else if(!_sOut.equals(other._sOut)){return false;}
    if(!cs.equals(other.cs)){return false;}
    return full == other.full && star==other.star;
    }
  final public List<C> cs;
  final public S _s;
  final public boolean full;
  final public boolean star;
  public P _path;
  public List<C> _cs;
  public S _sOut;
  }