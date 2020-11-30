package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class WebServer {

    //set log
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        log.debug("START");

        //port setting
        if (args != null && args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        //create socket with port
        try (ServerSocket listenSocket = new ServerSocket(port)) {

            log.info("web server started {} port", port);
            Socket connection;

            //wait for connection
            while((connection = listenSocket.accept()) != null) {

                //pass request to request handler
                log.debug("socket connected");

                RequestHandlers requestHandler = new RequestHandlers(connection);
                requestHandler.start();
            }

        } catch (IOException e) {

        }

    }
}
