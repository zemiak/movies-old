/* global Presenter, Mustache, DataReader, LOG, resourceLoaderLocal */

function FolderTemplate_fillFolders(data, folders) {
    for (var i in folders) {
        var itemData = folders[i];
        var item = {name: itemData.name,
            src: Presenter.options.BaseUrl + "tvml/covers?path=" + encodeURIComponent(itemData.path),
            action: "DataReader.navigate('" + itemData.path + "');"};

        data.folders.push(item);
    }
}

function FolderTemplate_fillMovies(data, movies) {
    for (var i in movies) {
        var itemData = movies[i];
        DataReader.saveMovie(itemData);

        itemData.src = Presenter.options.BaseUrl + "tvml/covers?path=" + itemData.id;
        itemData.action = "MoviePlayer.navigateToMovie('" + itemData.id + "')";

        data.movies.push(itemData);
    }
}

var Template = function() {
    var folder = DataReader.currentFolder;
    LOG.log("Preparing template for folder " + folder);

    var serverData = DataReader.getFolderData(folder);
    var data = {title: serverData.name, folders: [], movies: [], mainFolder: folder};

    LOG.log("Data: ", JSON.stringify(serverData));

    FolderTemplate_fillFolders(data, serverData.folders);
    FolderTemplate_fillMovies(data, serverData.movies);

    var template = resourceLoaderLocal.loadBundleResource("templates/Folder.mustache");
    var html = Mustache.render(template, data);
    LOG.log(html);
    return html;
}
