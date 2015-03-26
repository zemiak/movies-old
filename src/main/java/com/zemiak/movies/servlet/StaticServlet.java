package com.zemiak.movies.servlet;

import com.zemiak.movies.lookup.CDILookup;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Based on http://bruno.defraine.net/StaticServlet.java
 */
@WebServlet(urlPatterns = {"/images/*"})
public class StaticServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String imgPath;

    public StaticServlet() {
        imgPath = new CDILookup().lookup(ServletConfiguration.class).getImgPath();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            StaticFile file = new StaticFile(req);
            file.respondGet(resp);
        } catch (IllegalStateException ex) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            StaticFile file = new StaticFile(req);
            file.setHeaders(resp);
        } catch (IllegalStateException ex) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    private class StaticFile {
        private final String mimeType;
        private final long contentLength;
        private final Path path;
        
        public StaticFile(HttpServletRequest req) {
            path = Paths.get(imgPath, req.getPathInfo());
            String pathString = path.toString();
            mimeType = null == getServletContext().getMimeType(pathString) ? "application/octet-stream" : getServletContext().getMimeType(pathString);

            File f = new File(pathString);
            if (!f.isFile()) {
                throw new IllegalStateException("Not a file");
            }
            
            contentLength = f.length();
        }

        public void setHeaders(HttpServletResponse resp) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType(mimeType);
            resp.setContentLengthLong(contentLength);
        }

        public void respondGet(HttpServletResponse resp) throws IOException {
            setHeaders(resp);
            final OutputStream os;
            os = resp.getOutputStream();
            Files.copy(path, os);
        }
    }
}
