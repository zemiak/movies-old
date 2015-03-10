<%@tag description="Administration Grid Template" pageEncoding="UTF-8"%>

<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Admin</title>

    <link href="/movies/js/libs/foundation/css/normalize.css" rel="stylesheet" />
    <link href="/movies/js/libs/foundation/css/foundation.css" rel="stylesheet" />
    <link href="/movies/js/libs/foundation/icons/foundation-icons.css" rel="stylesheet" />
    <link href="/movies/js/libs/datatables/css/jquery.dataTables.css" rel="stylesheet" />
    <link href="/movies/js/libs/datatables/css/dataTables.foundation.css" rel="stylesheet" />

    <script type="text/javascript" src="/movies/js/libs/foundation/js/vendor/modernizr.js"></script>
    <script type="text/javascript" src="/movies/js/libs/foundation/js/vendor/jquery.js"></script>
    <script type="text/javascript" src="/movies/js/libs/foundation/js/vendor/fastclick.js"></script>
    <script type="text/javascript" src="/movies/js/libs/foundation/js/foundation.min.js"></script>
    <script type="text/javascript" src="/movies/js/libs/foundation/js/confirm_with_reveal.js"></script>
    <script type="text/javascript" src="/movies/js/libs/datatables/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="/movies/js/libs/datatables/js/dataTables.foundation.js"></script>
    <script type="text/javascript" src="/movies/js/libs/datatables/js/dataTables.tableTools.js"></script>
    <script type="text/javascript" src="/movies/js/dataTables.ext.js"></script>
    <script type="text/javascript" src="/movies/js/foundation.alert.js"></script>
  </head>

  <body>
    <div class="icon-bar small six-up" role="navigation">
        <a class="item" role="button" tabindex="0" aria-label="home" href="/movies/ipad/index.jsp">
            <i class="fi-home"></i>
        </a>
        <a class="item" role="button" tabindex="0" aria-label="new" href="/movies/admin2/movie/new.jsp">
            <i class="fi-page-add"></i>
        </a>
        <a class="item" role="button" tabindex="0" aria-label="all" href="/movies/admin2/movie/index.jsp">
            <i class="fi-video"></i>
        </a>
        <a class="item" role="button" tabindex="0" aria-label="series" href="/movies/admin2/serie/index.jsp">
            <i class="fi-page-multiple"></i>
        </a>
        <a class="item" role="button" tabindex="0" aria-label="genres" href="/movies/admin2/genre/index.jsp">
            <i class="fi-record"></i>
        </a>
        <a class="item" role="button" tabindex="0" aria-label="about" href="/movies/admin2/system/index.jsp">
            <i class="fi-widget"></i>
        </a>
    </div>

    <jsp:doBody/>
    
    <script>
        $(document).foundation();
        $(document).confirmWithReveal();
    </script>
  </body>
</html>
