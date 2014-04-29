package com.zemiak.movies.batch.service;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.strings.Joiner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vasko
 */
public final class CommandLine {
    private static final BatchLogger LOG = BatchLogger.getLogger(CommandLine.class.getName());
    private static final Logger LOG1 = Logger.getLogger(CommandLine.class.getName());
    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
    private static final Integer TIMEOUT = 300; // 5 minutes
    
    private CommandLine() {
    }

    public static List<String> execCmd(final String cmd, final List<String> arguments)
            throws IOException, InterruptedException, IllegalStateException {
        CommandLineResult result = new CommandLineResult();
        Callable<CommandLineResult> callable = getCallable(cmd, arguments);

        LOG1.log(Level.INFO, "run:{0} {1}", new Object[]{cmd, null == arguments ? "" : Joiner.join(arguments, "|")});
        //return new ArrayList<>();

        try {
            result = timedCall(callable, TIMEOUT, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            final List<String> lines = new ArrayList<>();
            lines.add("Timeout " + TIMEOUT + " seconds");

            result.setExitValue(-1);
            result.setOutput(lines);
        } catch (ExecutionException ex) {
            final List<String> lines = new ArrayList<>();
            lines.add("Execution: " + ex.getMessage());

            result.setExitValue(-2);
            result.setOutput(lines);
        }

        if (result.isError()) {
            LOG.log(Level.SEVERE, "... execCmd: error code is {0}, output is {1}",
                    new Object[]{result.getExitValue(), Joiner.join(result.getOutput(), "|")});
            IllegalStateException ex = new IllegalStateException("Exit code " + result.getExitValue() + " instead of success");
            throw ex;
        }

        return result.getOutput();
    }

    private static String streamToString(final InputStream stream) throws IOException {
        char[] buff = new char[1024];
        Writer stringWriter = new StringWriter();

        try {
            Reader bReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            int n;
            while ((n = bReader.read(buff)) != -1) {
                stringWriter.write(buff, 0, n);
            }
        } finally {
            stringWriter.close();
        }

        return stringWriter.toString();
    }

    private static <T> T timedCall(final Callable<T> c, final long timeout, final TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException
    {
        final FutureTask<T> task = new FutureTask<T>(c);
        THREAD_POOL.execute(task);
        return task.get(timeout, timeUnit);
    }

    private static Callable<CommandLineResult> getCallable(final String cmd, final List<String> arguments) {
        final List<String> command = new ArrayList<>(arguments);
        command.add(0, cmd);

        Callable<CommandLineResult> callable = new Callable<CommandLineResult>() {
            @Override
            public CommandLineResult call() throws Exception
                {
                    Process process = Runtime.getRuntime().exec(command.toArray(new String[]{}));

                    int exitValue = process.waitFor();

                    List<String> lines = new ArrayList<>();
                    try (InputStream stream = process.getInputStream();) {
                        lines.addAll(Arrays.asList(streamToString(stream).split(System.getProperty("line.separator"))));
                    }

                    CommandLineResult result = new CommandLineResult();
                    result.setExitValue(exitValue);
                    result.setOutput(lines);

                    return result;
                }
        };

        return callable;
    }
}
