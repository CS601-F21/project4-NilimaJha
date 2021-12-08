package utils;

/**
 * class to store config data from slack config file.
 * @author nilimajha
 */
public class LoginWithSlackConfig {
    // These variable names violate Java style guidelines
    // in order to be consistent with the naming conventions
    // in the Slack API
    private String redirect_uri;
    private String client_id;
    private String client_secret;

    /**
     * constructor to initialise all the attribute of the class.
     * @param redirect_uri
     * @param client_id
     * @param client_secret
     */
    public LoginWithSlackConfig(String redirect_uri, String client_id, String client_secret) {
        this.redirect_uri = redirect_uri;
        this.client_id = client_id;
        this.client_secret = client_secret;
    }

    /**
     * getter for class attribute redirect_uri.
     * @return redirect_uri
     */
    public String getRedirect_url() {
        return redirect_uri;
    }

    /**
     * getter for class attribute client_id.
     * @return client_id
     */
    public String getClient_id() {
        return client_id;
    }

    /**
     * getter for class attribute client_secret.
     * @return client_secret
     */
    public String getClient_secret() {
        return client_secret;
    }
}
