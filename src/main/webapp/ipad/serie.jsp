<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="movies" class="com.zemiak.movies.service.jsp.MovieWebService" scope="request"> </jsp:useBean>
    <jsp:useBean id="conf" class="com.zemiak.movies.service.jsp.ConfigWebService" scope="request"> </jsp:useBean>
<% movies.setSerieId(request); %>

<t:ipad>
    <jsp:body>
        <div class="container-fluid">
            <div class="movie-box">
                    <a href="javascript:window.history.back(-1);">
                        <img src="img/arrow-back.jpg" alt="Back" class="back-image" />
                    </a>
                </div>

            <c:forEach var="item" items="${movies.bySerieId}">
                <div class="movie-box">
                    <a href="movie.jsp?id=${item.id}">
                        <img class="movie-thumbnail" alt="${item.name} Thumbnail"
                            src="${conf.imgServer}movie/${item.pictureFileName}" />
                        <p class="movie-label">${item.name}</p>
                    </a>
                </div>
            </c:forEach>
        </div>
    </jsp:body>
</t:ipad>

