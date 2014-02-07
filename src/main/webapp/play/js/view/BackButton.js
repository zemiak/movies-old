var BackButtonView = Backbone.View.extend({
    tagName: 'div',
    className: 'span2 movie-box',

    template: null,
    url: null,

    initialize: function()
    {
        if (window.orientation) {
            if (window.orientation == -90 || window.orientation == 90) {
                // portrait
                this.className = 'span3 movie-box';
            }
        }

        this.template = _.template(ViewUtils.templates.get('backbutton'));
    },

    render: function()
    {
        this.$el.html(this.template({url: this.url}));
        return this;
    },

    setUrl: function(url)
    {
        this.url = url;
    }
});
