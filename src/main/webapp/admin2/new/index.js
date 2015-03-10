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
});

function editClick() {
    var id = DataTablesExt.getSelectedId("grid");
    
    if (null === id) {
        $("#select-one").show();
    } else {
        alert("Going to edit: " + id);
    }
};
