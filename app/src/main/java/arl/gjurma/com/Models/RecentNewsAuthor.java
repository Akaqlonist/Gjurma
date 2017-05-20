package arl.gjurma.com.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by KRYTON on 26-09-2016.
 */
public class RecentNewsAuthor implements Parcelable {
    @Expose
    private String name;
    @Expose
    private String first_name;
    @Expose
    private String last_name;
    @Expose
    private String nickname;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.first_name);
        dest.writeString(this.last_name);
        dest.writeString(this.nickname);
    }

    public RecentNewsAuthor() {
    }

    protected RecentNewsAuthor(Parcel in) {
        this.name = in.readString();
        this.first_name = in.readString();
        this.last_name = in.readString();
        this.nickname = in.readString();
    }

    public static final Creator<RecentNewsAuthor> CREATOR = new Creator<RecentNewsAuthor>() {
        @Override
        public RecentNewsAuthor createFromParcel(Parcel source) {
            return new RecentNewsAuthor(source);
        }

        @Override
        public RecentNewsAuthor[] newArray(int size) {
            return new RecentNewsAuthor[size];
        }
    };
}
