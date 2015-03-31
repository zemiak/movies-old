#!/bin/sh

# Exit, if the collection has not been changed
test -f /Volumes/media/Movies/itunes-refresh || exit 0

# Run the Sync Script
osascript /Users/vasko/bin/sync-itunes.scpt

# Remove the marker file
rm -f /Volumes/media/Movies/itunes-refresh
