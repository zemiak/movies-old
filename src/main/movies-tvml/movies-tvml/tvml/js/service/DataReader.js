var DataReader = {
    DATA_URL: null,
    currentFolder: null,
    movieCache: {},
    data: null,

    init: function() {
        DataReader.DATA_URL = Presenter.options.BaseUrl + "tvml/data";
    },

    readAndNavigate: function() {
        var request = new XMLHttpRequest();
        request.responseType = "text";
        request.addEventListener("load", function(){DataReader.newVersionLoaded(request);});
        request.open("GET", DataReader.DATA_URL);
        request.send();
        LOG.log("Sent data request to " + DataReader.DATA_URL);
    },

    getFolderData: function(folderName) {
        return DataReader.data.cache[folderName];
    },

    newVersionLoaded: function(that) {
        DataReader.data = JSON.parse(that.responseText);
        DataReader.navigate("/");
    },

    timeout: function(request) {
        var errorDoc = createAlert("Data Loading Error", "Error reading the data files.");
        navigationDocument.presentModal(errorDoc);
    },

    saveMovie: function(data) {
        DataReader.movieCache[data.id] = data;
    },

    loadMovie: function(id) {
        return DataReader.movieCache[id];
    },

    navigate: function(path) {
        DataReader.currentFolder = path;
        Presenter.navigate("Folder");
    }
};
