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

        <link href="movies-iphone.css" rel="stylesheet" type="text/css">

        <link rel="shortcut icon" href="img/favicon.ico">
        <link rel="apple-touch-icon-precomposed" sizes="1024x1024"
              href="../play/img/icon-1024.jpg">
        <link rel="apple-touch-icon-precomposed" sizes="512x512"
              href="../play/img/icon-512.jpg">
        <link rel="apple-touch-icon-precomposed" sizes="144x144"
              href="../play/img/icon-144.jpg">
        <link rel="apple-touch-icon-precomposed" sizes="114x114"
              href="../play/img/icon-114.jpg">
        <link rel="apple-touch-icon-precomposed" sizes="72x72"
              href="../play/img/icon-72.jpg">
        <link rel="apple-touch-icon-precomposed" href="img/icon-57.jpg">
        
        <jsp:useBean id="service" class="com.zemiak.movies.service.jsp.GenreService" scope="request"> </jsp:useBean>
    </head>

    <body>
        <div class="toolbar">
            <h1 id="pageTitle"></h1>
            <a id="backButton" class="button" href="#"></a>
        </div>
        
        <form id="search" title="Search" name="formname" action="" method="GET" selected="true">
            <div class="toolbar">
                <h1>Search</h1>
                <a class="button leftButton" href="javascript:window.history.back()">Back</a>
                
                <a class="button blueButton" 
                   href="javascript:window.location='searchResults.jsp?query='+encodeURIComponent(document.getElementById('query').value)">
                    Search
                </a>
            </div>
            <fieldset>
               <div class="row">
                    <label for="keyword">Search:</label>
                    <input type="text" name="query" id="query" placeholder="Type your keywords">
                </div>
            </fieldset>
        </form>
    </body>
</html>
