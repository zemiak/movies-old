<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:edit>
    <jsp:body>
        <h3>Edit Genre</h3>
            
        <div id="select-one" data-alert class="alert-box alert" style="display: none;">
            Please, select exactly one row.
            <a href="#" class="close">&times;</a>
        </div>

        <form method="post" action="/movies/rest/movies">
            <input type="hidden" id="id" name="id" value="${genreWeb.find(param["id"]).getId()}" />
            
            <ul class="button-group" style="padding-left: 1em;">
                <li><a href="javascript:closeClick();" class="small button">Close</a></li>
                <li><input type="submit" class="small button" value="Save & Close" /></li>
            </ul>
        </form>

        <script type="text/javascript" src="edit.js"></script>
    </jsp:body>
</t:edit>
