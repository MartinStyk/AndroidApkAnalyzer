package sk.styk.martin.apkanalyzer.view

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.dialog.InfoDialog

/**
 * Key value pair view
 *
 * @author Martin Styk
 * @version 06.07.2017.
 */
class DetailItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : LinearLayout(context, attrs, R.attr.detailItemViewStyle), View.OnClickListener {

    private val title: TextView
    private val value: TextView

    var titleText: String = ""
        set(value) {
            field = value
            title.text = value
        }

    var descriptionText: String
    var valueText: String? = null
        set(value) {
            field = value
            if (value == null || value == notShownExpression) {
                visibility = View.GONE
            } else {
                this.value.text = value
                visibility = View.VISIBLE
            }
        }

    private val notShownExpression: String?

    init {
        LayoutInflater.from(context).inflate(R.layout.view_detail_item, this, true)

        title = getChildAt(0) as TextView
        value = getChildAt(1) as android.widget.TextView

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.DetailItemView, 0, 0)
        notShownExpression = attributes.getString(R.styleable.DetailItemView_notShownExpression)
        titleText = attributes.getString(R.styleable.DetailItemView_titleText)
        valueText = attributes.getString(R.styleable.DetailItemView_valueText)
        descriptionText = attributes.getString(R.styleable.DetailItemView_descriptionText)
        attributes.recycle()

        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setOnClickListener(this)

    }

    override fun onClick(v: View) {
        InfoDialog.newInstance(titleText, valueText, descriptionText)
                .show((context as AppCompatActivity).supportFragmentManager, InfoDialog::class.java.simpleName)
    }

}
