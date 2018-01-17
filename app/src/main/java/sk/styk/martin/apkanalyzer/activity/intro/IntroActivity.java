package sk.styk.martin.apkanalyzer.activity.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.business.task.upload.MultipleAppDataUploadService;
import sk.styk.martin.apkanalyzer.util.FirstStartHelper;
import sk.styk.martin.apkanalyzer.util.networking.ConnectivityHelper;

/**
 * @author Martin Styk
 * @version 13.12.2017.
 */
public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage analyzeAppsSlide = new SliderPage();
        analyzeAppsSlide.setTitle(getString(R.string.intro_analyze_apps));
        analyzeAppsSlide.setDescription(getString(R.string.intro_analyze_apps_description));
        analyzeAppsSlide.setBgColor(getResources().getColor(R.color.accentLight));
        analyzeAppsSlide.setImageDrawable(R.drawable.ic_lupa);

        SliderPage permissionsAppsSlide = new SliderPage();
        permissionsAppsSlide.setTitle(getString(R.string.intro_permissions));
        permissionsAppsSlide.setDescription(getString(R.string.intro_permissions_description));
        permissionsAppsSlide.setBgColor(getResources().getColor(R.color.accentLight));
        permissionsAppsSlide.setImageDrawable(R.drawable.ic_permission);

        SliderPage statisticsAppsSlide = new SliderPage();
        statisticsAppsSlide.setTitle(getString(R.string.intro_statistics));
        statisticsAppsSlide.setDescription(getString(R.string.intro_statistics_description));
        statisticsAppsSlide.setBgColor(getResources().getColor(R.color.accentLight));
        statisticsAppsSlide.setImageDrawable(R.drawable.ic_chart);

        SliderPage uploadAppsSlide = new SliderPage();
        uploadAppsSlide.setTitle(getString(R.string.intro_upload));
        uploadAppsSlide.setDescription(getString(R.string.intro_upload_description));
        uploadAppsSlide.setBgColor(getResources().getColor(R.color.accentLight));
        uploadAppsSlide.setImageDrawable(R.drawable.ic_upload);

        addSlide(AppIntroFragment.newInstance(analyzeAppsSlide));
        addSlide(AppIntroFragment.newInstance(permissionsAppsSlide));
        addSlide(AppIntroFragment.newInstance(statisticsAppsSlide));
        addSlide(AllowMetadataUploadIntroSlide.newInstance(uploadAppsSlide));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);

        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        // save user preferences and trigger data upload if possible
        if (currentFragment instanceof AllowMetadataUploadIntroSlide) {
            boolean isUploadAllowed = AllowMetadataUploadIntroSlide.class.cast(currentFragment).isUploadAllowed();
            ConnectivityHelper.setConnectionAllowedByUser(getApplicationContext(), isUploadAllowed);

            if (isUploadAllowed)
                MultipleAppDataUploadService.start(getApplicationContext());
        }

        FirstStartHelper.setFirstStartFinished(getApplicationContext());
        finish();
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}