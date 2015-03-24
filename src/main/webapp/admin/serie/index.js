$(document).ready(function() {
    $('#grid').dataTable( {
        "ajax": "/movies/rest/series",
        "pagingType": "full",
        dom: 'T<"clear">lfrtip',
        tableTools: {
            "sRowSelect": "single",
            aButtons: []
        },
        "columns": [
            { "data": "id" },
            { "data": "name" },
            { "data": "genre" },
            { "data": "displayOrder" },
            { "data": "created" }
        ]
    });

    $('#grid tbody').on('dblclick', 'tr', function() {
	editClick();
    });
});

function editClick() {
    var id = DataTablesExt.getSelectedId("grid");
    
    if (null === id) {
        $("#select-one").show();
    } else {
        window.location = "edit.xhtml?id=" + id;
    }
};
