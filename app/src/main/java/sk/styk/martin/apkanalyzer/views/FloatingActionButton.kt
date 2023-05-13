package sk.styk.martin.apkanalyzer.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ListItemFabMenuBinding
import sk.styk.martin.apkanalyzer.databinding.ViewFloatingActionButtonBinding
import kotlin.math.min

class FloatingActionButton : RelativeLayout {

    private val SPEED_DIAL_ANIMATION_DURATION = 300L
    private val HIDE_SHOW_ANIMATION_DURATION = 300L

    private val layoutInflater by lazy { LayoutInflater.from(context) }

    private var isShowing: Boolean = true
    override fun isShown() = isShowing
    private var buttonPosition = POSITION_BOTTOM.or(POSITION_END)
    private var buttonBackgroundColour = 0xff0099ff.toInt()
    private var buttonIconResource = 0
    private var contentCoverColour = 0xccffffff.toInt()
    var contentCoverEnabled = true

    private var onClickListener: OnClickListener? = null
    private var speedDialMenuOpenListener: SpeedDialMenuOpenListener? = null
    private var speedDialMenuCloseListener: SpeedDialMenuCloseListener? = null

    var isSpeedDialMenuOpen = false
        private set
    private val speedDialMenuViews = mutableListOf<ListItemFabMenuBinding>()
    var speedDialMenuAdapter: SpeedDialMenuAdapter? = null
        set(value) {
            field = value
            rebuildSpeedDialMenu()
        }

    private var busyAnimatingFabIconRotation = false
    private var busyAnimatingContentCover = false
    private var busyAnimatingSpeedDialMenuItems = false
    private val isBusyAnimating
        get() = busyAnimatingFabIconRotation || busyAnimatingContentCover || busyAnimatingSpeedDialMenuItems

    private lateinit var binding: ViewFloatingActionButtonBinding

    companion object {
        const val POSITION_TOP = 1
        const val POSITION_BOTTOM = 2
        const val POSITION_START = 4
        const val POSITION_END = 8
        const val POSITION_LEFT = 16
        const val POSITION_RIGHT = 32
    }

    constructor(context: Context) :
        super(context) {
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet) :
        super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    override fun onSaveInstanceState(): Parcelable {
        val state = Bundle()
        state.putParcelable("_super", super.onSaveInstanceState())

        state.putBoolean("isShowing", isShowing)
        state.putInt("buttonPosition", buttonPosition)
        state.putInt("buttonBackgroundColour", buttonBackgroundColour)
        state.putInt("buttonIconResource", buttonIconResource)
        state.putInt("contentCoverColour", contentCoverColour)
        state.putBoolean("contentCoverEnabled", contentCoverEnabled)

        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            isShowing = state.getBoolean("isShowing", isShowing)
            if (isShowing) {
                show()
            } else {
                hide(true)
            }

            buttonPosition = state.getInt("buttonPosition", buttonPosition)
            setButtonPosition(buttonPosition)

            buttonBackgroundColour = state.getInt("buttonBackgroundColour", buttonBackgroundColour)
            setButtonBackgroundColour(buttonBackgroundColour)

            buttonIconResource = state.getInt("buttonIconResource", buttonIconResource)
            setButtonIconResource(buttonIconResource)

            contentCoverColour = state.getInt("contentCoverColour", contentCoverColour)
            setContentCoverColour(contentCoverColour)

            contentCoverEnabled = state.getBoolean("contentCoverEnabled", contentCoverEnabled)

            super.onRestoreInstanceState(state.getParcelable("_super"))
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private fun initView(attrs: AttributeSet?) {
        binding = ViewFloatingActionButtonBinding.inflate(LayoutInflater.from(context), this, true)
        applyAttributes(attrs)
        applyListeners()
        rebuildSpeedDialMenu()

        binding.contentCover.alpha = 0f

        addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            if (layoutParams is CoordinatorLayout.LayoutParams) {
                val lp = (layoutParams as CoordinatorLayout.LayoutParams)
                lp.behavior = MoveUpwardBehavior()
                layoutParams = lp
            }
        }
    }

    private fun applyAttributes(rawAttrs: AttributeSet?) {
        val attrs = context.theme.obtainStyledAttributes(rawAttrs, R.styleable.FloatingActionButton, 0, 0)
        try {
            setButtonPosition(attrs.getInteger(R.styleable.FloatingActionButton_buttonPosition, buttonPosition))
            setButtonBackgroundColour(attrs.getColor(R.styleable.FloatingActionButton_buttonBackgroundColour, buttonBackgroundColour))
            setButtonIconResource(attrs.getResourceId(R.styleable.FloatingActionButton_buttonIcon, 0))
            setContentCoverColour(attrs.getColor(R.styleable.FloatingActionButton_buttonCoverColour, contentCoverColour))
        } finally {
            attrs.recycle()
        }
    }

    private fun applyListeners() {
        binding.fabCard.setOnClickListener {
            if (speedDialMenuAdapter?.isEnabled() == true && (speedDialMenuAdapter?.getCount() ?: 0) > 0) {
                toggleSpeedDialMenu()
            } else {
                onClickListener?.onClick(this)
            }
        }
    }

    private fun setViewLayoutParams(view: View) {
        val layoutParams = view.layoutParams as LayoutParams
        clearParentAlignmentRules(layoutParams)

        if (buttonPosition.and(POSITION_TOP) > 0) {
            layoutParams.addRule(ALIGN_PARENT_TOP)
        }
        if (buttonPosition.and(POSITION_BOTTOM) > 0) {
            layoutParams.addRule(ALIGN_PARENT_BOTTOM)
        }
        if (buttonPosition.and(POSITION_START) > 0) {
            layoutParams.addRule(ALIGN_PARENT_START)
        }
        if (buttonPosition.and(POSITION_END) > 0) {
            layoutParams.addRule(ALIGN_PARENT_END)
        }
        if (buttonPosition.and(POSITION_LEFT) > 0) {
            layoutParams.addRule(ALIGN_PARENT_LEFT)
        }
        if (buttonPosition.and(POSITION_RIGHT) > 0) {
            layoutParams.addRule(ALIGN_PARENT_RIGHT)
        }

        view.layoutParams = layoutParams
    }

    private fun clearParentAlignmentRules(params: LayoutParams) {
        params.apply {
            removeRule(ALIGN_PARENT_TOP)
            removeRule(ALIGN_PARENT_BOTTOM)
            removeRule(ALIGN_PARENT_LEFT)
            removeRule(ALIGN_PARENT_RIGHT)
            removeRule(ALIGN_PARENT_START)
            removeRule(ALIGN_PARENT_END)
        }
    }

    private fun setSpeedDialMenuItemViewOrder(itemBinding: ListItemFabMenuBinding) {
        var labelFirst = true
        val isRightToLeft = false
        if (buttonPosition.and(POSITION_LEFT) > 0) {
            labelFirst = false
        }
        if (buttonPosition.and(POSITION_RIGHT) > 0) {
            labelFirst = true
        }
        if (buttonPosition.and(POSITION_START) > 0) {
            labelFirst = isRightToLeft
        }
        if (buttonPosition.and(POSITION_END) > 0) {
            labelFirst = !isRightToLeft
        }

        val label = itemBinding.menuItemLabel
        val icon = itemBinding.menuItemCard
        itemBinding.itemContainer.removeView(label)
        itemBinding.itemContainer.removeView(icon)

        if (labelFirst) {
            itemBinding.itemContainer.addView(label)
            itemBinding.itemContainer.addView(icon)
        } else {
            itemBinding.itemContainer.addView(icon)
            itemBinding.itemContainer.addView(label)
        }
    }

    fun setButtonPosition(position: Int) {
        this.buttonPosition = position

        setViewLayoutParams(binding.fabCard)
        setViewLayoutParams(binding.contentCover)
        speedDialMenuViews.forEach { setViewLayoutParams(it.itemContainer) }
        speedDialMenuViews.forEach { setSpeedDialMenuItemViewOrder(it) }
    }

    fun setButtonBackgroundColour(@ColorInt colour: Int) {
        this.buttonBackgroundColour = colour
        binding.fabCard.setCardBackgroundColor(colour)
        rebuildSpeedDialMenu()
    }

    fun setButtonIconResource(@DrawableRes icon: Int) {
        this.buttonIconResource = icon
        binding.fabIconWrapper.setBackgroundResource(icon)
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        onClickListener = listener
    }

    fun openSpeedDialMenu() {
        if (!isSpeedDialMenuOpen) {
            toggleSpeedDialMenu()
        }
    }

    fun closeSpeedDialMenu() {
        if (isSpeedDialMenuOpen) {
            toggleSpeedDialMenu()
        }
    }

    @Deprecated(
        "This method name is incorrect and is kept only for backwards compatibility",
        ReplaceWith("setOnSpeedDialMenuOpenListener"),
        DeprecationLevel.WARNING,
    )
    fun setOnSpeedMenuDialOpenListener(listener: SpeedDialMenuOpenListener?) {
        setOnSpeedDialMenuOpenListener(listener)
    }

    fun setOnSpeedDialMenuOpenListener(listener: SpeedDialMenuOpenListener?) {
        speedDialMenuOpenListener = listener
    }

    fun setOnSpeedDialMenuCloseListener(listener: SpeedDialMenuCloseListener?) {
        speedDialMenuCloseListener = listener
    }

    fun setContentCoverColour(@ColorInt colour: Int) {
        contentCoverColour = colour
        when (val background = binding.contentCover.background) {
            is GradientDrawable -> background.setColor(colour)
            else -> background.setTint(colour)
        }
    }

    fun show() {
        if (isShowing) {
            return
        }

        closeSpeedDialMenu()
        binding.fabCard.visibility = View.VISIBLE
        binding.fabCard.run {
            clearAnimation()
            animate()
                .translationY(0.toFloat())
                .setInterpolator(AccelerateInterpolator(2f))
                .setDuration(HIDE_SHOW_ANIMATION_DURATION)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        isShowing = true
                    }
                })
        }
    }

    fun hide(immediate: Boolean = false) {
        if (!isShowing && !immediate) {
            return
        }

        isShowing = false

        binding.fabCard.clearAnimation()
        binding.fabCard.animate()
            .translationY(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70f, context.resources.displayMetrics))
            .setInterpolator(AccelerateInterpolator(2f))
            .setDuration(if (immediate) 0L else HIDE_SHOW_ANIMATION_DURATION)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.fabCard.visibility = View.GONE
                    isShowing = false
                }
            })
    }

    @SuppressLint("InflateParams")
    fun rebuildSpeedDialMenu() {
        speedDialMenuViews.forEach { (it.itemContainer.parent as ViewGroup).removeView(it.itemContainer) }
        speedDialMenuViews.clear()

        if (speedDialMenuAdapter == null || speedDialMenuAdapter?.getCount() == 0) {
            return
        }

        val adapter = speedDialMenuAdapter!!

        for (i in (0 until adapter.getCount())) {
            val menuItem = adapter.getMenuItem(i)

            val itemBinding = ListItemFabMenuBinding.inflate(layoutInflater, null, true)
            binding.container.addView(itemBinding.itemContainer)
            speedDialMenuViews.add(itemBinding)

            setViewLayoutParams(itemBinding.itemContainer)
            setSpeedDialMenuItemViewOrder(itemBinding)

            itemBinding.menuItemLabel.text = context.getText(menuItem.label)
            speedDialMenuAdapter?.onPrepareItemLabel(context, i, itemBinding.menuItemLabel)

            itemBinding.menuItemCard.setCardBackgroundColor(adapter.getBackgroundColour(i, context) ?: buttonBackgroundColour)

            speedDialMenuAdapter?.onPrepareItemCard(context, i, itemBinding.menuItemCard)

            itemBinding.menuItemIconWrapper.apply {
                background = ResourcesCompat.getDrawable(context.resources, menuItem.icon, context.theme)
                backgroundTintList = ContextCompat.getColorStateList(context, menuItem.iconColor)
            }
            speedDialMenuAdapter?.onPrepareItemIconWrapper(context, i, itemBinding.menuItemIconWrapper)

            itemBinding.itemContainer.alpha = 0F
            itemBinding.itemContainer.visibility = GONE

            itemBinding.itemContainer.tag = i
            itemBinding.itemContainer.setOnClickListener { v ->
                val closeMenuAfterAction = adapter.onMenuItemClick(v.tag as Int)
                if (closeMenuAfterAction) {
                    toggleSpeedDialMenu()
                }
            }
        }

        if (isSpeedDialMenuOpen) {
            animateSpeedDialMenuItems(true)
        }
    }

    private fun toggleSpeedDialMenu() {
        if (isBusyAnimating) {
            return
        }

        isSpeedDialMenuOpen = !isSpeedDialMenuOpen

        if (isSpeedDialMenuOpen) {
            speedDialMenuOpenListener?.onOpen(this)
        } else {
            speedDialMenuCloseListener?.onClose(this)
        }

        animateFabIconRotation()
        animateContentCover()
        animateSpeedDialMenuItems()

        binding.contentCover.isClickable = isSpeedDialMenuOpen
        binding.contentCover.isFocusable = isSpeedDialMenuOpen
        if (isSpeedDialMenuOpen) {
            binding.contentCover.setOnClickListener { toggleSpeedDialMenu() }
        } else {
            binding.contentCover.setOnClickListener(null)
        }
    }

    private fun animateFabIconRotation() {
        if (busyAnimatingFabIconRotation) {
            return
        }
        busyAnimatingFabIconRotation = true

        binding.fabIconWrapper.animate()
            .rotation(
                if (isSpeedDialMenuOpen) {
                    speedDialMenuAdapter?.fabRotationDegrees()
                        ?: 0F
                } else {
                    0F
                },
            )
            .setInterpolator(OvershootInterpolator(2f))
            .setDuration(SPEED_DIAL_ANIMATION_DURATION)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    busyAnimatingFabIconRotation = false
                }
            })
    }

    private fun animateContentCover() {
        if (isSpeedDialMenuOpen && !contentCoverEnabled) {
            // isSpeedDialMenuOpen is checked to make sure the cover is closed if it is disabled whilst open
            return
        }

        if (busyAnimatingContentCover) {
            return
        }
        busyAnimatingContentCover = true

        binding.contentCover.visibility = View.VISIBLE
        binding.contentCover.animate()
            .scaleX(if (isSpeedDialMenuOpen) 50f else 0f)
            .scaleY(if (isSpeedDialMenuOpen) 50f else 0f)
            .alpha(if (isSpeedDialMenuOpen) 1f else 0f)
            .setDuration(SPEED_DIAL_ANIMATION_DURATION)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    busyAnimatingContentCover = false
                    if (!isSpeedDialMenuOpen) {
                        binding.contentCover.visibility = View.GONE
                    }
                }
            })
    }

    private fun animateSpeedDialMenuItems(immediate: Boolean = false) {
        if (busyAnimatingSpeedDialMenuItems) {
            return
        }
        busyAnimatingSpeedDialMenuItems = true

        val duration = if (immediate) {
            0L
        } else {
            SPEED_DIAL_ANIMATION_DURATION
        }

        val distance = binding.fabCard.height.toFloat()
        speedDialMenuViews.forEachIndexed { i, menuView ->
            if (isSpeedDialMenuOpen) {
                menuView.itemContainer.visibility = View.VISIBLE
            }
            val translation = if (isSpeedDialMenuOpen) {
                val direction = if (buttonPosition.and(POSITION_TOP) > 0) 1 else -1
                (i + 1.125F) * distance * direction
            } else {
                0f
            }
            menuView.itemContainer.animate()
                .translationY(translation)
                .alpha(if (isSpeedDialMenuOpen) 1f else 0f)
                .setDuration(duration)
                .setInterpolator(OvershootInterpolator(2f))
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        busyAnimatingSpeedDialMenuItems = false
                        if (!isSpeedDialMenuOpen) {
                            menuView.itemContainer.visibility = View.GONE
                        }
                    }
                })
        }
    }

    fun setShow(show: Boolean?) {
        if (show == true && !isShown) {
            show()
        } else if (show == false && isShown) {
            hide()
        }
    }

    inner class MoveUpwardBehavior : CoordinatorLayout.Behavior<View>() {

        override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
            return buttonPosition.and(POSITION_BOTTOM) > 0 && dependency is Snackbar.SnackbarLayout
        }

        override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
            child.translationY = min(0f, dependency.translationY - dependency.height)
            return true
        }

        override fun onDependentViewRemoved(parent: CoordinatorLayout, child: View, dependency: View) {
            ViewCompat.animate(child).translationY(0f).start()
        }
    }
}
