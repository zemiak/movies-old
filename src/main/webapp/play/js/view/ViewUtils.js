var ViewUtils = _.extend({}, {
    showLoading: function(messageText)
    {
        if (! messageText) {
            messageText = 'Čakajte...';
        }

        $.blockUI({
            css: { 
                border: 'none', 
                padding: '15px', 
                backgroundColor: '#000', 
                '-webkit-border-radius': '10px', 
                '-moz-border-radius': '10px', 
                opacity: .5, 
                color: '#fff' 
            },
            message: messageText
        }); 
    },

    hideLoading: function()
    {
        $.unblockUI();
    },

    templates: {
        // Hash of preloaded templates for the app
        templates: {},
     
        // Recursively pre-load all the templates for the app.
        // This implementation should be changed in a production environment:
        // All the template files should be concatenated in a single file.
        load: function(subdir, callback) {
            var that = this, names = ['genre', 'list', 'moviedetail', 'movieitem',
                'serie', 'backbutton'];;
     
            var loadTemplate = function(index) {
                var name = names[index];
                $.get('template/' + subdir + '/' + name + '.underscore', function(data) {
                    that.templates[name] = data;
                    index++;
                    if (index < names.length) {
                        loadTemplate(index);
                    } else {
                        callback();
                    }
                });
            }
     
            loadTemplate(0);
        },
     
        // Get template by name from hash of preloaded templates
        get: function(name) {
            return this.templates[name];
        }
    }
});
