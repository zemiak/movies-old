<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Videotéka</title>

        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
        <link rel="stylesheet" href="iui/t/default/default-theme.css" type="text/css"/>
        <link rel="stylesheet" href="iui/iui.css" type="text/css" />
        <script type="application/x-javascript" src="iui/iui.js"></script>

        <link href="css/movies-iphone.css" rel="stylesheet" type="text/css">

        <link rel="shortcut icon" href="img/favicon.ico">
        <link rel="apple-touch-icon-precomposed" sizes="1024x1024"
              href="../ipad/img/icon-1024.jpg">
        <link rel="apple-touch-icon-precomposed" sizes="512x512"
              href="../ipad/img/icon-512.jpg">
        <link rel="apple-touch-icon-precomposed" sizes="144x144"
              href="../ipad/img/icon-144.jpg">
        <link rel="apple-touch-icon-precomposed" sizes="114x114"
              href="../ipad/img/icon-114.jpg">
        <link rel="apple-touch-icon-precomposed" sizes="72x72"
              href="../ipad/img/icon-72.jpg">
        <link rel="apple-touch-icon-precomposed" href="img/icon-57.jpg">
        
        <jsp:useBean id="service" class="com.zemiak.movies.service.jsp.GenreService" scope="request"> </jsp:useBean>
    </head>

    <body>
        <div class="toolbar">
            <h1 id="pageTitle"></h1>
            <a id="backButton" class="button" href="#"></a>
        </div>

        <ul id="main" title="Videotéka" selected="true">
            <c:forEach var="item" items="${service.all}">
                <li>
                    <img src="http://lenovo-server.local:8081/movies/img/genre/${item.pictureFileName}" align="left" class="movies-thumbnail" />
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
            
            <li><a href="../ipad/play.jsp">Full GUI</a></li>
        </ul>
    </body>
</html>
