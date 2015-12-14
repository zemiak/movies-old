package com.zemiak.movies.batch.plex;

import com.zemiak.movies.batch.service.CommandLine;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class PlexProcess implements Serializable {
    private static final Logger LOG = Logger.getLogger(PlexProcess.class.getName());

    private Integer pidStart = null, pidPlex = null;


    @Inject String shellCommand;
    @Inject String pmsScript;

    @PostConstruct
    public void init() {
        findPids();
    }

    private void findPids() {
        List<String> out;

        try {
            out = new ArrayList<>(CommandLine.execCmd("ps", Arrays.asList("-ef")));
        } catch (IOException | InterruptedException | IllegalStateException ex) {
            LOG.log(Level.SEVERE, "Cannot get process list", ex);
            throw new RuntimeException(ex);
        }

        out.remove(0); // PS Header

        Map<Integer, String> processes = new HashMap<>();
        out.stream().map(String::trim).map(line -> line.split(" ")).forEach(parts -> {
            processes.put(Integer.valueOf(parts[0]), parts[1]);
        });

        pidStart = findPid(processes, "/usr/sbin/start_pms");
        pidPlex = findPid(processes, "./Plex Media Server");
    }

    public boolean isRunning() {
        return null != pidPlex;
    }

    public void stop() {
        if (! isRunning()) {
            return;
        }

        killPid(pidStart);
        if (! killPid(pidPlex)) {
            throw new RuntimeException("Error killing Plex");
        };
    }

    public void start() {
        if (isRunning()) {
            return;
        }

        startPlexShellProcess();
    }

    private final Integer findPid(Map<Integer, String> processes, String process) {
        Optional<Integer> pid = processes.keySet()
                .stream()
                .filter(key -> processes.get(key).contains(process))
                .findFirst();

        return pid.isPresent() ? pid.get() : null;
    }

    private boolean killPid(Integer pid) {
        try {
            CommandLine.execCmd("kill", Arrays.asList(String.valueOf(pid)));
        } catch (IOException | InterruptedException | IllegalStateException ex) {
            LOG.log(Level.SEVERE, "Cannot run kill command", ex);
            return false;
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, "Cannot sleep for half a second", ex);
        }

        findPids();
        if (isRunning()) {
            LOG.log(Level.SEVERE, "Cannot kill process {0}", pid);
            return false;
        }

        return true;
    }

    private void startPlexShellProcess() {
        try {
            CommandLine.execCmd(shellCommand, Arrays.asList(pmsScript));
        } catch (IOException | InterruptedException | IllegalStateException ex) {
            LOG.log(Level.SEVERE, "Cannot run start Plex shell command", ex);
            throw new RuntimeException(ex);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, "Cannot sleep for half a second", ex);
        }

        findPids();
        if (!isRunning()) {
            throw new RuntimeException("Cannot start Plex");
        }
    }
}
