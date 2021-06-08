package is.L42.meta;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//on github:
//setting, developer settings


record CommitFileOrGitHub() {
  /*
   serialize deserialize IS faster then parsing
   //fix todo class method S #$readBase64(S fileName)[System.JavaException]=error X"todo"
   7-remove deployJar
   -System.deployModule better name/parname
   */
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
    return in.replaceAll("[^a-zA-Z0-9_]", " ");  
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
//---------------------------
//As a single method body
class Single{
void meth(
    String url,String token,
    String contentE, String message
    )throws IOException, InterruptedException{
  for(var s:List.of("https://","http://")){
    if(url.startsWith(s)){
      url=url.substring(s.length(),url.length());
      break;
      }
    }
  String user="";
  String repository="";
  String fileName="";
  var prefixes=Map.of(
      "raw.githubusercontent.com/","main/",
      "github.com/","blob/HEAD/",
      "api.github.com/repos/","contents/"
      );
  for(var s:prefixes.entrySet()){
    if(url.startsWith(s.getKey())){
      
      break;
      }
    }
  //https://raw.githubusercontent.com/example42gdrive/Example1/main/bar2/foo.txt
  //https://github.com/example42gdrive/Example1/blob/HEAD/bar2/foo.txt
  //https://api.github.com/repos/example42gdrive/Example1/contents/bar2/foo.txt
  meth(user, repository, token, fileName, contentE, message);
}
void meth(
    String user, String repository,
    String token, String fileName,
    String contentE, String message
    )throws IOException, InterruptedException{
  user=user.replaceAll("[^a-zA-Z0-9_]", " ");
  repository=repository.replaceAll("[^a-zA-Z0-9_]", " ");
  message=message.replaceAll("[^a-zA-Z0-9_]", " ");
  contentE=contentE.replaceAll("[^a-zA-Z0-9_]", " ");
  HttpClient client = HttpClient.newHttpClient();
  var url1="https://api.github.com/repos/"+user+"/"+repository+"/";
  URI uri=URI.create(url1+"contents/"+fileName);
  HttpRequest request1=HttpRequest.newBuilder()
      .uri(uri)
      .header("Authorization","token "+token)
      .header("Accept", "application/vnd.github.v3+json")
      .build();
  HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
  String hasSha=response1.body();
  int index1 = hasSha.indexOf("\"sha\":\"");
  String sha="";
  if(index1!=-1){
    index1+=7;
    int index2 = hasSha.indexOf("\",",index1);
    sha=hasSha.substring(index1,index2);
    sha=",\n\"sha\":\""+sha+"\"";
    }
  String res2 = """
    {
    "owner":"%s",
    "repo":"%s",
    "message":"%s",
    "content":"%s"
    %s
    }
    """.formatted(user,repository,message,contentE,sha);
  HttpRequest request2 = HttpRequest.newBuilder()
    .uri(uri)
    .header("Authorization","token "+token)
    .header("Accept", "application/vnd.github.v3+json")
    .PUT(HttpRequest.BodyPublishers.ofString(res2))
    .build();    
  client.send(request2, HttpResponse.BodyHandlers.ofString());
  }
}


//--------------
public class NewDeployWorkInProgress {
  static void meth(
      String user, String repository,
      String token, String fileName,
      String contentE, String message
      )throws IOException, InterruptedException{
    user=user.replaceAll("[^a-zA-Z0-9_=]", " ");
    repository=repository.replaceAll("[^a-zA-Z0-9_=]", " ");
    message=message.replaceAll("[^a-zA-Z0-9_=]", " ");
    contentE=contentE.replaceAll("[^a-zA-Z0-9_=]", " ");
    var url="https://api.github.com/repos/"+user+"/"+repository+"/contents/"+fileName;
    var request1="""
      GET %s
      Authorization: token %s
      Accept: application/vnd.github.v3+json
      """.formatted(url,token);
    System.out.println(request1);
    String hasSha=SToHttpRequest.makeRequest(request1,"", 200);
    int index1 = hasSha.indexOf("\"sha\":\"");
    String sha="";
    if(index1!=-1){
      index1+=7;
      int index2 = hasSha.indexOf("\",",index1);
      sha=hasSha.substring(index1,index2);
      sha=",\n\"sha\": \""+sha+"\"";
      }
    var request2="""
      PUT %s
      Authorization: token %s
      Accept: application/vnd.github.v3+json
      """.formatted(url,token);
    var body="""  
      {
      "owner": "%s",
      "repo": "%s",
      "message": "%s",
      "content": "%s"%s
      } 
      """.formatted(user,repository,message,contentE,sha);
    System.out.println(request2);
    SToHttpRequest.makeRequest(request2,body, 200);
    }
  public static void main(String[] args) throws IOException, InterruptedException {
    Encoder enc = Base64.getMimeEncoder();
    String contentE=enc.encodeToString("Noooow mjjore 88text".getBytes());
    meth(
        /*user*/"example42gdrive", /*repository*/"Example1",
        /*token*/"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",
        /*fileName*/"bar2/foo.txt",
        /*contentE*/contentE,/*message*/"now with string repr"
        );
    }
  public static void _main(String[] args) throws IOException, InterruptedException {
    var a = new CommitGitHubFile(
      "example42gdrive",
      "Example1",
      "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",
      "bar2/foo.txt"
      );
    a.updateFile("Hi, new content here another file another folder again2".getBytes("UTF-8"),"an other commit message");
  }
}
//-------------
class SToHttpRequest{
 static String makeRequest(String _request, String _body, int _expectedResponse) throws IOException, InterruptedException{
   record SPair(String s1, String s2) {
     static SPair of(String s, String sep){
       int index1=s.indexOf(sep);
       if (index1==-1) {throw new IllegalArgumentException("Separator "+sep+" not present in string "+s);}
       var s1=s.substring(0,index1);
       var s2=s.substring(index1+sep.length(),s.length());
       return new SPair(s1,s2);
       }
     }
  String request=_request, body=_body;
  int expectedResponse=_expectedResponse;
  HttpClient client = HttpClient.newHttpClient();
  SPair top=request.lines().limit(1).map(s->SPair.of(s," ")).findFirst().get();
  var b = HttpRequest.newBuilder();
  b=b.uri(URI.create(top.s2()));
  List<SPair> headers=request.lines()
    .skip(1)//It is better to have the list
    .map(s->SPair.of(s,":"))
    .collect(Collectors.toUnmodifiableList());
  for(var pi:headers){
    b=b.header(pi.s1(),pi.s2());
    }
  b=switch(top.s1()) {
    case "GET" ->b.GET();
    case "PUT" ->b.PUT(HttpRequest.BodyPublishers.ofString(body));
    case "POST" ->b.POST(HttpRequest.BodyPublishers.ofString(body));
    case "DELETE" ->b.DELETE();
    default ->{throw new IllegalArgumentException("Method "+top.s1()+" not recognized"); }
    };
  HttpResponse<String> response = client.send(b.build(), HttpResponse.BodyHandlers.ofString());
  if(response.statusCode()!=expectedResponse){
    throw new IllegalArgumentException("Expected response was "+expectedResponse
      +"\nBut we got "+response.statusCode()+" instead. Response body below:\n\n"+response.body());
    }
  return response.body();
  }
}