var MovieRouter = Backbone.Router.extend({
    routes: {
        '':             'genres',
        'genres':       'genres',
        'genre/:id':    'genre',
        'serie/:id':    'serie',
        'movie/:id':    'movie',
        'search/:text': 'search'
    },

    // the currently active page
    pageName: 'items',

    playMovieUrl: '/movies/stream',

    /**
     * Lists all genres (the first main page)
     */
    genres: function()
    {
        var view = new ListView({el: $('#genres')}), items = [], v;

        for (var i in repository.genres.models) {
            v = new GenreItemView;
            v.setModel(repository.genres.models[i]);

            items.push(v);
        }
        view.setName('Genres');
        view.setItems(items);

        view.render();
        this.showPage('genres');
    },

    /**
     * Lists all series and standalone movies in the selected genre
     */
    genre: function(id)
    {
        var view = new ListView({el: $('#genre')});
        var items = [], v, series, movies, genreModel, m;

        // a back button
        v = new BackButtonView;
        v.setUrl('#genres');
        items.push(v);

        // series of that genre
        series = repository.series.findByGenreId(id);
        for (var i in series) {
            v = new SerieItemView;
            v.setModel(series[i]);

            items.push(v);
        }

        // standalone movies of that genre
        movies = repository.movies.findByGenreId(id);
        for (i in movies) {
            v = new MovieItemView;
            v.setModel(movies[i]);

            items.push(v);
        }

        genreModel = repository.genres.findById(id);
        view.setName(genreModel.getName());
        view.setItems(items);

        view.render();
        this.showPage('genre');
    },

    /**
     * Lists all movies in the given serie
     */
    serie: function(id)
    {
        var view = new ListView({el: $('#serie')});
        var items = [], v, series, movies, serieModel;

        // get the serie info
        serieModel = repository.series.findById(id);

        // a back button
        v = new BackButtonView;
        v.setUrl('#genre/' + serieModel.getGenreId());
        items.push(v);

        // standalone movies of that genre
        movies = repository.movies.findBySerieId(id);
        for (i in movies) {
            v = new MovieItemView;
            v.setModel(movies[i]);

            items.push(v);
        }

        view.setName(serieModel.getName());
        view.setItems(items);

        view.render();
        this.showPage('serie');
    },

    /**
     * Shows a movie detail page
     */
    movie: function(id)
    {
        var view = new MovieDetailView({el: $('#movie')}), movieModel;

        movieModel = repository.movies.findById(id);
        view.setModel(movieModel);
        view.render();

        this.showPage('movie');
    },

    searchKeydown: function(event)
    {
        if (event.keyCode == 13) {
            this.search($('#searchBox')[0].value);
        }
    },

    search: function(text)
    {
        if (! text) {
            text = $('#searchBox')[0].value;
        }

        var view = new ListView({el: $('#search')});
        var items = [];
        var v;

        // a back button
        var backv = new BackButtonView;
        backv.setUrl('#genres');
        items.push(backv);

        var srepo = new SerieSearchCollection;
        var that = this;
        srepo.setText(text);
        srepo.fetch({success: function(){
            srepo.forEach(function(serie){
                v = new SerieItemView;
                v.setModel(serie);

                items.push(v);
            });
            
            var mrepo = new MovieSearchCollection;
            mrepo.setText(text);
            mrepo.fetch({success: function(){
                mrepo.forEach(function(movie){
                    v = new MovieItemView;
                    v.setModel(movie);

                    items.push(v);
                });
                
                view.setName('Search results');
                view.setItems(items);

                view.render();
                that.showPage('search');
                that.navigate('#search/' + text, {trigger: false, replace: true});
            },
            error: function(collection, error, options){
                console.error("Collection", collection);
                console.error("Error", error);
                console.error("Options", options);
                alert("Search error: movies");
            }});
        },
        error: function(collection, error, options){
            console.error("Collection", collection);
            console.error("Error", error);
            console.error("Options", options);
            alert("Search error: series:" + error);
        }});
    },

    showPage: function(id)
    {
        var pages = ['genres', 'genre', 'serie', 'movie', 'about', 'search'];

        if (this.pageName == id) return;

        for (var i = 0; i < pages.length; i++) {
            $('#' + pages[i]).css('display', ((pages[i] == id) ? 'block' : 'none'))
        }

        this.pageName = id;

        // scroll away the URL line in Mobile Safari
        window.scrollTo && window.scrollTo(0, 1);
    },

    refreshFiles: function()
    {
        var that = this;

        ViewUtils.showLoading();
        $.ajax(this.refreshFilesUrl, {
            dataType: 'json',
            success: function(data)
            {
                ViewUtils.hideLoading();
                that.about();
            }
        });
    },

    playMovie: function(id)
    {
        ViewUtils.showLoading('Spúšťam prehrávanie...');
        setTimeout(function(){ViewUtils.hideLoading();}, 500);
        window.location = this.playMovieUrl + '?id=' + encodeURIComponent(id);
    },

    refreshData: function()
    {
        repository.clearLocalStorage();
        window.location.reload();
    }
});
