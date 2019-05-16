package tr.com.bracket.trade.utils.views;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taner.cavdar on 7.07.2017.
 */


public class ACTextViewBinder {

    private List<ACTextViewModel> _list = new ArrayList();
    private List<String> listValue = new ArrayList<String>();
    private AutoCompleteTextView _acTextView;
    private IACTextViewSelectListener _listener;
    private Context _context;

    //public String sSecilenValue = "";
    //public String sSecilenDisplay = "";

    private ACTextViewModel selectedModel;


    public ACTextViewBinder(Context context, final AutoCompleteTextView autoCompleteTextView) {
        _acTextView = autoCompleteTextView;
        _context=context;
        _acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // int nPosition = listDisplay.indexOf(parent.getItemAtPosition(position).toString());
               // sSecilenValue = listValue.get(nPosition);
               // sSecilenDisplay = listDisplay.get(nPosition);
                //TODO position yanlış.Özel adapter oluştur.
                selectedModel=_list.get(position);

               if(_listener!=null){
                   _listener.onSelected(_list.get(position));
               }


            }
        });
        _acTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _acTextView.showDropDown();
            }
        });
    }

    public void addItem(String displayName,String value){
        _list.add(new ACTextViewModel(displayName, value));
    }

    public void clearSelect() {
        selectedModel = null;
        _acTextView.setText("");
    }

    public void selectValue(String value) {
        for (int i = 0; i <_list.size() ; i++) {
            if(_list.get(i).value.equals(value)){
                selectedModel=_list.get(i);
                return;
            }
        }
       //if (listValue.contains(value)) {
       //    _acTextView.setText(listDisplay.get(listValue.indexOf(value)));
       //    sSecilenValue = value;
       //    sSecilenDisplay = _autoCompleteTextView.getText().toString();
       //} else {
       //    _acTextView.setText("");
       //    sSecilenValue = "";
       //    sSecilenDisplay = "";
       //}
    }

    public String getSelectedDisplayName(){
        return selectedModel==null ? "" : selectedModel.displayName;
    }

    public String getSelectedValue(){
        return selectedModel==null ? "" : selectedModel.value;
    }

    public void setOnItemSelectedListener(IACTextViewSelectListener listener) {
        _listener = listener;
    }

    public List<ACTextViewModel> getList(){
        return _list;
    }

    public void refreshData(){
        String[] charSequenceItems = new String[_list.size()];
        for (int i = 0; i < _list.size(); i++) {
            charSequenceItems[i]=_list.get(i).displayName;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(_context, android.R.layout.select_dialog_item, charSequenceItems);
        _acTextView.setThreshold(1);
        _acTextView.setAdapter(adapter);
    }

    public interface IACTextViewSelectListener {

        void onSelected(ACTextViewModel model);

    }

    public class ACTextViewModel {
        public String displayName = "";
        public String value = "";

        public ACTextViewModel(String displayName, String value) {
            this.displayName = displayName;
            this.value = value;
        }

    }


}
