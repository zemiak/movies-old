#!/bin/bash

test -f /Volumes/media/Movies/itunes-refresh || exit 0
osascript /Users/vasko/Documents/itunes-replace-movies.scpt
rm -f /Volumes/media/Movies/itunes-refresh
