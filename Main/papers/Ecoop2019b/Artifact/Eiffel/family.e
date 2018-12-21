class FAMILY
create make
feature {ANY}
	parents: ARRAYED_LIST[PERSON]
	children: ARRAYED_LIST[PERSON]
	birthday: INTEGER

	make(ps: ARRAYED_LIST[PERSON] cs: ARRAYED_LIST[PERSON]) do
		parents := ps
		children := cs
	end

	process_day(day_of_year: INTEGER) do
		across children as c loop
			c.item.process_day(day_of_year) end

		across parents as p loop
			p.item.process_day(day_of_year) end end

	add_child(child: PERSON) do
		children.put_right(child) end

	print_invariant: BOOLEAN do
		print ("Invariant !%N")
		Result := true end

invariant print_invariant and 
	across parents as p all
		across children as c all
			p.item.days_lived > c.item.days_lived end end
end
