package is.L42.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.Test;

import is.L42.sifo.LoopTest;
import is.L42.sifo.StringLattice;

public class TestSifoLattice {

  @Test
  public void testLeastUpperBound() {
    StringLattice lattice = new StringLattice();
    lattice.readLattice("bottom->a2,b2;a2->a1;b2->b1,a1;a1->top;b1->top;top");
    assertEquals(lattice.leastUpperBound(List.of("a2", "b2", "a1")), "a1");
    assertEquals(lattice.leastUpperBound(List.of("a1", "b1", "a1")), "top");
    assertEquals(lattice.leastUpperBound(List.of("a2", "b1")), "top");
    assertEquals(lattice.leastUpperBound(List.of("a2", "a1")), "a1");
    assertEquals(lattice.leastUpperBound(List.of("a1", "b2")), "a1");
    assertEquals(lattice.leastUpperBound(List.of("bottom", "bottom")), "bottom");
    assertEquals(lattice.leastUpperBound(List.of("bottom", "a1")), "a1");
    assertEquals(lattice.leastUpperBound(List.of("b2", "b2")), "b2");
    assertEquals(lattice.leastUpperBound(List.of("b2", "b1")), "b1");
    assertEquals(lattice.leastUpperBound(List.of("b2", "a2")), "a1");
  }

  @Test
  public void testLeastUpperBound2() {
    StringLattice lattice = new StringLattice();
    lattice.readLattice("bottom->a2,b2,c2;a2->a1;b2->b1,a1;c2->c1;c1->top;a1->top;b1->top;top");
    assertEquals(lattice.leastUpperBound(List.of("c2", "b2", "a2")), "top");
    assertEquals(lattice.leastUpperBound(List.of("bottom", "a2", "b2")), "a1");
    assertEquals(lattice.leastUpperBound(List.of("bottom", "c2")), "c2");
    assertEquals(lattice.leastUpperBound(List.of("a2", "c2")), "top");
    assertEquals(lattice.leastUpperBound(List.of("c1", "a2")), "top");
  }

  @Test
  public void testNoLattice() {
    StringLattice lattice = new StringLattice();
    lattice.readLattice("bottom->a2,b2;a2->a1,b1;b2->b1,a1;a1->top;b1->top;top");
    assertThrows(Error.class, () -> lattice.leastUpperBound(List.of("b2", "a2")));
  }

  @Test
  public void testLoops() {
    StringLattice lattice = new StringLattice();
    lattice.readLattice("bottom->a2,b2;a2->a1,b1;b2->b1,a1;a1->top;b1->top;top");
    LoopTest<String> loopTest = new LoopTest<String>(lattice);
    assertFalse(loopTest.testForLoop());
  }

  @Test
  public void testLoops2() {
    StringLattice lattice = new StringLattice();
    lattice.readLattice("bottom->a2,b2,c2;a2->a1;b2->b1,a1;c2->c1;c1->top;a1->top;b1->top;top");
    LoopTest<String> loopTest = new LoopTest<String>(lattice);
    assertFalse(loopTest.testForLoop());
  }

  @Test
  public void testLoops3() {
    StringLattice lattice = new StringLattice();
    lattice.readLattice("bottom->a2,b2;a2->a1;b2->b1,a1;a1->top;b1->top;top");
    LoopTest<String> loopTest = new LoopTest<String>(lattice);
    assertFalse(loopTest.testForLoop());
  }

  @Test
  public void testLoops4() {
    // loop b2 -> b1 and b1 -> b2
    StringLattice lattice = new StringLattice();
    lattice.readLattice("bottom->a2,b2;a2->a1;b2->b1;a1->top;b1->top;b1->b2;top");
    LoopTest<String> loopTest = new LoopTest<String>(lattice);
    assertTrue(loopTest.testForLoop());
  }

  @Test
  public void testLoops5() {
    // No bottom through loop
    StringLattice lattice = new StringLattice();
    lattice.readLattice("bottom->a2,b2;a2->a1;b2->b1;a1->top;b1->top;top->bottom");
    LoopTest<String> loopTestError = new LoopTest<String>(lattice);
    assertThrows(Error.class, () -> loopTestError.testForLoop());
  }

  @Test
  public void testLoops6() {
    // self loop b2
    StringLattice lattice = new StringLattice();
    lattice.readLattice("bottom->a2,b2;a2->a1;b2->b2,b1;a1->top;b1->top;top");
    LoopTest<String> loopTest = new LoopTest<String>(lattice);
    assertTrue(loopTest.testForLoop());
  }

  @Test
  public void testLoops7() {
    // two bottoms
    StringLattice lattice = new StringLattice();
    lattice.readLattice("bottom->a2,b2;a2->a1;b2->b1;a1->top;b1->top;top;bottom2");
    LoopTest<String> loopTestError2 = new LoopTest<String>(lattice);
    assertThrows(Error.class, () -> loopTestError2.testForLoop());
  }
  
  @Test
  public void testLoops8() {
    // two lattices
    StringLattice lattice = new StringLattice();
    lattice.readLattice("bottom->a2,b2;a2->a1;b2->b1;a1->top;b1->top;top;bottom2->top2");
    LoopTest<String> loopTestError = new LoopTest<String>(lattice);
    assertThrows(Error.class, () -> loopTestError.testForLoop());
  }
  
  @Test
  public void testLoops9() {
    // two lattices but one has loop
    StringLattice lattice = new StringLattice();
    lattice.readLattice("bottom->a2,b2;a2->a1;b2->b1;a1->top;b1->top;top;bottom2->top2;top2->bottom2");
    LoopTest<String> loopTestError = new LoopTest<String>(lattice);
    assertTrue(loopTestError.testForLoop());
  }
}
