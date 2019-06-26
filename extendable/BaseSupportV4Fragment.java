package tr.com.bracket.trade.utils.extendable;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import tr.com.bracket.trade.utils.DialogHelper;


public abstract class BaseSupportV4Fragment extends Fragment {

    private OnCreateMenuListener _onCreateMenuListener;
    private BaseFragmentSetupModel _setupModel=new BaseFragmentSetupModel();


   public DialogHelper dialogHelper;

    public abstract void onSetup(BaseFragmentSetupModel setupModel,LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    public abstract void onViewCreatedBF();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialogHelper = new DialogHelper(getContext());
      setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        onSetup(_setupModel,inflater,container,savedInstanceState);
        if(_setupModel.hiddenKeyboard){
           getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        return _setupModel.mainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       onViewCreatedBF();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (_onCreateMenuListener != null)
            _onCreateMenuListener.onAddMenu(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void addMenuListener(OnCreateMenuListener listener) {
        _onCreateMenuListener = listener;
    }
    
 public boolean onBackPressed(){
        return  true;
    }

    public final void closeKeyboard() {
        InputMethodManager keyboard = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(_setupModel.mainView.getWindowToken(), 0);
    }

    public final void showKeyboard(View view){
        //view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext(). getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    protected interface OnCreateMenuListener {
        void onAddMenu(Menu menu);
    }

    public ActionBar getToolbar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public class BaseFragmentSetupModel{

        public View mainView;
        public boolean hiddenKeyboard;

    }
}
