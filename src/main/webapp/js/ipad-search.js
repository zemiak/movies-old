function searchUrl() {
    var url = "searchResults.xhtml?query=" + encodeURIComponent($("#searchQuery").value());
    console.log("Redirecting to " + url);
    return url;
}

$.ready(function(){
    $("#searchQuery").keyup(function(e) {
        console.log("keyUp", e);
        var charCode = (typeof e.which === "number") ? e.which : e.keyCode;
        if (charCode === 13) {
            e.preventDefault();
            window.location = searchUrl();
        }
    });
});
