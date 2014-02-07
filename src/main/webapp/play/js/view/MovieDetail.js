var MovieDetailView = Backbone.View.extend({
    tagName: 'span',
    template: null,
    model: null,
    navigatePage: null,

    initialize: function()
    {
        if (window.orientation) {
            if (window.orientation == -90 || window.orientation == 90) {
                // portrait
            }
        }

        this.template = _.template(ViewUtils.templates.get('moviedetail'));
    },

    render: function()
    {
        this.initializeSelectsOptions();
        this.setBackUrl();
        this.$el.html(this.template(this.model.toJSON()));
        return this;
    },

    setBackUrl: function()
    {
        if (this.model.getSerieId()) {
            this.model.set('_backUrl', '#serie/' + this.model.getSerieId());
        } else {
            this.model.set('_backUrl', '#genre/' + this.model.getGenreId());
        }
    },

    setModel: function(model)
    {
        this.model = model;
    },

    initializeSelectsOptions: function()
    {
        var model = this.model;

        // genres
        var optionsGenres = repository.genres.map(function(item){
            if (item.getId() != model.getGenreId()) {
                return '<option value="' + item.getId() + '">' + item.getName() + '</option>';
            } else {
                return '<option value="' + item.getId() + '" selected>' + item.getName() + '</option>';
            }
        });
        model.set('_genre_options', optionsGenres.join(' '));

        // series
        var optionsSeries = repository.series.map(function(item){
            if (item.getId() != model.getSerieId()) {
                return '<option value="' + item.getId() + '">' + item.getName() + '</option>';
            } else {
                return '<option value="' + item.getId() + '" selected>' + item.getName() + '</option>';
            }
        });
        model.set('_serie_options', optionsSeries.join(' '));

        // language
        var optionsLang = repository.languages.map(function(item){
            if (item.getId() != model.getLanguage()) {
                return '<option value="' + item.getId() + '">' + item.getName() + '</option>';
            } else {
                return '<option value="' + item.getId() + '" selected>' + item.getName() + '</option>';
            }
        });
        model.set('_language_options', optionsLang.join(' '));

        // subtitles
        var optionsSub = repository.languages.map(function(item){
            if (item.getId() != model.getSubtitles()) {
                return '<option value="' + item.getId() + '">' + item.getName() + '</option>';
            } else {
                return '<option value="' + item.getId() + '" selected>' + item.getName() + '</option>';
            }
        });
        model.set('_subtitles_options', optionsSub.join(' '));

        // original language
        var optionsOrig = repository.languages.map(function(item){
            if (item.getId() != model.getOriginalLanguage()) {
                return '<option value="' + item.getId() + '">' + item.getName() + '</option>';
            } else {
                return '<option value="' + item.getId() + '" selected>' + item.getName() + '</option>';
            }
        });
        model.set('_original_language_options', optionsOrig.join(' '));
    }
});
