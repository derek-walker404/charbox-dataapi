/*global require*/
'use strict';

// Require.js allows us to configure shortcut alias
require.config({
    baseUrl: "js",
    // The shim config allows us to configure dependencies for
    // scripts that do not call define() to register a module
    shim: {
        underscore: {
            exports: '_'
        },
        backbone: {
            deps: [
                'underscore',
                'jquery'
            ],
            exports: 'Backbone'
        },
        backboneLocalstorage: {
            deps: ['backbone'],
            exports: 'Store'
        }
    },
    paths: {
        jquery: 'lib/jquery.min',
        underscore: 'lib/underscore-min',
        backbone: 'lib/backbone-min',
        backboneLocalstorage: 'lib/backbone.localStorage-min',
        text: 'lib/text'
    }
});

require([
    'backbone',
    'views/app',
    'routers/app-router'
], function (Backbone, AppView, Workspace) {
    /*jshint nonew:false*/
    // Initialize routing and start Backbone.history()
    new Workspace();
    Backbone.history.start();

    // Initialize the application view
    new AppView();
});


(function ($) {
    $(function () {
        $('.button-collapse').sideNav();
    });
})(jQuery);