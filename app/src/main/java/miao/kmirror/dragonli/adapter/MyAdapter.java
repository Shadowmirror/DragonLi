package miao.kmirror.dragonli.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.litepal.LitePal;

import java.util.List;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.activity.EditActivity;
import miao.kmirror.dragonli.bean.Text;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Text> mTextList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private int viewType;

    public static final int TYPE_LINEAR_LAYOUT = 0;
    public static final int TYPE_GRID_LAYOUT = 1;

    public MyAdapter(Context context, List<Text> mTextList) {
        this.mTextList = mTextList;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LINEAR_LAYOUT) {
            View view = mLayoutInflater.inflate(R.layout.list_item_layout, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        } else if (viewType == TYPE_GRID_LAYOUT) {
            View view = mLayoutInflater.inflate(R.layout.list_item_grid_layout, parent, false);
            MyViewHolder myGirdViewHolder = new MyViewHolder(view);
            return myGirdViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        if (holder instanceof MyViewHolder) {
            bindMyViewHolder((MyViewHolder) holder, position);
        }
    }

    private void bindMyViewHolder(MyViewHolder holder, int position) {
        Text text = mTextList.get(position);
        holder.mTvTitle.setText(text.getTitle());
        // 解密
        holder.mTvContent.setText(text.getContent());
        holder.mTvTime.setText(text.getCreatedTime());
        // 设置单击响应事件
        holder.rlContainer.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, EditActivity.class);
            intent.putExtra("text", text);
            mContext.startActivity(intent);
        });

        // 设置长按响应事件
        holder.rlContainer.setOnLongClickListener(v -> {
            // 弹出弹窗
            Dialog dialog = new Dialog(mContext, android.R.style.ThemeOverlay_Material_Dialog_Alert);
            View dialogView = mLayoutInflater.inflate(R.layout.list_item_dialog_layout, null);
            TextView tvDelete = dialogView.findViewById(R.id.tv_delete);
            TextView tvEdit = dialogView.findViewById(R.id.tv_edit);

            tvDelete.setOnClickListener(v1 -> {
                int row = LitePal.deleteAll(Text.class, "id = ?", text.getId().toString());
                if (row > 0) {
                    removeData(position);
                }
                dialog.dismiss();
            });

            tvEdit.setOnClickListener(v1 -> {
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtra("text", text);
                mContext.startActivity(intent);
                dialog.dismiss();
            });
            dialog.setContentView(dialogView);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            return true;
        });
    }

    public void refreshData(List<Text> texts) {
        this.mTextList = texts;
        notifyDataSetChanged();
    }

    private void removeData(int position) {
        mTextList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mTextList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTitle;
        TextView mTvContent;
        TextView mTvTime;
        ViewGroup rlContainer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTvTitle = itemView.findViewById(R.id.tv_title);
            this.mTvContent = itemView.findViewById(R.id.tv_content);
            this.mTvTime = itemView.findViewById(R.id.tv_time);
            this.rlContainer = itemView.findViewById(R.id.rl_item_container);
        }
    }

    class MyGridViewHolder extends RecyclerView.ViewHolder{
        TextView mTvTitle;
        TextView mTvContent;
        TextView mTvTime;
        ViewGroup rlContainer;

        public MyGridViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTvTitle = itemView.findViewById(R.id.tv_title);
            this.mTvContent = itemView.findViewById(R.id.tv_content);
            this.mTvTime = itemView.findViewById(R.id.tv_time);
            this.rlContainer = itemView.findViewById(R.id.rl_item_container);
        }
    }


}
