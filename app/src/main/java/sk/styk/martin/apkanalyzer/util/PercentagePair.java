package sk.styk.martin.apkanalyzer.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Pair of number and its percentage part of total count
 * Used in statistics task
 * <p>
 * @author Martin Styk
 * @version 21.01.2016.
 */
public class PercentagePair implements Parcelable {
    private Number count;
    private BigDecimal percentage;

    public PercentagePair(Number count, BigDecimal percentage) {
        this.count = count;
        this.percentage = percentage;
    }

    public PercentagePair(Number count, int total) {
        this.count = count;
        this.percentage = PercentageHelper.getPercentage(count.doubleValue(), total);
    }

    public int hashCode() {
        int hashFirst = count != null ? count.hashCode() : 0;
        int hashSecond = percentage != null ? percentage.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(Object other) {
        if (other instanceof PercentagePair) {
            PercentagePair otherPair = (PercentagePair) other;
            return
                    ((this.count.equals(otherPair.count) ||
                            (this.count != null && otherPair.count != null &&
                                    this.count.equals(otherPair.count))) &&
                            (this.percentage.equals(otherPair.percentage) ||
                                    (this.percentage != null && otherPair.percentage != null &&
                                            this.percentage.equals(otherPair.percentage))));
        }

        return false;
    }

    public Number getCount() {
        return count;
    }

    public void setCount(Number count) {
        this.count = count;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public String toString() {
        return "(" + count + ", " + percentage + ")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.count);
        dest.writeSerializable(this.percentage);
    }

    protected PercentagePair(Parcel in) {
        this.count = (Number) in.readSerializable();
        this.percentage = (BigDecimal) in.readSerializable();
    }

    public static final Parcelable.Creator<PercentagePair> CREATOR = new Parcelable.Creator<PercentagePair>() {
        @Override
        public PercentagePair createFromParcel(Parcel source) {
            return new PercentagePair(source);
        }

        @Override
        public PercentagePair[] newArray(int size) {
            return new PercentagePair[size];
        }
    };
}