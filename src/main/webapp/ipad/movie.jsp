<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="service" class="com.zemiak.movies.service.jsp.MovieWebService" scope="request"> </jsp:useBean>
<jsp:useBean id="conf" class="com.zemiak.movies.service.jsp.ConfigWebService" scope="request"> </jsp:useBean>
<% service.setMovieId(request); %>

<t:ipad>
    <jsp:body>
        <div class="row-fluid">
            <div class="span4">
                <a href="javascript:window.history.back(-1);">
                    <img src="img/arrow-back.jpg" alt="Back" class="back-image-small" />
                </a>
            </div>

            <div class="span5">
                <span class="movie-title">${service.movie.name}</span>
            </div>

            <c:if test="${!service.movie.urlEmpty}">
                <div class="span3">
                    <button onclick="javascript:window.open('${service.movie.url}');"
                        class="btn btn-lg btn-primary" id="url" name="url">
                        Movie Info (New Window)
                    </button>
                </div>
            </c:if>
        </div>

        <div class="row-fluid">
            <div class="span12">
                <center>
                    <video id="my_video" controls
                           preload="auto" poster="${conf.imgServer}movie/${service.movie.pictureFileName}"
                      width="559" height="432">
                      <source src="${conf.playServer}?id=${service.movie.id}" type='video/mp4'>
                    </video>
                </center>

                <div class="movie-description">${service.movie.description}</div>
            </div>
        </div>

        <div class="row-fluid" style="padding-top: 2em;">
            <div class="span4">
                <label>Genre</label>
                <input type="text" class="form-control" value="${service.movie.genreId.name}" disabled />
            </div>

            <div class="span4">
                <label>Serie</label>
                <input type="text" class="form-control" value="${service.movie.serieName}" disabled />
            </div>

            <div class="span4">
                <label>Language</label>
                <input type="text" class="form-control" value="${service.movie.languageName}" disabled />
            </div>
        </div>

        <div class="row-fluid">
            <div class="span4">
                <label>Original Language</label>
                <input type="text" class="form-control" value="${service.movie.originalLanguageName}" disabled />
            </div>

            <div class="span4">
                <label>Subtitles</label>
                <input type="text" class="form-control" value="${service.movie.subtitlesName}" disabled />
            </div>

            <div class="span4">
                <label>Original Name</label>
                <input type="text" class="form-control" value="${service.movie.originalName}" disabled />
            </div>
        </div>

        <script>
            fullScreenVideoInit("my_video");
        </script>
    </jsp:body>
</t:ipad>
