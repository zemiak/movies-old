/* global LOG */

var MoviePlayer = {
    player: null,
    currentMovie: null,

    play: function(url) {
        LOG.log("MoviePlayer.play(): begin");

        if (!MoviePlayer.player) {
            LOG.log("MoviePlayer.play(): creating");
            MoviePlayer.player = new Player();
            MoviePlayer.player.playlist = new Playlist();
        }

        LOG.log("MoviePlayer.play(): stopping");
        MoviePlayer.player.stop();
        MoviePlayer.player.playlist.pop();

        LOG.log("MoviePlayer.play(): setting up video");
        var mediaItem = new MediaItem("video", url);
        MoviePlayer.player.playlist.push(mediaItem);

        MoviePlayer.player.addEventListener("stateDidChange", function(event) {
            // (new) state: playing, paused, and scanning
            // oldState: playing, paused, and scanning
            console.log("Event: " + event.type + "\ntarget: " + event.target + "\ntimestamp: " + event.timeStamp + "\noldState: " + event.oldState + "\nnew state: " + event.state);
        });

        LOG.log("MoviePlayer.play(): playing");
        MoviePlayer.player.play();
    },

    navigateToMovie: function(id) {
        MoviePlayer.setCurrentMovie(id);
        Presenter.navigate("Detail");
    },

    setCurrentMovie: function(id) {
        MoviePlayer.currentMovie = id;
    },

    getCurrentMovieData: function() {
        return DataReader.loadMovie(MoviePlayer.currentMovie);
    }
};
