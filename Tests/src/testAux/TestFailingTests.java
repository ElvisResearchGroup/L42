package testAux;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Test;

import ast.ErrorMessage;
import ast.ExpCore;
import ast.ErrorMessage.FinalResult;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Functions;
import facade.ErrorFormatter;
import facade.Parser;
import helpers.TestHelper;
import newTypeSystem.ErrorKind;
import newTypeSystem.FormattedError;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;

public class TestFailingTests {
	public static void tp(ErrorKind kind,String ...code) {

		try{tp(code);assert false;}
		catch(FormattedError err){
			assert err.kind==kind;
		}
	}
	public static void tp(String ...code) {
		TestHelper.configureForTest();
		FinalResult res0;
		try{
			res0=facade.L42.runSlow(null,Functions.multiLine(code));
		}catch(ErrorMessage msg){
			ErrorFormatter.topFormatErrorMessage(msg);
			throw msg;
		}
		ClassB res=res0.getTopLevelProgram();
		ClassB.NestedClass nc=(ClassB.NestedClass)res.getMs().get(res.getMs().size()-1);
		ExpCore ee2=Desugar.of(Parser.parse(null,"{//@exitStatus\n//0\n\n}")).accept(new InjectionOnCore());
		TestHelper.assertEqualExp(nc.getInner(),ee2);
	}
	
	// Throws an assertion error. It should throw maybe a parsing error or maybe a method not present.
	// This was a bug as of 18/05/2018. Is this the correct error now?
	@Test
	public void testMethodCallOnVariable() {
		tp("{reuse L42.is/nanoBase0"
				,"A: {"
				,"c = c()"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}
	
	// Even if we can't make a unit of a unit, this gives us a "RawJavaException".
	// This was a bug as of 25/07/18. Is this the correct error now?.
	@Test
	public void testUnitofUnit() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: Units.of(Num)"
				,"B: Units.of(A)"
				,"Main: {"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}
	
	// Should throw a type error, not an assertion error as we shouldn't be able to update it.
	// This was a bug as of 18/05/18.
	@Test
	public void testBadUpdate() {
		tp("{"
				,"reuse L42.is/AdamTowel02"
				,"C:( s = 12Num"
				,"s := s.#plus(30Num)"
				,"{})"
				,"}");
	}
	


	// Should this work? The equivalent works in Java, Python, etc.
	@Test
	public void testNegativeVariable() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
				,"Num s = 1"
				,"Num j = -s"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	// Should this work? I'm not quite sure why this doesn't work. Something to do with
	// The a.update() call.
	@Test
	public void testMutVariable() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: Data <>< {"
					,"var Num a"
				,"mut method Void update() {"
					,"this.a(30Num)"
				,"}"
				,"B: {"
					,"A a = A(a: 1Num)"
					,"a.update()"
					,"X[a.a() != 1Num]"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	// If run in the IDE this will break the IDE until you use a text editor to change the file.
	// Kind of a bug, was a bug as of 18/05/18.
	@Test
	public void testLocalhost() {
		tp("{reuse localhost/nanoBase0"
				,"C:{"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	//If the test above throws an UnclosedParenthesis should this one throw an unopenedParenthesis?
	//This throws the correct error in the IDE. It throws ParenthesisMismatchRange instead
	// Was a bug as of 18/05/18.
	@Test(expected = ErrorMessage.UnopenedParenthesis.class)
	public void testUnopenedParen() {
		tp("{"
				,"C:"
				,"{})"
				,"}");
	}

	// Is there a reason why this can't infer the type? I know it works if we give a an
	// explicit type.
	@Test
	public void testInferVarType() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
				,"var a = 0Num"
				,"a := 1Num"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}
	
	// This is probably fine because we may not support this character. 
	// http://unicode.org/cldr/utility/character.jsp?a=00B6
	@Test
	public void testWeirdCharacters() {
		tp("{reuse L42.is/nanoBase0"
				,"A: {"
				,"¶ = Bool.True()"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	// This was a bug as of 18/05/18. Marco stated that he was working on fixing it.
	@Test
	public void testImplementAndBabelFish() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: Data <>< {implements S"
				,"}"
				,"B: {"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	// Are we not using IEEE 754 floating point standard? 
	@Test
	public void testFloatingPointAdd() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
				,"X[(0.2Num + 0.1Num) != (0.3Num)]"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	//This probably shouldn't work, but it throws a Java error.
	//Expecting some sort of parsing error? Or perhaps a meta error.
	// Was a bug as of 18/05/18.
	@Test 
	public void testNoClassname() {
		tp("{"
				,"{Debug()}"
				,"}");
	}

	// Is this error due to the fact that we don't have a field in a class extending
	// Data therefore the equals method can't be constructed properly?
	@Test
	public void testBabelFish() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: Data <>< {"
				,"}"
				,"B: {"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}
	// Should this work? It shows it in the tutorial.
	@Test
	public void testSidedDivision() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: Units.of(Num)"
				,"B: Units.of(Num)"
				,"C: Units.of(A per: B)"
				,"Main: {"
				,"t = C(41A per: 42B)"
				,"l = t /~ 32B"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}
	
	// Do we not have a modulo function?
	@Test
	public void testModulo() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
					,"X[(4 % 2) == 0Num]"
					,"return ExitCode.normal()"
				,"}"
		,"}");
	}

	// This should throw an error stating that we can't update because a is not a var but
	// it throws an assertion error.
	@Test
	public void testNonVarUpdate() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
				,"a = 0Num"
				,"a := 1Num"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	// Make sure adding two different units doesn't work. This has a slightly different error to before.
	// Is this the correct error?
	@Test(expected = newTypeSystem.FormattedError.class)
	public void testBadUnitAdd() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: Units.of(Num)"
				,"B: Units.of(Num) "
				,"Main: {"
				,"a = 5A"
				,"b = 4B"
				,"c = a + b"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}
	//This should be correct??
	// This was a bug as of 18/05/18
	@Test//(expected = ErrorMessage.NotWellFormedMsk.class)
	public void testAssignExitCode() {
		tp("{"
				,"C:{ s = return ExitCode.normal()"
				,"}"
				,"}");
	}//TODO: marco think:
	//-according to speck this should work, but
	//look so horrible that may be a bug in the speck?
	//and... today do not work, so, there is a delta between impl and speck?

	
	// Should this work okay? I think it's placing the == ahead of the + operator in precedence.
	@Test
	public void testOrderOfOperations() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
				,"X[0.2Num + 0.1Num == 0.3Num]"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}
	@Test //We should expect some sort of 42 stackoverflow error. We can count to about 5800.
	// This was a bug as of 18/05/2018. Note: The equivalent Java program counts to about 23000.
	public void testStackOverflow() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
				,"class method Num a(Num that) {"
				,"if that == 10000Num ("
				,"return that"
				,")"
				,"else ("
				,"return A.a(that + 1Num)"
				,")"
				,"}"
				,"}"
				,"B: {"
				,"Num s = A.a(1Num)"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}
	
	//Should this be allowed? It wouldn't work in Java as a nested type can't hide an
	//enclosing type.
	@Test
	public void testRepeatedNestedClasses() {
		tp("{reuse L42.is/AdamTowel02"
				,"B: {"
				,"A:{A:{}}"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}
	
	// This is meant to fail.
	@Test //Is this the right error message?
	public void testRefactor() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
					,"lRed = Refactor2.redirect(path:\"A\" into:S)<><{A:{} method A a(A that)that}"
					,"return ExitCode.normal()"
				,"}"
			,"}");
	}
	
	@Test
	public void testAlphanumeric() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: Alphanumeric <>< {"
					,"S a"
					,"S b"
					,"class method S parse(S that) {"
						//,"index = that.indexOf(S\"@\")"
						//,"local = that(end: index.get())"
						//,"domain = that(start: index.get() + 1Size)"
						//,"return This(that, local: local, domain: domain)"
						,"return S\"test\""
					,"}"
				,"}"
				,"B:{"
			    	,"myEmail= A\"arthur.dent@gmail.com\""
					,"return ExitCode.normal()"
				,"}"
			,"}");
	}
	
	@Test
	public void testAlphanumeric2() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: Alphanumeric <>< {"
					,"S local"
					,"S domain"
					,"class method This parse(S that) {"
						,"index = that.indexOf(S\"@\")"
					    ,"!index.isPresent() (error Alphanumeric.ParseFail\"@ not found\")"
						,"local = that(end: index.get())"
						,"domain = that(start: index.get() + 1Size)"
					    ,"if domain.contains(S\"@\") (error Alphanumeric.ParseFail\"multiple @ found\")"
						,"return This(that, local: local, domain: domain)"
					,"}"
				,"}"
				,"B:{"
			    	,"myEmail= A\"arthur.dent@gmail.com\""
					,"return ExitCode.normal()"
				,"}"
			,"}");
	}

	// Comments in the 42 code mean it throws a Java error.
	@Test
	public void testAlphanumeric3() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: Alphanumeric <>< {"
					,"S local"
					,"S domain"
					,"class method This parse(S that) {"
						,"index = that.indexOf(S\"@\")"
					    ,"//!index.isPresent() (error Alphanumeric.ParseFail\"@ not found\")"
						,"local = that(end: index.get())"
						,"domain = that(start: index.get() + 1Size)"
					    ,"//if domain.contains(S\"@\") (error Alphanumeric.ParseFail\"multiple @ found\")"
						,"return This(that, local: local, domain: domain)"
					,"}"
				,"}"
				,"B:{"
			    	,"myEmail= A\"arthur.dent@gmail.com\""
					,"return ExitCode.normal()"
				,"}"
			,"}");
	}
	
	// No comments = 42 error.
	@Test
	public void testAlphanumeric4() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: Alphanumeric <>< {"
					,"S local"
					,"S domain"
					,"class method This parse(S that) {"
						,"index = that.indexOf(S\"@\")"
					    //,"!index.isPresent() (error Alphanumeric.ParseFail\"@ not found\")"
						,"local = that(end: index.get())"
						,"domain = that(start: index.get() + 1Size)"
					    //,"if domain.contains(S\"@\") (error Alphanumeric.ParseFail\"multiple @ found\")"
						,"return This(that, local: local, domain: domain)"
					,"}"
				,"}"
				,"B:{"
			    	,"myEmail= A\"arthur.dent@gmail.com\""
					,"return ExitCode.normal()"
				,"}"
			,"}");
	}

	@Test
	public void testAssignSelf() {
		tp("{"
	,"reuse L42.is/AdamTowel02"
	,"CacheAdamTowel02:Load.cacheTowel()"
	,""
	,"Nums: Use.Over[Collections.vector(of: Num)] <>< {    "
	,"    method Nums pop() {"
	,"        this := this.withoutRight()"
	,"        return this"
	,"    }"
	,"  }"
	,""
	,"Main:{"
	,"    k = Nums[]"
	,""
	,"    return ExitCode.normal()"
	,"}"
	,"}");
	}

	@Test
	public void testReallyBigNumber() {
		tp("{"
	,"reuse L42.is/AdamTowel02"
	,"CacheAdamTowel02:Load.cacheTowel()"
	,""
	,"Main:{"
	,"    var Num i = 1Num"
	,"    var Num val = 53Num"
	,"    while (i < 100000000000000000Num) ("
	,"        val := val * i"
	,"        i := i + 1Num"
	,"    )"
	,"        "
	,"    return ExitCode.normal()"
	,"}"
	,"}");
	}
	
	@Test
	public void testUnitWithNum() {
	        tp("{"
	,"reuse L42.is/AdamTowel02"
	,"CacheAdamTowel02:Load.cacheTowel()"
	,""
	,"Meter: Units.of(Num)"
	,""
	,"Main: {"
	,""
	,"    res = (6Meter +4Meter)*2Num"
	,"    X[res == 20Meter]"
	,"        "
	,"    return ExitCode.normal()"
	,"}"
	,"}");
	}


}
