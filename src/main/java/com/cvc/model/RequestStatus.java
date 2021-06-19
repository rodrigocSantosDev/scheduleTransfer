package com.cvc.model;

import java.io.Serializable;

public class RequestStatus implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public RequestStatus() {
        super();
    }

    /**
     * @return the messageID
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * @param messageID the messageID to set
     */
    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    /**
     * @return the responseBody
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * @param responseBody the responseBody to set
     */
    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public RequestStatus(int status, String responseBody) {
        this.setStatus(status);
        this.setResponseBody(responseBody);
    }

    public RequestStatus(int status, String responseBody, String messageID) {
        this.setStatus(status);
        this.setResponseBody(responseBody);
        this.setMessageID(messageID);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if (status == 201)
            status = 200;
        this.status = status;
    }

    private int status = 200;
    private String responseBody = "";
    private String messageID = "";
    

}
