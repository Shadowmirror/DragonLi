package miao.kmirror.dragonli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.entity.WebInfo;

public class WebAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    class WebViewHolder extends RecyclerView.ViewHolder {

        TextView tvSkipName;
        ViewGroup rlSkipContainer;

        public WebViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvSkipName = itemView.findViewById(R.id.tv_skip_name);
            this.rlSkipContainer = itemView.findViewById(R.id.rl_item_skip_container);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (skipItemClickListener != null) {
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

    private List<WebInfo> mWebInfoList;
    private LayoutInflater inflater;
    private SkipItemClickListener skipItemClickListener;

    public WebAdapter(Context context, List<WebInfo> webInfoList) {
        inflater = LayoutInflater.from(context);
        this.mWebInfoList = webInfoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_skip_view, parent, false);
        WebViewHolder holder = new WebViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        if (holder instanceof WebViewHolder) {
            bindWebViewHolder((WebViewHolder) holder, position);
        }
    }

    private void bindWebViewHolder(WebViewHolder holder, int position) {
        WebInfo webInfo = mWebInfoList.get(position);
        holder.tvSkipName.setText(webInfo.getWebName());
    }

    @Override
    public int getItemCount() {
        return mWebInfoList.size();
    }

    public void setOnItemClickListener(SkipItemClickListener listener){
        skipItemClickListener = listener;
    }

    public void refreshData(List<WebInfo> webInfoList){
        this.mWebInfoList = webInfoList;
        notifyDataSetChanged();
    }

    public void removeData(int position){
        this.mWebInfoList.remove(position);
        notifyItemRemoved(position);
    }
}
