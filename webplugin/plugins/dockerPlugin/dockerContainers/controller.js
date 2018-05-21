(function () {
  'use strict';

  angular
    .module('myapp.dockerPlugin')
    .controller('dockerContainersCtrl', DockerContainersController);

  /* @ngInject */
  function DockerContainersController($scope, $routeParams, c8yAlert, c8yModal, DockerService, Selector) {
    var dockerService = new DockerService($routeParams.deviceId);
    var selector = new Selector();

    function load() {
      $scope.refreshLoading = true;

      dockerService.getData().then(function (dockerData) {

        $scope.containers = _.reduce(dockerData.containers, function(acc, x) {
          return _.concat(acc, _.assign({}, x, { is_active: /^Up/.test(x.status) }))
        }, []);
        $scope.images = dockerData.images;

        $scope.headers = _.keys(dockerData.containers[0]);
        $scope.refreshLoading = false;
      });
    }

    function startSelected() {
      selector.hasSelected() && dockerService.startContainers(selector.getSelected());
      selector.clear();
    }

    function stopSelected() {
      selector.hasSelected() && dockerService.stopContainers(selector.getSelected());
      selector.clear();
    }

    function deleteSelected() {
      var selected = selector.getSelected();

      selector.hasSelected() && c8yModal({
        title: 'Confirm delete?',
        body: "Are you sure you want to delete containers " + selected.toString()
      })
      .then(function() {
        dockerService.deleteContainers(selected);
        c8yAlert.success("Containers " + selected.toString() + " successfully deleted.")
        selector.clear();
      });
    }

    function createContainer() {
      dockerService.createContainer(
        $scope.createContainerForm.runOptions,
        $scope.createContainerForm.image,
        $scope.createContainerForm.containerCommand,
        $scope.createContainerForm.containerCommandArgs
      );
    }

    _.assign($scope, {
      // Data variables
      containers: [],
      images: [],
      headers: [],
      refreshLoading: false,
      createContainerForm: {
        image: undefined,
        runOptions: undefined,
        containerCommand: undefined,
        containerCommandArgs: undefined
      },
      
      // Methods
      selector: selector,
      startSelected: startSelected,
      stopSelected: stopSelected,
      deleteSelected: deleteSelected,
      createContainer: createContainer,
      load: load
    });

    load();
  }
}());