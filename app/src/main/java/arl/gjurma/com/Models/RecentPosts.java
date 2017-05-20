package arl.gjurma.com.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KRYTON on 25-09-2016.
 */
public class RecentPosts implements Parcelable {
    @Expose
    private int id;
    @Expose
    private String type;
    @Expose
    private String slug;
    @Expose
    private String url;
    @Expose
    private String status;
    @Expose
    private String title;
    @Expose
    private String title_plain;
    @Expose
    private String content;
    @Expose
    private String excerpt;
    @Expose
    private String date;
    @Expose
    private String thumbnail;
    @Expose
    private List<RecentNewsAttach> attachments = new ArrayList<>();
    @Expose
    private RecentNewsAuthor author;
    @Expose
    private CustomFields custom_fields;


    public RecentPosts() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_plain() {
        return title_plain;
    }

    public void setTitle_plain(String title_plain) {
        this.title_plain = title_plain;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<RecentNewsAttach> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<RecentNewsAttach> attachments) {
        this.attachments = attachments;
    }

    public RecentNewsAuthor getAuthor() {
        return author;
    }

    public void setAuthor(RecentNewsAuthor author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.type);
        dest.writeString(this.slug);
        dest.writeString(this.url);
        dest.writeString(this.status);
        dest.writeString(this.title);
        dest.writeString(this.title_plain);
        dest.writeString(this.content);
        dest.writeString(this.excerpt);
        dest.writeString(this.date);
        dest.writeString(this.thumbnail);
        dest.writeTypedList(this.attachments);
        dest.writeParcelable(this.author, flags);
        dest.writeParcelable(this.custom_fields, flags);
    }

    protected RecentPosts(Parcel in) {
        this.id = in.readInt();
        this.type = in.readString();
        this.slug = in.readString();
        this.url = in.readString();
        this.status = in.readString();
        this.title = in.readString();
        this.title_plain = in.readString();
        this.content = in.readString();
        this.excerpt = in.readString();
        this.date = in.readString();
        this.thumbnail = in.readString();
        this.attachments = in.createTypedArrayList(RecentNewsAttach.CREATOR);
        this.author = in.readParcelable(RecentNewsAuthor.class.getClassLoader());
        this.custom_fields = in.readParcelable(CustomFields.class.getClassLoader());
    }

    public static final Creator<RecentPosts> CREATOR = new Creator<RecentPosts>() {
        @Override
        public RecentPosts createFromParcel(Parcel source) {
            return new RecentPosts(source);
        }

        @Override
        public RecentPosts[] newArray(int size) {
            return new RecentPosts[size];
        }
    };

    public CustomFields getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(CustomFields custom_fields) {
        this.custom_fields = custom_fields;
    }
}
