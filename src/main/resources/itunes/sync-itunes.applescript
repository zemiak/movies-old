set script_path to "/Users/vasko/Sites/movies/backend/tools/itunes/"
set php_path to "/Applications/MAMP/bin/php/php5.3.14/bin/php "

tell application "iTunes"
	-- start it
	activate
	
	-- add new movies
	set AppleScript's text item delimiters to {"|"}
	set dataString to do shell script php_path & script_path & "sync.php"
	if (not dataString is equal to "") then
		set dataArray to (every text item in dataString) as list
		repeat with fileName in dataArray
			add fileName
		end repeat
	end if
	
	-- work with all movies
	reveal (some playlist whose special kind is Movies)
	set theMoviesPlayList to (get some playlist whose special kind is Movies)
	set these_tracks to (tracks of theMoviesPlayList)
	
	if these_tracks is {} then error "No tracks are selected in the front window."
	
	repeat with t in these_tracks
		-- check for dead iTunes entries
		if (location of t is not missing value) then
			-- read the movie data
			set dataString to do shell script php_path & script_path & "get_movie_data.php \"" & (location of t) & "\""
			
			-- expand it as an array / list
			set dataArray to (every text item in dataString) as list
			
			-- set the data fields
			set track_name to item 1 of dataArray
			set track_description to item 2 of dataArray
			set track_genre to item 3 of dataArray
			set track_serie to item 4 of dataArray
			set track_genre_order to item 5 of dataArray
			set track_serie_order to item 6 of dataArray
			set track_order to item 7 of dataArray
			
			set name of t to track_name
			set description of t to track_description
			set comment of t to track_description
			
			if (track_serie is equal to "Not Defined") then
				set track_serie to ""
			end if
			
			if (track_serie is not equal to "") then
				set genre of t to track_genre_order & " " & track_serie
			else
				set genre of t to track_genre
			end if
			
			set episode ID of t to track_name
		end if
	end repeat
end tell
