#!/bin/bash

test -f /Volumes/media/Movies/itunes-refresh || exit 0
/usr/bin/osascript sync-itunes.applescript
rm -f /Volumes/media/Movies/itunes-refresh
