var parameters = getParameters();

var startLat;
var startLng;

if( parameters.lat === undefined ) {
	startLat = 49.3033998;
} else {
	startLat = parameters.lat;
}

if( parameters.lng === undefined ) {
	startLng = 7.26917;
} else {
	startLng = parameters.lng;
}

var map = L.map( 'map', {
  center: [startLat, startLng],
  minZoom: 2,
  zoom: 11
});

//const baseURL = "http://tommiswelt.ddns.net:12567/";
const baseURL = "http://aqm.ddns.net:12567/";

/*
var map = L.map( 'map', {
  center: [49.3033998, 7.26917],
  minZoom: 2,
  zoom: 10
});
*/
/*
L.tileLayer( 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
 attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'//,
 //subdomains: ['a','b','c']
}).addTo( map );
*/

L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
	maxZoom: 18,
	attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
		'<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
		'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
	id: 'mapbox.streets'
}).addTo(map);


var blueIcon = L.icon({
	iconUrl: 'Points/pointBlue.png',
	iconSize:     [24, 24], // size of the icon
	iconAnchor:   [12, 12], // point of the icon which will correspond to marker's location
	popupAnchor:  [0, 0] // point from which the popup should open relative to the iconAnchor
});
var greenIcon 	= L.icon({iconUrl:'Points/pointGreen.png', iconSize:[24, 24],iconAnchor:[12, 12],popupAnchor:[0, 0]});
var yellowIcon	= L.icon({iconUrl:'Points/pointYellow.png', iconSize:[24, 24],iconAnchor:[12, 12],popupAnchor:[0, 0]});
var orangeIcon	= L.icon({iconUrl:'Points/pointOrange.png', iconSize:[24, 24],iconAnchor:[12, 12],popupAnchor:[0, 0]});
var redIcon 	= L.icon({iconUrl:'Points/pointRed.png', iconSize:[24, 24],iconAnchor:[12, 12],popupAnchor:[0, 0]});

/*
0-10 % Abweichung von Minimum	:	redIcon
Below Average					:	orangeIcon
+- 10% um Average				:	yellowIcon
Above Average					:	greenIcon
0-10% Abweichung von Maximum	:	blueIcon


*/

var avgPPM;
fetchAvgPPM();
var minPPM;
fetchMinPPM();
var maxPPM;
fetchMaxPPM();

catchData().catch(error => {
	console.log('Error!');
	console.error(error);
});

var oldCenter = map.getCenter();
var oldZoom = map.getZoom();
var markerClusters = L.layerGroup();//L.markerClusterGroup();

map.locate({setView: true, maxZoom: 11});
map.on('locationerror', onLocationError);


//Hander, wenn Karte bewegt wird
map.on('moveend', function(event) {   
	//TODO: Distanz anhand des Zooms festlegen
	if( oldCenter.distanceTo(map.getCenter()) > 500 || map.getZoom() != oldZoom) {
		deleteData();
		catchData().catch(error => {
			console.log('Error!');
			console.error(error);
		});
		oldCenter = map.getCenter();
		oldZoom = map.getZoom();
	}
	
	
	
});


// Refresh Data all 60 seconds
setInterval(function(){
		fetchAvgPPM();
		fetchMaxPPM();
		fetchMinPPM();
		
		catchData().catch(error => {
			console.log('Error!');
			console.error(error);
		});
	}, 60000);


async function catchData(){
	//const url = baseURL + "values";
	//const url = baseURL + "valuesNew?accuracy=100000";
	//const url = baseURL + "localValues?lat=" + map.getCenter().lat + "&lng=" + map.getCenter().lng + "&zoom=" + map.getZoom();
	const url = baseURL + "localValues2?lat=" + map.getCenter().lat + "&lng=" + map.getCenter().lng + "&zoom=" + map.getZoom() + "&accuracy=" + 10;
	//console.log(url);
	
	const response = await fetch(url, {
		mode: 'same-origin',
		method: "GET",
		headers: {
			"Accept": "application/json"
		}
	});
	const data = await response.json();
	console.log(data);
	
	data.forEach(element => {
		var ppm = element.ppm;
		var usedIcon = blueIcon;
		let date = new Date( Date.parse(element.date) );
		popup = "<b>" + element.ppm + "</b><br />" + date.toLocaleString("de-DE") + "<br />" + element.address;
		
		console.log("ppm: " + ppm);
		console.log("avg: " + avgPPM);
		console.log("min: " + minPPM);
		console.log("max: " + maxPPM);
		

		if (ppm < minPPM * 1.10) {
			
			usedIcon = blueIcon;
		} else if (ppm > maxPPM * 0.90) {
			usedIcon = redIcon;
		} else if (ppm >= avgPPM * 0.90 && ppm <= avgPPM * 1.10) {
			usedIcon = yellowIcon;
		} else if (ppm < avgPPM) {
			usedIcon = greenIcon;
		} else if (ppm > avgPPM) {
			usedIcon = orangeIcon;
		}

		console.log(usedIcon);

		
		var marker = L.marker( [element.lat, element.lng], {icon: usedIcon} ).bindPopup( popup );
		markerClusters.addLayer( marker );
	});
	
	map.addLayer( markerClusters );
}


async function fetchAvgPPM(){
	const url = baseURL + "avgPPM";
	
	const response = await fetch(url, {
		mode: 'same-origin',
		method: "GET",
		headers: {"Accept": "application/json"}
	});
	avgPPM = await response.json();	
}

async function fetchMaxPPM(){
	const url = baseURL + "maxPPM";
	
	const response = await fetch(url, {
		mode: 'same-origin',
		method: "GET",
		headers: {"Accept": "application/json"}
	});
	maxPPM = await response.json();	
}

async function fetchMinPPM(){
	const url = baseURL + "minPPM";
	
	const response = await fetch(url, {
		mode: 'same-origin',
		method: "GET",
		headers: {"Accept": "application/json"}
	});
	minPPM = await response.json();	
}


function deleteData(){
	markerClusters.clearLayers();
}

function onLocationError(e) {
	//Wenn die Location nicht geladen werden kann
	catchData().catch(error => {
		console.log('Error!');
		console.error(error);
	});
}


	
function getParameters() {
	var parameterFragments = location.search.substr(1).split("&");
	var parameters = {}
	for(var i = 0; i < parameterFragments.length; i++)
	{
		var splittedParameter = parameterFragments[i].split("=");
		parameters[splittedParameter[0]] = decodeURIComponent(splittedParameter[1]);
	}
	return parameters;
}





