import std.stdio;
import std.container;

int counter = 0;
class Person
{
	const string name;
	int daysLived;
	const int birthday;
	this(string name, int daysLived, int birthday) {
		this.name = name;
		this.daysLived = daysLived;
		this.birthday = birthday; }
    void processDay(int dayOfYear) {
        this.daysLived += 1;
        if (this.birthday == dayOfYear) {
            writeln("Happy birthday ", this.name, "!");
        }
    }

	invariant { 
		counter += 1;
		assert(this.name != "" && this.daysLived >= 0 && this.birthday >= 0 && this.birthday < 365);
	}
}

class Family
{
	Person[] parents;
	Person[] children;
	this(Person[] parents, Person[] children) {
		this.parents = parents;
		this.children = children;
	}
    void processDay(int dayOfYear) {
        foreach (c; this.children) { c.processDay(dayOfYear); }
        foreach (p; this.parents) { p.processDay(dayOfYear); }
    }

    void addChild(Person child) {
        this.children ~= child;
    }
    
    invariant {
     	counter += 1;
        foreach (p; this.parents) {
            foreach (c; this.children) {
                assert (p.daysLived > c.daysLived);
            }
        }
    }
}

void main()
{
    Family fam = new Family([new Person("Bob", 11720, 40), new Person("Alice", 12497, 87)], []);
    foreach (day; 0..365) { // Run for 1 year
  		fam.processDay(day);
	}
	foreach (day; 0..365) { // Run for 1 year
  		fam.processDay(day);
  		if (day == 45) {
    		fam.addChild(new Person("Tim", 0, day)); }
    }
	foreach (day; 0..365) { // Run for 1 year
  		fam.processDay(day);
  		if (day == 340) {
    		fam.addChild(new Person("Diana", 0, day)); }
    }
    
    writeln("Invariant count: ", counter);
}
