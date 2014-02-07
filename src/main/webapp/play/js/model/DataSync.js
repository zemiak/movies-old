/**
 * Syncing changes to both local and REST storage
 * Backbone.sync = function Sync() {
 *     Backbone.ajaxSync.apply(this, arguments);
 *     return Backbone.localSync.apply(this, arguments);
 * };
 */
var repository = _.extend({}, {
    tables: ['genres', 'series', 'languages', 'movies'],
    timestampUrl: 'http://localhost:8080/movies/rest/movies/version',

    initCollections: function()
    {
        this.languages = new LanguageCollection();
        this.genres = new GenreCollection();
        this.series = new SerieCollection();
        this.movies = new MovieCollection();
    },

    loadFromLocal: function(callback)
    {
        var that = this;

        // a recursive local repo load function, one by one
        var loadRepo = function(index) {
            var name = that.tables[index], model;

            repository[name].fetch({success: function(){
                index++;
                if (index < that.tables.length) {
                    loadRepo(index);
                } else {
                    callback();
                }
            }});
        }

        // start with loading
        loadRepo(0);
    },

    hasLocalData: function()
    {
        // fetch the data from the localStorage

        var filled = true;
        _.each(this.tables, function(table) {
            if (repository[table].isEmpty()) {
                filled = false;
            }
        });

        return filled;
    },

    clearLocalStorage: function()
    {
        window.localStorage.clear();
    },
    
    copyFromServer: function(callback)
    {
        var distantRepositories = [], that = this;

        _.each(this.tables, function(table){
            var Col = Backbone.Collection.extend({
                url: 'http://localhost:8080/movies/rest/' + table
            });

            distantRepositories[table] = new Col();
        });

        this.clearLocalStorage();

        // a recursive repo load function, one by one
        var loadRepo = function(index) {
            var name = that.tables[index], model;

            distantRepositories[name].fetch({success: function(){
                distantRepositories[name].each(function(row){
                    model = new repository[name].model(row.toJSON());
                    repository[name].add(model, {silent: true});
                    model.save();
                });

                index++;
                if (index < that.tables.length) {
                    loadRepo(index);
                } else {
                    // set the timestamp after a complete refresh
                    $.ajax(that.timestampUrl, {
                        dataType: 'html',
                        success: function(data)
                        {
                            window.localStorage.setItem('movies_dataTimestamp', data);
                            console.log('Refreshed data from server, timestamp: ' + data);
                            callback();
                        }
                    });
                }
            }});
        }

        // start with loading
        loadRepo(0);
    },

    load: function(callback) {
        var that = this, 
        localCallback = function(){
            ViewUtils.hideLoading();
            callback();
        }

        this.initCollections();
        this.loadFromLocal(function(){
            if (that.hasLocalData()) {
                // data are here
                callback();

                // ensure, that the data are current
                $.ajax(that.timestampUrl, {
                    dataType: 'html',
                    success: function(data)
                    {
                        localTimestamp = window.localStorage.getItem('movies_dataTimestamp');
                        
                        console.log("Timestamps: local: " + localTimestamp + ", remote: " + data);
                        
                        if (localTimestamp != data) {
                            that.clearLocalStorage();
                            window.location.reload();
                        }
                    }
                });
            } else {
                // this will be called after all repositories were fetched from REST API
                ViewUtils.showLoading();
                that.copyFromServer(localCallback);
                console.log("No local data copy, reading from server...");
            }
        });
    }
});
