/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mess;

/**
 *
 * @author Ioana
 */
public interface Consts {
    
    static final String passwords = "C:/Users/Ioana/Desktop/Mess/etc/Users.txt";
    static final int PORT = 4447;
    
    //commands
    static final String SIGN_UP = "signup";
    static final String LOG_IN = "login";
    static final String QUIT = "signout";
    static final String BCAST = "bcast";
    static final String LIST = "list";
    static final String MSG = "msg";
    static final String NICK = "nick";
    static final String QUIT_CLIENT = "Good bye!";
    //log messages
    static final String NEW_USER_ERROR = "Existing user. Try another.\n";
    static final String ERROR = "Error: \n";
    static final String SUCCESS = "Success"; 
    static final String WRONG_PASS = "Wrong password.\n";
    static final String INVALID_USER = "Invalid user.\n";
    static final String SERVER_ERROR = "Server error.\n";
    static final String USER_OK = "User ok.\n";
    static final String SIGN_UP_ATTEMPT = "User signup.\n";
    
    static final String CMD_FALSE = "Invalid command.\n";
    static final String LOGIN_FALSE = "Please login first.\n";
    static final String LOGIN_TRUE = "You are now logged in.\n";
    static final String OPTION_1 = "LOGIN, SIGNUP or QUIT?\n"
                + "*LOGIN cmd format: login user password;\n"
                + "*SIGNUP cmd format: signup user password;\n"
                + "*QUIT cmd format: signout.\n";
    static final String OPTION_2 = "Command list:\n"
                + "*NICK cmd format: nick new_user;\n"
                + "*LIST cmd format: list;\n"
                + "*MSG cmd format: msg to_user message;\n"
                + "*BCAST cmd format: bcast message;\n"
                + "*QUIT cmd format: signout.\n";
}
