package com.zemiak.movies.servlet;

/**
 * from http://bruno.defraine.net/StaticServlet.java
 */
import com.zemiak.movies.lookup.CDILookup;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/images/*"})
public class StaticServlet extends HttpServlet {
    private static final int DEFLATE_TRESHOLD = 4 * 1024;
    private static final int BUFFER_SIZE = 4 * 1024;

    private final String imgPath;

    public StaticServlet() {
        imgPath = new CDILookup().lookup(ServletConfiguration.class).getImgPath();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        lookup(req).respondGet(resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            lookup(req).respondHead(resp);
        } catch (UnsupportedOperationException e) {
            super.doHead(req, resp);
        }
    }

    @Override
    protected long getLastModified(HttpServletRequest req) {
        return lookup(req).getLastModified();
    }

    private LookupResult lookup(HttpServletRequest req) {
        LookupResult r = (LookupResult) req.getAttribute("lookupResult");
        if (r == null) {
            r = lookupNoCache(req);
            req.setAttribute("lookupResult", r);
        }
        return r;
    }

    private LookupResult lookupNoCache(HttpServletRequest req) {
        final String path = getPath(req);

        final String mimeType = getMimeType(path);

        File f = new File(path);
        if (!f.isFile()) {
            return new StaticServletError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not a file");
        } else {
            URL url;
            try {
                url = new URL("file://" + path);
            } catch (MalformedURLException ex) {
                return new StaticServletError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Malformed URL");
            }
            return new StaticFile(f.lastModified(), mimeType, (int) f.length(), acceptsDeflate(req), url);
        }
    }

    private String getPath(HttpServletRequest req) {
        return Paths.get(imgPath, req.getPathInfo()).toString();
    }

    private String getMimeType(String path) {
        return coalesce(getServletContext().getMimeType(path), "application/octet-stream");
    }

    private static boolean acceptsDeflate(HttpServletRequest req) {
        final String ae = req.getHeader("Accept-Encoding");
        return ae != null && ae.contains("gzip");
    }

    private static boolean deflatable(String mimetype) {
        return mimetype.startsWith("text/")
                || mimetype.equals("application/postscript")
                || mimetype.startsWith("application/ms")
                || mimetype.startsWith("application/vnd")
                || mimetype.endsWith("xml");
    }



    private static void transferStreams(InputStream is, OutputStream os) throws IOException {
        try {
            byte[] buf = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = is.read(buf)) != -1) {
                os.write(buf, 0, bytesRead);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    private static <T> T coalesce(T... ts) {
        for (T t : ts) {
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    private static class StaticFile implements LookupResult {
        protected final long lastModified;
        protected final String mimeType;
        protected final int contentLength;
        protected final boolean acceptsDeflate;
        protected final URL url;

        public StaticFile(long lastModified, String mimeType, int contentLength, boolean acceptsDeflate, URL url) {
            this.lastModified = lastModified;
            this.mimeType = mimeType;
            this.contentLength = contentLength;
            this.acceptsDeflate = acceptsDeflate;
            this.url = url;
        }

        @Override
        public long getLastModified() {
            return lastModified;
        }

        private boolean willDeflate() {
            return acceptsDeflate && deflatable(mimeType) && contentLength >= DEFLATE_TRESHOLD;
        }

        private void setHeaders(HttpServletResponse resp) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType(mimeType);
            if (contentLength >= 0 && !willDeflate()) {
                resp.setContentLength(contentLength);
            }
        }

        @Override
        public void respondGet(HttpServletResponse resp) throws IOException {
            setHeaders(resp);
            final OutputStream os;
            if (willDeflate()) {
                resp.setHeader("Content-Encoding", "gzip");
                os = new GZIPOutputStream(resp.getOutputStream(), BUFFER_SIZE);
            } else {
                os = resp.getOutputStream();
            }
            transferStreams(url.openStream(), os);
        }

        @Override
        public void respondHead(HttpServletResponse resp) {
            if (willDeflate()) {
                throw new UnsupportedOperationException();
            }
            setHeaders(resp);
        }
    }

    private static interface LookupResult {

        public void respondGet(HttpServletResponse resp) throws IOException;

        public void respondHead(HttpServletResponse resp);

        public long getLastModified();
    }

    private static class StaticServletError implements LookupResult {
        private final int statusCode;
        private final String message;

        public StaticServletError(int statusCode, String message) {
            this.statusCode = statusCode;
            this.message = message;
        }

        @Override
        public long getLastModified() {
            return -1;
        }

        @Override
        public void respondGet(HttpServletResponse resp) throws IOException {
            resp.sendError(statusCode, message);
        }

        @Override
        public void respondHead(HttpServletResponse resp) {
            throw new UnsupportedOperationException();
        }
    }
}
