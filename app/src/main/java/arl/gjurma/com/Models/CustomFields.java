package arl.gjurma.com.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by KRYTON on 11-10-2016.
 */
public class CustomFields implements Parcelable {
    @Expose
    private String video[];
    @Expose
    private String header[];
    @Expose
    private String footer[];

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(this.video);
        dest.writeStringArray(this.header);
        dest.writeStringArray(this.footer);
    }

    public CustomFields() {
    }

    protected CustomFields(Parcel in) {
        this.video = in.createStringArray();
        this.header = in.createStringArray();
        this.footer = in.createStringArray();
    }

    public static final Creator<CustomFields> CREATOR = new Creator<CustomFields>() {
        @Override
        public CustomFields createFromParcel(Parcel source) {
            return new CustomFields(source);
        }

        @Override
        public CustomFields[] newArray(int size) {
            return new CustomFields[size];
        }
    };

    public String[] getVideo() {
        return video;
    }

    public void setVideo(String[] video) {
        this.video = video;
    }

    public String[] getHeader() {
        return header;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    public String[] getFooter() {
        return footer;
    }

    public void setFooter(String[] footer) {
        this.footer = footer;
    }
}
