/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

/**
 *
 * @author Ioana
 */
public class Server implements Consts {

    private ServerSocket serverSocket;
    Hashtable sockets = new Hashtable();
    Socket clientSocket = null;

    public int start_server() {
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                clientSocket = serverSocket.accept();
                (new ServerThread(sockets, clientSocket)).start();
            }

        } catch (IOException e) {
            System.out.println(SERVER_ERROR);
            System.exit(-1);
        }

        return 0;
    }
}
