package com.gbw.scanner.source.iprange;

import java.util.ArrayList;
import java.util.List;

public class GBWIPRangePort2ScanType {

    private String proto;
    private GBWPortRange portRange;
    private List<Integer> ports;
    private List<String> scanTypes;

    public List<Integer> getPorts() {
        return ports;
    }

    public void setPorts(List<Integer> ports) {
        this.ports = ports;
    }

    public List<String> getScanTypes() {
        return scanTypes;
    }

    public void setScanTypes(List<String> scanTypes) {
        this.scanTypes = scanTypes;
    }

    public GBWPortRange getPortRange() {
        return portRange;
    }

    public void setPortRange(GBWPortRange portRange) {
        this.portRange = portRange;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public List<Integer> getFinalPorts(){

        int start = portRange.getStart();
        int end = portRange.getEnd();

        if(start == -1||end==-1||end<start)
            return ports;

        List<Integer> res = new ArrayList<>();
        for(int port = start;port<=end;port++){
            res.add(port);
        }

        return res;
    }

}
