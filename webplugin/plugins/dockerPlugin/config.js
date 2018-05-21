(function () {
  'use strict';

  angular
    .module('myapp.dockerPlugin')
    .config(configure);

  /* @ngInject */
  function configure(c8yViewsProvider) {
    var showIfDocker = ['$routeParams', 'c8yDevices', function ($routeParams, c8yDevices) {
      return c8yDevices.detailCached($routeParams.deviceId).then(function (res) {
        var device = res.data;
        return device && device.c8y_Docker;
      });
    }];

    c8yViewsProvider.when('/device/:deviceId', {
      name: 'Docker Containers',
      icon: 'cubes',
      priority: 1000,
      templateUrl: ':::PLUGIN_PATH:::/dockerContainers/views/index.html',
      controller: 'dockerContainersCtrl',
      showIf: showIfDocker
    });

    c8yViewsProvider.when('/device/:deviceId', {
      name: 'Docker Images',
      icon: 'database',
      priority: 1000,
      templateUrl: ':::PLUGIN_PATH:::/dockerImages/views/index.html',
      controller: 'dockerImagesCtrl',
      showIf: showIfDocker
    });
  }

}());