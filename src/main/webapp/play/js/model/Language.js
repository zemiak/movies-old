var LanguageModel = Backbone.Model.extend({
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

    comparator: function(item)
    {
        return item.getDisplayOrder();
    }
});

var LanguageCollection = Backbone.Collection.extend({
    localStorage: new Backbone.LocalStorage('movies_language'),
    model: LanguageModel,

    findById: function(id)
    {
        return this.find(function(item){return item.getId() == id;});
    }
});
