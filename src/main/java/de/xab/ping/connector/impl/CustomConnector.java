package de.xab.ping.connector.impl;

import com.google.common.base.Throwables;
import de.xab.ping.connector.JDBCConnector;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static de.xab.ping.common.Constants.DRIVER_CLASS;
import static de.xab.ping.common.Constants.DRIVER_FOLDER;

public class CustomConnector extends JDBCConnector {
    Logger logger = LoggerFactory.getLogger("PING");

    @Override
    protected Connection getConnection(String url, String username, String password, Properties properties) throws SQLException {
        String driverClass = getDriverClass(properties);
        String driverFolder = (String) properties.get(DRIVER_FOLDER);
        Driver driver = loadDriver(url, driverFolder, driverClass);
        return driver.connect(url, properties);
    }

    protected Driver loadDriver(String url, String driverFolder, String driverClass) {
        ClassLoader classLoader = null;
        try {
            classLoader = initClassloader(url, driverFolder);
        } catch (IOException e) {
            logger.error(Throwables.getStackTraceAsString(e));
        }
        try {
            return (Driver) Class.forName(driverClass, true, classLoader).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            logger.error(Throwables.getStackTraceAsString(e));
        }
        return null;
    }

    protected String getDriverClass(Properties properties) {
        return (String) properties.get(DRIVER_CLASS);
    }

    protected ClassLoader initClassloader(String url, String driverFolder) throws IOException {
        String resourceFolder = parseDriver(url, driverFolder);
        List<URL> list = null;
        try {
            URI uri = Thread.currentThread().getContextClassLoader().getResource(resourceFolder).toURI();
            Map<String, String> env = new HashMap<>();
            env.put("create", "true");
            FileSystems.newFileSystem(uri, env);
            Path dirPath = Paths.get(uri);
            list = Files.list(dirPath).map(path -> {
                try {
                    InputStream in = Files.newInputStream(path);
                    final File tempFile = File.createTempFile("PREFIX", "SUFFIX");
                    tempFile.deleteOnExit();
                    try (FileOutputStream out = new FileOutputStream(tempFile)) {
                        IOUtils.copy(in, out);
                    } catch (IOException e) {
                        logger.error(Throwables.getStackTraceAsString(e));
                    }
                    return tempFile.toURI().toURL();
                } catch (Exception e) {
                    logger.error(Throwables.getStackTraceAsString(e));
                }
                return null;
            }).collect(Collectors.toList());
        } catch (URISyntaxException e) {
            logger.error(Throwables.getStackTraceAsString(e));
        }
        URL[] array = new URL[]{};
        array = list.toArray(array);
        return new URLClassLoader(array);
    }

    protected String parseDriver(String url, String driverFolder) {
        return driverFolder;
    }
}
