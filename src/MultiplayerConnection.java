public class MultiplayerConnection {
    private String ip;
    private int port;

    public MultiplayerConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }


    public int getPort() {
        return port;
    }

}
