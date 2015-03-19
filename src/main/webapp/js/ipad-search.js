function searchClick() {
    window.location = "searchResults.xhtml?query=" + encodeURIComponent($("#query").value());
}

function searchKeyUp(e) {
    console.log("keyUp", e);
    var charCode = (typeof e.which === "number") ? e.which : e.keyCode;
    if (charCode === 13) {
        searchClick();
    }
}
