package com.gbw.scanner.source.fofa;

public class GBWFoFaHostData {

        private String id;
        private String host;
        private String proto;
        private int port;

        public GBWFoFaHostData(String id,String defProto,int defPort){

            this.id = id;
            String[] ids = id.replace("//","").split(":");

            this.proto = defProto;
            this.port = defPort;

            if(ids.length == 1){
                this.host = ids[0];
            }else if(ids.length == 2){

                char c = ids[0].charAt(0);
                if(Character.isDigit(c)){
                    this.host = ids[0];
                    this.port = Integer.parseInt(ids[1]);
                }else{
                    this.proto = ids[0];
                    this.host = ids[1];
                    if(proto.equalsIgnoreCase("https")){
                        port = 443;
                    }else if(proto.equalsIgnoreCase("http")){
                        port = 80;
                    }
                }
            }else if(ids.length == 3){

                this.proto = ids[0];
                this.host = ids[1];
                this.port = Integer.parseInt(ids[2]);
            }

        }

        public String getId() {
        return id;
    }

        public void setId(String id) {
            this.id = id;

        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getProto() {
            return proto;
        }

        public void setProto(String proto) {
            this.proto = proto;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String toString(){

            return String.format("id:%s,proto:%s,host:%s,%d",id,proto,host,port);
        }

    }
