package com.zemiak.movies.boundary;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.lookup.CDILookup;
import com.zemiak.movies.lookup.CustomResourceLookup;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author vasko
 */
public class FileStreamer {
    private static final int BUFFER_LENGTH = 1024 * 16;
    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24;
    private static final Pattern RANGE_PATTERN = Pattern.compile("bytes=(?<start>\\d*)-(?<end>\\d*)");
    
    private final String videoPath;
    private final MovieService service;

    public FileStreamer() {
        final Properties conf = new CustomResourceLookup().lookup("com.zemiak.movies");
        videoPath = conf.getProperty("path");
        service = new CDILookup().lookup(MovieService.class);
    }
    
    public void serveFileRange(final HttpServletRequest request, final HttpServletResponse response) 
            throws IOException {
        final Integer videoId = Integer.valueOf(URLDecoder.decode(request.getParameter("id"), "UTF-8"));
        final Movie movie = service.find(videoId);
        final String videoFilename = movie.getFileName();
        
        Path video = Paths.get(videoPath, videoFilename);

        int length = (int) Files.size(video);
        int start = 0;
        int end = length - 1;

        String range = request.getHeader("Range");
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
        response.setHeader("Content-Disposition", String.format("inline;filename=\"%s\"", videoFilename));
        response.setHeader("Accept-Ranges", "bytes");
        response.setDateHeader("Last-Modified", Files.getLastModifiedTime(video).toMillis());
        response.setDateHeader("Expires", System.currentTimeMillis() + EXPIRE_TIME);
        response.setContentType(Files.probeContentType(video));
        response.setHeader("Content-Range", String.format("bytes %s-%s/%s", start, end, length));
        response.setHeader("Content-Length", String.format("%s", contentLength));
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

        int bytesRead;
        int bytesLeft = contentLength;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_LENGTH);

        try (SeekableByteChannel input = Files.newByteChannel(video, StandardOpenOption.READ);
                OutputStream output = response.getOutputStream()) {

            input.position(start);

            while ((bytesRead = input.read(buffer)) != -1 && bytesLeft > 0) {
                buffer.clear();
                output.write(buffer.array(), 0, bytesLeft < bytesRead ? bytesLeft : bytesRead);
                bytesLeft -= bytesRead;
            }
        }
    }
}
