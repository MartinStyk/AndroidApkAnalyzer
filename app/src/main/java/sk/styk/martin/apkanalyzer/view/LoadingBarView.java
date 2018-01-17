package sk.styk.martin.apkanalyzer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sk.styk.martin.apkanalyzer.R;

/**
 * Key value pair view
 * <p>
 * Created by Martin Styk on 06.07.2017.
 */
public class LoadingBarView extends RelativeLayout {

    private ProgressBar progressBar;

    public LoadingBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_loading_bar, this, true);

        progressBar = (ProgressBar) getChildAt(1);
        progressBar.setProgress(0);
        progressBar.setMax(10);
    }

    public LoadingBarView(Context context) {
        this(context, null);
    }

    public void setProgress(int currentProgress, int maxProgress) {
        if (progressBar.getMax() != maxProgress)
            progressBar.setMax(maxProgress);

        progressBar.setProgress(currentProgress);
    }

}
