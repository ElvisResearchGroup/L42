trait PointT {
  var x: int
  var y: int

  method Clone() 
    returns (p: PointT) 
    ensures fresh(p)

  function method Equal(p: PointT): 
    bool reads this, p ensures this == p ==> this.Equal(p)
}

class Point extends PointT
{

  constructor (xx: int, yy: int) { x := xx; y := yy;  }
  function method Equal(p: PointT): bool 
    reads this, p ensures this == p ==> this.Equal(p) 
  {
    this.x == p.x && this.y == p.y 
  }
  method Clone() returns (p: PointT) ensures fresh(p) 
  {
      p := new Point(x, y);
  }
}

method {:opaque} Assume(b: bool) decreases * ensures b 
{
  if !b 
  {
    print "Asssumption failed!";
    while (true) decreases * { }
  }
}

class Hamster {
  var pos: PointT
  constructor (p: PointT) { this.pos := p; }
}

type Path = array<PointT>
class {:autocontracts} Cage
{
  var path: Path
  var h: Hamster

  constructor (h: Hamster, path: Path) decreases *
  {
    this.h := new Hamster(h.pos);   
    this.path := new PointT[path.Length](
      (i : nat) reads path requires i < path.Length => path[i]
    );
    new;
    Assume(this.Valid());
  }

  function method Valid(): bool 
    reads this, this.h, this.h.pos, this.path, this.path[..] 
    reads this, Repr
  {
    exists p : PointT | p in this.path[..] :: p.Equal(this.h.pos)
  }

  method Move()
    decreases *
    modifies this, this.h, this.h.pos
  { 
		var index := 1;
		while index <= this.path.Length && !this.path[index - 1].Equal(this.h.pos) { 
      index := index + 1;
    }

    //assert this.path.Length > 0;
		this.h.pos := this.path[index % this.path.Length];
    Assume(this.Valid());
	}
}

method Main() 
  decreases *
{
  print "hello, Dafny\n";
  var p0 := new Point(0, 0);
  var p1 := new Point(2, 2);
  var p2 := new Point(0, 0);
  var pl := new PointT[3][p0, p1, p2];

  var ph := new Point(0, 0);
  var h := new Hamster(ph);
  var c := new Cage(h, pl);

//  Assume(c.h.pos.x == 0 && c.h.pos.y == 0);
  c.Move();
 // Assume(c.h.pos.x == 2 && c.h.pos.y == 2);
  c.Move();
 // Assume(c.h.pos.x == 0 && c.h.pos.y == 0);
  c.Move();
// Assume(c.h.pos.x == 2 && c.h.pos.y == 2);
  //PlayWith(c);
}
