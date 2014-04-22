function fullScreenVideoInit(id) {
    var element = document.getElementById(id);
    element.addEventListener("click", fullScreenVideoTrigger, false);
}

function fullScreenVideoTrigger(event) {
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
