var SerieModel = Backbone.Model.extend({
    getId: function()
    {
        return this.get('id');
    },

    getName: function()
    {
        return this.get('name');
    },

    getSearch: function()
    {
        return this.get('search');
    },

    getPictureFileName: function()
    {
        return this.get('pictureFileName');
    },

    getDisplayOrder: function()
    {
        return this.get('displayOrder');
    },

    getGenreId: function()
    {
        return this.get('genreId');
    },

    comparator: function(item)
    {
        return item.getDisplayOrder();
    }
});

var SerieCollection = Backbone.Collection.extend({
    localStorage: new Backbone.LocalStorage('movies_serie'),
    model: SerieModel,

    findById: function(id)
    {
        return this.find(function(item){return item.getId() == id;});
    },

    findByGenreId: function(id)
    {
        return this.filter(function(item){return item.getGenreId() == id;});
    },

    findBySearch: function(text)
    {
        text = $.trim(text).toLowerCase();
        return this.filter(function(item){
            var itemText = item.getSearch();
            return (itemText.toLowerCase().indexOf(text) > -1);
        });
    }
});
