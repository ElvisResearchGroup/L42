package is.L42.visitors;

import static is.L42.tools.General.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import is.L42.generated.SettingsFileParser.MemOptContext;
import is.L42.generated.SettingsFileParser.NudeSettingsContext;
import is.L42.generated.SettingsFileParser.SecOptContext;
import is.L42.generated.SettingsFileParser.SettingContext;
import is.L42.main.Settings;
import is.L42.common.ErrMsg;
import is.L42.common.Parse;
import is.L42.flyweight.C;
import is.L42.generated.*;
import is.L42.generated.Full.CsP;

public class TopSettingsFileVisitor implements SettingsFileVisitor<Object>{
  public Path fileName;
  Pos lastPos;
  public StringBuilder errors=new StringBuilder();
  public TopSettingsFileVisitor(Path fileName){this.fileName=fileName;}
  Pos pos(ParserRuleContext prc){
    return new Pos(fileName.toUri(),prc.getStart().getLine(),prc.getStart().getCharPositionInLine()); 
    }
  void check(ParserRuleContext ctx){  
    if(ctx.children!=null){return;}
    throw new ParserFailed();
    }
  @Override public Void visit(ParseTree arg0) {throw bug();}
  @Override public Void visitChildren(RuleNode arg0) {throw bug();}
  @Override public Void visitErrorNode(ErrorNode arg0) {throw bug();}
  @Override public Void visitTerminal(TerminalNode arg0) {throw bug();}
  @Override public Object visitMemOpt(MemOptContext ctx) {throw bug();}
  @Override public Object visitSecOpt(SecOptContext ctx) {throw bug();}
  @Override public Object visitSetting(SettingContext ctx) {throw bug();}
  class SettingBuilder{
    Set<String> seen=new HashSet<>();
    Settings.Options options=Settings.defaultOptions;
    Map<List<C>,List<String>> permissions=new HashMap<>();
    void processMemOpt(MemOptContext o){
      if(o==null){return;}
      var n=o.Num().getText();        
      if(o.IMS()!=null) {options=options.withInitialMemorySize(n);seen(o.IMS());}
      if(o.MMS()!=null) {options=options.withMaxMemorySize(n);seen(o.MMS());}
      if(o.MSS()!=null) {options=options.withMaxStackSize(n);seen(o.MSS());}
      }
    void processSecOpt(SecOptContext o){
      if(o==null){return;}
      String s=o.CsP().getText();
      var res0=Parse.ctxCsP(Paths.get(lastPos.fileName()),s);
      if(res0.hasErr()){
        errors.append(lastPos+ ErrMsg.notValidC(
          s.contains("Any")?"Any":
          s.contains("Void")?"Void":        
          s.contains("Library")?"Library":
          s.contains("This")?"This":s
          ));
        return;
        }
      CsP res1=new AuxVisitor(lastPos).visitNudeCsP(res0.res);
      var invalid=res1.cs()==null || res1._p()!=null;
      if(invalid){errors.append(lastPos+ ErrMsg.invalidSettingKey(res1));}
      seen(res1.cs().toString());
      permissions.put(res1.cs(),processUrls(o.URL()));
      }
    List<String> processUrls(List<TerminalNode> urls){
      return urls.stream()
        .map(u->u.getText())
        .map(u->u.substring(1,u.length()-1))
        .toList();
      }
    void process(SettingContext s){
      processMemOpt(s.memOpt());
      processSecOpt(s.secOpt());
      }
    void seen(TerminalNode node){seen(node.getText());};
    void seen(String key){
      if(!seen.contains(key)){seen.add(key);return;}
      errors.append(lastPos+ ErrMsg.repeatedSetting(key));      
      }
    }
  @Override public Settings visitNudeSettings(NudeSettingsContext ctx) {
    var builder=new SettingBuilder();
    for(var s:ctx.setting()){
      lastPos=pos(s);
      builder.process(s);
    }
    return new Settings(builder.options,builder.permissions);
    }
  }