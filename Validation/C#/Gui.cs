using System;
using System.Collections.Generic;

internal partial class Gui
{
	internal class Event 
	{
		internal readonly string targetId;
		internal readonly string eventId;
		internal readonly string[] all;

		internal Event(string that)
		{
			string[] s = that.Split(new string[]{"::"}, StringSplitOptions.None);

			this.targetId = s[0];
			this.eventId = s[1];
			this.all = s;
		}
	}

	internal interface Widget 
	{
		string id { get; }
		int left { get; }
		int top { get; }
		int width { get; }
		int height { get; }
		string colour { get; }
		
		List<Widget> children { get; }
		bool dispatch(Event that);
	}

	internal abstract partial class DispatchTrait
	{
		internal string id; 
		internal List<Widget> children; 

		public abstract void process(Event event_); 

		public bool dispatch(Event that)
		{
			if (this.id == that.targetId)
			{
				this.process(that);
				return true;
			}

			foreach (Widget w in this.children)
				if (w.dispatch(that)) return true;

			return false;
		}
	
		internal DispatchTrait(string id, List<Widget> children)
		{ 
			this.children = children;
			this.id = id; 
		}
	}
}
