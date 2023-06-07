package sk.styk.martin.apkanalyzer.views

import android.content.Context
import android.text.format.Formatter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ViewMathStatisticsCardBinding
import sk.styk.martin.apkanalyzer.core.appstatistics.model.MathStatistics
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ARROW_ANIMATION_DURATION
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_FLIPPED
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_STANDARD
import sk.styk.martin.apkanalyzer.core.appstatistics.util.BigDecimalFormatter

class ExpandableMathStatisticsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private val binding = ViewMathStatisticsCardBinding.inflate(LayoutInflater.from(context), this, true)

    private var type: Type = Type.INTEGRAL

    var title: String = ""
        set(value) {
            field = value
            binding.itemTitle.text = value
        }

    var statistics: MathStatistics? = null
        set(value) {
            field = value
            value?.let {
                type.setStatistics(
                    it,
                    binding.itemArithmeticMean,
                    binding.itemMedian,
                    binding.itemMin,
                    binding.itemMax,
                    binding.itemDeviation,
                    binding.itemVariance,
                )
            }
        }

    var expandListener: OnClickListener? = null
        set(value) {
            field = value
            binding.titleContainer.setOnClickListener(expandListener)
        }

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ExpandableMathStatisticsView, 0, 0)
        title = attributes.getString(R.styleable.ExpandableMathStatisticsView_title) ?: ""
        type = Type.resolve(attributes.getString(R.styleable.ExpandableMathStatisticsView_type))
        attributes.recycle()

        orientation = VERTICAL
    }

    fun setExpanded(isExpanded: Boolean) {
        binding.toggleArrow.animate().apply {
            cancel()
            setDuration(ARROW_ANIMATION_DURATION).rotation(if (isExpanded) ROTATION_FLIPPED else ROTATION_STANDARD)
        }
        binding.expandableContainer.setExpanded(isExpanded, true)
    }

    internal enum class Type {
        INTEGRAL {
            override fun setStatistics(
                statistics: MathStatistics,
                mean: NewDetailListItemView,
                median: NewDetailListItemView,
                min: NewDetailListItemView,
                max: NewDetailListItemView,
                deviation: NewDetailListItemView,
                variance: NewDetailListItemView,
            ) {
                mean.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.arithmeticMean)
                median.valueText = BigDecimalFormatter.getFormat(0, 0).format(statistics.median)
                min.valueText = BigDecimalFormatter.getFormat(0, 0).format(statistics.min)
                max.valueText = BigDecimalFormatter.getFormat(0, 0).format(statistics.max)
                deviation.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.deviation)
                variance.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.variance)
            }
        },
        DECIMAL {
            override fun setStatistics(
                statistics: MathStatistics,
                mean: NewDetailListItemView,
                median: NewDetailListItemView,
                min: NewDetailListItemView,
                max: NewDetailListItemView,
                deviation: NewDetailListItemView,
                variance: NewDetailListItemView,
            ) {
                mean.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.arithmeticMean)
                median.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.median)
                min.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.min)
                max.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.max)
                deviation.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.deviation)
                variance.valueText = BigDecimalFormatter.getCommonFormat().format(statistics.variance)
            }
        },
        SIZE {
            override fun setStatistics(
                statistics: MathStatistics,
                mean: NewDetailListItemView,
                median: NewDetailListItemView,
                min: NewDetailListItemView,
                max: NewDetailListItemView,
                deviation: NewDetailListItemView,
                variance: NewDetailListItemView,
            ) {
                mean.valueText = Formatter.formatShortFileSize(mean.context, statistics.arithmeticMean.toLong())
                median.valueText = Formatter.formatShortFileSize(mean.context, statistics.median.toLong())
                min.valueText = Formatter.formatShortFileSize(mean.context, statistics.min.toLong())
                max.valueText = Formatter.formatShortFileSize(mean.context, statistics.max.toLong())
                deviation.valueText = Formatter.formatShortFileSize(mean.context, statistics.deviation.toLong())
                variance.valueText = Formatter.formatShortFileSize(mean.context, statistics.variance.toLong())
            }
        };

        internal abstract fun setStatistics(
            statistics: MathStatistics,
            mean: NewDetailListItemView,
            median: NewDetailListItemView,
            min: NewDetailListItemView,
            max: NewDetailListItemView,
            deviation: NewDetailListItemView,
            variance: NewDetailListItemView,
        )

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
