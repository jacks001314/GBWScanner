package com.gbw.scanner.plugins.scripts.windows.smb.MS17010;

import com.gbw.scanner.protocol.GBWBuffer;
import com.gbw.scanner.protocol.GBWProtoBuffer;
import com.gbw.scanner.protocol.smb.GBWSMBHeader;

public class SMBHeader implements GBWSMBHeader {

    private long serverComponent;
    private int smbCmd;
    private int errClass;
    private int reserved1;
    private int errCode;
    private int flags;
    private int flags2;
    private int processIDHigh;
    private long signature;
    private int reversed2;
    private int treeID;
    private int processID;
    private int userID;
    private int multiplexID;

    public SMBHeader(){

    }
    public SMBHeader(GBWBuffer<?> buffer) throws GBWBuffer.GBWBufferException {

        readFrom(buffer);
    }

    public SMBHeader(byte[] data) throws GBWBuffer.GBWBufferException {

        readFrom(new GBWProtoBuffer(data));
    }

    @Override
    public void writeTo(GBWProtoBuffer buffer) {

    }

    @Override
    public void readFrom(GBWBuffer<?> buffer) throws GBWBuffer.GBWBufferException {

        buffer.skip(4);
        this.serverComponent = buffer.readUInt32();
        this.smbCmd = buffer.readByte();
        this.errClass = buffer.readByte();
        this.reserved1 = buffer.readByte();
        this.errCode = buffer.readUInt16();
        this.flags = buffer.readByte();
        this.flags2 = buffer.readUInt16();
        this.processIDHigh = buffer.readUInt16();
        signature = buffer.readLong();
        this.reversed2 = buffer.readUInt16();
        this.treeID = buffer.readUInt16();
        this.processID = buffer.readUInt16();
        this.userID = buffer.readUInt16();
        this.multiplexID = buffer.readUInt16();
    }


    public long getServerComponent() {
        return serverComponent;
    }

    public void setServerComponent(long serverComponent) {
        this.serverComponent = serverComponent;
    }

    public int getSmbCmd() {
        return smbCmd;
    }

    public void setSmbCmd(int smbCmd) {
        this.smbCmd = smbCmd;
    }

    public int getErrClass() {
        return errClass;
    }

    public void setErrClass(int errClass) {
        this.errClass = errClass;
    }

    public int getReserved1() {
        return reserved1;
    }

    public void setReserved1(int reserved1) {
        this.reserved1 = reserved1;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getFlags2() {
        return flags2;
    }

    public void setFlags2(int flags2) {
        this.flags2 = flags2;
    }

    public int getProcessIDHigh() {
        return processIDHigh;
    }

    public void setProcessIDHigh(int processIDHigh) {
        this.processIDHigh = processIDHigh;
    }

    public int getReversed2() {
        return reversed2;
    }

    public void setReversed2(int reversed2) {
        this.reversed2 = reversed2;
    }

    public int getTreeID() {
        return treeID;
    }

    public void setTreeID(int treeID) {
        this.treeID = treeID;
    }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getMultiplexID() {
        return multiplexID;
    }

    public void setMultiplexID(int multiplexID) {
        this.multiplexID = multiplexID;
    }

    public long getSignature() {
        return signature;
    }

    public void setSignature(long signature) {
        this.signature = signature;
    }
}
