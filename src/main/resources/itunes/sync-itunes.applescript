tell application "iTunes"
	-- start it
	activate
	
	-- work with all movies
	reveal (some playlist whose special kind is Movies)
	set theMoviesPlayList to (get some playlist whose special kind is Movies)
	set these_tracks to (tracks of theMoviesPlayList)
	
	-- delete all movies
	repeat with t in these_tracks
		delete t
	end repeat
end tell

-- Click the “File” menu.
delay 0.5
set timeoutSeconds to 2.0
set uiScript to "click menu bar item \"File\" of menu bar 1 of application process \"iTunes\""
my doWithTimeout(uiScript, timeoutSeconds)

-- Add to Library…
delay 0.5
set timeoutSeconds to 2.0
set uiScript to "click menu item 5 of menu 1 of menu bar item \"File\" of menu bar 1 of application process \"iTunes\""
my doWithTimeout(uiScript, timeoutSeconds)

-- Press ⇧⌘G
delay 0.5
set timeoutSeconds to 2.0
set uiScript to "keystroke \"g\" using {shift down, command down}"
my doWithTimeout(uiScript, timeoutSeconds)

-- Type '/Volumes/media/Movies/'
delay 0.5
set timeoutSeconds to 2.0
set uiScript to "keystroke \"/Volumes/media/Movies/\""
my doWithTimeout(uiScript, timeoutSeconds)

-- Press Return
delay 0.5
set timeoutSeconds to 2.0
set uiScript to "keystroke \"
\" "
my doWithTimeout(uiScript, timeoutSeconds)

-- Click the “Open” button.
delay 0.5
set timeoutSeconds to 2.0
set uiScript to "click UI Element \"Open\" of window \"Add To Library\" of application process \"iTunes\""
my doWithTimeout(uiScript, timeoutSeconds)

on doWithTimeout(uiScript, timeoutSeconds)
	set endDate to (current date) + timeoutSeconds
	repeat
		try
			run script "tell application \"System Events\"
" & uiScript & "
end tell"
			exit repeat
		on error errorMessage
			if ((current date) > endDate) then
				error "Can not " & uiScript
			end if
		end try
	end repeat
end doWithTimeout
