package arl.gjurma.com.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KRYTON on 25-09-2016.
 */
public class RecentPost implements Parcelable {
    @Expose
    private List<RecentPosts> posts = new ArrayList<>();


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.posts);
    }

    public RecentPost() {
    }

    protected RecentPost(Parcel in) {
        this.posts = in.createTypedArrayList(RecentPosts.CREATOR);
    }

    public static final Creator<RecentPost> CREATOR = new Creator<RecentPost>() {
        @Override
        public RecentPost createFromParcel(Parcel source) {
            return new RecentPost(source);
        }

        @Override
        public RecentPost[] newArray(int size) {
            return new RecentPost[size];
        }
    };

    public List<RecentPosts> getPosts() {
        return posts;
    }

    public void setPosts(List<RecentPosts> posts) {
        this.posts = posts;
    }
}
