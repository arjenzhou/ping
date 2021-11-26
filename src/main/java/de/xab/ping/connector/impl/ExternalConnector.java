package de.xab.ping.connector.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ExternalConnector extends CustomConnector {
    protected ClassLoader initClassloader(String url, String driverFolder) throws IOException {
        List<URL> list;
        URI uri = new File(driverFolder).toURI();
        Path dirPath = Paths.get(uri);
        list = Files.list(dirPath).map(path -> {
            try {
                return path.toUri().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
        URL[] array = new URL[]{};
        array = list.toArray(array);
        return new URLClassLoader(array);
    }
}
