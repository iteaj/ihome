package com.iteaj.core.orm.mybatis;

import com.iteaj.core.io.OsgiUtils;
import org.apache.ibatis.io.DefaultVFS;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * create time: 2017/12/5
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
public class OsgiVFSImpl extends DefaultVFS {
    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public List<String> list(String path) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        List<String> names = new ArrayList<>();
        ArrayList<URL> list = Collections.list(classLoader.getResources(path));
        for (URL url : list) {
            try {
                URL convert = OsgiUtils.convertToLocalResource(url);
                names.addAll(list(convert, path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return names;
    }

}
