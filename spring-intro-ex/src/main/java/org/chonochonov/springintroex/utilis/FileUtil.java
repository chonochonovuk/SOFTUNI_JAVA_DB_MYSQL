package org.chonochonov.springintroex.utilis;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileUtil {
    String[] readFileContent(String path) throws IOException;
}
