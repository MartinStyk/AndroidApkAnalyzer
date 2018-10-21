package sk.styk.martin.apkanalyzer.ui.customview

import android.content.Context
import android.support.v7.widget.CardView
import android.text.format.Formatter
import android.util.AttributeSet
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.view_math_statistics_card.view.*
import sk.styk.martin.apkanalyzer.model.statistics.MathStatistics
import sk.styk.martin.apkanalyzer.util.BigDecimalFormatter

/**
 * Used in local statistics layout
 * Contains name of attribute and available statistics - mean, median, max, min, deviation, variance
 *
 * @author Martin Styk
 * @version 06.07.2017.
 */
class MathStatisticsCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : CardView(context, attrs) {

    private var type: Type = Type.INTEGRAL

    var title: String = ""
        set(value) {
            field = value
            item_title.text = value
        }

    var statistics: MathStatistics? = null
        set(value) {
            field = value
            value?.let {
                type.setStatistics(it, item_arithmetic_mean, item_median, item_min, item_max, item_deviation, item_variance)
            }
        }


    init {
        LayoutInflater.from(context).inflate(R.layout.view_math_statistics_card, this, true)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.MathStatisticsCardView, 0, 0)
        title = attributes.getString(R.styleable.MathStatisticsCardView_title)
        type = Type.valueOf(attributes.getString(R.styleable.MathStatisticsCardView_type)!!.toUpperCase())
        attributes.recycle()

        useCompatPadding = true
    }

    internal enum class Type {
        INTEGRAL {
            override fun setStatistics(statistics: MathStatistics, mean: DetailListItemView, median: DetailListItemView, min: DetailListItemView, max: DetailListItemView, deviation: DetailListItemView, variance: DetailListItemView) {

                mean.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.arithmeticMean)
                median.valueText = BigDecimalFormatter.getFormat(0, 0).format(statistics.median)
                min.valueText = BigDecimalFormatter.getFormat(0, 0).format(statistics.min)
                max.valueText = BigDecimalFormatter.getFormat(0, 0).format(statistics.max)
                deviation.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.deviation)
                variance.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.variance)
            }
        },
        DECIMAL {
            override fun setStatistics(statistics: MathStatistics, mean: DetailListItemView, median: DetailListItemView, min: DetailListItemView, max: DetailListItemView, deviation: DetailListItemView, variance: DetailListItemView) {
                mean.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.arithmeticMean)
                median.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.median)
                min.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.min)
                max.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.max)
                deviation.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.deviation)
                variance.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.variance)
            }
        },
        SIZE {
            override fun setStatistics(statistics: MathStatistics, mean: DetailListItemView, median: DetailListItemView, min: DetailListItemView, max: DetailListItemView, deviation: DetailListItemView, variance: DetailListItemView) {
                mean.valueText = Formatter.formatShortFileSize(mean.context, statistics.arithmeticMean.toLong())
                median.valueText = Formatter.formatShortFileSize(mean.context, statistics.median.toLong())
                min.valueText = Formatter.formatShortFileSize(mean.context, statistics.min.toLong())
                max.valueText = Formatter.formatShortFileSize(mean.context, statistics.max.toLong())
                deviation.valueText = Formatter.formatShortFileSize(mean.context, statistics.deviation.toLong())
                variance.valueText = Formatter.formatShortFileSize(mean.context, statistics.variance.toLong())
            }
        };

        internal abstract fun setStatistics(statistics: MathStatistics, mean: DetailListItemView, median: DetailListItemView, min: DetailListItemView, max: DetailListItemView, deviation: DetailListItemView, variance: DetailListItemView)

    }

}
