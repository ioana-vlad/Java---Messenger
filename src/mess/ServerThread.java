package mess;

import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread extends Thread implements Consts {

    private Hashtable Sockets = null;
    private String Name = null;
    Socket ClientSocket;
    BufferedReader in;
    PrintWriter out;

    public ServerThread(Hashtable sockets, Socket clientSocket) {
        super("ServerThread");
        this.Sockets = sockets;
        this.ClientSocket = clientSocket;

    }

    public int sign_up(String new_user, String new_pass) {
        try {
            Scanner keyboard = new Scanner(new FileReader(passwords));
            while (keyboard.hasNextLine()) {
                String line = keyboard.nextLine();
                StringTokenizer st = new StringTokenizer(line);
                String user = st.nextToken(), pass = st.nextToken();
                if (user.equals(new_user)) {
                    return -1;
                }
            }

            keyboard.close();
            FileWriter fw = new FileWriter(passwords, true); //the true will append the new data
            fw.write(new_user + " " + new_pass + "\n");//appends the string to the file
            fw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
        Name = new_user;
        Sockets.put(new_user, ClientSocket);
        Name = new_user;
        return 0;
    }

    public int log_in(String loguser, String logpass) {
        try {
            Scanner passFile = new Scanner(new FileReader(passwords));
            while (passFile.hasNextLine()) {
                String line = passFile.nextLine();
                StringTokenizer st = new StringTokenizer(line);
                String user = st.nextToken(), pass = st.nextToken();
                if (user.equals(loguser)) {
                    if (pass.equals(logpass)) {
                        Name = loguser;
                        Sockets.put(loguser, ClientSocket);
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }
            passFile.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
        return -3;
    }

    public int nick(String old_user, String new_user) {
        try {
            String new_file = "";
            Scanner sc = new Scanner(new FileReader(passwords));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                StringTokenizer st = new StringTokenizer(line);
                String user = st.nextToken(), pass = st.nextToken();
                if (user.equals(new_user)) {
                    return -1;
                }
                else if (user.equals(old_user)) {
                    new_file += new_user + " " + pass + "\n";
                } 
                else {
                    new_file += line + "\n";
                }
            }
            sc.close();
            FileWriter fstream;
            fstream = new FileWriter(passwords);
            BufferedWriter wr = new BufferedWriter(fstream);
            wr.write(new_file);
            wr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
        Sockets.remove(Name); 
        Sockets.put(new_user, ClientSocket);
        Name = new_user;
        return 0;
    }
    
    public int bcast(String from_user){
        return 0;
    }
    
    public String list_users(String from_user){
        /*try {
            out = new PrintWriter(ClientSocket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        String message = "list " + SUCCESS +  "\nOnline users are:\n";
        for(Object key: Sockets.keySet()){
        message += (String)key + "\n";
    }
        //out.print(message);
        return message;
    }

    int executeLine(String line) throws IOException {
        StringTokenizer st = new StringTokenizer(line);
        String function = st.nextToken();
        System.out.print("REQUEST: " + function + "\n");
        if (Name == null){
            if (function.equals(QUIT)){
               out = new PrintWriter(ClientSocket.getOutputStream(), true);
               out.write(QUIT_CLIENT);
               out.close();
               in.close();
               ClientSocket.close();
               System.out.print(SUCCESS + "\n");
               return 1;
            }
            else if (function.equals(LOG_IN)){
                int ret;
                ret = log_in(st.nextToken(), st.nextToken());
                String message = LOG_IN + " " + Name;
                if (ret == 0){
                    message += " " + SUCCESS;
                }
                else if (ret == -1){
                    message += " " + ERROR + " " + WRONG_PASS;
                }
                else {
                    message += " " + SERVER_ERROR;
                }
                out = new PrintWriter(ClientSocket.getOutputStream(), true);
                out.println(message);
                System.out.print(message);
            }
            else if (function.equals(SIGN_UP)){
                int ret;
                String message = SIGN_UP + " " + Name;
                ret = sign_up(st.nextToken(), st.nextToken());
                if (ret == 0){
                    message += " " + SUCCESS;
                }
                else if (ret == -1){
                    message += " " + ERROR + " " + NEW_USER_ERROR;
                }
                else {
                    message += " " + ERROR + " " + INVALID_USER;
                }
                out = new PrintWriter(ClientSocket.getOutputStream(), true);
                out.println(message);
                System.out.print(message);
            }
            else {
                System.out.print(CMD_FALSE);
                out = new PrintWriter(ClientSocket.getOutputStream(), true);
                out.print(CMD_FALSE);
            }
        }
        else {
            if (function.equals(QUIT)) {
                out = new PrintWriter(ClientSocket.getOutputStream(), true);
                out.print(QUIT_CLIENT);
                out.close();
                in.close();
                ClientSocket.close();
                Sockets.remove(Name); 
                System.out.print(SUCCESS + "\n");
                return 1;
            }
            else if (function.equals(MSG)){
                String to_user = st.nextToken();
                out = new PrintWriter(((Socket) Sockets.get(to_user)).getOutputStream(), true);
                String message = MSG + " from " + Name + ":";
                while (st.hasMoreElements()) {
                    message += " " + st.nextToken();
                }
                out.println(message);
                System.out.print("Message sent form " + Name + " to " + to_user + "\n");
                out = new PrintWriter(ClientSocket.getOutputStream(), true);
                out.print(MSG + " sent.");
            }
            else if (function.equals(NICK)){
                String new_user_name = st.nextToken();
                String message;
                int ret = nick(Name, new_user_name);
                //out = new PrintWriter(((Socket) Sockets.get(Name)).getOutputStream(), true);
                out = new PrintWriter(ClientSocket.getOutputStream(), true);
                if (ret == -1) {
                    message = ERROR + " " + NEW_USER_ERROR;
                }
                else if (ret == -2) {
                    message = ERROR + " " + SERVER_ERROR;
                } 
                else {
                    message = SUCCESS;
                    
                }
                out.println(message);
                System.out.println(message);
            }
            else if (function.equals(BCAST)){
                String message = "bcast from " + Name + ":";
                while (st.hasMoreElements()) {
                    message += " " + st.nextToken();
                }
               for(Object key: Sockets.keySet()) {
                    if (!Name.equals((String)key)){
                        out = new PrintWriter(((Socket) Sockets.get((String)key)).getOutputStream(), true);
                        out.println(message);
                    }
                    }
                    System.out.print("Bcast sent form " + Name + "\n");
                    message = BCAST + " sent";
                    out = new PrintWriter(((Socket) Sockets.get(Name)).getOutputStream(), true);
                    out.println(message);
                } 
            else if (function.equals(LIST)){
                out = new PrintWriter(((Socket) Sockets.get(Name)).getOutputStream(), true);
                //out.print("Online users:\n");
                String message = "list " + SUCCESS +  " Online users are: ";
                for(Object key: Sockets.keySet()) {
                    message += (String)key + " ";
                }
                System.out.print(message);
                out.println(message);
                //int ret = list(Name);
                
            }
            else {
                System.out.print(CMD_FALSE);
                out = new PrintWriter(ClientSocket.getOutputStream(), true);
                out.println(CMD_FALSE);
            }
        }
        return 0;
    }

    @Override
    public void run() {

        try {
            in = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            String inputLine = in.readLine();
            while (executeLine(inputLine) == 0) {
                inputLine = in.readLine();
            }
        } catch (IOException e) {
            System.out.print("Unknown error.\n");
        }
    }
}
