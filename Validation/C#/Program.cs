using System;
using System.Collections.Generic;

internal class MainC
{
	internal static void Main(string[] args)
	{
		var w = new SafeMovable(700, 700, "green", new Box("Green", 2, 2,
			new SafeMovable("A", 20, 40, 640, 640, "red",
				new SafeMovable("D", 280, 280, 64, 64, "blue"), 
				new SafeMovable("B", 50, 50, 128, 128, "blue",
					new SafeMovable("C", 6, 6, 32, 32, "black"))
			)
		));

		var events = new string[] {
			"A_Left::Pressed::_",
			"A_Right::Pressed::_",
			"A_Up::Pressed::_",
			"A_Down::Pressed::_",

			"B_Left::Pressed::_",
			"B_Right::Pressed::_",
			"B_Up::Pressed::_",
			"B_Down::Pressed::_",

			"C_Left::Pressed::_",
			"C_Right::Pressed::_",
			"C_Up::Pressed::_",
			"C_Down::Pressed::_",

			"D_Left::Pressed::_",
			"D_Right::Pressed::_",
			"D_Up::Pressed::_",
			"D_Down::Pressed::_",
			"EXIT"
		};
		new Gui("a1").display("Moving", 800, 800, w, events);
		Console.Error.WriteLine("#@Success@#");
		Console.Read();
	}
}
