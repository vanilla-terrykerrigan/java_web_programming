package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = new LoggerFactory.getLogger(this.class);

    private Socket connection = null;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    // Thread's run method overriding
    public void run() {
        log.debug("new client connect! ip {}, port {}", connection.getInetAddress(), connection.getPort());
    }

}
