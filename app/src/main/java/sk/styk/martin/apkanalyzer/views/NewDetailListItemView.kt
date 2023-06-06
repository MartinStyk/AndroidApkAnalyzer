package sk.styk.martin.apkanalyzer.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ViewDetailListItemBinding
import sk.styk.martin.apkanalyzer.core.appstatistics.model.PercentagePair
import sk.styk.martin.apkanalyzer.util.TextInfo

class NewDetailListItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(
    context,
    attrs,
    R.attr.detailListItemViewStyle,
) {

    private val binding = ViewDetailListItemBinding.inflate(LayoutInflater.from(context), this, true)

    var titleText: String = ""
        set(value) {
            field = value
            binding.attributeName.text = value
        }
    var valueText: String = ""
        set(value) {
            field = value
            binding.attributeValue.text = value
        }

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.NewDetailListItemView, 0, 0)
        titleText = attributes.getString(R.styleable.NewDetailListItemView_titleText) ?: ""
        valueText = attributes.getString(R.styleable.NewDetailListItemView_valueText) ?: ""
        attributes.recycle()

        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }

    fun setValueText(textInfo: TextInfo?) {
        valueText = textInfo?.getText(context)?.toString() ?: ""
    }

    fun setValueText(percentagePair: PercentagePair?) {
        valueText = percentagePair?.toString() ?: ""
    }

    fun setTitleText(textInfo: TextInfo) {
        titleText = textInfo.getText(context).toString()
    }
}
