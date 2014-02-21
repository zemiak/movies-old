var MovieModel = Backbone.Model.extend({
    getId: function()
    {
        return this.get('id');
    },

    getName: function()
    {
        return this.get('name');
    },

    getPictureFileName: function()
    {
        return this.get('pictureFileName');
    },

    getFileName: function()
    {
        return this.get('fileName');
    },

    getGenreId: function()
    {
        return this.get('genreId');
    },

    getSerieId: function()
    {
        return this.get('serieId');
    },

    getLanguage: function()
    {
        return this.get('language');
    },

    getSubtitles: function()
    {
        return this.get('subtitles');
    },

    getOriginalName: function()
    {
        return this.get('originalName');
    },

    getOriginalLanguage: function()
    {
        return this.get('originalLanguage');
    },

    getUrl: function()
    {
        return this.get('url');
    },

    getDisplayOrder: function()
    {
        return this.get('displayOrder');
    },

    getDescription: function()
    {
        return this.get('description');
    },

    comparator: function(item)
    {
        return item.getDisplayOrder();
    }
});

var MovieCollection = Backbone.Collection.extend({
    localStorage: new Backbone.LocalStorage('movies_movie'),
    model: MovieModel,

    findById: function(id)
    {
        return this.find(function(item){return item.getId() == id;});
    },

    findByGenreId: function(id)
    {
        id = parseInt(id, 10);
        if (id == 0) {
            console.log('by genre', id);
            return this.filter(function(item){
                console.log(parseInt(item.getGenreId()));
                return item.getGenreId() == null || parseInt(item.getGenreId()) == 0;
            });
        } else {
            return this.filter(function(item){
                return item.getGenreId() == id && (item.getSerieId() == null || parseInt(item.getSerieId()) == 0);
            });
        }
    },

    findBySerieId: function(id)
    {
        if (id == 0) {
            return this.filter(function(item){
                return (item.getSerieId() == null || parseInt(item.getSerieId()) == 0) && item.getGenreId() != 17;
            });
        } else {
            return this.filter(function(item){return item.getSerieId() == id;});
        }
    }
});
