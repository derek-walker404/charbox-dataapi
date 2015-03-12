define([
    'backbone',
    'views/app'
], function(Backbone, AppView) {
    var AppRouter = Backbone.Router.extend({
        routes: {
            "*path": "defaultRoute"
        },

        defaultRoute: function() {
            console.log('routing home');
            new AppView();
        }
    });
    return AppRouter;
});