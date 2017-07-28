package sk.styk.martin.apkanalyzer.util;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.math3.stat.StatUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Math Statistics collected about attributes of Apk file
 * <p>
 * Created by Martin Styk on 28.07.2017.
 */
public class MathStatistics implements Parcelable {

    private BigDecimal arithmeticMean;
    private Long median;
    private List<Long> modus;
    private BigDecimal max;
    private BigDecimal min;
    private BigDecimal variance;
    private BigDecimal deviation;

    public MathStatistics(float[] data) {
        computeStatistics(ConversionHelper.toDoubleArray(data));
    }


    private void computeStatistics(double[] array) {

        if (array == null || array.length == 0) {
            return;
        }

        Double mean = StatUtils.mean(array);
        Double median = StatUtils.percentile(array, 50);
        double[] modus = StatUtils.mode(array);
        Double minimum = StatUtils.min(array);
        Double maximum = StatUtils.max(array);
        Double variance = StatUtils.variance(array);
        Double deviation = Math.sqrt(variance);

        this.arithmeticMean = new BigDecimal(mean);
        this.median = median.longValue();
        this.modus = ConversionHelper.toLongList(modus);
        this.min = new BigDecimal(minimum);
        this.max = new BigDecimal(maximum);
        this.variance = new BigDecimal(variance);
        this.deviation = new BigDecimal(deviation);

    }

    public BigDecimal getArithmeticMean() {
        return arithmeticMean;
    }

    public void setArithmeticMean(BigDecimal arithmeticMean) {
        this.arithmeticMean = arithmeticMean;
    }

    public Long getMedian() {
        return median;
    }

    public void setMedian(Long median) {
        this.median = median;
    }

    public List<Long> getModus() {
        return modus;
    }

    public void setModus(List<Long> modus) {
        this.modus = modus;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getVariance() {
        return variance;
    }

    public void setVariance(BigDecimal variance) {
        this.variance = variance;
    }

    public BigDecimal getDeviation() {
        return deviation;
    }

    public void setDeviation(BigDecimal deviation) {
        this.deviation = deviation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MathStatistics that = (MathStatistics) o;

        if (arithmeticMean != null ? !arithmeticMean.equals(that.arithmeticMean) : that.arithmeticMean != null)
            return false;
        if (median != null ? !median.equals(that.median) : that.median != null) return false;
        if (modus != null ? !modus.equals(that.modus) : that.modus != null) return false;
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
        result = 31 * result + (modus != null ? modus.hashCode() : 0);
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
                ", modus=" + modus +
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
        dest.writeList(this.modus);
        dest.writeSerializable(this.max);
        dest.writeSerializable(this.min);
        dest.writeSerializable(this.variance);
        dest.writeSerializable(this.deviation);
    }

    protected MathStatistics(Parcel in) {
        this.arithmeticMean = (BigDecimal) in.readSerializable();
        this.median = (Long) in.readValue(Long.class.getClassLoader());
        this.modus = new ArrayList<Long>();
        in.readList(this.modus, Long.class.getClassLoader());
        this.max = (BigDecimal) in.readSerializable();
        this.min = (BigDecimal) in.readSerializable();
        this.variance = (BigDecimal) in.readSerializable();
        this.deviation = (BigDecimal) in.readSerializable();
    }

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
}
