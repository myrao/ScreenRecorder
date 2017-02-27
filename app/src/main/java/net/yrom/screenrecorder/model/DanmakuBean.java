package net.yrom.screenrecorder.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by raomengyang on 08/01/2017.
 */

public class DanmakuBean implements Serializable, Parcelable {

    private static final long serialVersionUID = 7186882618495894264L;

    /**
     * name : userName
     * message : userMessage
     */

    private String name;
    private String message;

    public DanmakuBean() {
    }

    protected DanmakuBean(Parcel in) {
        name = in.readString();
        message = in.readString();
    }

    public static final Creator<DanmakuBean> CREATOR = new Creator<DanmakuBean>() {
        @Override
        public DanmakuBean createFromParcel(Parcel in) {
            return new DanmakuBean(in);
        }

        @Override
        public DanmakuBean[] newArray(int size) {
            return new DanmakuBean[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(message);
    }

    @Override
    public String toString() {
        return "DanmakuBean{" +
                "name='" + name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
