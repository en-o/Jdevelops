//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.jdevelops.data.schema;

import java.io.InputStream;
import java.net.URL;

/**
 * @author tnnn
 */
public class ClassLoaderWrapper {
    ClassLoader defaultClassLoader;
    ClassLoader systemClassLoader;

    ClassLoaderWrapper() {
        try {
            this.systemClassLoader = ClassLoader.getSystemClassLoader();
        } catch (SecurityException var2) {
            var2.printStackTrace();
        }

    }

    public URL getResourceAsUrl(String resource) {
        return this.getResourceAsUrl(resource, this.getClassLoaders(null));
    }

    public URL getResourceAsUrl(String resource, ClassLoader classLoader) {
        return this.getResourceAsUrl(resource, this.getClassLoaders(classLoader));
    }

    public InputStream getResourceAsStream(String resource) {
        return this.getResourceAsStream(resource, this.getClassLoaders(null));
    }

    public InputStream getResourceAsStream(String resource, ClassLoader classLoader) {
        return this.getResourceAsStream(resource, this.getClassLoaders(classLoader));
    }

    public Class<?> classForName(String name) throws ClassNotFoundException {
        return this.classForName(name, this.getClassLoaders(null));
    }

    public Class<?> classForName(String name, ClassLoader classLoader) throws ClassNotFoundException {
        return this.classForName(name, this.getClassLoaders(classLoader));
    }

    InputStream getResourceAsStream(String resource, ClassLoader[] classLoader) {
        int length = classLoader.length;

        for(int i = 0; i < length; ++i) {
            ClassLoader cl = classLoader[i];
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

    URL getResourceAsUrl(String resource, ClassLoader[] classLoader) {
        int length = classLoader.length;

        for(int i = 0; i < length; ++i) {
            ClassLoader cl = classLoader[i];
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
        int length = classLoader.length;

        for(int i = 0; i < length; ++i) {
            ClassLoader cl = classLoader[i];
            if (null != cl) {
                try {
                    return Class.forName(name, true, cl);
                } catch (ClassNotFoundException var8) {
                    var8.printStackTrace();
                }
            }
        }

        throw new ClassNotFoundException("Cannot find class: " + name);
    }

    ClassLoader[] getClassLoaders(ClassLoader classLoader) {
        return new ClassLoader[]{classLoader, this.defaultClassLoader, Thread.currentThread().getContextClassLoader(), this.getClass().getClassLoader(), this.systemClassLoader};
    }
}
