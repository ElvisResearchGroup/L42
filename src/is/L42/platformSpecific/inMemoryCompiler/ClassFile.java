package is.L42.platformSpecific.inMemoryCompiler;

import java.io.*;
import java.net.URI;
import java.nio.CharBuffer;
import java.util.Arrays;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

public class ClassFile implements Serializable, JavaFileObject {
  private transient final ByteArrayOutputStream byteCode = new ByteArrayOutputStream();
  public final String name;
  protected final URI uri;
  protected final Kind kind;
  protected byte[] bytes=null;
  public ClassFile(String name, Kind kind) {
    this.uri = InMemoryJavaCompiler.strToUrl(name,kind);
    this.kind = kind;
    this.name=name;
    }
  protected void cacheBytes() {
    if(bytes!=null) {return;}
    bytes=byteCode.toByteArray();
    try {byteCode.close();}
    catch (IOException e) {throw new Error(e);}
    }
  public byte[] getBytes() {
    if(bytes==null) {cacheBytes();}
    return bytes;
    }
  public boolean equalBytes(ClassFile that){
    return Arrays.equals(this.bytes,that.bytes);
    }
  @Override public InputStream openInputStream() {
    return new ByteArrayInputStream(getBytes());
    }
  @Override public OutputStream openOutputStream() throws IOException {
    return byteCode;
    }
  private void writeObject(java.io.ObjectOutputStream out) throws IOException {
    this.cacheBytes();
    out.defaultWriteObject();
    }
  @Override public URI toUri() { return this.uri; }
  @Override public String getName() {  return this.name; }
  @Override public Kind getKind() { return this.kind; }
  @Override public NestingKind getNestingKind() { return null; }
  @Override public Modifier getAccessLevel() { return null; }
  @Override public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException { throw new UnsupportedOperationException(); }
  @Override public Reader openReader(boolean ignoreEncodingErrors) throws IOException { 
    CharSequence charContent = getCharContent(ignoreEncodingErrors);
    if(charContent == null) { throw new UnsupportedOperationException(); }
    if(charContent instanceof CharBuffer buffer) {
      if(buffer.hasArray()) { return new CharArrayReader(buffer.array()); }
      }
    return new StringReader(charContent.toString());
    }
  @Override public Writer openWriter() throws IOException { return new OutputStreamWriter(openOutputStream());  }
  @Override public long getLastModified() { return 0L; }
  @Override public boolean delete() { throw new UnsupportedOperationException(); }
  @Override
  public boolean isNameCompatible(String simpleName, Kind kind) {
    String baseName = simpleName + kind.extension;
    return kind.equals(getKind())
        && (baseName.equals(toUri().getPath())
            || toUri().getPath().endsWith("/" + baseName));
    }
  
  }