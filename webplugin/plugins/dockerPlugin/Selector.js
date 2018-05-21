(function() {
  'use strict';

  angular
    .module('myapp.dockerPlugin')
    .service('Selector', Selector);

    function Selector() {
      return function() {
        var selectedItems = [];

        this.select = function(ID, bool) {
          if (bool) {
            selectedItems = _.concat(selectedItems, ID);
          } else {
            selectedItems = _.pull(selectedItems, ID);
          }
        }

        this.getSelected = function() {
          return selectedItems;
        }

        this.hasSelected = function() {
          return selectedItems.length > 0;
        }

        this.clear = function() {
          selectedItems = [];
        }
      }
    }
})();