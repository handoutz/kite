function Hotspot() {
	var base = this;
	
	base.top = 0;
	base.right = 0;
	base.bottom = 0;
	base.left = 0;
	base.destination = 0;
	base.id = 0;
}
function App() {}
function Screen() {}
App.prototype.appDesc = "";
App.prototype.appIcon = "";
App.prototype.appName = "";
App.prototype.id = 0;
App.prototype.screens = {};
Screen.prototype.id = 0;
Screen.prototype.imageUrl = "";
Screen.prototype.name = "";
Screen.prototype.hotspots = {};
var app = new App();

function setCurrentScreen(scr){
	var $icon = $("#icon"), $hotspots = $("#hotspots");
	//alert(JSON.stringify(scr.hotspots));
	$icon.attr('src', scr.imageUrl);
	$hotspots.html('');
	_.each(scr.hotspots, function(v, k){
		var hs = $("<div class='hotspot'/>");
		hs.css({
			top: v.top + "%",
			left: v.left + "%",
			bottom: v.bottom + "%",
			right: v.right + "%"
		});
		hs.data('hotspotref', v);
		hs.click(function(){
			var $this = $(this);
			var hsr = $this.data('hotspotref');
			var nextScreen = _.find(app.screens, function(v){
				return v.id == hsr.destination;
			});
			console.log(JSON.stringify(nextScreen));
			setCurrentScreen(nextScreen);
		});
		hs.appendTo($hotspots);
	});
}

var hotspots = {};
window.onload = function() {
	var appIcon = document.getElementById('icon');
	var $icon = $("#icon");
	if(true) {
		var width = appIcon.width, height = appIcon.height;
		app.appDesc = appData.appDesc;
		app.appName = appData.appName;
		app.id = appData.id;
		_.each(appData.screens, function(v,k){
			var screen = new Screen();
			screen.id = k;
			screen.imageUrl = v.imageUrl;
			screen.name = v.name;
			_.each(v.hotspots, function(vv,kk){
				var hs = new Hotspot();
				hs.top = vv.hotspotArea.top;
				hs.bottom = vv.hotspotArea.bottom;
				hs.left = vv.hotspotArea.left;
				hs.right = vv.hotspotArea.right;
				hs.destination = vv.destination;
				hs.id = vv.id;
				screen.hotspots[vv.id] = hs;
			});
			app.screens[k] = screen;
		});
		setCurrentScreen(_.first(_.toArray(app.screens)));
	}
};