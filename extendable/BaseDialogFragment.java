package tr.com.bracket.trade.utils.extendable;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import tr.com.bracket.trade.R;
import tr.com.bracket.trade.utils.DialogHelper;


public abstract class BaseDialogFragment extends AppCompatDialogFragment {

    public static final String TAG = "BaseDialogFragment";

    private Toolbar _toolbar;
    private String _title = "";
    private DialogFragmentSetupModel _setupModel = new DialogFragmentSetupModel();

    public DialogHelper dialogHelper;

    public abstract void onViewCreatedDF(@NonNull View view);

    public abstract void onSetup(DialogFragmentSetupModel setupModel, LayoutInflater inflater, ViewGroup container);

    public final void onCloseDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);

        dialogHelper = new DialogHelper(getContext());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setWindowAnimations(android.R.style.Animation_Dialog);
        //dialog.getWindow().setWindowAnimations(R.style.MyTheme_DialogFragmentAnim);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.i(TAG, new Object() {
        }.getClass().getEnclosingMethod().getName());
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onSetup(_setupModel, inflater, container);
        for (int i = 0; i < ((ViewGroup) _setupModel.mainView).getChildCount(); i++) {
            View v = ((ViewGroup) _setupModel.mainView).getChildAt(i);
            if (v instanceof Toolbar) {
                _toolbar = (Toolbar) v;
                break;
            }
        }
        return _setupModel.mainView;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                setCancelable(_setupModel.cancelable);
                onViewCreatedDF(view);
                if (_setupModel.showBackNavigation) {
                    if (_toolbar != null) {
                        _toolbar.setNavigationIcon(R.drawable.ic_back_white);
                        _toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismiss();
                            }
                        });
                        _toolbar.setTitle(_title.isEmpty() ? getString(R.string.app_name) : _title);
                    }

                }
            }
        });

    }

    public final BaseDialogFragment setTitle(String title) {
        _title = title;
        _toolbar.setTitle(title);
        return this;
    }

    public final Toolbar getToolbar() {
        return _toolbar;
    }

    public final void addMenu(String name, int iconRes, int enumShowIcon, MenuItem.OnMenuItemClickListener clickListener) {
        _toolbar.getMenu().add(name)
                .setIcon(iconRes)
                .setOnMenuItemClickListener(clickListener)
                .setShowAsAction(enumShowIcon);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void dismiss() {
        closeKeyboard();
        super.dismiss();
        onCloseDialog();
    }

    public final void closeKeyboard() {
        InputMethodManager keyboard = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(_setupModel.mainView.getWindowToken(), 0);
    }

    public class DialogFragmentSetupModel {
        public View mainView;
        public boolean cancelable = true;
        public boolean showBackNavigation = true;
    }


}