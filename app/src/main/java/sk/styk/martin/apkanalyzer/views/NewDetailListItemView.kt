package sk.styk.martin.apkanalyzer.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_detail_list_item.view.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.statistics.PercentagePair
import sk.styk.martin.apkanalyzer.util.TextInfo

class NewDetailListItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs, R.attr.detailListItemViewStyle) {

    var titleText: String = ""
        set(value) {
            field = value
            attribute_name.text = value
        }
    var valueText: String = ""
        set(value) {
            field = value
            attribute_value.text = value.toString()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_detail_list_item, this, true)

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
