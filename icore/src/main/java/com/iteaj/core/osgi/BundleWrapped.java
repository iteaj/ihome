package com.iteaj.core.osgi;

import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

/**
 * create time: 2018/3/10
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
public class BundleWrapped {

    private Version version;
    private String SymbolicName;

    public BundleWrapped(Version version, String symbolicName) {
        this.version = version;
        SymbolicName = symbolicName;
    }

    public Version getVersion() {
        return version;
    }

    public String getSymbolicName() {
        return SymbolicName;
    }
}
