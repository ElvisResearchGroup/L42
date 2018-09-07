trait PointT {
  var x: real
  var y: real
  method clone() returns (p: PointT)
    ensures fresh(p)
  function method Equal(p: PointT): bool reads this, p ensures this == p ==> this.Equal(p)
}

class Point extends PointT
{
  function method Equal(p: PointT): bool reads this, p ensures this == p ==> this.Equal(p)
  { this.x == p.x && this.y == p.y }

  constructor (xx: real, yy: real) { x := xx; y := yy;  }

  method clone() returns (p: PointT)
    ensures fresh(p) {
      p := new Point(x, y);
  }
}

method Assume(b: bool) 
  decreases *
  ensures b {
  if !b {
    print "Asssumption failed!";
    while (true) decreases * { }
  }
}
class {:autocontracts} Hamster {
  var pos: PointT
  constructor (p: PointT)
    decreases *
  { 
    var p2 := p.clone();
    this.pos := p2;
    var b := ValidF(p); 
    Assume(b);
  }
  static function method ValidF(pos: PointT): bool reads pos { !pos.Equal(pos) }
  function method Valid(): bool reads this.pos { ValidF(this.pos) }
}


/*class Cage {
  var path: seq<PointT>;
}*/
method Main() 
  decreases *
{
  print "hello, Dafny\n";
  var p := new Point(0.0, 1.0);
  var x := new Hamster(p);
}
