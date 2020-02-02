package com.gbw.scanner.plugins.bruteforce.mail;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.bruteforce.*;
import com.sun.mail.smtp.SMTPTransport;
import com.xmap.api.utils.TextUtils;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.io.IOException;
import java.util.Properties;

public  class GBWBruteForceMail extends GBWAbstractBruteForce {


    public GBWBruteForceMail(GBWBruteForceMailConfig config) throws IOException, InterruptedException {

        super(config);
    }

    private GBWBruteForceResult smtpBruteForce(Host host, GBWDictEntry entry) {

        Properties props = System.getProperties();
        String proto = getProto(host);

        props.put("mail."+proto+".host",host.getServer());
        props.put("mail."+proto+".auth","true");
        // Get a Session object
        Session session = Session.getInstance(props, null);

        SMTPTransport transport = null;
        GBWBruteForceResult result = null;

        try {
            transport =
                    (SMTPTransport) session.getTransport(proto);
            /*
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("fuck@sohu.com"));*/

            transport.connect(host.getServer(),entry.getUser(),entry.getPasswd());
            /*ok*/
            result = new GBWBruteForceResult(entry, host,GBWBruteForcePlugin.BRUTEFORCEMAIL);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(transport!=null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    private GBWBruteForceResult otherBruteForce(Host host, GBWDictEntry entry){

        Properties props = System.getProperties();
        // Get a Session object
        Session session = Session.getInstance(props, null);

        // Get a Store object
        Store store = null;
        GBWBruteForceResult result = null;

        try {
            store = session.getStore(getProto(host));
            store.connect(host.getServer(),entry.getUser(),entry.getPasswd());
            /*ok*/
            result = new GBWBruteForceResult(entry,host,GBWBruteForcePlugin.BRUTEFORCEMAIL);

        }catch (Exception e){


        }finally {

            if(store!=null){
                try {
                    store.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    @Override
    public GBWBruteForceResult bruteForce(Host host, GBWDictEntry entry) {

        String proto = getProto(host);
        if(TextUtils.isEmpty(proto))
            return null;

        if(proto.toLowerCase().startsWith("smtp"))
            return smtpBruteForce(host,entry);

        return otherBruteForce(host,entry);
    }
}
