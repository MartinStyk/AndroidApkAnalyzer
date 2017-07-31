package sk.styk.martin.apkanalyzer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.util.BigDecimalFormatter;
import sk.styk.martin.apkanalyzer.util.MathStatistics;

/**
 * Created by Martin Styk on 06.07.2017.
 */
public class MathStatisticsCardView extends CardView {

    public MathStatisticsCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MathStatisticsCardView, 0, 0);
        String titleText = a.getString(R.styleable.MathStatisticsCardView_title);

        a.recycle();

        setUseCompatPadding(true);
        setPadding(4, 4, 4, 4);

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
        ((DetailItemView) findViewById(R.id.item_arithmetic_mean)).setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getArithmeticMean()));
        ((DetailItemView) findViewById(R.id.item_median)).setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getMedian()));
        ((DetailItemView) findViewById(R.id.item_min)).setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getMin()));
        ((DetailItemView) findViewById(R.id.item_max)).setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getMax()));
        ((DetailItemView) findViewById(R.id.item_deviation)).setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getDeviation()));
        ((DetailItemView) findViewById(R.id.item_variance)).setValue(BigDecimalFormatter.getCommonFormat().format(statistics.getVariance()));
    }

}
