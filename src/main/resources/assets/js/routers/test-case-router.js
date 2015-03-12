/*global define*/
define([
    'jquery',
    'backbone',
    'collections/testcases',
    'common'
], function ($, Backbone, TestCases, Common) {
    'use strict';

    var TestCaseRouter = Backbone.Router.extend({
        routes: {
            '*filter': 'setFilter'
        },

        setFilter: function (param) {
            // Set the current filter to be used
            Common.TestCaseFilter = param || '';

            // Trigger a collection filter event, causing hiding/unhiding
            // of the test case view items
            TestCases.trigger('filter');
        }
    });

    return TestCaseRouter;
});