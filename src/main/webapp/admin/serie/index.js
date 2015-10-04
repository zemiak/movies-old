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
            { "data": "tvShow" },
            { "data": "displayOrder" },
            { "data": "created" }
        ]
    });

    $('#grid tbody').on('dblclick', 'tr', function() {
	DataTablesExt.editDoubleClick(this);
    });
});
