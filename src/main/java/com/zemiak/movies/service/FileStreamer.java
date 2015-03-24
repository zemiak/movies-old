package com.zemiak.movies.service;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.lookup.CDILookup;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author vasko
 */
public class FileStreamer {
    private static final Logger LOG = Logger.getLogger(FileStreamer.class.getName());
    
    private static final int BUFFER_LENGTH = 1024 * 512;
    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24;
    private static final Pattern RANGE_PATTERN = Pattern.compile("bytes=(?<start>\\d*)-(?<end>\\d*)");

    private final String videoPath;
    private final MovieService service;

    public FileStreamer() {
        videoPath = new CDILookup().lookup(FileStreamerConfiguration.class).getPath();
        service = new CDILookup().lookup(MovieService.class);
    }

    public void serveFileRange(final HttpServletRequest request, final HttpServletResponse response)
             {
        final Integer videoId;

        logRequest(request);

        try {
            videoId = Integer.valueOf(URLDecoder.decode(request.getParameter("id"), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, "Decoding exception: {0}", ex.getMessage());
            return;
        }

        final Movie movie = service.find(videoId);
        final String videoFilename = movie.getFileName();

        Path video = Paths.get(videoPath, videoFilename);

        int length;
        try {
            length = (int) Files.size(video);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot get file size: {0}", ex.getMessage());
            return;
        }

        int start = 0;
        int end = length - 1;

        String range = request.getHeader("range");
        if (null != range) {
            Matcher matcher = RANGE_PATTERN.matcher(range);

            if (matcher.matches()) {
                String startGroup = matcher.group("start");
                start = startGroup.isEmpty() ? start : Integer.valueOf(startGroup);
                start = start < 0 ? 0 : start;

                String endGroup = matcher.group("end");
                end = endGroup.isEmpty() ? end : Integer.valueOf(endGroup);
                end = end > length - 1 ? length - 1 : end;
            }
        }

        int contentLength = end - start + 1;

        response.reset();
        response.setBufferSize(BUFFER_LENGTH);
        response.setHeader("Accept-Ranges", "0-" + length);
        response.setHeader("Content-Range", String.format("bytes %s-%s/%s", start, end, length));
        response.setHeader("Content-Length", String.format("%s", contentLength));
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

        int bytesRead;
        int bytesLeft = contentLength;

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_LENGTH);

        SeekableByteChannel input;
        try {
            input = Files.newByteChannel(video, StandardOpenOption.READ);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot open video file: {0}", ex.getMessage());
            return;
        }

        OutputStream output;
        try {
            output = response.getOutputStream();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot open streaming output stream: {0}", ex.getMessage());
            return;
        }

        try {
            input.position(start);

            while ((bytesRead = input.read(buffer)) != -1 && bytesLeft > 0) {
                buffer.clear();
                output.write(buffer.array(), 0, bytesLeft < bytesRead ? bytesLeft : bytesRead);
                bytesLeft -= bytesRead;
            }
        } catch (java.io.IOException | java.lang.IllegalArgumentException ex) {
            // pass, do not care
            LOG.log(Level.SEVERE, "Streaming exception: {0}", ex.getMessage());
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Cannot close video file: {0}", ex.getMessage());
            }

            try {
                output.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Cannot close output stream: {0}", ex.getMessage());
            }
        }

        logResponse(response);
    }

    private void logRequest(final HttpServletRequest request) {
        LOG.log(Level.INFO, "===");
        LOG.log(Level.INFO, "Request: {0}", request.getMethod());

        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            LOG.log(Level.INFO, "{0}: {1}", new Object[]{header, request.getHeader(header)});
        }

        LOG.log(Level.INFO, "---");
    }

    private void logResponse(final HttpServletResponse response) {
        LOG.log(Level.INFO, "Response: {0}", response.getStatus());

        for (String header: response.getHeaderNames()) {
            LOG.log(Level.INFO, "{0}{1}: ", new Object[]{response.getHeader(header), header});
        }

        LOG.log(Level.INFO, "===");
    }
}
