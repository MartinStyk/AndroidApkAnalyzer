package sk.styk.martin.apkanalyzer.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fab_container.view.*
import kotlinx.android.synthetic.main.fab_menu_item_icon.view.*
import kotlinx.android.synthetic.main.floating_action_button.view.*
import kotlinx.android.synthetic.main.list_item_fab_menu.view.*
import sk.styk.martin.apkanalyzer.R

//import sk.styk.martin.apkanalyzer.util.DP

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
    private val speedDialMenuViews = ArrayList<ViewGroup>()
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
        inflate(context, R.layout.fab_container, this)
        applyAttributes(attrs)
        applyListeners()
        rebuildSpeedDialMenu()

        content_cover.alpha = 0f

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
        fab_card.setOnClickListener {
            if (speedDialMenuAdapter?.isEnabled() == true && speedDialMenuAdapter?.getCount() ?: 0 > 0) {
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

    private fun setSpeedDialMenuItemViewOrder(view: ViewGroup) {
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

        val label = view.menu_item_label
        val icon = view.menu_item_card
        view.removeView(label)
        view.removeView(icon)

        if (labelFirst) {
            view.addView(label)
            view.addView(icon)
        } else {
            view.addView(icon)
            view.addView(label)
        }
    }

    fun setButtonPosition(position: Int) {
        this.buttonPosition = position

        setViewLayoutParams(fab_card)
        setViewLayoutParams(content_cover)
        speedDialMenuViews.forEach { setViewLayoutParams(it) }
        speedDialMenuViews.forEach { setSpeedDialMenuItemViewOrder(it) }
    }

    fun setButtonBackgroundColour(@ColorInt colour: Int) {
        this.buttonBackgroundColour = colour
        (fab_card as CardView).setCardBackgroundColor(colour)
        rebuildSpeedDialMenu()
    }

    fun setButtonIconResource(@DrawableRes icon: Int) {
        this.buttonIconResource = icon

        fab_icon_wrapper.setBackgroundResource(icon)
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
            DeprecationLevel.WARNING
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
        (content_cover.background as GradientDrawable).setColor(colour)
    }

    fun show() {
        if (isShowing) {
            return
        }

        closeSpeedDialMenu()
        fab_card.visibility = View.VISIBLE
        fab_card.run {
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

        fab_card.clearAnimation()
        fab_card.animate()
                .translationY(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70f, context.resources.displayMetrics))
                .setInterpolator(AccelerateInterpolator(2f))
                .setDuration(if (immediate) 0L else HIDE_SHOW_ANIMATION_DURATION)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        fab_card.visibility = View.GONE
                        isShowing = false
                    }
                })
    }

    fun rebuildSpeedDialMenu() {
        speedDialMenuViews.forEach { (it.parent as ViewGroup).removeView(it) }
        speedDialMenuViews.clear()

        if (speedDialMenuAdapter == null || speedDialMenuAdapter?.getCount() == 0) {
            return
        }

        val adapter = speedDialMenuAdapter!!

        for (i in (0 until adapter.getCount())) {
            val menuItem = adapter.getMenuItem(i)

            val view = layoutInflater.inflate(R.layout.list_item_fab_menu, null) as ViewGroup
            container.addView(view)
            speedDialMenuViews.add(view)

            setViewLayoutParams(view)
            setSpeedDialMenuItemViewOrder(view)

            view.menu_item_label.text = context.getText(menuItem.label)
            speedDialMenuAdapter?.onPrepareItemLabel(context, i, view.menu_item_label)

                (view.menu_item_card as CardView).setCardBackgroundColor(adapter.getBackgroundColour(i, context)
                        ?: buttonBackgroundColour)

            speedDialMenuAdapter?.onPrepareItemCard(context, i, view.menu_item_card)

            view.menu_item_icon_wrapper.apply {
                background = ResourcesCompat.getDrawable(context.resources, menuItem.icon, context.theme)
                backgroundTintList = ContextCompat.getColorStateList(context, menuItem.iconColor)
            }
            speedDialMenuAdapter?.onPrepareItemIconWrapper(context, i, view.menu_item_icon_wrapper)

            view.alpha = 0F
            view.visibility = GONE

            view.tag = i
            view.setOnClickListener { v ->
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

        content_cover.isClickable = isSpeedDialMenuOpen
        content_cover.isFocusable = isSpeedDialMenuOpen
        if (isSpeedDialMenuOpen) {
            content_cover.setOnClickListener({ toggleSpeedDialMenu() })
        } else {
            content_cover.setOnClickListener(null)
        }
    }

    private fun animateFabIconRotation() {
        if (busyAnimatingFabIconRotation) {
            return
        }
        busyAnimatingFabIconRotation = true

        fab_icon_wrapper.animate()
                .rotation(if (isSpeedDialMenuOpen) speedDialMenuAdapter?.fabRotationDegrees()
                        ?: 0F else 0F)
                .setInterpolator(OvershootInterpolator(2f))
                .setDuration(SPEED_DIAL_ANIMATION_DURATION)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
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

        content_cover.visibility = View.VISIBLE
        content_cover.animate()
                .scaleX(if (isSpeedDialMenuOpen) 50f else 0f)
                .scaleY(if (isSpeedDialMenuOpen) 50f else 0f)
                .alpha(if (isSpeedDialMenuOpen) 1f else 0f)
                .setDuration(SPEED_DIAL_ANIMATION_DURATION)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        busyAnimatingContentCover = false
                        if (!isSpeedDialMenuOpen) {
                            content_cover.visibility = View.GONE
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

        val distance = fab_card.height.toFloat()
        speedDialMenuViews.forEachIndexed { i, v ->
            if (isSpeedDialMenuOpen) {
                v.visibility = View.VISIBLE
            }
            val translation = if (isSpeedDialMenuOpen) {
                val direction = if (buttonPosition.and(POSITION_TOP) > 0) 1 else -1
                (i + 1.125F) * distance * direction
            } else {
                0f
            }
            v.animate()
                    .translationY(translation)
                    .alpha(if (isSpeedDialMenuOpen) 1f else 0f)
                    .setDuration(duration)
                    .setInterpolator(OvershootInterpolator(2f))
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            busyAnimatingSpeedDialMenuItems = false
                            if (!isSpeedDialMenuOpen) {
                                v.visibility = View.GONE
                            }
                        }
                    })
        }
    }

    val cardView: View
        get() = fab_card

    val contentCoverView: View
        get() = content_cover

    val iconWrapper: LinearLayout
        get() = fab_icon_wrapper


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
            child.translationY = Math.min(0f, dependency.translationY - dependency.height)
            return true
        }

        override fun onDependentViewRemoved(parent: CoordinatorLayout, child: View, dependency: View) {
            ViewCompat.animate(child).translationY(0f).start()
        }
    }
}
