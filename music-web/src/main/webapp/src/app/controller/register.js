'use strict';

/**
 * Register controller.
 */
angular.module('music').controller('Register', function($rootScope, $scope, $state, $dialog, User, Playlist, NamedPlaylist, Websocket, Restangular) {
  $scope.register = function() {
        console.log($scope.user)
        var promise = null;

        promise = Restangular
              .one('user')
              .put($scope.user);

        promise.then( function() {
          $state.transitionTo('login');
          });
    }
//    , function() {
//      var title = 'Login failed';
//      var msg = 'Username or password invalid';
//      var btns = [{ result:'ok', label: 'OK', cssClass: 'btn-primary' }];

//      $dialog.messageBox(title, msg, btns);
//    });
  });