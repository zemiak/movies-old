$(document).ready(function() {
    $('#grid').dataTable( {
        "ajax": "/movies/rest/movies",
        "pagingType": "full",
        dom: 'T<"clear">lfrtip',
        tableTools: {
            "sRowSelect": "single",
            aButtons: []
        },
        "columns": [
            { "data": "id" },
            { "data": "name" },
            { "data": "year" },
            { "data": "serie" },
            { "data": "genre" },
            { "data": "displayOrder" },
            { "data": "created" }
        ]
    });

    $('#grid tbody').on('dblclick', 'tr', function() {
	DataTablesExt.editDoubleClick(this);
    });
});
