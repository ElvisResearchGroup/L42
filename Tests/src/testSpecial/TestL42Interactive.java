package testSpecial;

import helpers.TestHelper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;






import org.junit.Assert;
import org.junit.Test;

import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.Expression;
import ast.ExpCore.ClassB;
import ast.Ast;
import ast.Ast.Stage;
import ast.Ast.Path;
import auxiliaryGrammar.Program;
import facade.L42;
import facade.Parser;

public class TestL42Interactive {


@Test
public void test1() throws IOException{
    TestHelper.configureForTest();
    L42.setRootPath(Paths.get("dummy"));
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"  Gui:{'@plugin"
,"    'L42.is/connected/withHtml"
,"    ()}"
,"  C:{"
,"    var count=S\"|\""
,"    use Gui check open(S\""
,"      '<body>"
,"      '<div id=\"Alpha\"> zuppa</div>"
,"      '<font size=\"3\" color=\"red\">This is some text!</font>"
,"      '<button type=\"button\" onclick=\"event42('Pressed!')\">Click Me!</button>"
,"      '</body>"
,"      \".that(),"
,"      title:S\"FIRST!\".that()"
,"      ) error void"
,"    while True() ("
,"      event=S.#stringParser(use Gui check eventPending() exception void)"
,"      current=S\""
,"        '<div id=\"Alpha\">"
,"        \"++count++S\"</div>\""
,"      use Gui check set(current.that(),id:S\"Alpha\".that()) error void"
,"      count++=S\"|\""
,"      )"
,"    return ExitCode.normal()"
,"    }"
,"  }"
)).getErrCode(),0);}

@Test
public void test2() throws IOException{
    TestHelper.configureForTest();
    L42.setRootPath(Paths.get("dummy"));
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"        {reuse L42.is/miniBase"
,"          Gui:Load[]<{reuse L42.is/GuiLib}"
/*
,"        {reuse L42.is/microBase"
,"          Gui:{'@plugin"
,"            'L42.is/connected/withHtml"
,"            ()"
,"            type method Void open(S title S body) "
,"              use Gui check open(body.that(), title:title.that())error void"
,"            type method Void set(S that,S id) ("
//,"              use Gui check set(that.that(),id:id.that()) error void"
,"              cmd=S\"$(\'#\"++id++S\"').replaceWith('\"++that++S\"');\""
//$('#Alpha').replaceWith('<div id='Alpha'><img src='L.png' height='100' width='100'/></div>');
//need string.replace(S,S)
,"              use Alu check stringDebug(cmd.that()) void"
,"              x=Gui.executeJs(cmd)"
,"              use Alu check stringDebug(x.that()) void"
,"              void)"
,"            type method S executeJs(S that)"
,"              S.#stringParser(use Gui check executeJs(that.that()) error void)"
,"            type method mut Iterator events() Iterator(current:S\"\")"
,"            Iterator:{mut(var S current)"
,"                mut method Void #next() exception Void ("
,"                  Library s=use Gui check eventPending() exception void"
,"                  this.current(S.#stringParser(s))"
,"                  )"
,"                read method Void #checkEnd() void"
,"                read method S #inner() (this.current())"
,"                read method Void #close() void"
,"                }"
,"            }"*/
,"          SList:Collections.list(S)"
,"          C:{"
,"            list=SList[S\"L.png\";S\"A.jpg\";S\"N.jpg\";S\"h.jpg\";S\"h.jpg\";]"
,"            var msg=S\"<div id='Alpha'>\""
,"            "
,"Gui gui=Gui(id:S\"a1\")"
,"gui.open("
,"    title:S\"FIRST!\""
//,"    basePath:S\"file:///C:/Users/marco/Desktop/latexHelper/elvisMarcoServetto/L42ProgrammingGuide/L42TestRunApril/examples/\""
,"    basePath:S\"file:///C:/Users/marco/Desktop/EclipseLuna4.4.2/L42_2015/Tests/dummy/\""
,"      body:S\""
,"        '<div id=\"Alpha\"> </div>"
,"        '<font size=\"3\" color=\"blue\">Press the button to make stuff happens! (slowly)</font>"
,"      '<button type=\"button\" onclick=\"event42('Pressed!')\">Click Me!</button>"
,"      \""
,"    x:600N"
,"    y:400N"
,"    )"

//,"            Gui.open(title:S\"FIRST!\", body:S\""
//,"              '<body>"
//,"              '<a href=\"www.google.com\">foo</a>" links are not active now
//,"              '<div id=\"Alpha\"> </div>"
//,"              '<font size=\"3\" color=\"blue\">Press the button to make stuff happens! (slowly)</font>"
//,"              '<button type=\"button\" onclick=\"event42('Pressed!')\">Click Me!</button>"
//,"              '</body>"
//,"              \")"
,"            with event in gui.events(), h in list.valsCut() ("
,"              msg++=S\"<img src='\"++h++S\"' height='100' width='100'/>\""
//,"              x=gui.executeJs(S\"alert('Hello!!!!')\")"
//,"              y=gui.executeJs(S\"alert('Hello222')\")"
//,"              Debug(x)"
,"              gui.set(msg++S\"</div>\", id:S\"Alpha\")"
//,"              z=gui.executeJs(S\"alert('HelloDoneDone')\")"
,"              void )"
,"            return ExitCode.normal()"
,"            }"
,"          }"

        )).getErrCode(),0);}
}
