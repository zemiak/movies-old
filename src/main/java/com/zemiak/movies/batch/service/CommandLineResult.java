package com.zemiak.movies.batch.service;

import java.util.List;

/**
 *
 * @author vasko
 */
public class CommandLineResult {
    private int exitValue;
    private List<String> output;

    public CommandLineResult() {
    }

    public int getExitValue() {
        return exitValue;
    }

    public void setExitValue(int exitValue) {
        this.exitValue = exitValue;
    }

    public List<String> getOutput() {
        return output;
    }

    public void setOutput(List<String> output) {
        this.output = output;
    }

    public boolean isError() {
        return exitValue != 0;
    }
}
