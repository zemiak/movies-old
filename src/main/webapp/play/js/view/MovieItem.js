var MovieItemView = Backbone.View.extend({
    tagName: 'div',
    className: 'span2 movie-box',

    template: null,
    model: null,

    initialize: function()
    {
        if (window.orientation) {
            if (window.orientation == -90 || window.orientation == 90) {
                // portrait
                this.className = 'span3 movie-box';
            }
        }

        this.template = _.template(ViewUtils.templates.get('movieitem'));
    },

    render: function()
    {
        this.$el.html(this.template(this.model.toJSON()));
        return this;
    },

    setModel: function(model)
    {
        this.model = model;
    }
});
