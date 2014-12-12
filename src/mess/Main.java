package mess;

import java.util.Scanner;

public class Main{

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int serverClient = sc.nextInt();
        if (serverClient == 1){
            Server x = new Server();
            x.start_server();
        }
        else {
            Client y = new Client();
            y.start_client();            
        }
    }
}
