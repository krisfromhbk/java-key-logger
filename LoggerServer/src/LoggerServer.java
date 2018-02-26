import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class LoggerServer extends Listener {

    private static Logger logger = LoggerFactory.getLogger(LoggerServer.class);

    private static Server server;
    private static int tcpPort = 80, udpPort = 80;

    public static void main(String[] args) {
        logger.debug("starting server");

        server = new Server();
        server.getKryo().register(Integer.class);
        try {
            server.bind(tcpPort, udpPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
        server.addListener(new LoggerServer());
    }

    public void connected(Connection c) {
        logger.debug("new connection from {}", c.getRemoteAddressTCP().getHostName());
    }

    public void received(Connection c, Object p) {
        logger.debug("get some data:");
        if ((p instanceof Integer) & (p != null)) {
            Integer receivedInteger = (Integer) p;
            logger.debug(String.valueOf(receivedInteger));
        }
    }

    /**public void disconnected(Connection c) {
        logger.debug("disconnected: {}", c.getRemoteAddressTCP().getHostName());
    }**/
}
