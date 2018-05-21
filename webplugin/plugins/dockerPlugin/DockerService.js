(function() {
  'use strict';

  angular
    .module('myapp.dockerPlugin')
    .service('DockerService', DockerService);
  
  function DockerService(c8yInventory, c8yDeviceControl) {
    return function(deviceID) {

      this.getData = function() {
        return c8yInventory.detail(deviceID).then(_.property('data.c8y_Docker'));
      }

      this.deleteImage = function(ID) {
        return c8yDeviceControl.create({
          deviceId: deviceID,
          description: "Delete image " + ID.toString(),
          c8y_Docker: {
            dockerCommand: "image rm",
            imageID: ID
          }
        });
      }

      this.deleteImages = function(array) {
        return Promise.all(_.map(array, this.deleteImage));
      }

      this.pullImage = function(image) {
        return c8yDeviceControl.create({
          deviceId: deviceID,
          description: "Pull image " + image.toString(),
          c8y_Docker: {
            dockerCommand: "image pull",
            image: image
          }
        });
      }

      this.deleteContainer = function(ID) {
        console.log(ID)
        return c8yDeviceControl.create({
          deviceId: deviceID,
          description: "Delete container " + ID.toString(),
          c8y_Docker: {
            dockerCommand: "container rm",
            containerID: ID
          }
        });
      }

      this.deleteContainers = function(array) {
        return Promise.all(_.map(array, this.deleteContainer));
      }

      this.stopContainer = function(ID) {
        return c8yDeviceControl.create({
          deviceId: deviceID,
          description: "Stop container " + ID.toString(),
          c8y_Docker: {
            dockerCommand: "container stop",
            containerID: ID
          }
        });
      }

      this.stopContainers = function(array) {
        return Promise.all(_.map(array, this.stopContainer));
      }

      this.startContainer = function(ID) {
        return c8yDeviceControl.create({
          deviceId: deviceID,
          description: "Start container " + ID.toString(),
          c8y_Docker: {
            dockerCommand: "container start",
            containerID: ID
          }
        });
      }

      this.startContainers = function(array) {
        return Promise.all(_.map(array, this.startContainer));
      }


      this.createContainer = function(containerOptions, image, containerCmd, containerCmdArgs) {
        if (_.isEmpty(image)) {
          throw new Error("Must specify image");
        }

        return c8yDeviceControl.create({
          deviceId: deviceID,
          description: "Run image " + image,
          c8y_Docker: {
            dockerCommand: "container run",

            containerOptions: containerOptions || "",
            imageName: image,
            containerCmd: containerCmd || "",
            containerCmdArgs: containerCmdArgs || ""
          }
        })
      }
    }
  }
})();