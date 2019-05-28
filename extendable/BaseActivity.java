package tr.com.bracket.trade.utils.extendable;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import tr.com.bracket.trade.R;
import tr.com.bracket.trade.utils.DialogHelper;


public abstract class BaseActivity extends AppCompatActivity {

    private OnCreateMenuListener _onCreateMenuListener;
    private PermissonResultListener _PermissonResultListener;
    private boolean _disableBackButton;
    private BaseActivitySetupModel _setupModel = new BaseActivitySetupModel();
    private DialogHelper _dialogHelper;
    private Toolbar _toolbar;

    public abstract void onSetup(BaseActivitySetupModel setupModel);

    public abstract void onViewCreated();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //db = new KomisyoncuDB(this);
//
        _dialogHelper = new DialogHelper(this);
        //firebaseHelper = new FirebaseDataHelper(this);
//
        //generalManager = new GeneralManagerFire(firebaseHelper);
        //bl = new BL(this, generalManager);
        //ftpHelper = new FTPHelper(bl.getRemoteFTPLoginModel(), dialogHelper);
        //
        enterAnim();
        onSetup(_setupModel);
         if (_setupModel.isFullScreen) {
            try {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } catch (Exception e) {
            }
        }
        if (_setupModel.hiddenKeyboard) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        View view = _setupModel.mainView.findViewById(R.id.toolbar);
        if (view != null) {
            _toolbar = (Toolbar) view;
            setSupportActionBar(_toolbar);
        } else {
            for (int i = 0; i < ((ViewGroup) _setupModel.mainView).getChildCount(); i++) {
                View v = ((ViewGroup) _setupModel.mainView).getChildAt(i);
                if (v instanceof Toolbar) {
                    _toolbar = (Toolbar) v;
                    setSupportActionBar(_toolbar);
                    break;
                }
            }
        }
        setContentView(_setupModel.mainView);
        onViewCreated();
    }

    @Override
    public void onBackPressed() {
        if (_setupModel.disableBack)
            return;
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (_onCreateMenuListener != null)
            _onCreateMenuListener.onAddMenu(menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (_PermissonResultListener != null)
                        _PermissonResultListener.onSuccess();
                    //izin verildi
                    // finish();
                } else {
                    if (_PermissonResultListener != null)
                        _PermissonResultListener.onFailed();
                    //izin verilmedi
                    //finish();
                }
            }
        }
        //finish();
    }

    @Override
    public void finish() {
        super.finish();
        exitAnim();
    }

    public void enterAnim() {
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void exitAnim() {
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void requestPermisson(String permisson, PermissonResultListener listener) {
        _PermissonResultListener = listener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Kullanıcı Tekrar Sorma işaretledi ise
            if (shouldShowRequestPermissionRationale(permisson)) {
                _dialogHelper.basicDialog("İzin İsteği", "Programın çalışabilmesi içi tüm izinlerin verilmesi gerekiyor.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        finish();
                    }
                });
                //izin iste
            } else if (checkSelfPermission(permisson) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{permisson}, 1);
            } else {
                // finish();
            }
        }
        //finish();
    }


    public final ActionBar getActionbar() {
        return getSupportActionBar();
    }

    public final Toolbar getToolbar() {
        return _toolbar;
    }

//    public final void setToolbar(Toolbar toolbar) {
//        setSupportActionBar(toolbar);
//    }

    public void setDisableBackButton() {
        _disableBackButton = true;
    }
    
    public final void closeKeyboard() {
        InputMethodManager keyboard = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(_setupModel.mainView.getWindowToken(), 0);
    }

    public void addMenuListener(OnCreateMenuListener listener) {
        _onCreateMenuListener = listener;
    }

    public interface OnCreateMenuListener {
        void onAddMenu(Menu menu);
    }

    public interface PermissonResultListener {
        void onSuccess();

        void onFailed();
    }

    public class BaseActivitySetupModel {

        public boolean disableBack;
        public View mainView;
        public boolean hiddenKeyboard;
        public boolean isFullScreen = true;

    }


}
