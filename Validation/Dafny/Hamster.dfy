class {:autocontracts} PointT {
  var x: int
  var y: int

  function method Valid(): bool ensures Valid() reads {} { true }
	constructor (xx: int, yy: int) { x := xx; y := yy;  }

  function method Equal(p: PointT): bool 
    reads this, p, this.Repr
  {
    this.x == p.x && this.y == p.y 
  }
}


method  Check(b: bool) decreases * ensures b { if !b { print "Asssumption failed!"; while true decreases * { } } }
function method {:verify false} Assume(b: bool): () ensures b { () }

class {:autocontracts} Hamster {
  var pos: PointT
  constructor (p: PointT) { this.pos := new PointT(p.x, p.y); }
}

type Path = array<PointT>
class /*{:autocontracts}*/ Cage
{
	ghost var Repr: set<object?> // GENERATED
	var path: Path
	var h: Hamster

	constructor (h: Hamster, path: Path) decreases *
		//requires exists p : PointT | p in path[..] :: p.Equal(h.pos)
		ensures fresh({this.h, this.path})
		modifies this // GENERATED
		ensures this.Valid() // GENERATED
		ensures fresh(Repr - {this}) // GENERATED
	{
		this.h := new Hamster(h.pos);   
		this.path := new PointT[path.Length](
			(i : nat) reads path requires i < path.Length => path[i]
		);
		new;
				
		this.Repr := {this}; // GENERATED
		if (this.path !in Repr) { Repr := Repr + {this.path}; } // GENERATED
		if (this.h !in Repr) { Repr := Repr + {this.h}; } // GENERATED

		Check(this.Valid()); // I need to add this here (after the above lines)
		var _ := Assume(this.Valid());
	}

	function method Valid(): bool 
		reads this, this.h, this.h.pos, this.path, this.path[..] 
		reads this, this.Repr // GENERATED
	{
		//forall p:  PointT | p in this.path[..] :: p.Valid() &&
		exists p : PointT | p in this.path[..] :: p.Equal(this.h.pos)
	}

	method Move() modifies this.h, this.h.pos
		decreases *
		ensures this.h == old(this.h)
		ensures this.h.pos in this.path[..]
		requires this.Valid() // GENERATED
		ensures this.Valid() // GENERATED
		ensures fresh(this.Repr - old(this.Repr)) // GENERATED
	{ 
		var index := 1;
		while index <= this.path.Length && !this.path[index - 1].Equal(this.h.pos) { 
			index := index + 1;
		}

		//assert this.path.Length > 0;
		this.h.pos := this.path[index % this.path.Length];
		Check(this.Valid());
		var _ := Assume(this.Valid()); // This should be unnecessary...
	}
}

method Main() 
  decreases *
{
  print "hello, Dafny\n";
  var p0 := new PointT(0, 0);
  var p1 := new PointT(2, 2);
  var p2 := new PointT(0, 0);
  var pl := new PointT[3][p0, p1, p2];

  var ph := new PointT(0, 0);
  var h := new Hamster(ph);
  var c := new Cage(h, pl);

//  Check(c.h.pos.x == 0 && c.h.pos.y == 0);
  c.Move();
 // Check(c.h.pos.x == 2 && c.h.pos.y == 2);
  c.Move();
 // Check(c.h.pos.x == 0 && c.h.pos.y == 0);
  c.Move();
// Check(c.h.pos.x == 2 && c.h.pos.y == 2);
  //PlayWith(c);
}
