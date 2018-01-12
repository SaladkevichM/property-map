package org.props;

import org.apache.commons.collections4.map.ReferenceMap;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

public final class Reader {

    private static Map<Object, Object> cache = new ReferenceMap<>();

    public static final String property(String name) {
        if (cache.get(name) == null) {
            refresh("");
        }
        return (String) cache.get(name);
    }

    public static final String property(String name, String filename) {
        cache.clear();
        refresh(filename);
        return (String) cache.get(name);
    }

    private static void refresh(String filename) {
        Collection<File> files = FileUtils.listFiles(new File(System.getProperty("user.dir")),
                new String[] {"properties"}, true);
        for (File file : files) {
            if (!filename.isEmpty() && !filename.equals(file.getName())) {
                continue;
            }
            if (file.exists()) {
                Properties props = new Properties();
                try {
                    props.load(new FileInputStream(file));
                } catch (IOException e) {
                    continue; // skip current iteration
                }
                for (Map.Entry<Object, Object> entry : props.entrySet()) {
                    cache.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private Reader() {

    }
}
