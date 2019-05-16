package tr.com.bracket.aiku360.utils;

import android.app.ProgressDialog;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import tr.com.bracket.aiku360.utils.interfaces.IResultListener;

public class RxJavaHelper {

    private DialogHelper _dialogHelper;

    public RxJavaHelper(DialogHelper dialogHelper){
        _dialogHelper = dialogHelper;
    }

    public  <T> Observable<T> createObservable(Observable.OnSubscribe<T> subscribe) {
        return rx.Observable.create(subscribe);
    }

    public  <T> void createObservableWithDialog(Observable.OnSubscribe<T> subscribe, final IResultListener<T> resultListener) {
        final ProgressDialog dialog = _dialogHelper.getProgressDialog("", "Lütfen bekleyin...");
        dialog.show();
        Observable<T> observable = Observable
                .create(subscribe)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        dialog.dismiss();
                    }
                });
        observable.subscribe(new Observer<T>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                resultListener.onError(e);
            }

            @Override
            public void onNext(T t) {
                resultListener.onResult(t);
            }
        });
        //return observable;
    }


}
