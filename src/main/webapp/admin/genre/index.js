$(document).ready(function() {
    $('#grid').dataTable( {
        "ajax": "/movies/rest/genres",
        "pagingType": "full",
        dom: 'T<"clear">lfrtip',
        tableTools: {
            "sRowSelect": "single",
            aButtons: []
        },
        "columns": [
            { "data": "id" },
            { "data": "name" },
            { "data": "displayOrder" },
            { "data": "created" }
        ]
    });

    $('#grid tbody').on('dblclick', 'tr', function() {
	DataTablesExt.editDoubleClick(this);
    });
});
