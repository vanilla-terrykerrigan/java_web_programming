package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.Socket;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection = null;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    // Thread's run method overriding
    public void run() {
        log.debug("new client connect! ip {}, port {}", connection.getInetAddress(), connection.getPort());

        // get socket's stream (input, output)
        try(InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream() ){

            //handling todo!!!

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = "hello world".getBytes();
            response200Header(dos, body.length);
            responseBody(dos, body);

        } catch(IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int length) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-type: text/html;charset=utf-8/r/n");
            dos.writeBytes("Content-Length: " + length + "\r\n");
            dos.writeBytes("\r\n");
        } catch(IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
