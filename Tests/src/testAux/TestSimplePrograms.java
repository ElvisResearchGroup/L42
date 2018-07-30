package testAux;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import helpers.TestHelper;
import newTypeSystem.ErrorKind;
import newTypeSystem.FormattedError;
import platformSpecific.javaTranslation.Resources;
import facade.ErrorFormatter;
import facade.L42;
import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.Ast;
import ast.ErrorMessage;
import ast.ErrorMessage.FinalResult;
import ast.ErrorMessage.PathMetaOrNonExistant;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Functions;

public class TestSimplePrograms {
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


	/**
	 * Aden's tests.
	 */

	@Test(expected = ErrorMessage.UnclosedParenthesis.class)
	public void testUnclosedParen() {
		tp("{"
				,"C: ("
				,"{}");
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


	@Test
	public void testNanoBase() {
		tp("{reuse L42.is/nanoBase0"
				,"C:{"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}


	// These towels are no longer supported.

	// We should be able to use this towel?
	/*@Test
		  public void testMicroBase() {
		  	tp("{reuse L42.is/microBase"
		  			,"A: {"
		  				,"return ExitCode.normal()"
		  			,"}"
		  	,"}");
		  }

		  // We should also be able to use this towel?
		  @Test
		  public void testOtherNanobase() {
		  	tp("{reuse L42.is/nanoBase2"
		  		,"C:{"
		  			,"return ExitCode.normal()"
		  		,"}"
		  	,"}");
		  }*/


	@Test(expected=ErrorMessage.VariableUsedNotInScope.class)
	public void testUnderscoreVariableName(){
		tp("{"
				," C:{class method Any m(Any that) that}"
				," Main: ("
				,"  arr = C.m(_)"
				,"  {})"
				,"}");
	}

	//This should be correct??
	// This was current as of 18/05/18
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

	@Test(expected = ErrorMessage.InvalidCharacter.class)
	public void testTabBadCharacter() {
		tp("{"
				,"C:{ s = 12Num"
				,"	s = 13Num"
				,"}"
				,"}");
	}

	//This should work as nanoBase doesn't load in the string class.
	@Test
	public void testClassnameSOk() {
		tp("{reuse L42.is/nanoBase0"
				,"S: {"
				,  "return ExitCode.normal()"
				,"}"
				,""
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


	/**
	 * These tests use AdamTowel02, therefore the first one may take a while to run before the towel
	 * is cached.
	 */

	@Test
	public void testCollectionNotEmpty() {
		tp("{reuse L42.is/AdamTowel02"
				,"CacheAdamTowel02:Load.cacheTowel()"
				,"Strs: Collections.vector(of: S)"
				,"C: {"
				,"Strs t = Strs[S\"1\"; S\"2\"; S\"3\"]"
				,"X[t.isEmpty() == Bool.false()]"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	// Test collection size == number of elements.
	@Test
	public void testCollectionSize() {
		tp("{reuse L42.is/AdamTowel02"
				,"CacheAdamTowel02:Load.cacheTowel()"
				,"Strs: Collections.vector(of: S)"
				,"C: {"
				,"Strs t = Strs[S\"1\"; S\"2\"; S\"3\"]"
				,"X[t.size() == 3Size]"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	// Test that an empty collection has a size of 0.
	@Test
	public void testEmptyCollection() {
		tp("{reuse L42.is/AdamTowel02"
				,"CacheAdamTowel02:Load.cacheTowel()"
				,"Strs: Collections.vector(of: S)"
				,"C: {"
				,"Strs t = Strs[]"
				,"X[t.size() == 0Size]"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	//This needs to cache the towel to use S (string) as the class name.
	// This should fail as we're using S as a class name which is already in the towel.
	@Test(expected = ErrorMessage.NotWellFormedMsk.class)
	public void testClassnameS() {
		tp("{reuse L42.is/AdamTowel02"
				,""
				,"S: ("
				,  "return ExitCode.normal()"
				,"{})"
				,""
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

	@Test
	public void testCollectionMethod() {
		tp("{"
				,"reuse L42.is/AdamTowel02"
				,"Nums: Collections.vector(of: Num)"
				,"A: {"
				,"class method Nums give() {"
				,"return Nums[1Num; 2Num; 3Num]"
				,"}"
				,"}"
				,"C: {"
				,"Nums t = A.give()"
				,"Nums t2 = Nums[4Num]"
				,"X[t.size() != t2.size()]"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	//Test equality of collections and test having the exitCode on the only reachable path.
	@Test
	public void testCollectionsEqual() {
		tp("{reuse L42.is/AdamTowel02"
				,"CacheAdamTowel02:Load.cacheTowel()"
				,"Nums: Collections.vector(of: Num)"
				,"C: {"
					,"t1 = Nums[1Num; 2Num]"
					,"Nums t2 = Nums[1Num; 2Num]"
					,"if !t1.equals(t2) ("
					,")"
					,"else ("
					,"return ExitCode.normal()"
				,")"
				,"}"
				,"}");
	}

	//Test equality of collections which are not equal.
	@Test
	public void testCollectionsNotEqual() {
		tp("{reuse L42.is/AdamTowel02"
				,"CacheAdamTowel02:Load.cacheTowel()"
				,"Nums: Collections.vector(of: Num)"
				,"C: {"
				,"t1 = Nums[1Num; 2Num]"
				,"Nums t2 = Nums[1Num]"
				,"if !t1.equals(t2) ("
				,"return ExitCode.normal()"
				,")"
				,"error X\"\""
				,"}"
				,"}");
	}

	// If this code is run twice in the IDE it will fail, but that is not the case here.
	@Test
	public void testSymbol() {
		tp("{reuse L42.is/AdamTowel02"
				,"C: {"
				,"class method S m(S that) {"
				,"return that"
				,"}"
				,"}"
				,"Main: {"
				,"t = C.m(that: S\"t\")"
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

	@Test
	public void testGoodRecursion() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
				,"class method Num a(Num that) {"
				,"if that == 1000Num ("
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

	// Throws an assertion error. It should throw maybe a parsing error or maybe a method not present.
	// This was a bug as of 18/05/2018.
	@Test
	public void testMethodCallOnVariable() {
		tp("{reuse L42.is/nanoBase0"
				,"A: {"
				,"c = c()"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	// This was working before does not work as of 30/07/18.
	@Test
	public void testGetCollectionValue() {
		tp("{reuse L42.is/AdamTowel02"
				,"Nums: Collections.vector(of: Num)"
				,"A: Data <>< {"
					,"Nums list"
					,"method Num search(Size index) {"
						,"return this.list().val(index)"
					,"}"
				,"}"
				,""
				,"B: {"
					,"Nums toCheck = Nums[3Num; 2Num; 4Num; 5Num]"
					,"A a = A(list: toCheck)"
					,"X[a.search(index: 1Size) == 2Num]"
					,"return ExitCode.normal()"
				,"}"
			,"}");
	}

	// This works correctly but there isn't a suitable error to expect.
	@Test//(expected = ErrorMessage.)
	public void testIndexOutOfBounds() {
		try{tp("{reuse L42.is/AdamTowel02"
				,"Nums: Collections.vector(of: Num)"
				,"A: {"
				,"Nums toCheck = Nums[3Num; 2Num; 4Num; 5Num]"
				,"Num outOfBounds = toCheck.val(12Size)"
				,"return ExitCode.normal()"
				,"}"
				,"}");}
		catch(Resources.Error e) {
			Object e42 = e.unbox;
			if(!e42.getClass().getName().contains("Guard£CParameter")) {
				fail();
			}
		}
	}


	@Test
	public void testNegativeNumbers() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
				,"Num negative = Num\"-1\""
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	// Division by zero. Is it intended behaviour to not throw an error?
	// This was a bug as of 18/05/2018. This has been fixed as of 30/07/18.
	@Test
	public void testDivideByZero() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
				,"Num negative = Num\"-1\""
				,"Debug(negative / 0Num)"
				,"Debug(0Num / 0Num)"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	/*// This throws a FileNotFoundException.
		  @Test
		  public void testIntDivision() {
		  	tp("{reuse L42.is/AdamTowel02"
		  			,"Int: Load <>< {reuse L42.is/Numbers/Int}"
		  			,"A: {"
		  				,"Debug(0Int / 0Int)"
		  				,"return ExitCode.normal()"
		  			,"}"
		  	,"}");

		  }
		  // Should this work? Should the predictor be able to find the type of 1? And not
		  // have it as void. It works on methods, does this not count constructors?
		  // It could also be nice to have predictions on "Num n = 12\".
		  @Test
		  public void testSuggestion() {
		  	tp("{reuse L42.is/AdamTowel02"
		  			,"A: Data <>< {"
		  				,"Num n"
		  			,"}"
		  			,""
		  			,"B: {"
		  				,"A a = A(n: 321312\\)"
		  				,"return ExitCode.normal()"
		  			,"}"
		  	,"}");
		  }
	 */
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

	// Should this work? I'm not quite sure why this doesn't work. Something to do with
	// The a.update() call.

	@Test
	public void testMutVariable() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: Data <>< {"
				,"var Num a"
				,""
				,"mut method Void update()"
				,"this.a(30Num)"
				,"}"
				,"B: {"
				,"A a = A(a: 1Num)"
				,"a.update()"
				,"X[a.a() == 1Num]"
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

	// This is not a bug.
	@Test
	public void testOverride() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
				,"l1={class method S foo()this.bar() class method S bar() S\"hi\"}"
				,"l2={class method S bar()S\"hello\"}"
				,"l3=Use.Override[l1]<><l2"
				,"l3.bar()"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	// This is meant to fail.
	@Test
	public void testRefactor() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
				,"lRed = Refactor2.redirect(path:\"A\" into:S)<><{A:{} method A a(A that)that}"
				,"lRed.a(S\"s\")"
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

	@Test
	public void testSum() {
		tp("{reuse L42.is/AdamTowel02"
				,"Nums: Collections.vector(of: Num)"
				,"A: {"
				,"vec = Nums[12Num; 33Num; 16Num]"
				,"var Num sum = 0Num"
				,"with e in vec.vals() (sum := sum + e)"
				,"X[sum == 61Num]"
				,"return ExitCode.normal()"
				,"}"
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

	@Test
	public void testEmptyGivesEmpty() {
		tp("{reuse L42.is/AdamTowel02"
				,"Nums: Collections.vector(of: Num)"
				,"A: {"
				,"k = Nums.empty()"
				,"X[k.isEmpty()]"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	// Make sure adding two different units doesn't work. This has a slightly different error to before.
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

	@Test
	public void testGoodUnitAdd() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: Units.of(Num)"
				,"Main: {"
				,"a = 5A"
				,"b = 4A"
				,"c = a + b"
				,"X[c == 9A]"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	// Even if we can't make a unit of a unit, this gives us a "RawJavaException".
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

	@Test
	public void testCompositeUnit() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: Units.of(Num)"
				,"B: Units.of(Num)"
				,"C: Units.of(A per: B)"
				,"Main: {"
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

	@Test
	public void testOverride2() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
				,"l1 = {class method S foo()this.bar() class method S bar() S\"hi\"}"
				,"l2 = {class method S bar()S\"hello\"}"
				,"l3 = Use.Override[l1]<><l2" 
				,"return l3"
				,"}"

				,"B : {"
				,"l1 = {class method S foo()this.bar() class method S bar() S\"hi\"}"
				,"l2 = {class method S bar()S\"hello\"}"
				,"l3 = Use.Over[l1]<><l2" 
				,"return l3"
				,"}"

				,"C: {"
				,"X[A.foo() == S\"hello\"]"
				,"X[B.foo() == S\"hi\"]"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	// This used to work but no longer does. As of 30/07/18.
	@Test
	public void testRedirect1() {
		tp("{reuse L42.is/AdamTowel02"
				,"D: Data <>< {"
					,"Num that"
				,"}"

				,"B: {"
					,"b = Refactor2.redirect(path:\\\"A\" into:D)<><{A:{} class method A a(A that)that}"
					,"return b"
				,"}"

				,"C: {"
					,"X[B.a(D(42Num)).that() == 42Num]"
					,"return ExitCode.normal()"
				,"}"
			,"}");
	}

	@Test
	public void testRedirect2() {
		tp("{reuse L42.is/AdamTowel02"			
				,"B: {"
				,"b = Refactor2.redirect(path:\\\"A\" into:S)<><{A:{} class method A a(A that)that ++ S\", World!\"}"
				,"return b"
				,"}"

				,"C: {"
				,"X[B.a(S\"Hello\") == S\"Hello, World!\"]"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	@Test
	public void testBasicClone() {
		tp("{reuse L42.is/AdamTowel02"
				,"A: {"
				,"a = S\"A\""
				,"X[a.clone() == a]"
				,"return ExitCode.normal()"
				,"}"
				,"}");
	}

	@Test
	public void testNested() {
		tp("{reuse L42.is/AdamTowel02"
				,"Foo: {"
					,"Bar: {"
						,"class method S do() { return S\"Foo.Bar\" }"
					,"}"
				,"}"
				,"A: {"
					,"k = Foo.Bar.do()"
					,"X[k == S\"Foo.Bar\"]"
					,"return ExitCode.normal()"
				,"}"
		,"}");
	}

	// This doesn't work in the IDE, but does work here.
	@Test
	public void testTwoCache() {
		tp("{reuse L42.is/AdamTowel02"
			,"CacheAdamTowel02:Load.cacheTowel()"
			,"reuse L42.is/AdamTowel02"
			,"CacheAdamTowel02:Load.cacheTowel()"
				,"A: {"
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

	@Test
	public void testPrimes() {
		tp("{reuse L42.is/AdamTowel02"
				,"Nums: Collections.vector(of: Num)"
				,"A: {"
			    	,"primes = Nums[2Num; 11Num; 13Num; 17Num; 19Num]"
			    	,"var Num initial = 2520Num"
					,"with prime in primes.vals() (initial := initial * prime)"
					,"X[initial == 232792560Num]"
			    	,"return ExitCode.normal()"
				,"}"
		,"}");
	}

	// This works fine here but not in the IDE?
	@Test
	public void testCollectionInClass() {
		tp("{reuse L42.is/AdamTowel02"
				,"CacheAdamTowel02:Load.cacheTowel()" 
				,"A: {"
					,"Nums: Collections.vector(of: Num)"
					,"return ExitCode.normal()"
				,"}"
		,"}");

	}


}
