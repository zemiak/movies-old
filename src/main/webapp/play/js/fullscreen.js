function fullScreenVideoInit(id) {
    var element = document.getElementById(id);
    element.addEventListener("play", fullScreenVideoTrigger, false);
}

function fullScreenVideoTrigger(event) {
    var element = event.target;
    var classToAdd = 'fullscreen-video';

    if ((' '+element.className+' ').indexOf(' '+classToAdd+' ') == -1) {
        element.className = element.className === '' ? classToAdd : element.className + ' ' + classToAdd;
    }

    document.documentElement.webkitRequestFullscreen();
}
