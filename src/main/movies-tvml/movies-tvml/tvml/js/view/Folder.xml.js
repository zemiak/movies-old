/* global Presenter, Mustache, DataReader, LOG, resourceLoaderLocal */

function FolderTemplate_fillFolders(data, folders) {
    for (var i in folders) {
        var itemData = folders[i];
        var item = {name: itemData.name,
            src: Presenter.options.BaseUrl + "tvml/covers?path=" + encodeURIComponent(itemData.path),
            action: "DataReader.read('" + itemData.path + "');"};

        data.folders.push(item);
    }
}

function FolderTemplate_fillMovies(data, movies) {
    for (var i in movies) {
        var itemData = movies[i];
        DataReader.saveMovie(itemData);

        var item = {name: itemData.name,
            src: Presenter.options.BaseUrl + "tvml/covers?path=" + itemData.id,
            action: "MoviePlayer.play('" + itemData.path + "')"};

        data.movies.push(item);
    }
}

var Template = function() {
    var folder = DataReader.currentFolder;
    LOG.log("Preparing template for folder " + folder);

    var serverData = DataReader.getFolderData(folder);
    var data = {title: serverData.title, folders: [], movies: [], mainFolder: folder};

    LOG.log("Data: ", serverData);

    FolderTemplate_fillFolders(data, serverData.folders);
    FolderTemplate_fillMovies(data, serverData.movies);

    var template = resourceLoaderLocal.loadBundleResource("templates/Folder.mustache");
    var html = Mustache.render(template, data);
    LOG.log(html);
    return html;
}
