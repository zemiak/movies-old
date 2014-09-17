<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:iphone>
    <jsp:body>
        <div class="toolbar">
            <h1 id="pageTitle"></h1>
            <a id="backButton" class="button" href="index.jsp"></a>
            
            <a class="button blueButton" 
                href="javascript:window.location='searchResults.jsp?query='+encodeURIComponent(document.getElementById('query').value)">
                 Search
            </a>
        </div>
        
        <form id="search" title="Search" name="formname" action="" method="GET" selected="true">
            <fieldset>
               <div class="row">
                    <label for="keyword">Search:</label>
                    <input type="text" name="query" id="query" placeholder="Type your keywords">
                </div>
            </fieldset>
        </form>
    </jsp:body>
</t:iphone>
