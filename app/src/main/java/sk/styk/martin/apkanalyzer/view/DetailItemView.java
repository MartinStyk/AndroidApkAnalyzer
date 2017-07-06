package sk.styk.martin.apkanalyzer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.R;

/**
 * Created by Martin Styk on 06.07.2017.
 */
public class DetailItemView extends LinearLayout {

    private TextView title;
    private TextView value;


    public DetailItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DetailItemView, 0, 0);
        String titleText = a.getString(R.styleable.DetailItemView_titleText);
        String valueText = a.getString(R.styleable.DetailItemView_valueText);

        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setClickable(true);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_detail_item, this, true);

        title = (TextView) getChildAt(0);
        title.setText(titleText);

        value = (TextView) getChildAt(1);
        value.setText(valueText);
    }

    public DetailItemView(Context context) {
        this(context, null);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setValue(String value) {
        this.value.setText(value);
    }

}
