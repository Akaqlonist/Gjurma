package arl.gjurma.com.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by KRYTON on 26-09-2016.
 */
public class RecentNewsAttach implements Parcelable {
    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    public RecentNewsAttach() {
    }

    protected RecentNewsAttach(Parcel in) {
        this.url = in.readString();
    }

    public static final Creator<RecentNewsAttach> CREATOR = new Creator<RecentNewsAttach>() {
        @Override
        public RecentNewsAttach createFromParcel(Parcel source) {
            return new RecentNewsAttach(source);
        }

        @Override
        public RecentNewsAttach[] newArray(int size) {
            return new RecentNewsAttach[size];
        }
    };
}
