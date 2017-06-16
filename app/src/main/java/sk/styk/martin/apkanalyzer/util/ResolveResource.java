package sk.styk.martin.apkanalyzer.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.util.TypedValue;

/**
 * Created by Martin Styk on 16.06.2017.
 */
public class ResolveResource {

    /**
     * Get color int for color resource defined in your theme
     *
     * @param ctx           context which uses theme referencing color
     * @param colorResource id of color resource
     * @return
     */
    public static
    @ColorInt
    int getColor(Context ctx, @AttrRes int colorResource) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = ctx.getTheme();
        theme.resolveAttribute(colorResource, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }
}
