
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
<title>Video Presentation</title>
<style type="text/css">
body {
	background: #222;
	color: #eee;
	margin-top: 20px;
	font-family: Arial, "Helvetica Neue", Helvetica, sans-serif;
}

a {
	color: #FFF;
}

a:hover {
	color: yellow;
	text-decoration: underline;
}

#thumbnails img {
	height: 80px;
	border: 4px solid #555;
	padding: 1px;
	margin: 0 10px 10px 0;
}

#thumbnails img:hover {
	border: 4px solid #00ccff;
	cursor: pointer;
}

#player {
	border: 4px solid #444;
	padding: 1px;
}

#controls {
	border: none;
	background: transparent;
	background-size: contain;
	background-repeat: no-repeat;
}

#loadVideo {
	background: none;
	background-image: url("/Ten/icons/load.png");
	background-position: center center;
	background-repeat: no-repeat;
	border: 0px;
	border-radius: 4px;
	background-color: gray;
	cursor: pointer;
	width: 32px;
	height: 32px;
}

#playpause {
	background: none;
	background-image: url("/Ten/icons/play.png");
	background-position: center center;
	background-repeat: no-repeat;
	border: 0px;
	border-radius: 4px;
	background-color: gray;
	cursor: pointer;
	width: 32px;
	height: 32px;
}

#volumeDown {
	background: none;
	background-image: url("/Ten/icons/volDown.png");
	background-position: center center;
	background-repeat: no-repeat;
	border: 0px;
	border-radius: 4px;
	background-color: gray;
	cursor: pointer;
	width: 32px;
	height: 32px;
}

#volumeUp {
	background: none;
	background-image: url("/Ten/icons/volUp.png");
	background-position: center center;
	background-repeat: no-repeat;
	border: 0px;
	border-radius: 4px;
	background-color: gray;
	cursor: pointer;
	width: 32px;
	height: 32px;
}

#stop {
	background: none;
	background-image: url("/Ten/icons/stop.png");
	background-position: center center;
	background-repeat: no-repeat;
	border: 0px;
	border-radius: 4px;
	background-color: gray;
	cursor: pointer;
	width: 32px;
	height: 32px;
}

#mute {
	background: none;
	background-image: url("/Ten/icons/unMute.png");
	background-position: center center;
	background-repeat: no-repeat;
	border: 0px;
	border-radius: 4px;
	background-color: gray;
	cursor: pointer;
	width: 32px;
	height: 32px;
}

#info {
	background: none;
	background-image: url("/Ten/icons/info.png");
	background-position: center center;
	background-repeat: no-repeat;
	border: 0px;
	border-radius: 4px;
	background-color: gray;
	cursor: pointer;
	width: 32px;
	height: 32px;
}
</style>


</head>
<body>

	<div id="presentation" align="center">
		<h2>TEN - Video Presentation</h2>
		<p>
			Created for <a target="_blank" href="http://localhost:8080/Ten/main">Tribal
				Education Network</a>
		</p>
		<br />

		<!-- <form action="${pageContext.request.contextPath}/view/coursedetails/videopresentation/shotdetection" 
	method="post" enctype="multipart/form-data"> -->
		<div id="controls" align="center">
			<video id="player" controls width="640" height="360">
				<!-- <source id="sourceMP4" src="/Ten/videos/sample3.mp4" type="video/mp4" >Video not available!!!</source> -->
				<source id="sourceMP4" src="" type="video/mp4"> </source>
			</video>
			<br />

			<button id="loadVideo" title="Loads a specific video"
				onclick="loadVideo()"></button>
			<button id="playpause" title="Play video" onclick="toggleVideo()"></button>
			<button id="stop" title="Stop video" onclick="stopVideo()"></button>
			<button id="volumeDown" title="Volume down"
				onclick="changeVolume('-')"></button>
			<button id="volumeUp" title="Volume up" onclick="changeVolume('+')"></button>
			<button id="mute" title="Mute audio" onclick="mutePlayer()"></button>
			<button id="info" title="Click here for usage details"
				onclick="detailsPopup()"></button>
			<button id="detect"
				onclick="passRequest('${pageContext.request.contextPath}/view/coursedetails/videopresentation/shotdetection')">Detect
				Shots</button>
		</div>
	</div>
	<br />
	<div id="thumbnails"></div>

	<script>
	//Array that will dynamically retrieve extracted frame numbers from Java 
	//VideoShotAction backend. This is achieved via an ajax call.
	var images = [2000, 2201, 2410, 2454, 2696, 2795, 2993, 4104, 4357, 4555, 4698];
	var video = document.getElementsByTagName("video")[0];
	var videoSource = document.getElementById("sourceMP4");
	//video.controls = false;
	var fileNameSrc = "";	//String to store video name which will be retrieved
	var ppbutton = document.getElementById("playpause");
	function passRequest(url)
	{
		alert("Inside passRequest! " + url);
		
		var ajaxReq = new XMLHttpRequest();
		ajaxReq.onreadystatechange = function() {
			if (ajaxReq.readyState == 4 && ajaxReq.status == 200) {
			      //document.getElementById("demo").innerHTML = xhttp.responseText;
			      console.log(ajaxReq.responseText);
			      var json = JSON.parse(ajaxReq.responseText);
			      alert("Received from Action class: " + json.fileName);
			      console.log(typeof(json.fileName));
			      images = json.fileName.split(',');
			      /*console.log(typeof(jsonArray));
			      alert("Parsed JSON frame number array test: " + jsonArray[0]);*/
			      displayShots();
			    }
		};
		
		var data = "fileName=" + encodeURIComponent(fileNameSrc);//JSON.stringify({"fileName":"TEST"});
		//ajaxReq.open("GET", "view/coursedetails/videopresentation/shotdetection", true);
		ajaxReq.open("POST", url, true);
		//ajaxReq.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		ajaxReq.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		console.log("fileName=" + fileNameSrc);
		//ajaxReq.setRequestHeader("Content-type", "application/json");
		
		ajaxReq.send(data);
		//ajaxReq.send({"fileName": fileNameSrc});
		//ajaxReq.send(tempForm);
		//ajaxReq.send("fileName=" + fileNameSrc);
		
		
	}
	// This function dynamically creates image thumbnails
	// from the first frame numbers received in "image" array
	// from the Server backend logic.
	function displayShots()
	{
		/*var canvas = document.createElement('canvas');
		var w = video.videoWidth * 0.25;
		var h = video.videoHeight * 0.25;
		canvas.width = w;
		canvas.height = h;
		var context = canvas.getContext('2d');*/
		
		for(var i = 0; i < images.length; i++)
		{
			var temp = parseInt(images[i], 10);
			video.currentTime = temp / 23;
			//context.drawImage(video, 0, 0, w, h);
			
			var img = document.createElement('img');
			img.width = "120";		//Fixed value as per trial
			img.height = "80";
			img.id = images[i];
			img.src = "/Ten/frames/" + images[i] + ".png";
			//img.src = canvas.toDataURL("image/png");
			//console.log("Type of canvas data URL: " + typeof(canvas.toDataURL()));
			//console.log("Contents of data URL: " + canvas.toDataURL());
			img.onclick = function(){
				//console.log(typeof(this.id)); //Is a string
				temp = this.id;		//the id of currently clicked thumbnail
				temp = parseInt(temp, 10);
				//console.log(typeof(temp));
				video.currentTime = temp / 23;
				video.play();
			};
			document.getElementById('thumbnails').appendChild(img);
		}	
	}
	
	function stopVideo()
	{
		video.pause();
		video.currentTime = 0;
	}
	
	// Change the volume of the video up or down
	// It is called when either the volumeDown or volumeUp buttons are clicked
	function changeVolume(direction)
	{
		// Rounding current volume to the nearest decimal place
		var volume = Math.floor(video.volume * 10) / 10;
		// Unmute the video if it's muted
		video.muted = false;
		// For volume down/reduce
		if (direction == "-") 
		{
			// If the current volume is <= 0.1, set the volume to be 0
			if (volume <= 0.1) video.volume = 0;
			// Otherwise reduce the volume by 0.1
			else video.volume -= 0.1;
		}
		//Else up/increase volume
		else {
			// If the current volume is >= 0.9, then set it to be 1
			if (volume >= 0.9) video.volume = 1;
			// Otherwise increase the volume by 0.1
			else video.volume += 0.1;
		}	
	}
	
	// This function toggles the mute value and changes the icon accordingly
	// No mute event raised
	function mutePlayer() 
	{
		var mute = document.getElementById("mute");
		if (video.muted) {
			//mute.innerHTML = "mute";
			mute.style.backgroundImage = "url('/Ten/icons/unMute.png')";
			video.muted = false;
		}
		else {
			//mute.innerHTML = "unmute";
			mute.style.backgroundImage = "url('/Ten/icons/mute.png')";
			video.muted = true;
		}
	}
	
	function detailsPopup()
	{
		alert("1. Use the first button to load video\n"
			+ "2. Use other buttons to control playback, volume, etc.\n"
			+ "3. Use Detect Shots to extract shots from the video file");
	}
	
	function loadVideo()
	{
		//Logic for getting the Video file source name from URL
		var queryString = window.top.location.search.substring(1);
		var parameterName = "fileName=";
		ppbutton.style.backgroundImage = "url('/Ten/icons/play.png')";

		if (queryString.length > 0)
		{
			begin = queryString.indexOf(parameterName);
			if (begin != -1) 
			{
				begin += parameterName.length;
				end = queryString.indexOf("&", begin);
				if (end == -1)
				{
					end = queryString.length;
				}
				//The begin and end is after ? in URL
				fileNameSrc = queryString.substring(begin, end);
			}
		}
		if(fileNameSrc !== "")
		{	
			videoSource.setAttribute("src", "/Ten/videos/" + fileNameSrc.toString());
			video.load();
		}
		else
		{
			alert("Invalid or empty file name!!!");
		}	
	}
	
	function toggleVideo()
	{
		if(video.paused || video.ended)
		{
			if(video.ended) video.currentTime = 0;
			ppbutton.style.backgroundImage = "url('/Ten/icons/pause.png')";
			video.play();
		}
		else
		{
			ppbutton.style.backgroundImage = "url('/Ten/icons/play.png')";
			video.pause();
		}	
	}
	//In order to keep in toggle sync with the in built video controls
	//play and pause trigger events and we are listening to those events
	video.addEventListener('play', function() {
		ppbutton.title = "pause";
		ppbutton.style.backgroundImage = "url('/Ten/icons/pause.png')";
		//ppbutton.innerHTML = "pause";
	}, false);
	
	video.addEventListener('pause', function() {
		ppbutton.title = "play";
		//ppbutton.innerHTML = "play";
	}, false);
	
	//this refers to the video which raise pause event
	video.addEventListener('ended', function() {
		ppbutton.style.backgroundImage = "url('/Ten/icons/play.png')";
		this.pause(); 
	}, false);
	
</script>

</body>
</html>