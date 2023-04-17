package sk.styk.martin.apkanalyzer.views

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import sk.styk.martin.apkanalyzer.R

interface SpeedDialMenuOpenListener {
    fun onOpen(floatingActionButton: FloatingActionButton)
}

interface SpeedDialMenuCloseListener {
    fun onClose(floatingActionButton: FloatingActionButton)
}

abstract class SpeedDialMenuAdapter {

    /**
     * Gets the number of items represented by this adapter.
     * Note: returning zero has the same effect as return `false` from `isEnabled()`.
     * @return the number of items represented by this adapter
     */
    abstract fun getCount(): Int

    /**
     * Gets the menu item to display at the specified position in the range 0 to `getCount() - 1`.
     * See `SpeedDialMenuItem` for more details.
     * Note: positions start at zero closest to the FAB and increase for items further away.
     * @return the menu item to display at the specified position
     */
    abstract fun getMenuItem(position: Int): SpeedDialMenuItem

    /**
     * Handler for click events on menu items.
     * The position passed corresponds to positions passed to `getMenuItem()`.
     * @return `true` to close the menu after the click; `false` to leave it open
     */
    open fun onMenuItemClick(position: Int): Boolean = true

    /**
     * Gets the colour of the background tile behind the menu item.
     * Positions correspond to positions passed to `getMenuItem()`.
     * Note: this method should return an aRGB colour integer, *not* a colour resource ID.
     * @return the colour of the card behind the icon at the specified position
     */
    @ColorInt
    open fun getBackgroundColour(position: Int, context: Context): Int? = null

    /**
     * Apply formatting to the `TextView` used for the label of the menu item at the given position.
     * Note: positions start at zero closest to the FAB and increase for items further away.
     */
    open fun onPrepareItemLabel(context: Context, position: Int, label: TextView) {}

    /**
     * Apply formatting to the view used for the card behind the icon at the given position.
     * Note: the view will be a `CardView` on SDK 21+ and a `LinearLayout` on SDK 20 and below.
     * Note: positions start at zero closest to the FAB and increase for items further away.
     */
    open fun onPrepareItemCard(context: Context, position: Int, card: View) {}

    /**
     * Apply formatting to the `LinearLayout` that wraps the icon of the menu item at the given position.
     * This is called after the icon is set as the background of the given wrapper.
     * Note: positions start at zero closest to the FAB and increase for items further away.
     */
    open fun onPrepareItemIconWrapper(context: Context, position: Int, label: LinearLayout) {}

    /**
     * Gets the number of degrees to rotate the FAB's icon when the speed-dial menu opens.
     * This is useful for the popular "plus icon"/"close icon" transition.
     * @return the number of degrees to rotate the FAB's icon when the speed-dial menu opens
     */
    open fun fabRotationDegrees() = 0F

    /**
     * Determines whether or not the speed-dial menu is enabled.
     * @return `true` if the menu is enabled; `false` if it is not
     */
    open fun isEnabled() = true
}

class SpeedDialMenuItem(@DrawableRes val icon: Int, @StringRes val label: Int, @ColorRes val iconColor: Int = R.color.colorWhite)
