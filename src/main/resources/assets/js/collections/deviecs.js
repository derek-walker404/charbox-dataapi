/*global define */
define([
    'underscore',
    'backbone',
    'backboneLocalstorage',
    'models/device'
], function (_, Backbone, Store, Device) {
    'use strict';

    var DeviceCollection = Backbone.Collection.extend({
        model: Device,

        // Save all of the device items under the `"device"` namespace.
        localStorage: new Store('device-backbone')

    });

    return new DeviceCollection();
});