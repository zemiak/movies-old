package com.zemiak.movies.servlet;

import com.zemiak.movies.boundary.FileStreamer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author vasko
 */
@WebServlet(urlPatterns = {"/stream"})
public class StreamingServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(StreamingServlet.class.getName());
    
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            new FileStreamer().serveFileRange(request, response);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "IO Exception", ex);
        }
    }
}
