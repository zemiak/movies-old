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
});

function editClick() {
    var id = DataTablesExt.getSelectedId("grid");
    
    if (null === id) {
        $("#select-one").show();
    } else {
        alert("Going to edit: " + id);
    }
};

function removeClick() {
    var id = DataTablesExt.getSelectedId("grid");

    $(".reveal-modal").foundation('reveal', 'close');
    if (null === id) {
        $("#select-one").show();
    } else {
        alert("Going to remove: " + id);
    }
};
