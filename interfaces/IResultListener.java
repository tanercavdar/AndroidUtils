package tr.com.bracket.trade.utils.interfaces;

public interface IResultListener<T> {

    void onResult(T result);

    void onError(Throwable exception);

}
