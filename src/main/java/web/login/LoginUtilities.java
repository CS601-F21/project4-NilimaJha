package web.login;

import com.google.gson.Gson;
import model.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.StringReader;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Class containing utility functions required for login via slack.
 *
 * @author nilimajha
 */
public class LoginUtilities {
    // for parsing JSON
    private static final Gson gson = new Gson();

    /**
     * Hashes the session ID to generate a nonce.
     *
     * @param sessionId
     * @return
     */
    public static String generateNonce(String sessionId) {
        String sha256hex = DigestUtils.sha256Hex(sessionId);
        return sha256hex;
    }

    /**
     * Generates the URL to make the initial request to the slack's authorize API.
     * @param clientId
     * @param state
     * @param nonce
     * @param redirectURI
     * @return
     */
    public static String generateSlackAuthorizeURL(String clientId, String state, String nonce, String redirectURI) {

        String url = String.format("https://%s/%s?%s=%s&%s=%s&%s=%s&%s=%s&%s=%s&%s=%s",
                LoginServerConstants.HOST,
                LoginServerConstants.AUTH_PATH,
                LoginServerConstants.RESPONSE_TYPE_KEY,
                LoginServerConstants.RESPONSE_TYPE_VALUE,
                LoginServerConstants.SCOPE_KEY,
                LoginServerConstants.SCOPE_VALUE,
                LoginServerConstants.CLIENT_ID_KEY,
                clientId,
                LoginServerConstants.STATE_KEY,
                state,
                LoginServerConstants.NONCE_KEY,
                nonce,
                LoginServerConstants.REDIRECT_URI_KEY,
                redirectURI
        );
        return url;
    }

    /**
     * Generates the URL to exchange the initial auth for a token.
     * @param clientId
     * @param clientSecret
     * @param code
     * @param redirectURI
     * @return
     */
    public static String generateSlackTokenURL(String clientId, String clientSecret, String code, String redirectURI) {

        String url = String.format("https://%s/%s?%s=%s&%s=%s&%s=%s&%s=%s",
                LoginServerConstants.HOST,
                LoginServerConstants.TOKEN_PATH,
                LoginServerConstants.CLIENT_ID_KEY,
                clientId,
                LoginServerConstants.CLIENT_SECRET_KEY,
                clientSecret,
                LoginServerConstants.CODE_KEY,
                code,
                LoginServerConstants.REDIRECT_URI_KEY,
                redirectURI
        );
        return url;
    }

    /**
     * Convert a JSON-formatted String to a Map.
     * @param jsonString
     * @return
     */
    public static Map<String, Object> jsonStrToMap(String jsonString) {
        System.out.println("jsonString from slack :-" + jsonString);
        Map<String, Object> map = gson.fromJson(new StringReader(jsonString), Map.class);
        System.out.println("State :" +map.get(LoginServerConstants.STATE_KEY));
        return map;
    }

    /**
     * Verify the response from the token API.
     * If successful, returns a ClientInfo object with information about the authed client.
     * Returns null if not successful.
     * @param map
     * @param sessionId
     * @return
     */
    public static User verifyTokenResponse(Map<String, Object> map, String sessionId) {
        // verify ok: true
        if(!map.containsKey(LoginServerConstants.OK_KEY) || !(boolean)map.get(LoginServerConstants.OK_KEY)) {
            return null;
        }
        // verify state is the users session cookie id
        if(!map.containsKey(LoginServerConstants.STATE_KEY) || !map.get(LoginServerConstants.STATE_KEY).equals(sessionId)) {
            return null;
        }
        // retrieve and decode id_token
        String idToken = (String)map.get("id_token");
        Map<String, Object> payloadMap = decodeIdTokenPayload(idToken);
        //verify nonce
        String expectedNonce = generateNonce(sessionId);
        String actualNonce = (String) payloadMap.get(LoginServerConstants.NONCE_KEY);
        if(!expectedNonce.equals(actualNonce)) {
            return null;
        }
        // extract name from response
        Set<String> set = payloadMap.keySet();
        String userName = (String) payloadMap.get(LoginServerConstants.NAME_KEY);
        String userEmailId = (String) payloadMap.get(LoginServerConstants.EMAIL_KEY);
        String userAccessToken = (String)map.get(LoginServerConstants.ACCESS_TOKEN_KEY);
        return new User(userEmailId, userName, userAccessToken);
    }

    /**
     *
     * From the Slack documentation:
     * id_token is a standard JSON Web Token (JWT). You can decode it with off-the-shelf libraries in any programming
     * language, and most packages that handle OpenID will handle JWT decoding.
     *
     * Method decodes the String id_token and returns a Map with the contents of the payload.
     *
     * @param idToken
     * @return
     */
    private static Map<String, Object> decodeIdTokenPayload(String idToken) {
        // Decoding process taken from:
        // https://www.baeldung.com/java-jwt-token-decode
        String[] chunks = idToken.split("\\.");
        System.out.println("Idtoken: " + idToken);
        Base64.Decoder decoder = Base64.getDecoder();
        System.out.println(chunks[1]);

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        // convert the id_token payload to a map
        Map<String, Object> payloadMap = gson.fromJson(new StringReader(payload), Map.class);
        return payloadMap;
    }
}
