package is.L42.meta;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Map;
import java.util.Base64.Encoder;

public class NewDeployWorkInProgress {

}


record CommitFileOrGitHub() {
  /*
   in Library,
   out write out?
   - missing bit is a java Consumer<byte[]>
   42 standard can provide a toJar: Library -> S (Base64)
   then we can write a library that:
     - takes such string and save it to a file? (a twist on the existing File IO?)
     - takes much more info and write it to git
     - takes an url and dispatch on one of the two
   
   FileSystem should be in the towel? //NO
   --DeployLibrary becomes a primitiveDeploy and does not get @Public
   it deploys to /primitiveDeploy instead of localhost
   --DeployJar disappear
   --add DeployLibToS and DeployLibJarToS
   -Make a new github project
      L42Libraries
   -Make a Deploy module (it will selfdeploy and deploy the FS too :) )
     -it will also Deploy adamtowel on L42Libraries
   -reuse will hardcode that L42.is/ translates to ??L42Libraries/
   
   
   DeployLibrary("deployLibrary",Map.of(
    Meta,use("return %s.deployLibrary(%s,%s,%Gen1::wrap);",sigI(Void,String,Lib)))),
   DeployJar("deployJar",Map.of(
    Meta,use("return %s.deployJar(%s,%s,%Gen1::wrap);",sigI(Void,String,Lib)))),

   */
  //https://raw.githubusercontent.com/example42gdrive/Example1/main/bar2/foo.txt
  //https://github.com/example42gdrive/Example1/blob/main/bar2/foo.txt
  //https://api.github.com/repos/example42gdrive/Example1/contents/bar2/foo.txt
}
record CommitGitHubFile(
    String user, String repository, String token, String fileName
    ){
  static HttpClient client = HttpClient.newHttpClient();
  static Encoder enc = Base64.getMimeEncoder();
  
  String getResponse(HttpRequest request)  throws IOException, InterruptedException {
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.println(response.body().replace(",","\n"));
    return response.body();
    }
  
  URI makeURI() {
    var url1="https://api.github.com/repos/"+user+"/"+repository+"/";
    return URI.create(url1+"contents/"+fileName);
    }
  String getSha() throws IOException, InterruptedException {    
    HttpRequest request = HttpRequest.newBuilder()
      .uri(makeURI())
      .header("Authorization","token "+token)
      .header("Accept", "application/vnd.github.v3+json")
      .build();    
    String res = getResponse(request);
    int index1 = res.indexOf("\"sha\":\"");
    if(index1==-1) {return "";}
    index1+=7;
    int index2 = res.indexOf("\",",index1);
    return res.substring(index1,index2);  
    }
  private String escape(String in){
    return in;//TODO:
    }
  public BodyPublisher requestJSon(byte[] content,String message,String sha) {
    String contentE=enc.encodeToString(content);
    if(!sha.isEmpty()){sha=",\n\"sha\":\""+sha+"\"";}
    String res = """
      {
      "owner":"%s",
      "repo":"%s",
      "message":"%s",
      "content":"%s"
      %s
      }
      """
      .formatted(escape(user),escape(repository),
          escape(message),contentE,sha);
    System.out.println(res);
    return HttpRequest.BodyPublishers.ofString(res);
    }
  void updateFile(byte[] content,String message) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
      .uri(makeURI())
      .header("Authorization","token "+token)
      .header("Accept", "application/vnd.github.v3+json")
      .PUT(requestJSon(content,message,getSha()))
      .build();    
    getResponse(request);
    }
  }

class SimpleRemoteFileStorage {
  public static void main(String[] args) throws IOException, InterruptedException {
    var a = new CommitGitHubFile(
      "example42gdrive",
      "Example1",
      "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",
      "bar2/foo.txt"
      );
    a.updateFile("Hi, new content here another file another folder again2".getBytes("UTF-8"),"an other commit message");
  }
}