package tr.com.bracket.trade.utils.rview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import tr.com.bracket.trade.R;
import tr.com.bracket.trade.databinding.ViewMasterviewrvBinding;

public abstract class FastRVAdapter<TModel> extends RecyclerView.Adapter<FastRVAdapter.RvHolder> {

    public static final String TAG= FastRVAdapter.class.getSimpleName();

    private List<TModel> _listFiltered;
    private List<TModel> _list;
    private Context _context;
    private final int ROW_TYPE_MASTER = 0;
    private final int ROW_TYPE_CONTENT = 1;
    private int _resItemLayout;
    private MasterViewModel _emptyViewModel;
    private MasterViewModel _errorViewModel;
    private RvSetupModel _setupModel = new RvSetupModel();
    //private Class<TModel> _classModel;
    private boolean _bShowProgress;
    private boolean _bShowError;
    private boolean _bShowEmpty;
    private boolean _bRefreshingData;

    public FastRVAdapter(Context context, int resRowLayout) {
        _listFiltered = new ArrayList<>();
        _list = new ArrayList<>();
        _context = context;
        _resItemLayout = resRowLayout;
       // _classModel = classModel;
    }

    public abstract void bindViewRv(ViewDataBinding binding, TModel model, int pos);

    public abstract void onSetup(RvSetupModel setup);

    public void onDataLoaded() {
    }

    public void onItemCountChanged(Integer itemCount) {
    }

    public void onSetClickRv(ViewDataBinding binding, RvHolder holder) {
    }

    @Override
    public RvHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == ROW_TYPE_CONTENT) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(_context), _resItemLayout, viewGroup, false);
            RvHolder holder = new RvHolder(binding, viewType);
            onSetClickRv(binding, holder);
            return holder;
        } else if (viewType == ROW_TYPE_MASTER) {
            ViewMasterviewrvBinding binMaster = DataBindingUtil.inflate(LayoutInflater.from(_context), R.layout.view_masterviewrv, viewGroup, false);
            return new RvHolder(binMaster, viewType);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(FastRVAdapter.RvHolder holder, int pos) {
        int viewType = getItemViewType(pos);
        if (viewType == ROW_TYPE_MASTER && _bShowProgress) {
            _bShowProgress = false;
            holder.binMaster.btn.setVisibility(View.GONE);
            holder.binMaster.ivResim.setVisibility(View.GONE);
            holder.binMaster.lblAciklama.setVisibility(View.GONE);
            holder.binMaster.lblBaslik.setVisibility(View.GONE);
            holder.binMaster.progressBar.setVisibility(View.VISIBLE);
        } else if (viewType == ROW_TYPE_MASTER && _bShowEmpty) {
            _bShowEmpty = false;
            holder.binMaster.lblAciklama.setVisibility(View.VISIBLE);
            holder.binMaster.lblBaslik.setVisibility(View.VISIBLE);
            holder.binMaster.progressBar.setVisibility(View.GONE);
            if (_emptyViewModel.nDrawable != 0) {
                holder.binMaster.ivResim.setVisibility(View.VISIBLE);
                holder.binMaster.ivResim.setImageResource(_emptyViewModel.nDrawable);
            } else {
                holder.binMaster.ivResim.setVisibility(View.GONE);
            }
            holder.binMaster.lblBaslik.setText(_emptyViewModel.sTitle);
            holder.binMaster.lblAciklama.setText(_emptyViewModel.sDescription);
            if (_emptyViewModel.onClickListener != null) {
                holder.binMaster.btn.setVisibility(View.VISIBLE);
                holder.binMaster.btn.setOnClickListener(_emptyViewModel.onClickListener);
                holder.binMaster.btn.setText(_emptyViewModel.sBtnText);
            } else {
                holder.binMaster.btn.setVisibility(View.GONE);
            }
        } else if (viewType == ROW_TYPE_MASTER && _bShowError) {
            _bShowError = false;
            holder.binMaster.lblAciklama.setVisibility(View.VISIBLE);
            holder.binMaster.lblBaslik.setVisibility(View.VISIBLE);
            holder.binMaster.progressBar.setVisibility(View.GONE);
            if (_errorViewModel.nDrawable != 0) {
                holder.binMaster.ivResim.setVisibility(View.VISIBLE);
                holder.binMaster.ivResim.setImageResource(_errorViewModel.nDrawable);
            } else {
                holder.binMaster.ivResim.setVisibility(View.GONE);
            }
            holder.binMaster.lblBaslik.setText(_errorViewModel.sTitle);
            holder.binMaster.lblAciklama.setText(_errorViewModel.sDescription);
            if (_errorViewModel.onClickListener != null) {
                holder.binMaster.btn.setVisibility(View.VISIBLE);
                holder.binMaster.btn.setOnClickListener(_errorViewModel.onClickListener);
                holder.binMaster.btn.setText(_errorViewModel.sBtnText);
            } else {
                holder.binMaster.btn.setVisibility(View.GONE);
            }
        } else if (viewType == ROW_TYPE_CONTENT) {
            TModel model = _listFiltered.get(pos);
            bindViewRv(holder.binding, model, pos);
        }
    }

    @Override
    public int getItemCount() {
        if (_listFiltered.size() == 0 && (_bShowProgress || _bShowError || _emptyViewModel != null)) {
            return 1;
        } else {
            return _listFiltered.size();
        }
    }

    @Override
    public int getItemViewType(int pos) {
        if (_listFiltered.size() == 0 && getItemCount() > 0)
            return ROW_TYPE_MASTER;
        else
            return ROW_TYPE_CONTENT;
    }

    public final void setFilter(String sWord) {
        _listFiltered.clear();
        if (sWord.length() == 0) {
            _listFiltered.addAll(_list);
        } else {
            for (int i = 0; i < _list.size(); i++) {
                if (getFilter(_list.get(i), sWord)) {
                    _listFiltered.add(_list.get(i));
                }
            }
        }
        notifyDataSetChanged();
        //onDataLoaded();
        onItemCountChanged(_listFiltered.size());
    }

    public boolean getFilter(TModel model, String sWord) {
        return true;
    }

    public final void removeItemAll(TModel model) {
        int pos = _listFiltered.indexOf(model);
        _listFiltered.remove(model);
        _list.remove(model);
        if (_listFiltered.isEmpty()) {
            _bShowEmpty = true;
            _bShowProgress = false;
        }
        notifyItemRemoved(pos);
        onItemCountChanged(_listFiltered.size());
    }

    public final void addItem(int pos, TModel model) {
        _listFiltered.add(pos, model);
        _list.add(model);
        notifyItemInserted(pos);
        onItemCountChanged(_listFiltered.size());
    }

    public final void changeItem(TModel old, TModel newModel) {
        int pos = _listFiltered.indexOf(old);
        removeItemAll(old);
        addItem(pos, newModel);
    }

    public final void refreshData() {
        if (_bRefreshingData) {
            return;
        } else {
            _bRefreshingData = true;
        }
         onSetup(_setupModel);
        if (_setupModel.observableData != null) {
            _setupModel.observableData //.delay(500, TimeUnit.MILLISECONDS, Schedulers.newThread())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            _listFiltered.clear();
                            _list.clear();
                            _bShowProgress = true;
                            notifyDataSetChanged();
                        }
                    })
                    .doOnUnsubscribe(new Action0() {
                        @Override
                        public void call() {
                            _bRefreshingData = false;
                        }
                    })
                    .subscribe(new Observer<List<TModel>>() {
                        @Override
                        public void onCompleted() {
                            notifyDataSetChanged();
                            onDataLoaded();
                            onItemCountChanged(_listFiltered.size());
                        }

                        @Override
                        public void onError(Throwable e) {
                            _errorViewModel = new MasterViewModel();
                            _errorViewModel.sTitle = e.getMessage();
                            _bShowError = true;
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onNext(List<TModel> listData) {
                            if (listData.size() == 0)
                                _bShowEmpty = true;
                            _list.addAll(listData);
                            _listFiltered.addAll(listData);

                        }
                    });
        }else if(_setupModel.data!=null){
            _listFiltered.clear();
            _list.clear();
            if(_setupModel.data.isEmpty()){
                _bShowEmpty=true;
                notifyDataSetChanged();
            }else{
                _listFiltered.addAll(_setupModel.data);
                _list.addAll(_setupModel.data);
                notifyDataSetChanged();
                onDataLoaded();
                onItemCountChanged(_listFiltered.size());
            }
              _bRefreshingData = false;
        }else{
            _bRefreshingData = false;
        }
    }

    public List<TModel> getListFiltered() {
        return _listFiltered;
    }
    
      public List<TModel> getListOriginal() {
        return _list;
    }

    public TModel getModelFromHolder(RvHolder holder) {
        return getListFiltered().get(holder.getAdapterPosition());
    }

    public TModel getModelFromPos(int pos) {
        return getListFiltered().get(pos);
    }

    public Integer getListItemCount() {
        return _listFiltered.size();
    }

    public class RvHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;
        ViewMasterviewrvBinding binMaster;

        public RvHolder(ViewDataBinding bindingView, int viewType) {
            super(bindingView.getRoot());
            if (viewType == ROW_TYPE_CONTENT) {
                binding = bindingView;
            } else if (viewType == ROW_TYPE_MASTER) {
                binMaster = (ViewMasterviewrvBinding) bindingView;
            }
        }
    }

    public class MasterViewModel {

        public int nDrawable;
        public String sTitle = "";
        public String sDescription = "";
        public String sBtnText = "";
        public View.OnClickListener onClickListener;
        boolean bShowProgress;

    }

    public class RvSetupModel {

        public Observable<List<TModel>> observableData;
        public List<TModel> data;

        public void setEmptyView(int nDrawable, String title, String description, String sBtnText, View.OnClickListener onClickListener) {
            if (nDrawable != 0 || !title.isEmpty() || !description.isEmpty()) {
                MasterViewModel model = new MasterViewModel();
                model.nDrawable = nDrawable;
                model.sTitle = title;
                model.sDescription = description;
                model.onClickListener = onClickListener;
                model.sBtnText = sBtnText;
                _emptyViewModel = model;
            }
        }
    }
}
