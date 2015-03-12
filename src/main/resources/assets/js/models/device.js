/*global define*/
define([
    'underscore',
    'backbone'
], function (_, Backbone) {
    'use strict';

    var Device = Backbone.Model.extend({
        defaults: {
            _id: 0,
            deviceId: 0,
            configId: '',
            registered: false
        }
    });

    return Device;
});