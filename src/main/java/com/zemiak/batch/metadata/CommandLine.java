package com.zemiak.batch.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author vasko
 */
public class CommandLine {
    public static List<String> execCmd(String cmd) throws IOException {
        List<String> lines;
        
        try (InputStream stream = Runtime.getRuntime().exec(cmd).getInputStream(); 
                Scanner scanner = new Scanner(stream)) {
            lines = new ArrayList<>();
            
            while (scanner.hasNext()) {
                lines.add(scanner.next());
            }
        }
        
        return lines;
    }
}
