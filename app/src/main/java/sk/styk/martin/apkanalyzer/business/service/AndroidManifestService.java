package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Tooling for reading data directly from AndroidManifest.xml cpackaged within application
 * <p>
 * Created by Martin Styk on 22.06.2017.
 */
public class AndroidManifestService {


    private PackageManager packageManager;
    private String packageName;

    public AndroidManifestService(PackageManager packageManager, String packageName) {
        this.packageManager = packageManager;
        this.packageName = packageName;
    }

    public String loadAndroidManifest() {
        String manifest = readManifest(packageManager, packageName);
        String formatted = formatManifest(manifest);
        return formatted;
    }

    private String formatManifest(String manifest) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result = new StreamResult(new StringWriter());
            Source source = new StreamSource(new ByteArrayInputStream(manifest.getBytes("UTF-8")));
            transformer.transform(source, result);
            String xmlString = result.getWriter().toString();
            return xmlString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String readManifest(PackageManager packageManager, String packageName) {
        Resources mApk1Resources = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            mApk1Resources = packageManager.getResourcesForApplication(packageName);
            XmlResourceParser parser = mApk1Resources.getAssets().openXmlResourceParser("AndroidManifest.xml");
            int eventType;

            while ((eventType = parser.next()) != parser.END_DOCUMENT) {

                // start tag found
                if (eventType == XmlResourceParser.START_TAG) {
                    //start with opening element and writing its name
                    stringBuilder.append("<" + parser.getName());

                    //for each attribute in given element append attrName="attrValue"
                    for (int attribute = 0; attribute < parser.getAttributeCount(); attribute++) {
                        String attributeName = parser.getAttributeName(attribute);
                        String attributeValue = getAttributeValue(attributeName, parser.getAttributeValue(attribute));

                        stringBuilder.append(" " + attributeName + "=\"" + attributeValue + "\"");
                    }

                    stringBuilder.append(">");

                    if (parser.getText() != null) {
                        // if there is  body of xml element, add it there
                        stringBuilder.append(parser.getText());
                    }
                } else if (eventType == XmlResourceParser.END_TAG) {
                    stringBuilder.append("</" + parser.getName() + ">");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private String getAttributeValue(String attributeName, String attributeValue) {
        if (attributeValue.startsWith("@")) {
            Resources resources = null;
            try {
                int id = Integer.valueOf(attributeValue.substring(1));
                resources = packageManager.getResourcesForApplication(packageName);

                String value;

                switch (attributeName){
                    case "theme":
                    case "resource":
                        value = resources.getResourceEntryName(id);
                        break;
                    default:
                        value = resources.getString(id);
                }

                String escaped = TextUtils.htmlEncode(value);
                return escaped;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return attributeValue;
    }

    /**
     * It is not possible to get minSdkVersions using Android PackageManager - parse AndroidManifest of app
     */
    public static int getMinSdkVersion(String packageName, PackageManager packageManager) {
        Resources mApk1Resources = null;
        try {
            mApk1Resources = packageManager.getResourcesForApplication(packageName);
            XmlResourceParser parser = mApk1Resources.getAssets().openXmlResourceParser("AndroidManifest.xml");
            int eventType = -1;

            while (eventType != parser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG) {
                    String tagValue = parser.getName();
                    if (tagValue.equals("uses-sdk")) {
                        return parser.getAttributeIntValue("http://schemas.android.com/apk/res/android", "minSdkVersion", 0);
                    }
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
