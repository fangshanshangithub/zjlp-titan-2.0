package com.zjlp.face.titan.common;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class EsClient {
    public  TransportClient getEsClient(String clusterName, String hosts, int port) {
        Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).put("client.transport.sniff", true).build();

        TransportClient client = TransportClient.builder().settings(settings).build();
        String[] hostips = hosts.split(",");
        for (String hostip : hostips) {
            try {
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostip), port));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return client;
    }

    public  TransportClient getEsClient(String clusterName, String hosts) {
        return getEsClient(clusterName, hosts, 9300);
    }
}
