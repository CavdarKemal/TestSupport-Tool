package de.creditreform.crefoteam.cte.tesun.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Ableitung von {@link Properties}, die die Einfüge-Reihenfolge der
 * Properties erhält. Literal-Port aus {@code testsupport_client.tesun_util}.
 */
public class OrderedProperties extends Properties {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected static class EnumerationBackedByIterator<T> implements Enumeration<T> {
        private final Iterator<T> iterator;

        public EnumerationBackedByIterator(Iterator<T> iterator) {
            this.iterator = iterator;
        }

        @Override public boolean hasMoreElements() { return iterator.hasNext(); }
        @Override public T nextElement() { return iterator.next(); }
    }

    private final String loadStoreCharsetName;
    private transient Charset loadStoreCharset;
    private final ArrayList<Object> propertyNames;

    public OrderedProperties() {
        this(DEFAULT_CHARSET);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.loadStoreCharset = (loadStoreCharsetName == null) ? null : Charset.forName(loadStoreCharsetName);
    }

    public OrderedProperties(Charset loadStoreCharset) {
        super();
        this.loadStoreCharset = loadStoreCharset;
        this.loadStoreCharsetName = loadStoreCharset == null ? null : loadStoreCharset.name();
        propertyNames = new ArrayList<>();
    }

    @Override
    public synchronized Enumeration<Object> propertyNames() {
        return new EnumerationBackedByIterator<>(propertyNames.iterator());
    }

    @Override
    public Set<String> stringPropertyNames() {
        Map<String, String> target = new LinkedHashMap<>();
        enumerateStringProperties(target);
        return target.keySet();
    }

    public synchronized void enumerateStringProperties(Map<String, String> target) {
        if (defaults != null) {
            enumerateStringProperties(defaults, target);
        }
        enumerateStringProperties(this, target);
    }

    @SuppressWarnings("rawtypes")
    protected void enumerateStringProperties(Properties p, Map<String, String> target) {
        for (Enumeration e = p.propertyNames(); e.hasMoreElements(); ) {
            Object k = e.nextElement();
            Object v = get(k);
            if (k instanceof String && v instanceof String) {
                target.put((String) k, (String) v);
            }
        }
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        propertyNames.remove(key);
        propertyNames.add(key);
        return super.put(key, value);
    }

    @Override
    public synchronized Object remove(Object key) {
        propertyNames.remove(key);
        return super.remove(key);
    }

    public void dumpPropertiesInto(Map<? super String, ? super String> targetMap) {
        for (String key : stringPropertyNames()) {
            targetMap.put(key, getProperty(key));
        }
    }

    @Override
    public synchronized void load(InputStream inStream) throws IOException {
        if (loadStoreCharset == null) {
            super.load(inStream);
        } else {
            super.load(new InputStreamReader(inStream, loadStoreCharset));
        }
    }

    @Override
    public synchronized void loadFromXML(InputStream in) throws IOException {
        if (loadStoreCharset == null) {
            super.loadFromXML(in);
        } else {
            throw new UnsupportedOperationException("loadFromXML mit automatischer Umsetzung des Charset wird nicht unterstützt");
        }
    }

    @Override
    public void store(OutputStream out, String comments) throws IOException {
        if (loadStoreCharset == null) {
            super.store(out, comments);
        } else {
            super.store(new OutputStreamWriter(out, loadStoreCharset), comments);
        }
    }

    @Override
    public void storeToXML(OutputStream os, String comment) throws IOException {
        if (loadStoreCharset == null) {
            super.storeToXML(os, comment);
        } else {
            super.storeToXML(os, comment, loadStoreCharset.name());
        }
    }
}
