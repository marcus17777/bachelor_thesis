(function () {
  'use strict';

  angular
    .module('myapp.dockerPlugin')
    .controller('dockerImagesCtrl', DockerImagesController);

  /* @ngInject */
  function DockerImagesController($scope, $http, $routeParams, c8yAlert, DockerService, Selector) {
    var dockerService = new DockerService($routeParams.deviceId);
    var selector = new Selector();

    function load() {
      $scope.refreshLoading = true;

     dockerService.getData().then(function (dockerData) {
        $scope.images = dockerData.images;
        $scope.headers = _.keys(dockerData.images[0]);
        $scope.refreshLoading = false;
      });
    }

    function mapToIDs(images) {
      return _.map(images, 'ID');
    }

    function deleteSelected() {
      if (selector.hasSelected()) {
        dockerService.deleteImages(mapToIDs(selector.getSelected()));
        selector.clear()
      }
    }

    function pullImage() {
      dockerService.pullImage(
        $scope.pullImageForm.image
      );
    }
    _.assign($scope, {
      // Data variables
      images: [],
      headers: [],
      refreshLoading: false,
      pullImageForm: {
        image: undefined,
      },

      // Methods
      selector: selector,
      deleteSelected: deleteSelected,
      pullImage: pullImage,
      load: load,
    });

    load();
  }

}());