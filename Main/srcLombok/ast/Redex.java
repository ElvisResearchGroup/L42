package ast;

import java.util.List;

import ast.ExpCore.Block;
import ast.ExpCore.Signal;
import lombok.Value;

public interface Redex {
  public static Redex invalid(){return NoRedex.instance;}
  @Value public static class NoRedex implements Redex{private final static Redex instance=new NoRedex();}
  @Value public static class MethCall implements Redex{ExpCore.MCall that;}
  @Value public static class Garbage implements Redex{ExpCore thatLessGarbage;}
  @Value public static class Ph implements Redex{ExpCore.Block that; int phIndex;}
  @Value public static class Using implements Redex{ExpCore.Using that;ExpCore toReplace;}
  @Value public static class UsingOut implements Redex{ExpCore.Using that;}
  @Value public static class LoopR implements Redex{ExpCore.Loop that;}
  @Value public static class BlockElim implements Redex{ExpCore.Block that; int elimIndex;}
  @Value public static class Subst implements Redex{ExpCore.Block that; int substIndex;}
  @Value public static class Meta implements Redex{ExpCore.ClassB that;}
  @Value public static class CaptureOrNot implements Redex{ExpCore.Block that; int throwIndex;Signal throwExtracted;}
  @Value public static class NoThrowRemoveOn implements Redex{ExpCore.Block that;}
  @Value public static class FUpdateExtended implements Redex{ExpCore ctx;Block aroundCtx; int positionX;List<Block.Dec>dvs;}
}
