package sk.styk.martin.apkanalyzer.activity.intro

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup

import com.github.paolorotolo.appintro.AppIntroBaseFragment
import com.github.paolorotolo.appintro.ISlidePolicy
import com.github.paolorotolo.appintro.model.SliderPage

import sk.styk.martin.apkanalyzer.R

/**
 * @author Martin Styk
 * @version 13.12.2017
 */
class AllowMetadataUploadIntroSlide : AppIntroBaseFragment(), ISlidePolicy {
    private var groupAllowUpload: RadioGroup? = null
    private var buttonAllowUpload: RadioButton? = null
    private var buttonNotAllowUpload: RadioButton? = null

    private val isSelected: Boolean = false

    val isUploadAllowed: Boolean
        get() = buttonAllowUpload!!.isChecked

    override fun getLayoutId(): Int {
        return R.layout.fragment_allow_metadata_upload_intro_slide
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        groupAllowUpload = root!!.findViewById(R.id.allow_upload_group)
        buttonAllowUpload = root.findViewById(R.id.allow_upload)
        buttonNotAllowUpload = root.findViewById(R.id.not_allow_upload)

        return root
    }

    override fun isPolicyRespected(): Boolean {
        return buttonAllowUpload!!.isChecked || buttonNotAllowUpload!!.isChecked
    }

    override fun onUserIllegallyRequestedNextPage() {
        // User illegally requested next slide
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