package com.gbw.scanner.utils;

import com.xmap.api.utils.IPUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AssetsIPS {

    private List<AssetsItem> assertsList;

    public AssetsIPS(String xmlPath) throws DocumentException {
        this.assertsList = new ArrayList<>();
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(xmlPath));

        Element rootElement = document.getRootElement();

        Element settingsElement = rootElement.element("settings");

        Iterator iterator = settingsElement.elementIterator();

        while(iterator.hasNext()){

            Element element = (Element)iterator.next();

            String v = element.attribute("enable").getValue();
            if(v.toLowerCase().equals("true")){

                AssetsItem item = new AssetsItem(element.attribute("ipStart").getValue(),element.attribute("ipEnd").getValue());

                assertsList.add(item);
            }
        }

    }

    public String getSearch(String field) {

        if(assertsList == null ||assertsList.size()==0)
            return null;

        StringBuffer sb = new StringBuffer();
        sb.append("(");

        for(int i = 0;i<assertsList.size();i++){

            if(i!=0){
                sb.append(" OR ");
            }
            sb.append(field);
            sb.append(":");
            sb.append(assertsList.get(i).getPrefix());
        }

        sb.append(")");

        return sb.toString();
    }


    private class AssetsItem{

        private long ipStart;
        private long ipEnd;
        private long netmask;
        private String prefix;

        public AssetsItem(String ipStartStr,String ipEndStr){

            this.ipStart = IPUtils.ipv4LongLE(ipStartStr);
            this.ipEnd = IPUtils.ipv4LongLE(ipEndStr);

            int sameCount = 0;


            StringBuffer sb = new StringBuffer();

            String[] ipStarts = ipStartStr.split("\\.");
            String[] ipEnds = ipEndStr.split("\\.");

            if(ipStarts.length!=4||ipEnds.length!=4)
                throw new IllegalArgumentException();

            for(int i=0;i<4;i++){

                if(ipStarts[i].equals(ipEnds[i])){

                    if(i!=0){
                        sb.append(".");
                    }

                    sb.append(ipStarts[i]);
                    sameCount++;
                }
                else
                    break;
            }

            if(sameCount == 0)
                throw new IllegalArgumentException();

            if(sameCount<4){
                sb.append("*");
            }

            switch (sameCount){
                case 1:
                    netmask = IPUtils.ipv4LongLE("255.0.0.0");
                    break;
                case 2:
                    netmask = IPUtils.ipv4LongLE("255.255.0.0");
                    break;
                case 3:
                    netmask = IPUtils.ipv4LongLE("255.255.255.0");
                    break;

                    default:
                        netmask = IPUtils.ipv4LongLE("255.255.255.255");

            }

            prefix = sb.toString();

        }

        public long getIpStart() {
            return ipStart;
        }

        public void setIpStart(long ipStart) {
            this.ipStart = ipStart;
        }

        public long getIpEnd() {
            return ipEnd;
        }

        public void setIpEnd(long ipEnd) {
            this.ipEnd = ipEnd;
        }

        public long getNetmask() {
            return netmask;
        }

        public void setNetmask(long netmask) {
            this.netmask = netmask;
        }

        public String getPrefix() {
            return prefix;
        }


    }

    private boolean isContains(AssetsItem item,String ip){

        long ipInt = IPUtils.ipv4LongLE(ip);
        long netmask = item.getNetmask();
        long ipStart = item.getIpStart();
        long ipEnd = item.getIpEnd();

        if(ipInt == 0)
            return false;

        if((ipInt&netmask)!=(netmask&ipStart))
            return false;

        return (ipInt>=ipStart)&&(ipInt<=ipEnd);
    }

    public boolean isMatch(String ip){

        if(assertsList.isEmpty())
            return true;

        for(AssetsItem item:assertsList){

            if(isContains(item,ip))
                return true;
        }

        return false;
    }

}
