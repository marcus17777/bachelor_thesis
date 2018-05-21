#!/bin/bash

case $(uname) in
  "Linux")
    xdg-open './docker.pdf'
    ;;
  "Darwin")
    #open './docker.pdf'
    ;;
esac

BIBTEX='bibtex docker'
PDFLATEX='pdflatex -interaction=nonstopmode docker'

WATCHBIB="nodemon --watch docker.bib --watch docker.aux --exec '$BIBTEX'"
WATCHPDF="nodemon --watch docker.tex --exec '$PDFLATEX'"

# concurrently "'$WATCHBIB'" "'$WATCHPDF'"
concurrently "'$WATCHPDF'"
