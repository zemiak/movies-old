package com.zemiak.movies.servlet;

import com.zemiak.movies.boundary.FileStreamer;
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
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        new FileStreamer().serveFileRange(request, response);
    }
}
