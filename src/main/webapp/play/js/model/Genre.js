var GenreModel = Backbone.Model.extend({
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

    getDisplayOrder: function()
    {
        return this.get('displayOrder');
    },

    getProtected: function()
    {
        return this.get('protected1');
    },

    comparator: function(item)
    {
        return item.getDisplayOrder();
    }
});

var GenreCollection = Backbone.Collection.extend({
    localStorage: new Backbone.LocalStorage('movies_genre'),
    model: GenreModel,

    findById: function(id)
    {
        return this.find(function(item){return item.getId() == id;});
    }
});
