package sk.styk.martin.apkanalyzer.views

import android.content.Context
import android.text.format.Formatter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_math_statistics_card.view.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.statistics.MathStatistics
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ARROW_ANIMATION_DURATION
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_FLIPPED
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_STANDARD
import sk.styk.martin.apkanalyzer.util.BigDecimalFormatter


class ExpandableMathStatisticsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var type: Type = Type.INTEGRAL

    var title: String = ""
        set(value) {
            field = value
            itemTitle.text = value
        }

    var statistics: MathStatistics? = null
        set(value) {
            field = value
            value?.let {
                type.setStatistics(it, item_arithmetic_mean, item_median, item_min, item_max, item_deviation, item_variance)
            }
        }

    var expandListener: OnClickListener? = null
        set(value) {
            field = value
            titleContainer.setOnClickListener(expandListener)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_math_statistics_card, this, true)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.MathStatisticsCardView, 0, 0)
        title = attributes.getString(R.styleable.MathStatisticsCardView_title) ?: ""
        type = Type.resolve(attributes.getString(R.styleable.MathStatisticsCardView_type))
        attributes.recycle()

        orientation = VERTICAL
    }

    fun setExpanded(isExpanded: Boolean) {
        toggleArrow.animate().apply {
            cancel()
            setDuration(ARROW_ANIMATION_DURATION).rotation(if (isExpanded) ROTATION_FLIPPED else ROTATION_STANDARD)
        }
        expandableContainer.setExpanded(isExpanded, true)
    }

    internal enum class Type {
        INTEGRAL {
            override fun setStatistics(statistics: MathStatistics, mean: NewDetailListItemView, median: NewDetailListItemView, min: NewDetailListItemView, max: NewDetailListItemView, deviation: NewDetailListItemView, variance: NewDetailListItemView) {
                mean.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.arithmeticMean)
                median.valueText = BigDecimalFormatter.getFormat(0, 0).format(statistics.median)
                min.valueText = BigDecimalFormatter.getFormat(0, 0).format(statistics.min)
                max.valueText = BigDecimalFormatter.getFormat(0, 0).format(statistics.max)
                deviation.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.deviation)
                variance.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.variance)
            }
        },
        DECIMAL {
            override fun setStatistics(statistics: MathStatistics, mean: NewDetailListItemView, median: NewDetailListItemView, min: NewDetailListItemView, max: NewDetailListItemView, deviation: NewDetailListItemView, variance: NewDetailListItemView) {
                mean.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.arithmeticMean)
                median.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.median)
                min.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.min)
                max.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.max)
                deviation.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.deviation)
                variance.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.variance)
            }
        },
        SIZE {
            override fun setStatistics(statistics: MathStatistics, mean: NewDetailListItemView, median: NewDetailListItemView, min: NewDetailListItemView, max: NewDetailListItemView, deviation: NewDetailListItemView, variance: NewDetailListItemView) {
                mean.valueText = Formatter.formatShortFileSize(mean.context, statistics.arithmeticMean.toLong())
                median.valueText = Formatter.formatShortFileSize(mean.context, statistics.median.toLong())
                min.valueText = Formatter.formatShortFileSize(mean.context, statistics.min.toLong())
                max.valueText = Formatter.formatShortFileSize(mean.context, statistics.max.toLong())
                deviation.valueText = Formatter.formatShortFileSize(mean.context, statistics.deviation.toLong())
                variance.valueText = Formatter.formatShortFileSize(mean.context, statistics.variance.toLong())
            }
        };

        internal abstract fun setStatistics(statistics: MathStatistics, mean: NewDetailListItemView, median: NewDetailListItemView, min: NewDetailListItemView, max: NewDetailListItemView, deviation: NewDetailListItemView, variance: NewDetailListItemView)

        companion object {
            fun resolve(stringAttribute: String?): Type {
                return when (stringAttribute) {
                    "size" -> SIZE
                    "decimal" -> DECIMAL
                    "integral" -> INTEGRAL
                    else -> INTEGRAL
                }
            }
        }

    }

}
