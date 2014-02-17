package com.zemiak.movies.servlet;

import com.zemiak.movies.boundary.FileStreamer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author vasko
 */
@WebServlet(urlPatterns = {"/stream"}, asyncSupported = true)
public class StreamingServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(StreamingServlet.class.getName());
    
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.start(new StreamerRunner(asyncContext));
    }
    
    private class StreamerRunner implements Runnable {
        final AsyncContext ctx;
        
        public StreamerRunner(final AsyncContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            try {
                new FileStreamer()
                        .serveFileRange((HttpServletRequest) ctx.getRequest(), 
                                (HttpServletResponse) ctx.getResponse());
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Async stream I/O Exception", ex);
            }
        }
        
    }
}
