define([
    'jquery',
    'underscore',
    'backbone',
    'collections/devices',
    'text!templates/device-list'
], function($, _, Backbone, Devices, devicesListTemplate) {
    var DeviceListView = Backbone.View.extend({
        el: '#content',
        template: _.template(devicesListTemplate),

        events: { },

        initialize: function() {

            this.listenTo(Devices, 'add', this.addOne);

            Devices.fetch({reset: true});
        },

        render: function() {

            return this;
        },

        addOne: function(device) {

        }

    });
    return DeviceListView;
});