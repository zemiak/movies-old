$(document).ready(function() {
    $('#grid').dataTable( {
        "ajax": "/movies/rest/logs",
        "pagingType": "full",
        dom: 'T<"clear">lfrtip',
        tableTools: {
            "sRowSelect": "single",
            aButtons: []
        },
        "columns": [
            { "data": "id" },
            { "data": "created" }
        ],
        "order": [[1, "desc"]]
    });
});

function editClick() {
    var id = DataTablesExt.getSelectedId("grid");
    
    if (null === id) {
        $("#select-one").show();
    } else {
        window.location = "view.xhtml?id=" + id;
    }
};
