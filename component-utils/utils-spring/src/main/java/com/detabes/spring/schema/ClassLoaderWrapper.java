//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.detabes.spring.schema;

import java.io.InputStream;
import java.net.URL;

public class ClassLoaderWrapper {
    ClassLoader defaultClassLoader;
    ClassLoader systemClassLoader;

    ClassLoaderWrapper() {
        try {
            this.systemClassLoader = ClassLoader.getSystemClassLoader();
        } catch (SecurityException var2) {
        }

    }

    public URL getResourceAsURL(String resource) {
        return this.getResourceAsURL(resource, this.getClassLoaders((ClassLoader)null));
    }

    public URL getResourceAsURL(String resource, ClassLoader classLoader) {
        return this.getResourceAsURL(resource, this.getClassLoaders(classLoader));
    }

    public InputStream getResourceAsStream(String resource) {
        return this.getResourceAsStream(resource, this.getClassLoaders((ClassLoader)null));
    }

    public InputStream getResourceAsStream(String resource, ClassLoader classLoader) {
        return this.getResourceAsStream(resource, this.getClassLoaders(classLoader));
    }

    public Class<?> classForName(String name) throws ClassNotFoundException {
        return this.classForName(name, this.getClassLoaders((ClassLoader)null));
    }

    public Class<?> classForName(String name, ClassLoader classLoader) throws ClassNotFoundException {
        return this.classForName(name, this.getClassLoaders(classLoader));
    }

    InputStream getResourceAsStream(String resource, ClassLoader[] classLoader) {
        ClassLoader[] arr$ = classLoader;
        int len$ = classLoader.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            ClassLoader cl = arr$[i$];
            if (null != cl) {
                InputStream returnValue = cl.getResourceAsStream(resource);
                if (null == returnValue) {
                    returnValue = cl.getResourceAsStream("/" + resource);
                }

                if (null != returnValue) {
                    return returnValue;
                }
            }
        }

        return null;
    }

    URL getResourceAsURL(String resource, ClassLoader[] classLoader) {
        ClassLoader[] arr$ = classLoader;
        int len$ = classLoader.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            ClassLoader cl = arr$[i$];
            if (null != cl) {
                URL url = cl.getResource(resource);
                if (null == url) {
                    url = cl.getResource("/" + resource);
                }

                if (null != url) {
                    return url;
                }
            }
        }

        return null;
    }

    Class<?> classForName(String name, ClassLoader[] classLoader) throws ClassNotFoundException {
        ClassLoader[] arr$ = classLoader;
        int len$ = classLoader.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            ClassLoader cl = arr$[i$];
            if (null != cl) {
                try {
                    Class<?> c = Class.forName(name, true, cl);
                    if (null != c) {
                        return c;
                    }
                } catch (ClassNotFoundException var8) {
                }
            }
        }

        throw new ClassNotFoundException("Cannot find class: " + name);
    }

    ClassLoader[] getClassLoaders(ClassLoader classLoader) {
        return new ClassLoader[]{classLoader, this.defaultClassLoader, Thread.currentThread().getContextClassLoader(), this.getClass().getClassLoader(), this.systemClassLoader};
    }
}
