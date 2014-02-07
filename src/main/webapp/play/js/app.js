var app;

// DOM and all the libraries are ready
$(document).ready(function(){
    // device
    var device = 'tablet'; // tablet or phone

    // a naive device detection
    if (/iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
        device = 'phone';
    }

    // load templates
    ViewUtils.templates.load(device, function() {

        // refresh the data if the local storage is empty
        repository.load(function(){

            // initialize routing
            app = new MovieRouter();
            Backbone.history.start();

            // spin up the movie HDD
            $.ajax({url: app.spinDriveUrl, cache: false});

            // scroll away the URL line in Mobile Safari
            window.scrollTo && window.scrollTo(0, 1);
        });
    });

});
