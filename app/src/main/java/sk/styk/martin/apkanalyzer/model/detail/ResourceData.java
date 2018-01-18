package sk.styk.martin.apkanalyzer.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Martin Styk
 * @version 03.07.2017.
 */

public class ResourceData implements Parcelable {

    //types of drawables
    private int pngDrawables;
    private int ninePatchDrawables;
    private int jpgDrawables;
    private int gifDrawables;
    private int xmlDrawables;

    private int drawables;
    //number of drawables with different name (i.e. /res/hdpi/a.png == /res/xhdpi/a.png)
    private int differentDrawables;

    //drawables according to dimensions
    private int ldpiDrawables;
    private int mdpiDrawables;
    private int hdpiDrawables;
    private int xhdpiDrawables;
    private int xxhdpiDrawables;
    private int xxxhdpiDrawables;
    private int nodpiDrawables;
    private int tvdpiDrawables;
    private int unspecifiedDpiDrawables;

    private int layouts;
    //number of layouts with different name (i.e. /res/layout/a.xml == /res/layout-land/a.xml)
    private int differentLayouts;

    public ResourceData(int pngDrawables, int ninePatchDrawables, int jpgDrawables, int gifDrawables, int xmlDrawables, int drawables, int differentDrawables, int ldpiDrawables, int mdpiDrawables, int hdpiDrawables, int xhdpiDrawables, int xxhdpiDrawables, int xxxhdpiDrawables, int nodpiDrawables, int tvdpiDrawables, int unspecifiedDpiDrawables, int layouts, int differentLayouts) {
        this.pngDrawables = pngDrawables;
        this.ninePatchDrawables = ninePatchDrawables;
        this.jpgDrawables = jpgDrawables;
        this.gifDrawables = gifDrawables;
        this.xmlDrawables = xmlDrawables;
        this.drawables = drawables;
        this.differentDrawables = differentDrawables;
        this.ldpiDrawables = ldpiDrawables;
        this.mdpiDrawables = mdpiDrawables;
        this.hdpiDrawables = hdpiDrawables;
        this.xhdpiDrawables = xhdpiDrawables;
        this.xxhdpiDrawables = xxhdpiDrawables;
        this.xxxhdpiDrawables = xxxhdpiDrawables;
        this.nodpiDrawables = nodpiDrawables;
        this.tvdpiDrawables = tvdpiDrawables;
        this.unspecifiedDpiDrawables = unspecifiedDpiDrawables;
        this.layouts = layouts;
        this.differentLayouts = differentLayouts;
    }

    public int getPngDrawables() {
        return pngDrawables;
    }

    public int getNinePatchDrawables() {
        return ninePatchDrawables;
    }

    public int getJpgDrawables() {
        return jpgDrawables;
    }

    public int getGifDrawables() {
        return gifDrawables;
    }

    public int getXmlDrawables() {
        return xmlDrawables;
    }

    public int getDifferentDrawables() {
        return differentDrawables;
    }

    public int getDrawables() {
        return drawables;
    }

    public int getLdpiDrawables() {
        return ldpiDrawables;
    }

    public int getMdpiDrawables() {
        return mdpiDrawables;
    }

    public int getHdpiDrawables() {
        return hdpiDrawables;
    }

    public int getXhdpiDrawables() {
        return xhdpiDrawables;
    }

    public int getXxhdpiDrawables() {
        return xxhdpiDrawables;
    }

    public int getXxxhdpiDrawables() {
        return xxxhdpiDrawables;
    }

    public int getNodpiDrawables() {
        return nodpiDrawables;
    }

    public int getTvdpiDrawables() {
        return tvdpiDrawables;
    }

    public int getUnspecifiedDpiDrawables() {
        return unspecifiedDpiDrawables;
    }

    public int getLayouts() {
        return layouts;
    }

    public int getDifferentLayouts() {
        return differentLayouts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceData that = (ResourceData) o;

        if (pngDrawables != that.pngDrawables) return false;
        if (ninePatchDrawables != that.ninePatchDrawables) return false;
        if (jpgDrawables != that.jpgDrawables) return false;
        if (gifDrawables != that.gifDrawables) return false;
        if (xmlDrawables != that.xmlDrawables) return false;
        if (drawables != that.drawables) return false;
        if (differentDrawables != that.differentDrawables) return false;
        if (ldpiDrawables != that.ldpiDrawables) return false;
        if (mdpiDrawables != that.mdpiDrawables) return false;
        if (hdpiDrawables != that.hdpiDrawables) return false;
        if (xhdpiDrawables != that.xhdpiDrawables) return false;
        if (xxhdpiDrawables != that.xxhdpiDrawables) return false;
        if (xxxhdpiDrawables != that.xxxhdpiDrawables) return false;
        if (nodpiDrawables != that.nodpiDrawables) return false;
        if (tvdpiDrawables != that.tvdpiDrawables) return false;
        if (unspecifiedDpiDrawables != that.unspecifiedDpiDrawables) return false;
        if (layouts != that.layouts) return false;
        return differentLayouts == that.differentLayouts;

    }

    @Override
    public int hashCode() {
        int result = pngDrawables;
        result = 31 * result + ninePatchDrawables;
        result = 31 * result + jpgDrawables;
        result = 31 * result + gifDrawables;
        result = 31 * result + xmlDrawables;
        result = 31 * result + drawables;
        result = 31 * result + differentDrawables;
        result = 31 * result + ldpiDrawables;
        result = 31 * result + mdpiDrawables;
        result = 31 * result + hdpiDrawables;
        result = 31 * result + xhdpiDrawables;
        result = 31 * result + xxhdpiDrawables;
        result = 31 * result + xxxhdpiDrawables;
        result = 31 * result + nodpiDrawables;
        result = 31 * result + tvdpiDrawables;
        result = 31 * result + unspecifiedDpiDrawables;
        result = 31 * result + layouts;
        result = 31 * result + differentLayouts;
        return result;
    }

    @Override
    public String toString() {
        return "ResourceData{" +
                "pngDrawables=" + pngDrawables +
                ", ninePatchDrawables=" + ninePatchDrawables +
                ", jpgDrawables=" + jpgDrawables +
                ", gifDrawables=" + gifDrawables +
                ", xmlDrawables=" + xmlDrawables +
                ", drawables=" + drawables +
                ", differentDrawables=" + differentDrawables +
                ", ldpiDrawables=" + ldpiDrawables +
                ", mdpiDrawables=" + mdpiDrawables +
                ", hdpiDrawables=" + hdpiDrawables +
                ", xhdpiDrawables=" + xhdpiDrawables +
                ", xxhdpiDrawables=" + xxhdpiDrawables +
                ", xxxhdpiDrawables=" + xxxhdpiDrawables +
                ", nodpiDrawables=" + nodpiDrawables +
                ", tvdpiDrawables=" + tvdpiDrawables +
                ", unspecifiedDpiDrawables=" + unspecifiedDpiDrawables +
                ", layouts=" + layouts +
                ", differentLayouts=" + differentLayouts +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pngDrawables);
        dest.writeInt(this.ninePatchDrawables);
        dest.writeInt(this.jpgDrawables);
        dest.writeInt(this.gifDrawables);
        dest.writeInt(this.xmlDrawables);
        dest.writeInt(this.drawables);
        dest.writeInt(this.differentDrawables);
        dest.writeInt(this.ldpiDrawables);
        dest.writeInt(this.mdpiDrawables);
        dest.writeInt(this.hdpiDrawables);
        dest.writeInt(this.xhdpiDrawables);
        dest.writeInt(this.xxhdpiDrawables);
        dest.writeInt(this.xxxhdpiDrawables);
        dest.writeInt(this.nodpiDrawables);
        dest.writeInt(this.tvdpiDrawables);
        dest.writeInt(this.unspecifiedDpiDrawables);
        dest.writeInt(this.layouts);
        dest.writeInt(this.differentLayouts);
    }

    protected ResourceData(Parcel in) {
        this.pngDrawables = in.readInt();
        this.ninePatchDrawables = in.readInt();
        this.jpgDrawables = in.readInt();
        this.gifDrawables = in.readInt();
        this.xmlDrawables = in.readInt();
        this.drawables = in.readInt();
        this.differentDrawables = in.readInt();
        this.ldpiDrawables = in.readInt();
        this.mdpiDrawables = in.readInt();
        this.hdpiDrawables = in.readInt();
        this.xhdpiDrawables = in.readInt();
        this.xxhdpiDrawables = in.readInt();
        this.xxxhdpiDrawables = in.readInt();
        this.nodpiDrawables = in.readInt();
        this.tvdpiDrawables = in.readInt();
        this.unspecifiedDpiDrawables = in.readInt();
        this.layouts = in.readInt();
        this.differentLayouts = in.readInt();
    }

    public static final Creator<ResourceData> CREATOR = new Creator<ResourceData>() {
        @Override
        public ResourceData createFromParcel(Parcel source) {
            return new ResourceData(source);
        }

        @Override
        public ResourceData[] newArray(int size) {
            return new ResourceData[size];
        }
    };
}
