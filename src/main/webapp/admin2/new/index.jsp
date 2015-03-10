<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:admin-grid>
    <jsp:body>
        <h3>New Movies</h3>
            
        <div id="select-one" data-alert class="alert-box alert" style="display: none;">
            Please, select exactly one row.
            <a href="#" class="close">&times;</a>
        </div>

        <table id="grid" class="display" cellspacing="0" width="100%">
            <thead> <tr>
                <th>Id</th>  <th>File Name</th> <th>Name</th>
            </tr> </thead>
        </table>

        <ul class="button-group" style="padding-left: 1em;">
            <li><a href="javascript:editClick();" class="small button">Edit</a></li>
        </ul>

        <script type="text/javascript" src="index.js"></script>
    </jsp:body>
</t:admin-grid>
