package com.qy.ccm.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class SocketClient extends WebSocketClient {

    static final String message = "{\"type_id\":\"13\",\"name\":0,\"price\":0,\"zd\":0,\"cn\":\"CNY\",\"state\":\"2\",\"me_member_id\":\"0\",\"xid\":\"\"}";
    public static String result = "";
    public static WebSocketClient client;

    /**
     * 此方法为了直接运行测试用例，实际使用自行此类重写
     *
     * @param args
     * @throws URISyntaxException
     */
    public static void main(String[] args) throws URISyntaxException {
//        System.out.println("===="+webMarket());
        webMarket();
    }

    public static void webMarket() throws URISyntaxException {
        client = new SocketClient(new URI("wss://www.ccm.one/wss2000/"));
        client.connect();

//        System.out.println(client.isConnecting());
//        while (true) {
//            try {
//                Thread.sleep(1000L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
////            System.out.println(client.isConnecting());
//            System.out.println(client.isOpen());
//
//            System.out.println(result);
//        }
    }

    public SocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("opened connection");
        send(message);
    }

    @Override
    public void onMessage(String message) {
//        System.out.println(message);
        this.result = message;
        try {
            Thread.sleep(500L);

            send(this.message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed by " + (remote ? "remote peer" : "us"));
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

}
