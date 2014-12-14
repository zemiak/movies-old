<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="service" class="com.zemiak.movies.service.jsp.SearchWebService" scope="request"> </jsp:useBean>
<jsp:useBean id="conf" class="com.zemiak.movies.service.jsp.ConfigWebService" scope="request"> </jsp:useBean>
<% service.search(request);%>

<t:iphone>
    <jsp:body>
        <div class="toolbar">
            <h1 id="pageTitle"></h1>
            <a class="button leftButton" href="index.jsp">Back</a>
        </div>

        <ul id="main" title="Hľadať" selected="true">
            <c:forEach var="item" items="${service.series}">
                <li>
                    <img src="${conf.imgServer}serie/${item.pictureFileName}" align="left" class="movies-thumbnail" />
                    <a href="serie.jsp?id=${item.id}" target="_self">*${item.name}</a>
                </li>
            </c:forEach>

            <c:forEach var="item" items="${service.movies}">
                <li>
                    <img src="${conf.imgServer}movie/${item.pictureFileName}" align="left" class="movies-thumbnail" />
                    <a href="${conf.playServer}?id=${item.id}" target="_self">${item.name}</a>
                </li>
            </c:forEach>
        </ul>
    </jsp:body>
</t:iphone>
