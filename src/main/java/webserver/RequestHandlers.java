package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
//import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.User;

public class RequestHandlers extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandlers.class);

    private Socket connection = null;

    public RequestHandlers(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    // Thread's run method overriding
    public void run() {
        log.debug("new client connect! ip {}, port {}", connection.getInetAddress(), connection.getPort());

        // get socket's stream (input, output)
        try(InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream() ) {

            //handling todo!!!
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String line = br.readLine();
            if (line == null) {
                return;
            }

            String[] token = line.split(" ");
            int contentLength = 0;
            boolean body_start = false;

            String uri = token[1];

            if (uri.endsWith(".html")) {
                DataOutputStream dos = new DataOutputStream(out);
                Path path = Paths.get("./webapp" + uri);
                byte[] body = Files.readAllBytes(path);
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
            else if(uri.startsWith("/user/create")) {
                log.info(line);

                //get 방식으로 처리한 유저 생성.=======================
//                //parsing query
//                String[] tmp = uri.split("\\?");
//                String[] quries = tmp[1].split("&");
//                Map<String, String> map = new HashMap<String, String>();
//                for (String query : quries)
//                {
//                    String name = query.split("=")[0];
//                    String value = query.split("=")[1];
//                    map.put(name, value);
//                }
//
//                //
//                User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
//                log.info("user : {}", user);리


                //post 방식으로 처리한 유저 생성.=======================
                while(!line.equals("")) {

                    if(line.contains("Content-Length")) {
                        String[] content_len = line.replace(" ", "").split(":");
                        contentLength = Integer.parseInt(content_len[1]);
                    }

                    line = br.readLine();
                }

                // read body data
                char[] req_body_data = new char[contentLength];
                br.read(req_body_data, 0, contentLength);
                String str_body_data = String.copyValueOf(req_body_data);

                // parsign body data
                String[] quries = str_body_data.split("&");
                Map<String, String> map = new HashMap<String, String>();
                for (String query : quries)
                {
                    String name = query.split("=")[0];
                    String value = query.split("=")[1];
                    map.put(name, value);
                }

                // set user account info
                User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
                log.info("user : {}", user);




            } else {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = "hello world".getBytes();
                response200Header(dos, body.length);
                responseBody(dos, body);
            }

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
