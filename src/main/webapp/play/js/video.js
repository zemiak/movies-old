function fullScreenVideoInit(id) {
    var element = document.getElementById(id);
    element.addEventListener("click", fullScreenVideoClickTrigger, false);
    element.addEventListener("ended", fullScreenVideoEndedTrigger, false);
}

function fullScreenVideoClickTrigger(event) {
    var elem = event.target;

    if (! elem.playing) {
        elem.play();

        if (elem.requestFullscreen) {
            elem.requestFullscreen();
        } else if (elem.msRequestFullscreen) {
            elem.msRequestFullscreen();
        } else if (elem.mozRequestFullScreen) {
            elem.mozRequestFullScreen();
        } else if (elem.webkitRequestFullscreen) {
            elem.webkitRequestFullscreen();
        }
    }
}

function fullScreenVideoEndedTrigger(event) {
    if (document.exitFullscreen) {
        document.exitFullscreen();
    } else if (document.msExitFullscreen) {
        document.msExitFullscreen();
    } else if (document.mozExitFullScreen) {
        document.mozExitFullScreen();
    } else if (document.webkitExitFullscreen) {
        document.webkitExitFullscreen();
    }
}
