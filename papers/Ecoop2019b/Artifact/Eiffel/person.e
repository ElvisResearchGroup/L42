class PERSON
create make

feature {ANY}
	name: STRING
	days_lived: INTEGER
	birthday: INTEGER

	make(n: STRING dl: INTEGER bd: INTEGER) do
		name := n
		days_lived := dl
		birthday := bd end

	process_day(day_of_year: INTEGER) do
		days_lived := days_lived + 1;
		if birthday = day_of_year then
			print("Happy birthday " + name + "!%N") end end

	print_invariant: BOOLEAN do
		print ("Invariant !%N")
		Result := true end

invariant print_invariant and 
	name /= "" and days_lived >= 0 and birthday >= 0 and birthday < 365
end
