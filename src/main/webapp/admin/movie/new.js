$(document).ready(function() {
    $('#grid').dataTable( {
        "ajax": "/movies/rest/movies/new",
        "pagingType": "full",
        dom: 'T<"clear">lfrtip',
        tableTools: {
            "sRowSelect": "single",
            aButtons: []
        },
        "columns": [
            { "data": "id" },
            { "data": "fileName" },
            { "data": "name" }
        ]
    });

    $('#grid tbody').on('dblclick', 'tr', function() {
	DataTablesExt.editDoubleClick(this);
    });
});
