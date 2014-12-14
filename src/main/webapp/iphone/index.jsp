<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="service" class="com.zemiak.movies.service.jsp.GenreWebService" scope="request"> </jsp:useBean>
<jsp:useBean id="conf" class="com.zemiak.movies.service.jsp.ConfigWebService" scope="request"> </jsp:useBean>

<t:iphone>
    <jsp:body>
        <div class="toolbar">
            <h1 id="pageTitle"></h1>
            <a id="backButton" class="button" href="#"></a>
        </div>
        
        <ul id="main" title="Videotéka" selected="true">
            <c:forEach var="item" items="${service.all}">
                <li>
                    <img src="${conf.imgServer}genre/${item.pictureFileName}" align="left" class="movies-thumbnail" />
                    <a href="genre.jsp?id=${item.id}" target="_self">${item.name}</a>
                </li>
            </c:forEach>
            
            <li>
                <img src="img/search.jpg" align="left" class="movies-thumbnail" />
                <a href="search.jsp" target="_self">Search</a>
            </li>
            
            <li>
                <img src="img/calendar.jpg" align="left" class="movies-thumbnail" />
                <a href="searchResults.jsp?months=6" target="_self">Recent</a>
            </li>
            
            <li><a href="../ipad/index.jsp">Full GUI</a></li>
        </ul>
    </jsp:body>
</t:iphone>
