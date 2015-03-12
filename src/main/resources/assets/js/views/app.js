/*global define*/
define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/home-content.html',
    'common'
], function ($, _, Backbone, homeContentTemplate, Common) {
    'use strict';

    var AppView = Backbone.View.extend({

        el: '#content',

        events: { },

        initialize: function () {
            this.render();
        },

        render: function () {
            this.$el.html(homeContentTemplate);
        }
    });

    return AppView;
});