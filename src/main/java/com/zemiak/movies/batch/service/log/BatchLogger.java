package com.zemiak.movies.batch.service.log;

/**
 *
 * @author vasko
 */
public class BatchLogger {
    public static LoggerInstance getLogger(final String clazz) {
        return new LoggerInstance(clazz);
    }
}
