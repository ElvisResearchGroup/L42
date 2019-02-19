#!/usr/bin/fish
latexmk presentation.tex
sed 's/\\documentclass\[/\\documentclass\[handout,/' presentation.tex > presentation-handout.tex
latexmk -pdfxe presentation-handout.tex
latexmk -c -e 'push @generated_exts, ("snm", "nav", "xdv")' presentation.tex presentation-handout.tex
rm presentation-handout.tex
