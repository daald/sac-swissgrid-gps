# sac-swissgrid-gps
This project is for converting Routes from the SAC Tourenportal https://www.sac-cas.ch/de/huetten-und-touren/sac-tourenportal/ to GPX tracks.

## Downloading the source file

Unfortunately, you need to login to get the source data. To get a file, do the following steps:

1. Go to a tour page, like https://www.sac-cas.ch/de/huetten-und-touren/sac-tourenportal/8601/snowshoe_tour
2. Click on the tour in the list
3. Login, if not already done
4. Open the developer tool of you browser. Try F12 if you don't know how
5. Go to the network tab and filter XHR
6. Click on the line in the map, maybe try it in the big map. Doesn't matter which part of the line
7. There should be one or more calls to a url like https://www.sac-cas.ch/de/?type=1567765346410&tx_usersaccas2020_sac2020[routeId]=2927&output_lang=de
8. Save to contents of this file in your developer tool, using the clipboard or any other way you know
9. Save the file into the data directory of the downloaded project

## converting in IDE:

1. Run the main class ch.alder.swisstoposacgpx.Main with the .json file as argument (I assume you know how to handle java)

## converting using build:

1. run `./gradlew jar`
2. run `java -jar ./build/libs/swissgrid-gps-all.jar <yourfile>.json`

## Working with Schweizmobil files

Schweizmobil is also supported, for example from the following link, which should show a route:
https://map.wanderland.ch/?lang=de&bgLayer=pk&resolution=9.88&photos=yes&logo=yes&detours=yes&season=summer&E=2692273&N=1239183&layers=Station&trackId=2571626

1. Use the developer tool of your browser for finding a url like https://map.wanderland.ch/api/4/tracks/2571626, or take this link and replace the number by the trackId parameter from the first URL
2. Download this file
3. Convert the file using one of the above commands

## Disclaimer:

This is not a well-finished project, and I don't have the intention to finish this. It is more a proof of concept but the result is still a fully-working gpx file with the correct coordinates. Feel free to continue my work and let me know if you have something to merge ;)

## Third-party:

This project uses the coordinate converter from Swisstopo, which was released under the MIT Licence.

See https://github.com/ValentinMinder/Swisstopo-WGS84-LV03 for more information

## License

MIT Licence like the souce file from Swisstopo. See LICENSE for more information
