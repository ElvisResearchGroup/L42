using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;

internal partial class Gui
{
	internal readonly string id;

	internal void display(string title, int x, int y, Widget body, string[] events)
	{
		this.open(title, "<div id='Top'></div>", x, y);
		this.draw(body);
		this.dispatch(body, events);
		// TODO: this.dispatch(body, this.events);
	}

	void draw(Widget that)
		=> this.set_("<div id='Top' style='position: relative; height: 750px; width: 750px; border: solid black 2px;'>" 
			+ this.format(that) + "</div>", "Top");

	string JsEscape(string that) 
		=> that.Replace("\\", @"\\").Replace("\"", @"\""").Replace("\'", @"\'").Replace("\n", @"\n");

	void set_(string that, string id)
	{
		string qid = "'" + JsEscape(id) + "'";
		string qText = "'" + JsEscape(that) + "'";
		string cmd = @"var oldElem = document.getElementById(" + qid + @"');
			oldElem.innerHTML = " + qText + @"';
			var parentElem = oldElem.parentNode;
			var innerElem; 
			while (innerElem = oldElem.firstChild) {
				parentElem.insertBefore(innerElem, oldElem); 
			}
			parentElem.removeChild(oldElem);";

		this.executeJs(cmd);
	}

	string format(Widget that) 
		=> "<div id='" + id + "' onclick=\"event42('" + id + "::Pressed::more')\" style='position: absolute;"
			+ "left: " + that.left + "px;"
			+ "top: " + that.top + "px;"
			+ "height: " + that.height + "px;"
			+ "width: " + that.width + "px;"
			+ "background-color: " + that.colour + ";'>"
			+ "<div style='position: relative;'>" 
			+ string.Join("", (from w in that.children select this.format(w)))
			+ "</div></div>";

	void open(string title, string body, int x, int y)
		=> this.open(HtmlHeader + "<title>" + title + "</title></head><body>" + body + "</body></html>", x, y);

	const string HtmlHeader
		= "<!DOCTYPE html><html><head><meta http-equiv='content-class' content='text/html; charset=UTF-8'>";

	void dispatch(Widget body, IEnumerable<string> events)
	{
		foreach (string event_ in events) 
		{
			if (event_ == "EXIT") return;
			body.dispatch(new Event(event_));
			this.draw(body);
		}
	}


	void open(string html, int x, int y)  /*mut*/ 
	{
		//Console.Error.WriteLine("GuiPlugin.open(wName: {0}, html: {1}, x: {2}, y: {3})", this.id, html, x, y);
		// TODO: PLUGIN
	}

	string executeJs(string that) /*mut*/ 
	{
		//Console.Error.WriteLine("GuiPlugin.executeJs(wName: {0}, command: {1})", this.id, that);
		// TODO: PLUGIN
		return "";
	}

	internal Gui(string id) { this.id = id; }
}