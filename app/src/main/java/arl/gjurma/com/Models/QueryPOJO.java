package arl.gjurma.com.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KRYTON on 04-10-2016.
 */
public class QueryPOJO implements Parcelable {
    public String category;
    public String include;
    public String count;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.category);
        dest.writeString(this.include);
        dest.writeString(this.count);
    }

    public QueryPOJO() {
    }

    protected QueryPOJO(Parcel in) {
        this.category = in.readString();
        this.include = in.readString();
        this.count = in.readString();
    }

    public static final Creator<QueryPOJO> CREATOR = new Creator<QueryPOJO>() {
        @Override
        public QueryPOJO createFromParcel(Parcel source) {
            return new QueryPOJO(source);
        }

        @Override
        public QueryPOJO[] newArray(int size) {
            return new QueryPOJO[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
