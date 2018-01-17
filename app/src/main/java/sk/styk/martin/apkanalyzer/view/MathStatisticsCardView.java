package sk.styk.martin.apkanalyzer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.model.statistics.MathStatistics;
import sk.styk.martin.apkanalyzer.util.BigDecimalFormatter;

/**
 * Used in local statistics layout
 * Contains name of attribute and available statistics - mean, median, max, min, deviation, variance
 *
 * @author Martin Styk
 * @version 06.07.2017.
 */
public class MathStatisticsCardView extends CardView {

    private final Type type;

    public MathStatisticsCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MathStatisticsCardView, 0, 0);
        String titleText = a.getString(R.styleable.MathStatisticsCardView_title);
        type = Type.valueOf(a.getString(R.styleable.MathStatisticsCardView_type).toUpperCase());

        a.recycle();

        setUseCompatPadding(true);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_math_statistics_card, this, true);

        ((TextView) findViewById(R.id.item_title)).setText(titleText);
    }

    public MathStatisticsCardView(Context context) {
        this(context, null);
    }

    public void setTitle(String title) {
        ((TextView) findViewById(R.id.item_title)).setText(title);
    }

    public void setStatistics(MathStatistics statistics) {
        type.setStatistics(statistics,
                (DetailListItemView) findViewById(R.id.item_arithmetic_mean),
                (DetailListItemView) findViewById(R.id.item_median),
                (DetailListItemView) findViewById(R.id.item_min),
                (DetailListItemView) findViewById(R.id.item_max),
                (DetailListItemView) findViewById(R.id.item_deviation),
                (DetailListItemView) findViewById(R.id.item_variance));
    }

    enum Type {
        INTEGRAL {
            @Override
            void setStatistics(MathStatistics statistics, DetailListItemView mean, DetailListItemView median, DetailListItemView min, DetailListItemView max, DetailListItemView deviation, DetailListItemView variance) {
                mean.setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getArithmeticMean()));
                median.setValue(BigDecimalFormatter.getFormat(0, 0).format(statistics.getMedian()));
                min.setValue(BigDecimalFormatter.getFormat(0, 0).format(statistics.getMin()));
                max.setValue(BigDecimalFormatter.getFormat(0, 0).format(statistics.getMax()));
                deviation.setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getDeviation()));
                variance.setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getVariance()));
            }
        },
        DECIMAL {
            @Override
            void setStatistics(MathStatistics statistics, DetailListItemView mean, DetailListItemView median, DetailListItemView min, DetailListItemView max, DetailListItemView deviation, DetailListItemView variance) {
                mean.setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getArithmeticMean()));
                median.setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getMedian()));
                min.setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getMin()));
                max.setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getMax()));
                deviation.setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getDeviation()));
                variance.setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getVariance()));
            }
        },
        SIZE {
            @Override
            void setStatistics(MathStatistics statistics, DetailListItemView mean, DetailListItemView median, DetailListItemView min, DetailListItemView max, DetailListItemView deviation, DetailListItemView variance) {
                mean.setValue(Formatter.formatShortFileSize(mean.getContext(), statistics.getArithmeticMean().longValue()));
                median.setValue(Formatter.formatShortFileSize(mean.getContext(), statistics.getMedian().longValue()));
                min.setValue(Formatter.formatShortFileSize(mean.getContext(), statistics.getMin().longValue()));
                max.setValue(Formatter.formatShortFileSize(mean.getContext(), statistics.getMax().longValue()));
                deviation.setValue(Formatter.formatShortFileSize(mean.getContext(), statistics.getDeviation().longValue()));
                variance.setValue(Formatter.formatShortFileSize(mean.getContext(), statistics.getVariance().longValue()));
            }
        };

        abstract void setStatistics(MathStatistics statistics, DetailListItemView mean, DetailListItemView median, DetailListItemView min, DetailListItemView max, DetailListItemView deviation, DetailListItemView variance);

    }

}
