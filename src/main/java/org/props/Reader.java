package org.props;

import org.apache.commons.collections4.map.ReferenceMap;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Reader {

    private static Map<Object, Object> cache = new ReferenceMap<>();
    private static Logger logger = Logger.getLogger(Reader.class.getName());

    public static final String property(String name) {
        if (cache.get(name) == null) {
            refresh("");
        }
        return (String) cache.getOrDefault(name, "");
    }

    public static final String property(String name, String filename) {
        cache.clear();
        refresh(filename);
        return (String) cache.getOrDefault(name, "");
    }

    private static void refresh(String filename) {

        Collection<File> files = FileUtils.listFiles(new File(System.getProperty("user.dir")),
                new String[] {"properties"}, true);

        files.stream()        
                .filter(file -> file.getName().contains(filename))
                .filter(File::exists)
                .forEach(file -> {

                    Properties props = new Properties();
                    try {
                        props.load(new FileInputStream(file));
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "Exception in Reader.refresh()", e);
                    }
                    props.entrySet().stream().forEach(e -> cache.put(e.getKey(), e.getValue()));

                });
    }

    private Reader() {

    }
}
