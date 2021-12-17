package miao.kmirror.dragonli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.entity.AppPackage;

public class AppAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AppPackage> mAppPackages;
    private LayoutInflater inflater;
    private SkipItemClickListener skipItemClickListener;

    public AppAdapter(Context context, List<AppPackage> mAppPackages) {
        inflater = LayoutInflater.from(context);
        this.mAppPackages = mAppPackages;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_skip_view, parent, false);
        AppViewHolder holder = new AppViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //  在这里设置数据
        if (holder == null) {
            return;
        }
        if (holder instanceof AppViewHolder) {
            bindAppViewHolder((AppViewHolder) holder, position);
        }
    }

    private void bindAppViewHolder(AppViewHolder holder, int position) {
        AppPackage appPackage = mAppPackages.get(position);
        holder.tvSkipName.setText(appPackage.getAppName());
    }

    public void refreshData(List<AppPackage> appPackages) {
        this.mAppPackages = appPackages;
        notifyDataSetChanged();
    }

    public void removeData(int position){
        this.mAppPackages.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mAppPackages.size();
    }

    class AppViewHolder extends RecyclerView.ViewHolder {

        TextView tvSkipName;
        ViewGroup rlSkipContainer;

        public AppViewHolder(View itemView) {
            super(itemView);
            this.tvSkipName = itemView.findViewById(R.id.tv_skip_name);
            this.rlSkipContainer = itemView.findViewById(R.id.rl_item_skip_container);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(skipItemClickListener != null){
                        skipItemClickListener.onItemClick(v, getAbsoluteAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(skipItemClickListener != null){
                        skipItemClickListener.onItemLongClick(v, getAbsoluteAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    public void setOnItemClickListener(SkipItemClickListener listener){
        skipItemClickListener = listener;
    }
}
