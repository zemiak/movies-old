var AboutView = Backbone.View.extend({
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

        this.template = _.template(ViewUtils.templates.get('about'));
    },

    render: function()
    {
        var html = '';

        this.$el.html(this.template({version: '0.1a'}));

        return this;
    }
});
