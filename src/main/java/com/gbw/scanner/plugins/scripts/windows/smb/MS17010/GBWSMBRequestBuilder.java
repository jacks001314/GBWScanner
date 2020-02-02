package com.gbw.scanner.plugins.scripts.windows.smb.MS17010;

import com.gbw.scanner.protocol.GBWProtoBuffer;

public class GBWSMBRequestBuilder {

    private GBWSMBRequestBuilder(){

    }

    public static final byte[] makeNegotiateProtoRequest(){

        GBWProtoBuffer smbBuffer = new GBWProtoBuffer();

        /*netbios header*/
        smbBuffer.putByte((byte)0x0); /*Message byte*/
        smbBuffer.putByte((byte)0x0); /*flag*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x00,(byte)0x54}); /*length*/

        /*smb header*/
        smbBuffer.putRawBytes(new byte[]{(byte) 0xFF, 'S', 'M', 'B'}); // Protocol (4 bytes)
        smbBuffer.putByte((byte)0x72); /*smb_command: Negotiate Protocol*/
        smbBuffer.putUInt32(0x0);      /*nt_status*/
        smbBuffer.putByte((byte)0x18);  /*flag*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x01,(byte)0x28}); /*flag2*/
        smbBuffer.putUInt16(0x0); /*process_id_high*/
        smbBuffer.putUInt64(0x0); /*SecurityFeatures (8 bytes)*/
        smbBuffer.putReserved2(); /*reserved*/
        smbBuffer.putUInt16(0x0); /*tree id*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x2F,(byte)0x4B}); /*process id*/
        smbBuffer.putUInt16(0x0); /*user id*/
        smbBuffer.putRawBytes(new byte[]{(byte)0xC5,(byte)0x5E}); /*multiplex_id*/

        /*nego data request*/
        smbBuffer.putByte((byte)0x0); /*word_count*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x31,(byte)0x0}); /*byte_count*/

        smbBuffer.putByte((byte)0x02); /*dialet_buffer_format*/
        smbBuffer.putRawBytes("LANMAN1.0\0".getBytes()); /*LANMAN1.0"*/

        smbBuffer.putByte((byte)0x02); /*dialet_buffer_format*/
        smbBuffer.putRawBytes("LM1.2X002\0".getBytes()); /*LM1.2X002*/

        smbBuffer.putByte((byte)0x02); /*dialet_buffer_format*/
        smbBuffer.putRawBytes("NT LANMAN 1.0\0".getBytes()); /*NT LANMAN 1.0*/

        smbBuffer.putByte((byte)0x02); /*dialet_buffer_format*/
        smbBuffer.putRawBytes("NT LM 0.12\0".getBytes());
        return smbBuffer.getCompactData();
    }

    public static final byte[] makeSessionSetupAndXRequest(){

        GBWProtoBuffer smbBuffer = new GBWProtoBuffer();

        /*netbios header*/
        smbBuffer.putByte((byte)0x0); /*Message byte*/
        smbBuffer.putByte((byte)0x0); /*flag*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x00,(byte)0x63}); /*length*

        /*smb header*/
        smbBuffer.putRawBytes(new byte[]{(byte) 0xFF, 'S', 'M', 'B'}); // Protocol (4 bytes)
        smbBuffer.putByte((byte)0x73); /*smb_command: Session Setup AndX*/
        smbBuffer.putUInt32(0x0);      /*nt_status*/
        smbBuffer.putByte((byte)0x18);  /*flag*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x01,(byte)0x20}); /*flag2*/
        smbBuffer.putUInt16(0x0); /*process_id_high*/
        smbBuffer.putUInt64(0x0); /*SecurityFeatures (8 bytes)*/
        smbBuffer.putReserved2(); /*reserved*/
        smbBuffer.putUInt16(0x0); /*tree id*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x2F,(byte)0x4B}); /*process id*/
        smbBuffer.putUInt16(0x0); /*user id*/
        smbBuffer.putRawBytes(new byte[]{(byte)0xC5,(byte)0x5E}); /*multiplex_id*/

        /*nego data request*/
        smbBuffer.putByte((byte)0x0D); /*word_count*/
        smbBuffer.putByte((byte)0xFF); /*AndXCommand: No further command*/
        smbBuffer.putReserved1();
        smbBuffer.putUInt16(0x0); /*AndXOffset*/
        smbBuffer.putRawBytes(new byte[]{(byte)0xDF,(byte)0xFF}); /*Max Buffer*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x02,(byte)0x0}); /*Max Mpx Count*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x01,(byte)0x0}); /*VC Number*/
        smbBuffer.putUInt32(0x0); /*Session Key*/
        smbBuffer.putUInt16(0x0); /*ANSI Password Length*/
        smbBuffer.putUInt16(0x0); /*Unicode Password Length*/
        smbBuffer.putReserved4();
        smbBuffer.putRawBytes(new byte[]{(byte)0x40,(byte)0x0,(byte)0x0,(byte)0x0}); /*Capabilities*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x26,(byte)0x0}); /*Byte Count*/
        smbBuffer.putByte((byte)0x0); /*Account*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x2e,(byte)0x0}); /*Primary Domain*/
        smbBuffer.putRawBytes("Windows 2000 2195\0".getBytes());
        smbBuffer.putRawBytes("Windows 2000 5.0\0".getBytes());

        return smbBuffer.getCompactData();
    }

    private static final String getIPC(String ip){

        StringBuilder b = new StringBuilder("\\\\");
        b.append(ip);
        b.append("\\IPC$\0");
        return b.toString();
    }

    public static final byte[] makeTreeConnectAndxRequest(String ip,int userId){

        GBWProtoBuffer smbBuffer = new GBWProtoBuffer();
        String ipc = getIPC(ip);

        /*netbios header*/
        smbBuffer.putByte((byte)0x0); /*Message byte*/
        smbBuffer.putByte((byte)0x0); /*flag*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x00,(byte)0x47}); /*length*

        /*smb header*/
        smbBuffer.putRawBytes(new byte[]{(byte) 0xFF, 'S', 'M', 'B'}); // Protocol (4 bytes)
        smbBuffer.putByte((byte)0x75); /*smb_command: Tree Connect AndX*/
        smbBuffer.putUInt32(0x0);      /*nt_status*/
        smbBuffer.putByte((byte)0x18);  /*flag*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x01,(byte)0x20}); /*flag2*/
        smbBuffer.putUInt16(0x0); /*process_id_high*/
        smbBuffer.putUInt64(0x0); /*SecurityFeatures (8 bytes)*/
        smbBuffer.putReserved2(); /*reserved*/
        smbBuffer.putUInt16(0x0); /*tree id*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x2F,(byte)0x4B}); /*process id*/
        smbBuffer.putUInt16(userId); /*user id*/
        smbBuffer.putRawBytes(new byte[]{(byte)0xC5,(byte)0x5E}); /*multiplex_id*/

        /*nego data request*/
        smbBuffer.putByte((byte)0x04); /*word_count*/
        smbBuffer.putByte((byte)0xFF); /*AndXCommand: No further command*/
        smbBuffer.putReserved1();
        smbBuffer.putUInt16(0x0); /*AndXOffset*/
        smbBuffer.putUInt16(0x0); /*Flags*/

        smbBuffer.putRawBytes(new byte[]{(byte)0x01,(byte)0x0}); /*passwd length*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x1C,(byte)0x0}); /*byte count*/
        smbBuffer.putByte((byte)0x0); /*passwd*/
        smbBuffer.putRawBytes(ipc.getBytes()); /* \\xxx.xxx.xxx.xxx\IPC$*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x3f,(byte)0x3f,(byte)0x3f,(byte)0x3f,(byte)0x3f,(byte)0x0}); /*service*/

        return smbBuffer.getCompactData();
    }

    public static final byte[] makePeekNamedPipeReqeust(int treeId, int processId, int userId, int multiplexId){

        GBWProtoBuffer smbBuffer = new GBWProtoBuffer();

        /*netbios header*/
        smbBuffer.putByte((byte)0x0); /*Message byte*/
        smbBuffer.putByte((byte)0x0); /*flag*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x00,(byte)0x4a}); /*length*

        /*smb header*/
        smbBuffer.putRawBytes(new byte[]{(byte) 0xFF, 'S', 'M', 'B'}); // Protocol (4 bytes)
        smbBuffer.putByte((byte)0x25); /*smb_command: peekpipe*/
        smbBuffer.putUInt32(0x0);      /*nt_status*/
        smbBuffer.putByte((byte)0x18);  /*flag*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x01,(byte)0x28}); /*flag2*/
        smbBuffer.putUInt16(0x0); /*process_id_high*/
        smbBuffer.putUInt64(0x0); /*SecurityFeatures (8 bytes)*/
        smbBuffer.putReserved2(); /*reserved*/
        smbBuffer.putUInt16(treeId); /*tree id*/
        smbBuffer.putUInt16(processId); /*process id*/
        smbBuffer.putUInt16(userId); /*user id*/
        smbBuffer.putUInt16(multiplexId); /*multiplex_id*/

        /*trans data request*/
        smbBuffer.putByte((byte)0x10); /*word_count*/
        smbBuffer.putUInt16(0x0); /*Total Parameter Count*/
        smbBuffer.putUInt16(0x0); /*Total data Count*/
        smbBuffer.putRawBytes(new byte[]{(byte)0xFF,(byte)0xFF}); /*Max params count*/
        smbBuffer.putRawBytes(new byte[]{(byte)0xFF,(byte)0xFF}); /*Max data count*/
        smbBuffer.putByte((byte)0x0); /*max setup count*/
        smbBuffer.putReserved1();

        smbBuffer.putUInt16(0x0); /*flags*/
        smbBuffer.putUInt32(0x0); /*Timeout: Return immediately*/
        smbBuffer.putReserved2();
        smbBuffer.putUInt16(0x0); /*Parameter Count*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x4a,(byte)0x0}); /*Parameter Offset*/
        smbBuffer.putUInt16(0x0); /*data Count*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x4a,(byte)0x0}); /*Data offset*/
        smbBuffer.putByte((byte)0x02); /*setup count*/
        smbBuffer.putReserved1();
        smbBuffer.putRawBytes(new byte[]{(byte)0x23,(byte)0x0}); /*SMB Pipe Protocol: Function: PeekNamedPipe (0x0023)*/
        smbBuffer.putUInt16(0x0); /*SMB Pipe Protocol: FID*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x07,(byte)0x0});

        smbBuffer.putRawBytes(new byte[]{(byte)0x5c,(byte)0x50,(byte)0x49,(byte)0x50,(byte)0x45,(byte)0x5c,(byte)0x0}); /*\PIPE\*/

        return smbBuffer.getCompactData();
    }

    public static final byte[] makeTransRequest(int treeId, int processId, int userId, int multiplexId){

        GBWProtoBuffer smbBuffer = new GBWProtoBuffer();

        /*netbios header*/
        smbBuffer.putByte((byte)0x0); /*Message byte*/
        smbBuffer.putByte((byte)0x0); /*flag*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x00,(byte)0x4f}); /*length*

        /*smb header*/
        smbBuffer.putRawBytes(new byte[]{(byte) 0xFF, 'S', 'M', 'B'}); // Protocol (4 bytes)
        smbBuffer.putByte((byte)0x32); /*smb_command: tran2*/
        smbBuffer.putUInt32(0x0);      /*nt_status*/
        smbBuffer.putByte((byte)0x18);  /*flag*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x07,(byte)0xc0}); /*flag2*/
        smbBuffer.putUInt16(0x0); /*process_id_high*/
        smbBuffer.putUInt64(0x0); /*SecurityFeatures (8 bytes)*/
        smbBuffer.putReserved2(); /*reserved*/
        smbBuffer.putUInt16(treeId); /*tree id*/
        smbBuffer.putUInt16(processId); /*process id*/
        smbBuffer.putUInt16(userId); /*user id*/
        smbBuffer.putUInt16(multiplexId); /*multiplex_id*/

        /*trans data request*/
        smbBuffer.putByte((byte)0x0f); /*word_count*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x0c,(byte)0xFF}); /*total params count*/
        smbBuffer.putUInt16(0x0); /*Total data Count*/

        smbBuffer.putRawBytes(new byte[]{(byte)0x01,(byte)0x0}); /*Max params count*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x0,(byte)0x0}); /*Max data count*/
        smbBuffer.putByte((byte)0x0); /*max setup count*/
        smbBuffer.putReserved1();

        smbBuffer.putUInt16(0x0); /*flags*/

        smbBuffer.putRawBytes(new byte[]{(byte)0xa6,(byte)0xd9,(byte)0xa4,(byte)0x0}); /*Timeout: 3 hours, 3.622 seconds*/
        smbBuffer.putReserved2();


        smbBuffer.putRawBytes(new byte[]{(byte)0x0c,(byte)0x0}); /*Parameter Count*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x42,(byte)0x0}); /*Parameter Offset*/
        smbBuffer.putUInt16(0x0); /*data Count*/

        smbBuffer.putRawBytes(new byte[]{(byte)0x4e,(byte)0x0}); /*Data offset*/

        smbBuffer.putByte((byte)0x01); /*setup count*/
        smbBuffer.putReserved1();

        smbBuffer.putRawBytes(new byte[]{(byte)0x0e,(byte)0x0}); /*ubcommand: SESSION_SETUP*/

        smbBuffer.putUInt16(0x0); /*byte count*/
        smbBuffer.putRawBytes(new byte[]{(byte)0x0c,(byte)0x00});
        smbBuffer.putReserved(12);

        return smbBuffer.getCompactData();
    }

}
