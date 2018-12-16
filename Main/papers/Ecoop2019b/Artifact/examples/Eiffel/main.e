class MAIN
create make

feature {NONE}
	make
	local
		fam : FAMILY
		parents: ARRAYED_LIST[PERSON]
		children: ARRAYED_LIST[PERSON]
		p: PERSON
	do
		-- there has to be a better way to do this right?
		create parents.make(2)
		create p.make("Bob", 11720, 40)

		parents.put_right(p)
		create p.make("Alice", 12497, 87)
		parents.put_right(p)
		create children.make(0);
		create fam.make(parents, children)

		across 0 |..| 364 as day
		loop
			fam.process_day(day.item)
		end


		across 0 |..| 364 as day
		loop
			fam.process_day(day.item)
			if day.item = 45 then
				create p.make ("Tim", 0, day.item)
				fam.add_child(p)
			end
		end

		across 0 |..| 364 as day
		loop
			fam.process_day(day.item)
			if day.item = 340 then
				create p.make ("Diana", 0, day.item)
				fam.add_child(p)
			end
		end
	end
end
