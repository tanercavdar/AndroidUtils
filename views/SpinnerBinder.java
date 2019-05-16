package tr.com.bracket.trade.utils.views;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpinnerBinder {

    private Spinner _spinner;
    private HashMap<Integer, String> _listKeys;
    private Context _context;
    private String _sAddCustomItem;
    private boolean _bAddEmptyValue;
    private List<SpinnerItemModel> _listData;
    private ArrayAdapter _adapter;

    public SpinnerBinder(Context context, Spinner spinner, String sAddCustomItem, boolean bAddEmptyValue) {
        _listData = new ArrayList<>();
        _context = context;
        _spinner = spinner;
        _sAddCustomItem = sAddCustomItem;
        _bAddEmptyValue = bAddEmptyValue;
        spinner.setAdapter(null);
    }

    public void addItem(String key, String value) {
        _listData.add(new SpinnerItemModel(key, value));
    }

    public void addItemList(List<SpinnerItemModel> listData) {
        _listData = listData;
    }

    public void refreshData() {
        final ArrayList<String> listValues = new ArrayList<>();
        final HashMap<Integer, String> listKeys = new HashMap<>();
        int position = 0;
        if (_bAddEmptyValue) {
            listKeys.put(position, "");
            listValues.add("");
            position++;
        }
        if (_sAddCustomItem.equals("") == false) {
            listKeys.put(position, "");
            listValues.add(_sAddCustomItem);
            position++;
        }
        for (SpinnerItemModel model : _listData) {
            listKeys.put(position, model.key);
            listValues.add(model.value);
            position++;
        }
        if (listValues.size() > 0) {
            _adapter = new ArrayAdapter(_context, android.R.layout.simple_spinner_item, listValues);
            _adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            _spinner.setAdapter(_adapter);
        }
        _listKeys = listKeys;
    }

    public void selectKey(String key) {
        if (_listKeys != null && _listKeys.size() > 0) {
            for (Map.Entry<Integer, String> entry : _listKeys.entrySet()) {
                if (entry.getValue().equals(key)) {
                    _spinner.setSelection(entry.getKey());
                    break;
                }
            }
        }
    }

    public void removeItem(String key) {
        boolean exitLoop = false;
        for (SpinnerItemModel model : _listData) {
            if (exitLoop)
                return;
            if (model.key.equals(key)) {
                _listData.remove(model);
                _listKeys.remove(key);
                _adapter.remove(model.value);
                _adapter.notifyDataSetChanged();
                exitLoop = true;
                break;
            }
        }
    }

    public void addItemNotify(String key, String value) {
        _listKeys.put(_adapter.getCount(), key);
        _listData.add(new SpinnerItemModel(key, value));
        _adapter.add(value);
        _adapter.notifyDataSetChanged();
    }

    public String getSelectedValue() {
        return _spinner.getSelectedItem().toString();
    }

    public String getSelectedKey() {
        if (_listKeys.size() > 0) {
            return _listKeys.get(_spinner.getSelectedItemPosition());
        } else {
            return "";
        }
    }

    public Spinner getSpinner() {
        return _spinner;
    }

    public List<SpinnerItemModel> getItemList() {
        return _listData;
    }

    public static class SpinnerItemModel {

        public String key = "";
        public String value = "";

        public SpinnerItemModel() {
        }

        public SpinnerItemModel(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }

}