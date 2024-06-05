package laba7.common.interaction;


import java.io.Serializable;

public class
Response implements Serializable {
    private final ResponseCode responseCode;
    private final String responseBody;
    public Response(ResponseCode responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
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
