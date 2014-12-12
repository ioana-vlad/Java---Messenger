/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ioana
 */
public class ClientThread extends Thread implements Consts {

    Socket clientSocket;
    String Name = null;

    ClientThread(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line = in.readLine();
            while (!line.equals(QUIT_CLIENT)) {
                StringTokenizer st = new StringTokenizer(line);
                String function = st.nextToken();
                String user, status;
                if (Name == null) {
                    if (function.equals(LOG_IN) || function.equals(SIGN_UP)) {
                        user = st.nextToken();
                        status = st.nextToken();
                        if (status.equals(SUCCESS)) {
                            Name = user;
                            System.out.print(LOGIN_TRUE + OPTION_2);
                        } else {
                            System.out.print(ERROR + line + "\n");
                        }
                    } else {
                        System.out.print(LOGIN_FALSE);
                    }
                } else {
                    if (function.equals(MSG) || function.equals(NICK) || function.equals(BCAST) || function.equals(LIST)) {
                        System.out.print(line + "\n");
                    } else {
                        System.out.print(CMD_FALSE);
                    }
                }

                line = in.readLine();
            }
            System.out.print(line + "\n");
            in.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
