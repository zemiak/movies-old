<%@tag description="Editing Form Template" pageEncoding="UTF-8"%>

<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Edit</title>

    <link href="/movies/js/libs/foundation/css/normalize.css" rel="stylesheet" />
    <link href="/movies/js/libs/foundation/css/foundation.css" rel="stylesheet" />
    <link href="/movies/js/libs/foundation/icons/foundation-icons.css" rel="stylesheet" />

    <script type="text/javascript" src="/movies/js/libs/foundation/js/vendor/modernizr.js"></script>
    <script type="text/javascript" src="/movies/js/libs/foundation/js/vendor/jquery.js"></script>
    <script type="text/javascript" src="/movies/js/libs/foundation/js/vendor/fastclick.js"></script>
    <script type="text/javascript" src="/movies/js/libs/foundation/js/foundation.min.js"></script>
    <script type="text/javascript" src="/movies/js/libs/foundation/js/confirm_with_reveal.js"></script>
    <script type="text/javascript" src="/movies/js/foundation.alert.js"></script>
  </head>

  <body>
    <jsp:doBody/>
    
    <script>
        $(document).foundation();
        $(document).confirmWithReveal();
    </script>
  </body>
</html>
