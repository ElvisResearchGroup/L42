note

class
	PERSON

create
	make

feature {ANY}
	name: STRING -- const
	days_lived: INTEGER
	birthday: INTEGER -- const

	make(n: STRING dl: INTEGER bd: INTEGER) do
		name := n
		days_lived := dl
		birthday := bd
	end

	process_day(day_of_year: INTEGER) do
		days_lived := days_lived + 1;
		if birthday = day_of_year then
			print("Happy birthday " + name + "!%N")
		end
	end

	print_invariant: BOOLEAN do -- prints invariant...
		print ("InvariantP !%N")
		Result := true
	end
invariant
-- counter+= 1
	print_invariant and name /= "" and days_lived >= 0 and birthday >= 0 and birthday < 365
end
