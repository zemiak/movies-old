#!/bin/sh

# Compile the AppleScript
osacompile -o /Users/vasko/bin/sync-itunes.scpt sync-itunes.applescript.txt

# Copy the binary runner
cp sync-itunes.sh /Users/vasko/bin/
cp sync-itunes.plist /Users/vasko/Library/LaunchAgents/com.zemiak.movies.sync-itunes.plist
launchctl unload /Users/vasko/Library/LaunchAgents/com.zemiak.movies.sync-itunes.plist
launchctl load /Users/vasko/Library/LaunchAgents/com.zemiak.movies.sync-itunes.plist
