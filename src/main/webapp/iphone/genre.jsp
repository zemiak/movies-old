<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="series" class="com.zemiak.movies.service.jsp.SerieService" scope="request"> </jsp:useBean>
<jsp:useBean id="movies" class="com.zemiak.movies.service.jsp.MovieService" scope="request"> </jsp:useBean>

<% 
    series.setGenreId(request);
    movies.setGenreId(request);
%>

<t:iphone>
    <jsp:body>
        <div class="toolbar">
            <h1 id="pageTitle"></h1>
            <a class="button leftButton" href="javascript:window.history.back()">Back</a>
        </div>
        
        <ul id="main" title="Žáner" selected="true">
            <c:forEach var="item" items="${series.byGenreId}">
                <li>
                    <img src="http://lenovo-server.local:8081/movies/img/serie/${item.pictureFileName}" align="left" class="movies-thumbnail" />
                    <a href="serie.jsp?id=${item.id}" target="_self">*${item.name}</a>
                </li>
            </c:forEach>
                
            <c:forEach var="item" items="${movies.byGenreId}">
                <li>
                    <img src="http://lenovo-server.local:8081/movies/img/movie/${item.pictureFileName}" align="left" class="movies-thumbnail" />
                    <a href="http://lenovo-server.local:8081/movies/play.php?id=${item.id}" target="_self">${item.name}</a>
                </li>
            </c:forEach>
        </ul>
    </jsp:body>
</t:iphone>
