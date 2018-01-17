package sk.styk.martin.apkanalyzer.model.statistics;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Math Statistics collected about attributes of Apk file
 * <p>
 * @author Martin Styk
 * @version 28.07.2017.
 */
public class MathStatistics implements Parcelable {

    public static final Parcelable.Creator<MathStatistics> CREATOR = new Parcelable.Creator<MathStatistics>() {
        @Override
        public MathStatistics createFromParcel(Parcel source) {
            return new MathStatistics(source);
        }

        @Override
        public MathStatistics[] newArray(int size) {
            return new MathStatistics[size];
        }
    };
    private BigDecimal arithmeticMean;
    private BigDecimal median;
    private BigDecimal max;
    private BigDecimal min;
    private BigDecimal variance;
    private BigDecimal deviation;


    protected MathStatistics(BigDecimal arithmeticMean,
                           BigDecimal median,
                           BigDecimal max,
                           BigDecimal min,
                           BigDecimal variance,
                           BigDecimal deviation) {
        this.arithmeticMean = arithmeticMean;
        this.median = median;
        this.max = max;
        this.min = min;
        this.variance = variance;
        this.deviation = deviation;
    }

    protected MathStatistics(Parcel in) {
        this.arithmeticMean = (BigDecimal) in.readSerializable();
        this.median = (BigDecimal) in.readValue(Long.class.getClassLoader());
        this.max = (BigDecimal) in.readSerializable();
        this.min = (BigDecimal) in.readSerializable();
        this.variance = (BigDecimal) in.readSerializable();
        this.deviation = (BigDecimal) in.readSerializable();
    }


    public BigDecimal getArithmeticMean() {
        return arithmeticMean;
    }

    public BigDecimal getMedian() {
        return median;
    }

    public BigDecimal getMax() {
        return max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getVariance() {
        return variance;
    }

    public BigDecimal getDeviation() {
        return deviation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MathStatistics that = (MathStatistics) o;

        if (arithmeticMean != null ? !arithmeticMean.equals(that.arithmeticMean) : that.arithmeticMean != null)
            return false;
        if (median != null ? !median.equals(that.median) : that.median != null) return false;
        if (max != null ? !max.equals(that.max) : that.max != null) return false;
        if (min != null ? !min.equals(that.min) : that.min != null) return false;
        if (variance != null ? !variance.equals(that.variance) : that.variance != null)
            return false;
        return deviation != null ? deviation.equals(that.deviation) : that.deviation == null;

    }

    @Override
    public int hashCode() {
        int result = arithmeticMean != null ? arithmeticMean.hashCode() : 0;
        result = 31 * result + (median != null ? median.hashCode() : 0);
        result = 31 * result + (max != null ? max.hashCode() : 0);
        result = 31 * result + (min != null ? min.hashCode() : 0);
        result = 31 * result + (variance != null ? variance.hashCode() : 0);
        result = 31 * result + (deviation != null ? deviation.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MathStatistics{" +
                "arithmeticMean=" + arithmeticMean +
                ", median=" + median +
                ", max=" + max +
                ", min=" + min +
                ", variance=" + variance +
                ", deviation=" + deviation +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.arithmeticMean);
        dest.writeValue(this.median);
        dest.writeSerializable(this.max);
        dest.writeSerializable(this.min);
        dest.writeSerializable(this.variance);
        dest.writeSerializable(this.deviation);
    }
}
