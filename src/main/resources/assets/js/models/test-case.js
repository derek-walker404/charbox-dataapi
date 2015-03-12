/*global define*/
define([
    'underscore',
    'backbone'
], function (_, Backbone) {
    'use strict';

    var TestCase = Backbone.Model.extend({
        defaults: {
            _id: 0,
            name: '',
            uri: '',
            active: true
        }
    });

    return TestCase;
});