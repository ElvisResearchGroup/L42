using System;
using System.Diagnostics.Contracts;
using System.Collections.Generic;
using System.Linq;

internal sealed class Box: Gui.DispatchTrait 
{
	internal int left;
	internal int top;

	public override void process(Gui.Event event_) { }

	internal Box(string id, int left, int top, params Gui.Widget[] children)
		: base(id, children.ToList())
	{
		this.left = left;
		this.top = top;
	}
}


internal sealed class ButtonW: Gui.DispatchTrait, Gui.Widget 
{
	internal Box outer;
	internal int leftDelta;
	internal int topDelta;

	public override string ToString() => this.id;

	public override void process(Gui.Event event_)
	{
		Console.Error.WriteLine("Button pressed: " + this.id);
		this.outer.left = this.outer.left + this.leftDelta;
		this.outer.top  = this.outer.top  + this.topDelta;
	}

	internal static ButtonW Left(Box outer, int scale) 
		=> new ButtonW(outer, scale, "_Left", 0, 13, 6, 6, -5, 0);
	internal static ButtonW Right(Box outer, int scale)
		=> new ButtonW(outer, scale, "_Right", 26, 13, 6, 6, 5, 0); 
	internal static ButtonW Up(Box outer, int scale)
		=> new ButtonW(outer, scale, "_Up", 13, 0, 6, 6, 0, -5);
	internal static ButtonW Down(Box outer, int scale)
		=> new ButtonW(outer, scale, "_Down", 13, 26, 6, 6, 0, 5);

	ButtonW(Box outer, int scale, string suffix, int left, int top, int width, int height, int leftDelta, int topDelta)
		: base(outer.id + suffix, new List<Gui.Widget>())
	{
		this.top    = top   *scale;
		this.left   = left  *scale;
		this.width  = width *scale;
		this.height = height*scale;

		this.outer     = outer;
		this.leftDelta = leftDelta;
		this.topDelta  = topDelta;
	}

	// Auto-implemented properties
	public int left   { get; }
	public int top    { get; }
	public int width  { get; }
	public int height { get; }
	public string colour { get { return "grey"; } }

	// Can't inherit Gui.Widget.left.get from DispatchTrait since it dosn't implement Gui.Widget
	string Gui.Widget.id { get { return this.id; } }
	List<Gui.Widget> Gui.Widget.children { get { return this.children; } }
}

internal sealed class SafeMovable: Gui.Widget 
{
	private readonly Box box;

	internal SafeMovable(int width, int height, string colour, Box box) 
	{
		this.width = width;
		this.height = height;
		this.colour = colour;
		this.box = box;
	}

	// C#, unlike Spec#, requires a call to a base constructor to be the first thing in a constructor
	// So I make this method here
	private static Box makeBox(string id, int left, int top, int width, int height, Gui.Widget[] children) 
	{
		Box box = new Box(id, left, top, children);

		box.children.Add(ButtonW.Left(box, height / 32));
		box.children.Add(ButtonW.Right(box, height / 32));
		box.children.Add(ButtonW.Up(box, height / 32));
		box.children.Add(ButtonW.Down(box, height / 32));
		return box;
	}
	
	internal SafeMovable(string id, int left, int top, int width, int height, string colour, params Gui.Widget[] children) 
		: this(width, height, colour, makeBox(id, left, top, width, height, children)) { }

	public string id { get { return this.box.id; } }
	public List<Gui.Widget> children { get { return this.box.children; } }
	public int left { get { return this.box.left; } }
	public int top { get { return this.box.top; } }

	public bool dispatch(Gui.Event that) => this.box.dispatch(that);

	public int width     { get; }
	public int height    { get; }
	public string colour { get; }

	[ContractInvariantMethod]
	void invariant_() { Contract.Invariant(this.invariant()); }

	[Pure] private bool invariant()
	{
		Console.Error.WriteLine("Invariant #");
		foreach (Gui.Widget w1 in this.box.children)
		{
			if (!Inside(w1)) return false;
			foreach (Gui.Widget w2 in this.box.children)
				if (w1.id != w2.id && Overlap(w1, w2)) 
					return false;
		}

		return true;
	}

	private static bool Overlap(Gui.Widget w1, Gui.Widget w2) 
	{
		if (w1.left > w2.left + w2.width || w2.left > w1.left + w1.width)
			return false;
		if (w1.top > w2.top + w2.height || w2.top > w1.top + w1.height)
			return false;

		return true;
	}

	private bool Inside(Gui.Widget that)
	{
		if (that.left < 0) return false;
		if (that.left + that.width > this.width) return false;
		if (that.top < 0) return false;
		if (that.top + that.height > this.height) return false;
		return true;
	}
}