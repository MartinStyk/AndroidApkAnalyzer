package sk.styk.martin.apkanalyzer.ui.activity.intro

import android.os.Bundle
import android.support.design.widget.Snackbar
import com.github.paolorotolo.appintro.AppIntroBaseFragment
import com.github.paolorotolo.appintro.ISlidePolicy
import com.github.paolorotolo.appintro.model.SliderPage
import kotlinx.android.synthetic.main.fragment_allow_metadata_upload_intro_slide.*
import sk.styk.martin.apkanalyzer.R

/**
 * @author Martin Styk
 * @version 13.12.2017
 */
class AllowMetadataUploadIntroSlide : AppIntroBaseFragment(), ISlidePolicy {

    val isUploadAllowed: Boolean
        get() = allow_upload.isChecked

    override fun getLayoutId(): Int {
        return R.layout.fragment_allow_metadata_upload_intro_slide
    }

    override fun isPolicyRespected(): Boolean {
        return allow_upload.isChecked || not_allow_upload.isChecked
    }

    override fun onUserIllegallyRequestedNextPage() {
        Snackbar.make(activity.findViewById(android.R.id.content), R.string.intro_upload_require_input, Snackbar.LENGTH_SHORT).show()
    }

    companion object {

        fun newInstance(sliderPage: SliderPage): AllowMetadataUploadIntroSlide {
            val slide = AllowMetadataUploadIntroSlide()
            val args = Bundle()
            args.putString(AppIntroBaseFragment.ARG_TITLE, sliderPage.titleString)
            args.putString(AppIntroBaseFragment.ARG_TITLE_TYPEFACE, sliderPage.titleTypeface)
            args.putString(AppIntroBaseFragment.ARG_DESC, sliderPage.descriptionString)
            args.putString(AppIntroBaseFragment.ARG_DESC_TYPEFACE, sliderPage.descTypeface)
            args.putInt(AppIntroBaseFragment.ARG_DRAWABLE, sliderPage.imageDrawable)
            args.putInt(AppIntroBaseFragment.ARG_BG_COLOR, sliderPage.bgColor)
            args.putInt(AppIntroBaseFragment.ARG_TITLE_COLOR, sliderPage.titleColor)
            args.putInt(AppIntroBaseFragment.ARG_DESC_COLOR, sliderPage.descColor)
            slide.arguments = args

            return slide
        }
    }
}