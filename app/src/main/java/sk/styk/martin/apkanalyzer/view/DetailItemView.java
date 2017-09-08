package sk.styk.martin.apkanalyzer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.dialog.InfoDialog;

/**
 * Key value pair view
 *
 * Created by Martin Styk on 06.07.2017.
 */
public class DetailItemView extends LinearLayout implements View.OnClickListener {

    private TextView title;
    private TextView value;

    private String titleText;
    private String valueText;
    private String descriptionText;

    public DetailItemView(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.detailItemViewStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DetailItemView, 0, 0);
        titleText = a.getString(R.styleable.DetailItemView_titleText);
        valueText = a.getString(R.styleable.DetailItemView_valueText);
        descriptionText = a.getString(R.styleable.DetailItemView_descriptionText);

        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setOnClickListener(this);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_detail_item, this, true);

        title = (TextView) getChildAt(0);
        title.setText(titleText);

        value = (TextView) getChildAt(1);
        setValue(valueText);
    }

    public DetailItemView(Context context) {
        this(context, null);
    }

    public void setTitle(String title) {
        this.titleText = title;
        this.title.setText(title);
    }

    public void setValue(String value) {
        this.valueText = value;
        if (value == null) {
            setVisibility(GONE);
        } else {
            this.value.setText(value);
            setVisibility(VISIBLE);
        }
    }

    public void setDescription(String description) {
        this.descriptionText = description;
    }

    @Override
    public void onClick(View v) {
        InfoDialog.newInstance(titleText, valueText, descriptionText)
                .show(((AppCompatActivity) getContext()).getSupportFragmentManager(), InfoDialog.class.getSimpleName());
    }

}
