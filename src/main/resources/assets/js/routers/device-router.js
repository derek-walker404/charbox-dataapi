/*global define*/
define([
    'jquery',
    'backbone',
    'views/devices'
], function ($, Backbone, Devices) {
    'use strict';

    var DeviceRouter = Backbone.Router.extend({
        routes: {
            't/devices': 'deviceList'
        },

        deviceList: function() {
            new
        }
    });

    return DeviceRouter;
});