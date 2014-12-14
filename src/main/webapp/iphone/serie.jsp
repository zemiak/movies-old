<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="movies" class="com.zemiak.movies.service.jsp.MovieWebService" scope="request"> </jsp:useBean>
<jsp:useBean id="conf" class="com.zemiak.movies.service.jsp.ConfigWebService" scope="request"> </jsp:useBean>

<% movies.setSerieId(request); %>

<t:iphone>
    <jsp:body>
        <div class="toolbar">
            <h1 id="pageTitle"></h1>
            <a class="button leftButton" href="javascript:window.history.back()">Back</a>
        </div>

        <ul id="main" title="Séria" selected="true">
            <c:forEach var="item" items="${movies.bySerieId}">
                <li>
                    <img src="${conf.imgServer}movie/${item.pictureFileName}" align="left" class="movies-thumbnail" />
                    <a href="${conf.playServer}?id=${item.id}" target="_self">${item.name}</a>
                </li>
            </c:forEach>
        </ul>
    </jsp:body>
</t:iphone>
