var DataTablesExt = {
    getSelectedId: function(divId) {
        var tt = TableTools.fnGetInstance(divId);
        var rows = tt.fnGetSelectedData();

        return 1 !== rows.length ? null : rows[0].id;
    }
};
