package org.chonochonov.springintroex.utilis;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;

@Component
public class FileUtilImpl implements FileUtil {
    @Override
    public String[] readFileContent(String path) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(new File(path)));

        Deque<String> stack = new ArrayDeque<>();
        String line;

        while ((line = bufferedReader.readLine()) != null){
            if (!"".equals(line)){
               stack.push(line);
            }
        }
        return stack.toArray(String[]::new);
    }
}
