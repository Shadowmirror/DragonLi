package miao.kmirror.dragonli.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.TestActivity;
import miao.kmirror.dragonli.entity.AppPackage;
import miao.kmirror.dragonli.utils.ToastUtils;

public class AppAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AppPackage> mAppPackages;

    public AppAdapter(List<AppPackage> mAppPackages) {
        this.mAppPackages = mAppPackages;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = View.inflate(parent.getContext(), R.layout.list_item_app_view, parent);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_app_view, parent, false);
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
        System.out.println(appPackage.toString());
        holder.tvAppPackage.setText(appPackage.getAppPackageName());
        holder.tvAppName.setText(appPackage.getAppName());

        holder.rlAppContainer.setOnClickListener(v -> {
            
            Log.i("TestActivity", "bindAppViewHolder:  You Click me");

        });
    }

    public void refreshData(List<AppPackage> appPackages) {
        this.mAppPackages = appPackages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mAppPackages.size();
    }

    class AppViewHolder extends RecyclerView.ViewHolder {
        TextView tvAppName;
        TextView tvAppPackage;
        ViewGroup rlAppContainer;

        public AppViewHolder(View itemView) {
            super(itemView);
            this.tvAppName = itemView.findViewById(R.id.tv_app_name);
            this.tvAppPackage = itemView.findViewById(R.id.tv_app_package);
            this.rlAppContainer = itemView.findViewById(R.id.rl_item_app_container);
        }
    }
}