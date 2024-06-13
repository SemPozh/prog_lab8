package laba8.laba8.common.interaction;


import laba8.laba8.common.data.Organization;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedDeque;

public class
Response implements Serializable {
    private final ResponseCode responseCode;
    ConcurrentLinkedDeque<Organization> collection;
    private final String responseBody;
    private String[] responseBodyArgs;
    public Response(ResponseCode responseCode, String responseBody, ConcurrentLinkedDeque<Organization> collection, String[] responseBodyArgs) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.collection = collection;
        this.responseBodyArgs = responseBodyArgs;
    }

    public String[] getResponseBodyArgs() {
        return responseBodyArgs;
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

    public ConcurrentLinkedDeque<Organization> getCollection() {
        return collection;
    }
}
