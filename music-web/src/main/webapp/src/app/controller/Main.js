"use strict";

/**
 * Main controller.
 */
angular
  .module("music")
  .controller(
    "Main",
    function ($rootScope, $state, $scope, Playlist, Album, Restangular) {
      $scope.partyMode = Playlist.isPartyMode();

      // Keep party mode in sync
      $scope.data = function (platform) {
        console.log("the platform is ", platform);
        let query = document.getElementById("searchbar").value;
        console.log(query);
        console.log("inside data ");
        Restangular.one("search/getData")
          .get({ searchQuery: query, platform: platform })
          .then((resp) => {
            console.log(resp);
            let arr;
            if (platform === "lastfm") {
              arr = resp.results.trackmatches.track;
            } else if (platform === "spotify") {
              arr = resp.tracks.items;
            }
            console.log(arr[0].name);
            // let val = arr.map((elem)=>{

            // });
            let st = `Search results from ${platform}` + `<ul>`;
            for (let i = 0; i < Math.min(10, arr.length); i++) {
              st += `<li>${arr[i].name}</li>`;
            }
            st += "</ul>";
            document.getElementById("bend").innerHTML = st;
          });
      };

      $rootScope.$on("playlist.party", function (e, partyMode) {
        $scope.partyMode = partyMode;
      });

      // Start party mode
      $scope.startPartyMode = function () {
        Playlist.party(true, true);
        $state.transitionTo("main.playing");
      };

      // Stop party mode
      $scope.stopPartyMode = function () {
        Playlist.setPartyMode(false);
      };

      // Clear the albums cache if the previous state is not main.album
      $scope.$on("$stateChangeStart", function (e, to, toParams, from) {
        if (to.name == "main.music.albums" && from.name != "main.album") {
          Album.clearCache();
        }
      });
    }
  );
