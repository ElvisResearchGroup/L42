package is.L42.common;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import is.L42.generated.Pos;

public interface ToNameUrl{
  record NameUrl(String fullName,URL fullPath){}
  Optional<NameUrl> apply(String url) throws MalformedURLException;
  default Optional<NameUrl> applyWrap(String url) {
    try {return apply(url);}
    catch(InvalidPathException|MalformedURLException e){return Optional.empty();}
    }
  static String removePrefix(String url, String...options) throws MalformedURLException {
    return Stream.of(options)
      .filter(p->url.startsWith(p))
      .map(p->url.substring(p.length(),url.length()))
      .findFirst()
      .orElseThrow(MalformedURLException::new);
    }
  static String removeSuffix(String url, String...options) throws MalformedURLException {
    return Stream.of(options)
      .filter(p->url.endsWith(p))
      .map(p->url.substring(0,url.length()-p.length()))
      .findFirst()
      .orElseThrow(MalformedURLException::new);
    }
  //Store in variable to avoid repeated reflection calls and try-catch
  //Possible future TODO: Generalize this as a callable method with a class, var name parameter
  static String l42IsRepoPath = getL42IsRepoPath();
  static String getL42IsRepoPath() {
    try {
      Class<?> override = Class.forName("is.L42.override.L42IsRepoPathOverride");
      MethodHandle handle = MethodHandles.lookup().findStaticGetter(override, "l42IsRepoPathOverride", String.class);
      return (String) handle.invoke();
      } 
    catch (ClassNotFoundException e) {
      return "Language42/is";
      } 
    catch (NoSuchFieldException e) {
      throw new Error("Override class is.L42.override.L42IsRepoPathOverride found, but no field called (String) l42IsRepoPathOverride", e);
      } 
    catch (IllegalAccessException e) {
      throw new Error("Override class is.L42.override.L42IsRepoPathOverride found, but field l42IsRepoPathOverride not accessible", e);
      } 
    catch (Throwable e) {
      throw new Error("Override class is.L42.override.L42IsRepoPathOverride found, but there was an error reading field l42IsRepoPathOverride. (Perhaps variable is undefined?)", e);
      }
    }
//TODO: add gitapi?
  static ToNameUrl forGitUrl=(url)->{
    //https://github.com/example42gdrive/Example1/blob/HEAD/FileSystem.L42?raw=true
    String rest=removePrefix(url,"https://github.com/","http://github.com/","github.com/");
    var index1=rest.indexOf("/");
    if(index1==-1){return Optional.empty();}
    var index2=rest.indexOf("/",index1+1);
    if(index2==-1){return Optional.empty();}
    var userRep=rest.substring(0,index2+1);// "username/reponame/"
    rest=rest.substring(index2+1,rest.length());
    if(!rest.startsWith("blob/")){rest="blob/HEAD/"+rest;}
    rest=removeSuffix(rest,".L42?raw=true","?raw=true",".L42","");
    rest=removeSuffix(rest,"/","");
    rest="https://github.com/"+userRep+rest+".L42?raw=true";
    return Optional.of(new NameUrl(rest,new URL(rest)));
    };
  //https://github.com/Language42/is
  static ToNameUrl forL42Is=(url)->{
    String rest=removePrefix(url,"https://L42.is/","http://L42.is/","L42.is/");
      return forGitUrl.apply("https://github.com/"+l42IsRepoPath+"/"+rest);
    };
  static ToNameUrl forFile=(url)->{
    if(url.isBlank() || url.contains(" ")){return Optional.empty();}
    url=removePrefix(url,"file:///","file://localhost/","file:///","file://","");
    url=removeSuffix(url,".L42/","/",".L42","");
    return Optional.of(new NameUrl(
      "localhost"+File.separator+url.replace("/",File.separator)+".L42",
      Constants.localhost.resolve(url+".L42").toUri().toURL()
      ));
    };
  static ToNameUrl forUrl=(url)->{
    if(!url.replace(".L42","").contains(".")){return Optional.empty();}
    String rest=removePrefix(url,"https://","http://","");
    rest=removeSuffix(rest,".L42/","/",".L42","");
    rest="https://"+rest+".L42";
    return Optional.of(new NameUrl(rest,new URL(rest)));
    };
  static NameUrl of(String url,List<Pos>poss){
    return Stream.of(
      forL42Is,
      forGitUrl,
      forUrl,
      forFile
      ).map(o->o.applyWrap(url))
      .filter(o->o.isPresent())
      .findFirst()
      .orElseGet(()->{throw new EndError.UrlNotExistent(poss,url);})
      .get();
    }
  }