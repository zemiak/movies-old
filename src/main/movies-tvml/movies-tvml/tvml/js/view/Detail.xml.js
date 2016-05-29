/* global resourceLoaderLocal, Mustache */

var Template = function() {
    var data = MoviePlayer.getCurrentMovieData();
    data.artwork = Presenter.options.BaseUrl + "tvml/covers?path=" + data.id;

    var related = [];
    if ("" !== data.serieKey && "s0" !== data.serieKey) {
        LOG.log("Building related movies for serie " + data.serieKey);
        data.hasRelated = true;
        var serie = DataReader.getFolderData(data.serieKey);
        for (var i in serie.movies) {
            var item = serie.movies[i];

            if (item.id == data.id) {
                continue;
            }
            
            item.artwork = Presenter.options.BaseUrl + "tvml/covers?path=" + item.id;
            related.push(item);
        }
    } else {
        data.hasRelated = false;
    }

    data.related = related;

    var template = resourceLoaderLocal.loadBundleResource("templates/Detail.mustache");
    var html = Mustache.render(template, data);
    LOG.log(html);
    return html;
}
