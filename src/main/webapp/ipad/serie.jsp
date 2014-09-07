<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Videotéka</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Le fav and touch icons -->
    <link rel="shortcut icon" href="img/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="1024x1024"
        href="img/icon-1024.jpg">
    <link rel="apple-touch-icon-precomposed" sizes="512x512"
        href="img/icon-512.jpg">
    <link rel="apple-touch-icon-precomposed" sizes="144x144"
        href="img/icon-144.jpg">
    <link rel="apple-touch-icon-precomposed" sizes="114x114"
        href="img/icon-114.jpg">
    <link rel="apple-touch-icon-precomposed" sizes="72x72"
        href="img/icon-72.jpg">
    <link rel="apple-touch-icon-precomposed" href="img/icon-57.jpg">

    <!-- desktop == iPad Landscape -->

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
    </style>
    <link href="css/bootstrap-responsive.min.css" rel="stylesheet">

    <link href="css/movies.css" rel="stylesheet" type="text/css">

    <!-- JS libraries, DEV versions -->
    <script type="text/javascript" src="js/vendor/jquery.js"></script>
    <script type="text/javascript" src="js/vendor/bootstrap.js"></script>

    <jsp:useBean id="movies" class="com.zemiak.movies.service.jsp.MovieService" scope="request"> </jsp:useBean>
  </head>

  <body>
      <!-- Toolbar / menu -->
      <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="#genres">Videotéka</a>
          <div class="nav-collapse">
            <ul class="nav">
                <li class="dropdown">
                  <a class="dropdown-toggle" href="#" data-toggle="dropdown">Admin<b class="caret"></b></a>
                  <ul class="dropdown-menu">
                      <li><a href="/movies/admin/movienew/List.xhtml">Nové filmy</a></li>
                      <li><a href="/movies/admin/movieall/List.xhtml">Všetky filmy</a></li>
                      <li><a href="/movies/admin/genre/List.xhtml">Žánre</a></li>
                      <li><a href="/movies/admin/serie/List.xhtml">Série</a></li>
                      <li><a href="/movies/admin/language/List.xhtml">Jazyky</a></li>
                      <li><a href="/movies/admin/about/About.xhtml">O programe</a></li>
                  </ul>
                </li>
              <li class>
                  <form class="navbar-search" action="searchResults.jsp" method="get">
                    <input class="search-query span2" type="text" name="query"
                      placeholder="Hľadať podľa mena..." id="query"
                      />
                    <input type="submit" value="Search" class="btn-mini" style="display:none" />
                </form>
              </li>
            </ul>
          </div> <!--/.nav-collapse -->
        </div>
      </div>
    </div>
      
    <% 
        movies.setSerieId(request);
    %>

    <div class="container-fluid">
        <div class="span2 movie-box">
            <a href="javascript:window.history.back(-1);" class="back">
                <img src="img/arrow-back.jpg" style="width:48px; height:48px;" />
            </a>
        </div>
        
        <c:forEach var="item" items="${movies.bySerieId}">
            <div class="span2 movie-box">
                <a href="movie.jsp?id=${item.id}">
                    <img class="movie-thumbnail"
                        src="http://lenovo-server.local:8081/movies/img/movie/${item.pictureFileName}" />
                    <p class="movie-label">${item.name}</p>
                </a>
            </div>
        </c:forEach>
    </div>
  </body>
</html>
