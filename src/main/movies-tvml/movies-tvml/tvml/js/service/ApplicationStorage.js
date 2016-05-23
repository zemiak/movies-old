var ApplicationStorage = {
    get: function(key, defaultValue) {
        var value = localStorage.getItem("com.zemiak.movies." + key);

        return (!value) ? defaultValue : JSON.parse(value);
    },

    set: function(key, value) {
        localStorage.setItem("com.zemiak.movies." + key, JSON.stringify(value));
    }
};
