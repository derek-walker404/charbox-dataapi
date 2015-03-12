/*global define */
define([
    'underscore',
    'backbone',
    'backboneLocalstorage',
    'models/testcase'
], function (_, Backbone, Store, TestCase) {
    'use strict';

    var TestCaseCollection = Backbone.Collection.extend({
        model: TestCase,

        // Save all of the test case items under the `"testcase"` namespace.
        localStorage: new Store('testcase-backbone')

    });

    return new TestCaseCollection();
});