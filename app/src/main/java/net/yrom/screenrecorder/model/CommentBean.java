package net.yrom.screenrecorder.model;

import java.io.Serializable;

/**
 * Created by raomengyang on 08/01/2017.
 */

public class CommentBean implements Serializable {

    private static final long serialVersionUID = 7186882618495894264L;


    /**
     * name : userName
     * message : userMessage
     */

    private String name;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
