package web.login;

/**
 * A helper class to maintain constants used for the LoginServer example.
 */
public class LoginServerConstants {

    public static final String HOST = "slack.com";
    public static final String AUTH_PATH = "openid/connect/authorize";
    public static final String TOKEN_PATH = "api/openid.connect.token";
    public static final String RESPONSE_TYPE_KEY = "response_type";
    public static final String RESPONSE_TYPE_VALUE= "code";
    public static final String CODE_KEY= "code";
    public static final String SCOPE_KEY = "scope";
    public static final String SCOPE_VALUE = "openid%20profile%20email";
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String CLIENT_SECRET_KEY = "client_secret";
    public static final String STATE_KEY = "state";
    public static final String NONCE_KEY = "nonce";
    public static final String REDIRECT_URI_KEY = "redirect_uri";
    public static final String OK_KEY = "ok";


    public static final String CONFIG_KEY = "config_key";
    public static final String CLIENT_INFO_KEY = "client_info_key";
    public static final String BUTTON_URL = "https://platform.slack-edge.com/img/sign_in_with_slack@2x.png";

    public static final String IS_AUTHED_KEY = "is_authed";
    public static final String NAME_KEY = "name";
    public static final String EMAIL_KEY = "email";
    public static final String ACCESS_TOKEN_KEY = "access_token";

    public static final String TRANSACTION_TYPE_PURCHASED = "Purchased";
    public static final String TRANSACTION_TYPE_TRANSFERRED = "Transferred";
    public static final String TRANSACTION_TYPE_RECEIVED = "Received";

    public static final String JDBC_CONFIG_FILE_NAME = "jdbcConfig.json";
    public static final String SLACK_CONFIG_FILE_NAME = "slackConfig.json";

    public static final String TICKET_TRANSFER_SUCCESS_PAGE_HEADER = "Transaction Successful!";
    public static final String TICKET_TRANSFER_SUCCESS_PAGE_MESSAGE = "Ticket Successfully transferred!";

    public static final String TICKET_TRANSFER_UNSUCCESSFUL_PAGE_HEADER = "Oops! Issue Encountered.";
    public static final String TICKET_TRANSFER_UNSUCCESSFUL_PAGE_MESSAGE = "Ticket Transfer Failed. Please try again.";

    public static final String CREATE_EVENT_SUCCESS_PAGE_HEADER = "Event Creation Successful!";
    public static final String CREATE_EVENT_SUCCESS_PAGE_MESSAGE = "Event Successfully Created!";

    public static final int EVENT_DESCRIPTION_SIZE_CONSTRAINT = 380;
    public static final String EVENT_DESCRIPTION_ERROR_MESSAGE = "Description should be maximum of 380 character.";

    public static final int EVENT_NAME_SIZE_CONSTRAINT = 100;
    public static final String NAME_SIZE_ERROR_MESSAGE = "Name should be maximum of 100 character.";

    public static final String FILED_NOT_NULL_ERROR_MESSAGE = "This field can not be null.";
    public static final String FIELD_NOT_EMPTY_ERROR_MESSAGE = "This field can not be empty.";
    public static final String FIELD_NOT_BLANK_ERROR_MESSAGE = "This field can not be blank.";

}
