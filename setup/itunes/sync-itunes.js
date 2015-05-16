var MARKER_FILE = "M:\\Movies\\itunes-refresh";
var fso = new ActiveXObject("Scripting.FileSystemObject");
if (! fso.FileExists(MARKER_FILE)) {
	WScript.Quit(1);
}

var shell = WScript.CreateObject("WScript.Shell");
var itunes = WScript.CreateObject("iTunes.Application");
var playlist = itunes.LibraryPlaylist;

while (playlist.Tracks.Count > 0) {
	var track = playlist.Tracks.Item(1);
	track.Delete();
}

playlist.AddFile("M:\\Music");
playlist.AddFile("M:\\Movies");
