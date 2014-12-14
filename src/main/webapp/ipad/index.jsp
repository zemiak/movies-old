<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="service" class="com.zemiak.movies.service.jsp.GenreWebService" scope="request"> </jsp:useBean>
<jsp:useBean id="conf" class="com.zemiak.movies.service.jsp.ConfigWebService" scope="request"> </jsp:useBean>

<t:ipad>
    <jsp:body>
        <div class="container-fluid">
            <div class="page">
                <c:forEach var="item" items="${service.all}">
                    <div class="span2 movie-box genre-box">
                        <a href="genre.jsp?id=${item.id}">
                            <img class="movie-thumbnail" alt="Genre Thumbnail"
                                src="${conf.imgServer}genre/${item.pictureFileName}" />
                            <p class="movie-label">${item.name}</p>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>
    </jsp:body>
</t:ipad>
