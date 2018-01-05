package sk.styk.martin.apkanalyzer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.model.detail.AppSource;
import sk.styk.martin.apkanalyzer.model.list.AppListData;

/**
 * App list adapter for list view.
 * Used in AppListFragment
 * Supports filtering
 */
public class AppListAdapter extends ArrayAdapter<AppListData> {

    private final Object lock = new Object();
    private final LayoutInflater layoutInflater;
    private List<AppListData> displayedObjects = new ArrayList<>();
    private boolean notifyOnChange = true;
    private List<AppListData> originalValues;

    private ListFilter listFilter;

    public AppListAdapter(Context context) {
        super(context, R.layout.list_item_application);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void add(@Nullable AppListData object) {
        synchronized (lock) {
            if (originalValues != null) {
                originalValues.add(object);
            } else {
                displayedObjects.add(object);
            }
        }
        if (notifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void addAll(@NonNull Collection<? extends AppListData> collection) {
        synchronized (lock) {
            if (originalValues != null) {
                originalValues.addAll(collection);
            } else {
                displayedObjects.addAll(collection);
            }
        }
        if (notifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void addAll(AppListData... items) {
        synchronized (lock) {
            if (originalValues != null) {
                Collections.addAll(originalValues, items);
            } else {
                Collections.addAll(displayedObjects, items);
            }
        }
        if (notifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void insert(@Nullable AppListData object, int index) {
        synchronized (lock) {
            if (originalValues != null) {
                originalValues.add(index, object);
            } else {
                displayedObjects.add(index, object);
            }
        }
        if (notifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void remove(@Nullable AppListData object) {
        synchronized (lock) {
            if (originalValues != null) {
                originalValues.remove(object);
            } else {
                displayedObjects.remove(object);
            }
        }
        if (notifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void clear() {
        synchronized (lock) {
            if (originalValues != null) {
                originalValues.clear();
            } else {
                displayedObjects.clear();
            }
        }
        if (notifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void sort(@NonNull Comparator<? super AppListData> comparator) {
        synchronized (lock) {
            if (originalValues != null) {
                Collections.sort(originalValues, comparator);
            } else {
                Collections.sort(displayedObjects, comparator);
            }
        }
        if (notifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        notifyOnChange = true;
    }

    @Override
    public void setNotifyOnChange(boolean notifyOnChange) {
        this.notifyOnChange = notifyOnChange;
    }

    @Override
    public int getCount() {
        return displayedObjects.size();
    }

    @Override
    @Nullable
    public AppListData getItem(int position) {
        return displayedObjects.get(position);
    }

    public int getPosition(@Nullable AppListData item) {
        return displayedObjects.indexOf(item);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.list_item_application, parent, false);
        } else {
            view = convertView;
        }

        AppListData item = getItem(position);
        ((ImageView) view.findViewById(R.id.package_img)).setImageDrawable(item.getIcon());
        ((TextView) view.findViewById(R.id.application_name)).setText(item.getApplicationName());
        ((TextView) view.findViewById(R.id.package_name)).setText(item.getPackageName());
        view.setTag(item);
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (listFilter == null)
            listFilter = new ListFilter();
        return listFilter;
    }

    public void filterOnAppSource(AppSource appSource) {
        if (appSource == null)
            getFilter().filter("clear_source:");
        else
            getFilter().filter("source:" + appSource.ordinal());
    }

    public void filterOnAppName(String appName) {
        if (TextUtils.isEmpty(appName))
            getFilter().filter("clear_name:");
        else
            getFilter().filter("name:" + appName);
    }

    private class ListFilter extends Filter {

        private String currentStringFilter;
        private AppSource currentSourceFilter;

        @Override
        protected FilterResults performFiltering(CharSequence expression) {
            updateFilterParams(expression);

            final FilterResults results = new FilterResults();

            if (originalValues == null) {
                synchronized (lock) {
                    originalValues = new ArrayList<>(displayedObjects);
                }
            }

            // if no filter is defined, return whole dataset
            if (currentSourceFilter == null && currentStringFilter == null) {
                final ArrayList<AppListData> list;
                synchronized (lock) {
                    list = new ArrayList<>(originalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                ArrayList<AppListData> filteredSet = new ArrayList<>();
                ArrayList<AppListData> originalSet;
                synchronized (lock) {
                    originalSet = new ArrayList<>(originalValues);
                }

                for (int i = 0; i < originalSet.size(); i++) {
                    final AppListData value = originalSet.get(i);

                    if (matchesNameFilter(value) && matchesSourceFilter(value)) {
                        filteredSet.add(value);
                    }
                }

                results.values = filteredSet;
                results.count = filteredSet.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            synchronized (lock) {
                displayedObjects = (List<AppListData>) results.values;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

        private void updateFilterParams(CharSequence expression) {
            String stringExpression = expression.toString();

            if (stringExpression.startsWith("clear_source:")) {
                currentSourceFilter = null;
                return;
            }

            if (stringExpression.startsWith("clear_name:")) {
                currentStringFilter = null;
                return;
            }

            String appSourceString = stringExpression.contains("source:") ? stringExpression.substring(stringExpression.indexOf(":") + 1) : null;
            currentSourceFilter = TextUtils.isEmpty(appSourceString) ? currentSourceFilter : AppSource.values()[Integer.parseInt(appSourceString)];
            String currentNameString = stringExpression.contains("name:") ? stringExpression.substring(stringExpression.indexOf(":") + 1) : null;
            currentStringFilter = TextUtils.isEmpty(currentNameString) ? currentStringFilter : currentNameString.toLowerCase();
        }


        private boolean matchesNameFilter(AppListData appListData) {
            boolean result = currentStringFilter == null ||
                    matchesNameFilter(appListData.getApplicationName().toLowerCase()) ||
                    matchesNameFilter(appListData.getPackageName().toLowerCase());
            return result;
        }

        private boolean matchesNameFilter(String valueText) {
            // First match against the whole, non-splitted value
            if (valueText.startsWith(currentStringFilter)) {
                return true;
            } else {
                // if no match, match against tokenized string
                final String[] words = valueText.split(" ");
                for (String word : words) {
                    if (word.startsWith(currentStringFilter)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean matchesSourceFilter(AppListData appListData) {
            AppSource itemSource = appListData.getSource();
            boolean result = currentSourceFilter == null || currentSourceFilter.equals(itemSource);
            return result;
        }

    }
}