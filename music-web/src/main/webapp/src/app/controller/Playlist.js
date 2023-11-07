"use strict";

/**
 * Playlist controller.
 */
angular
  .module("music")
  .controller(
    "Playlist",
    function (
      $scope,
      $state,
      $stateParams,
      Restangular,
      Playlist,
      NamedPlaylist
    ) {
      // Load playlist

      Restangular.one("playlist", $stateParams.id)
        .get()
        .then(function (data) {
          $scope.playlist = data;
          console.log(data.tracks[0].artist.name);
          console.log(data.tracks[0].title);
          console.log(data);
        });

      //recommendation
      $scope.recommendation = (recommender) => {
        console.log("reco ", recommender);
        let obj = { artist: [], title: [], recommender: recommender };
        console.log(obj);
        let playlist = $scope.playlist;
        for (let i = 0; i < playlist.tracks.length; i++) {
          obj["artist"].push(playlist.tracks[i].artist.name);
          obj["title"].push(playlist.tracks[i].title);
        }
        let str = JSON.stringify(obj);
        console.log(str, typeof str);

        console.log("recommendation");
        Restangular.one("playlist/recommend")
          .get({ playlist: str })
          .then((resp) => {
            console.log("inside then of recommendation");
            console.log("resp ", resp);
            let st = `<h2>${recommender} Recommendations<h2>` + `<ul>`;
            //let obj=JSON.parse(resp);
            //let arr = obj["similarSongs"];
            //console.log("array",obj);

            for (str in resp["similarSongs"]) {
              st += `<li> ${resp["similarSongs"][str]} </li>`;
            }

            st += `</ul>`;

            document.getElementById("reco").innerHTML = st;
            //$scope.recommendations=st;
          });
      };

      // Play a single track
      $scope.playTrack = function (track) {
        Playlist.removeAndPlay(track);
      };

      // Add a single track to the playlist
      $scope.addTrack = function (track) {
        Playlist.add(track, false);
      };

      // Add all tracks to the playlist in a random order
      $scope.shuffleAllTracks = function () {
        Playlist.addAll(
          _.shuffle(_.pluck($scope.playlist.tracks, "id")),
          false
        );
      };

      // Play all tracks
      $scope.playAllTracks = function () {
        Playlist.removeAndPlayAll(_.pluck($scope.playlist.tracks, "id"));
      };

      // Add all tracks to the playlist
      $scope.addAllTracks = function () {
        Playlist.addAll(_.pluck($scope.playlist.tracks, "id"), false);
      };

      // Like/unlike a track
      $scope.toggleLikeTrack = function (track) {
        Playlist.likeById(track.id, !track.liked);
      };

      // Remove a track
      $scope.removeTrack = function (order) {
        NamedPlaylist.removeTrack($scope.playlist, order).then(function (data) {
          $scope.playlist = data;
        });
      };

      // Delete the playlist
      $scope.remove = function () {
        NamedPlaylist.remove($scope.playlist).then(function () {
          $state.go("main.default");
        });
      };

      // Update UI on track liked
      $scope.$on("track.liked", function (e, trackId, liked) {
        var track = _.findWhere($scope.playlist.tracks, { id: trackId });
        if (track) {
          track.liked = liked;
        }
      });

      // Configuration for track sorting
      $scope.trackSortableOptions = {
        forceHelperSize: true,
        forcePlaceholderSize: true,
        tolerance: "pointer",
        handle: ".handle",
        containment: "parent",
        helper: function (e, ui) {
          ui.children().each(function () {
            $(this).width($(this).width());
          });
          return ui;
        },
        stop: function (e, ui) {
          // Send new positions to server
          $scope.$apply(function () {
            NamedPlaylist.moveTrack(
              $scope.playlist,
              ui.item.attr("data-order"),
              ui.item.index()
            );
          });
        },
      };
    }
  );
