package sk.styk.martin.apkanalyzer.activity.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.paolorotolo.appintro.AppIntroBaseFragment;
import com.github.paolorotolo.appintro.ISlidePolicy;
import com.github.paolorotolo.appintro.model.SliderPage;

import sk.styk.martin.apkanalyzer.R;

public class AllowMetadataUploadIntroSlide extends AppIntroBaseFragment implements ISlidePolicy {
    private RadioGroup groupAllowUpload;
    private RadioButton buttonAllowUpload;
    private RadioButton buttonNotAllowUpload;

    private boolean isSelected;

    public static AllowMetadataUploadIntroSlide newInstance(SliderPage sliderPage) {
        AllowMetadataUploadIntroSlide slide = new AllowMetadataUploadIntroSlide();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, sliderPage.getTitleString());
        args.putString(ARG_TITLE_TYPEFACE, sliderPage.getTitleTypeface());
        args.putString(ARG_DESC, sliderPage.getDescriptionString());
        args.putString(ARG_DESC_TYPEFACE, sliderPage.getDescTypeface());
        args.putInt(ARG_DRAWABLE, sliderPage.getImageDrawable());
        args.putInt(ARG_BG_COLOR, sliderPage.getBgColor());
        args.putInt(ARG_TITLE_COLOR, sliderPage.getTitleColor());
        args.putInt(ARG_DESC_COLOR, sliderPage.getDescColor());
        slide.setArguments(args);

        return slide;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_intro_1;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        groupAllowUpload = root.findViewById(R.id.allow_upload_group);
        buttonAllowUpload = root.findViewById(R.id.allow_upload);
        buttonNotAllowUpload = root.findViewById(R.id.not_allow_upload);

        return root;
    }

    @Override
    public boolean isPolicyRespected() {
        return buttonAllowUpload.isChecked() || buttonNotAllowUpload.isChecked();
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        // User illegally requested next slide
        Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.intro_upload_require_input, Snackbar.LENGTH_LONG).show();
    }

    public boolean isUploadAllowed() {
        return buttonAllowUpload.isChecked();
    }
}