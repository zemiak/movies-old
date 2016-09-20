var DataUpdater = {
    PLAYED_URL: null,

    init: function() {
        DataUpdater.PLAYED_URL = Presenter.options.BaseUrl + "tvml/movies/";
    },

    moviePlayed: function(id) {
        var request = new XMLHttpRequest();
        request.responseType = "text";
        request.addEventListener("load", function(){DataUpdater.moviePlayedSuccess(id);});
        request.open("PUT", DataUpdater.PLAYED_URL + id + "/played");
        request.send();
        LOG.log("Sent data request to " + DataUpdater.PLAYED_URL + id + "/played");
    },

    moviePlayedSuccess: function(id) {
        var data = DataReader.loadMovie(id);
        data.played = true;
        DataReader.saveMovie(data);
    },

    movieStopped: function(id) {
        var data = DataReader.loadMovie(id);
        data.played = true;
        DataReader.saveMovie(data);
    }
};
