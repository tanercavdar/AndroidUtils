package tr.com.bracket.aiku360.utils;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import tr.com.bracket.aiku360.databinding.RowActpersonelBinding;


public abstract class ACTAdapter<TModel> extends ArrayAdapter<TModel> {
    private List<TModel> _list = new ArrayList<>();
    private List<TModel> _listFiltered = new ArrayList<>();
    private Context _context;
    private int _resItemLayout;


    public abstract void onBindView(ViewDataBinding binding, TModel model);

    public abstract boolean filterCondition(TModel model, String word);

    public ACTAdapter(Context context, int resRowLayout, List<TModel> itemList) {
        super(context, android.R.layout.select_dialog_item, itemList);
        _context = context;
        _list.addAll(itemList);
        _resItemLayout = resRowLayout;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RowActpersonelBinding b = DataBindingUtil.getBinding(convertView);

        if (b == null) {
            LayoutInflater layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            b = DataBindingUtil.inflate(layoutInflater, _resItemLayout, parent, false);
            convertView = b.getRoot();
        }
        TModel model = getItem(position);
        onBindView(b, model);
        return convertView;
    }

    public void clearFilter() {
        _listFiltered.clear();
        _listFiltered.addAll(_list);
        getFilter().filter("");
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //TODO filtremeden sonra showDropDown() filtreli metodu gösteriyor. clearFilter ile çözmeye çalıştım
                if (constraint != null) {
                    _listFiltered.clear();
                    if (constraint.toString().isEmpty()) {
                        _listFiltered.addAll(_list);
                    } else {
                        for (TModel model : _list) {
                            if (filterCondition(model, constraint.toString())) {
                                _listFiltered.add(model);
                            }
                        }
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = _listFiltered;
                    filterResults.count = _listFiltered.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<TModel> filteredList = (List<TModel>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    // for (TModel model : filteredList) {
                    //     add(model);
                    addAll(filteredList);
                    // }
                    notifyDataSetChanged();
                }
                //_listFiltered.clear();
                //_listFiltered.addAll((List<TModel>) results.values);
                //notifyDataSetChanged();
            }


        };
    }


}