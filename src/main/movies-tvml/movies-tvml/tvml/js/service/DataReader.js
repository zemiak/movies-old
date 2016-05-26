var DataReader = {
    DATA_URL: null,
    VERSION_URL: null,
    currentFolder: null,
    movieCache: {},

    init: function() {
        DataReader.DATA_URL = Presenter.options.BaseUrl + "tvml";
        DataReader.VERSION_URL = Presenter.options.BaseUrl + "tvml/data";
    },

    clearCache: function() {
        MovieData.cache = {};
        LOG.log("DataReader.clearCache: cleared");
    },

    check: function() {
        MovieData = ApplicationStorage.get("MovieData", MovieData);

        LOG.log("lastCheckedDay " + MovieData.version.lastCheckedDay + " version " + MovieData.version.version);

        var currentDayOfMonth = new Date().getDate();
        if (MovieData.version.lastCheckedDay === currentDayOfMonth) {
            return;
        }

        MovieData.version.lastCheckedDay = currentDayOfMonth;
        DataReader.save();

        DataReader.requestNewVersion();
    },

    getFolderData: function(folderName) {
        return MovieData.cache[folderName];
    },

    getVersion: function() {
        return MovieData.version;
    },

    requestNewVersion: function() {
        LOG.log("DataReader.requestNewVersion: Refreshing movie data");

        var request = new XMLHttpRequest();
        request.responseType = "text";
        request.addEventListener("load", function(){DataReader.newVersionLoaded(request);});
        request.open("GET", DataReader.VERSION_URL);
        request.send();
    },

    newVersionLoaded: function(that) {
        var data = JSON.parse(that.responseText);
        var newVersion = data.version.version;
        var ourVersion = MovieData.version.version;

        if (newVersion == ourVersion) {
            LOG.log("newVersionLoaded: We have the most recent version");
            return;
        }

        LOG.log("newVersionLoaded: Upgrading data to version " + newVersion + " from version " + ourVersion);

        var currentDayOfMonth = new Date().getDate();
        data.version.lastCheckedDay = currentDayOfMonth;
        if (! data.cache) {
            data.cache = {};
        }

        MovieData = data;
        DataReader.save();

        if (data.version.motd) {
            LOG.log("newVersionLoaded: Update message: " + MovieData.version.motd);
            DataReader.showMessage();
        }
    },

    save: function() {
        ApplicationStorage.set("MovieData", MovieData);
    },

    showMessage: function() {
//        var errorDoc = createAlert("Dáta o filmoch boli aktualizované", MovieData.version.version + ": " + MovieData.version.motd);
//        navigationDocument.presentModal(errorDoc);

        if (null === DataReader.currentFolder) {
            DataReader.currentFolder = "/";
        }

        Presenter.navigateReplace("Folder");
    },

    read: function(folder) {
        DataReader.currentFolder = folder;

        if (MovieData.cache[folder]) {
            LOG.log("DataReader.read: Cache hit for folder " + folder);
            Presenter.navigate("Folder");
            return;
        }

        LOG.log("DataReader.read: Reading data for folder " + folder);

        var request = new XMLHttpRequest();
        request._folder = folder;
        request.responseType = "text";
        request.timeout = 15000; // 15 seconds
        request.ontimeout = function(){DataReader.timeout(request);};
        request.onerror = function(){DataReader.timeout(request);};
        request.addEventListener("load", function(){DataReader.folderDataLoaded(request);});
        request.open("GET", DataReader.DATA_URL + "?path=" + encodeURIComponent(folder));
        request.send();
    },

    timeout: function(request) {
        var errorDoc = createAlert("Data Loading Error", "Error reading the data files.");
        navigationDocument.presentModal(errorDoc);
    },

    folderDataLoaded: function(that) {
        MovieData.cache[that._folder] = JSON.parse(that.responseText);
        LOG.log("folderDataLoaded: Got data for folder " + that._folder);

        Presenter.navigate("Folder");
    },

    saveMovie: function(data) {
        DataReader.movieCache[data.path] = data;
    },

    loadMovie: function(path) {
        return DataReader.movieCache[path];
    }
};
