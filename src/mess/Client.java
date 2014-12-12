/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mess;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ioana
 */
public class Client implements Consts {

    String Name = null;

    void start_client() {

        System.out.print(OPTION_1);

        Socket clientSocket;
        try {
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            clientSocket = new Socket("localhost", PORT);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            ClientThread reader = new ClientThread(clientSocket);
            reader.start();
            while (!line.equals(QUIT)) {
                out.println(line);
                line = sc.nextLine();
            }
            out.println(line);
            out.close();

        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}