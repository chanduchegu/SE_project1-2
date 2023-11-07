"use strict";

/**
 * Named playlist service.
 */
angular
  .module("music")
  .factory(
    "NamedPlaylist",
    function ($rootScope, $modal, Restangular, toaster) {
      $rootScope.playlists = [];
      var service = {};

      service = {
        update: function () {
          Restangular.one("playlist")
            .get({
              limit: 1000,
            })
            .then(function (data) {
              $rootScope.playlists = data.items;
            });
        },

        addToPlaylist: function (playlist, tracks) {
          Restangular.one("playlist/" + playlist.id + "/multiple")
            .put({
              ids: _.pluck(tracks, "id"),
              clear: false,
            })
            .then(function () {
              toaster.pop(
                "success",
                "Track" +
                  (tracks.length > 1 ? "s" : "") +
                  " added to " +
                  playlist.name,
                _.pluck(tracks, "title").join("\n")
              );
            });
        },

        removeTrack: function (playlist, order) {
          return Restangular.one("playlist/" + playlist.id, order).remove();
        },

        remove: function (playlist) {
          return Restangular.one("playlist/" + playlist.id)
            .remove()
            .then(function () {
              $rootScope.playlists = _.reject(
                $rootScope.playlists,
                function (p) {
                  return p.id === playlist.id;
                }
              );
            });
        },

        moveTrack: function (playlist, order, neworder) {
          return Restangular.one("playlist/" + playlist.id, order).post(
            "move",
            {
              neworder: neworder,
            }
          );
        },

        createPlaylist: function (tracks) {
          $modal
            .open({
              templateUrl: "partial/modal.createplaylist.html",
              controller: function ($scope, $modalInstance) {
                "ngInject";
                $scope.ok = function (name) {
                  //console.log(name,isPrivate);

                  // if (isPrivate === "1") {
                  //   isPrivate = true;
                  // } else {
                  //   isPrivate = false;
                  // }
                  $modalInstance.close(name);
                };

                $scope.cancel = function () {
                  $modalInstance.dismiss("cancel");
                };
              },
            })
            .result.then(function (name) {
              //console.log("inside then", name);
              const myArray = name.split("_");
              let isPrivate = myArray[1];
              //console.log(myArray);
              if (isPrivate === "1") {
                isPrivate = true;
                console.log(isPrivate);
              } else {
                isPrivate = false;
              }
              //console.log("is private",isPrivate);
              Restangular.one("playlist")
                .put({
                  name: myArray[0],
                  isPrivate: isPrivate,
                })
                .then(function (data) {
                  console.log("data-item", data.item);
                  $rootScope.playlists.push(data.item);
                  service.addToPlaylist(data.item, tracks);
                  toaster.pop("success", "Playlist created", name);
                });
            });
        },
      };

      return service;
    }
  );
