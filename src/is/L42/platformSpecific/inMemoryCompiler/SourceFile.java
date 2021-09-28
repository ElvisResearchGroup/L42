package is.L42.platformSpecific.inMemoryCompiler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.URI;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

public class SourceFile implements Serializable, JavaFileObject {
  public final String className;
  protected final String contents;
  protected final URI uri;
  protected final Kind kind;
  public SourceFile(String className, String contents) {
    if(className.contains("$")) { throw new Error("Illegal class name; contains '$'"); }
    this.uri = InMemoryJavaCompiler.strToUrl(className,Kind.SOURCE);
    this.kind = Kind.SOURCE;
    this.className = className;
    this.contents = contents;
    }
  public CharSequence getCharContent(boolean b){return contents;}
  public String toString(){
    return contents;
    }
  @Override public URI toUri() { return this.uri; }
  @Override public String getName() {  return this.className; }
  @Override public Kind getKind() { return this.kind; }
  @Override public NestingKind getNestingKind() { return null; }
  @Override public Modifier getAccessLevel() { return null; }
  @Override public long getLastModified() { return 0L; }
  @Override public boolean delete() { throw new UnsupportedOperationException(); }
  @Override  public InputStream openInputStream() throws IOException { throw new UnsupportedOperationException(); }
  @Override public OutputStream openOutputStream() throws IOException { throw new UnsupportedOperationException(); }
  @Override public Reader openReader(boolean ignoreEncodingErrors) throws IOException { throw new UnsupportedOperationException(); }
  @Override public Writer openWriter() throws IOException { throw new UnsupportedOperationException(); }
  @Override
  public boolean isNameCompatible(String simpleName, Kind kind) {
    String baseName = simpleName + kind.extension;
    return kind.equals(getKind())
        && (baseName.equals(toUri().getPath())
            || toUri().getPath().endsWith("/" + baseName));
    }
  }