var ListView = Backbone.View.extend({
    tagName: 'span',
    template: null,
    items: null,

    initialize: function()
    {
        if (window.orientation) {
            if (window.orientation == -90 || window.orientation == 90) {
                // portrait
            }
        }

        this.template = _.template(ViewUtils.templates.get('list'));
    },

    render: function()
    {
        var html = '';

        this.$el.html(this.template({name: this.name, items: ''}));

        for (var i in this.items) {
            this.items[i].render();
            this.$el.append(this.items[i].$el);
        }

        return this;
    },

    setItems: function(items)
    {
        this.items = items;
    },

    setName: function(name)
    {
        this.name = name;
    }
});
