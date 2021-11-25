package web.login;

/**
 * A class to maintain info about each client.
 */
public class ClientInfo {
    private String name;
    private String email;
    private String access_token;

    /**
     * Constructor
     * @param name
     */
    public ClientInfo(String name, String email, String access_token) {
        this.name = name;
        this.email = email;
        this.access_token = access_token;
    }
//    public ClientInfo(String name) {
//        this.name = name;
//        this.email = email;
//    }

    /**
     * return name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * return email
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * return access_token
     * @return
     */
    public String getAccessToken() {
        return access_token;
    }
}
