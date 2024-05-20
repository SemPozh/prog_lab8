package laba6.common.interaction;

import laba6.common.data.User;

import java.io.Serializable;

public class Response implements Serializable {
    private final ResponseCode responseCode;
    private final String responseBody;
    private final User user;

    public Response(ResponseCode responseCode, String responseBody, User user) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    /**
     * @return Response сode.
     */
    public ResponseCode getResponseCode() {
        return responseCode;
    }

    /**
     * @return Response body.
     */
    public String getResponseBody() {
        return responseBody;
    }

    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }
}
