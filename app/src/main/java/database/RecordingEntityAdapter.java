package database;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.denver.recorder_ui.R;

import java.util.List;


public class RecordingEntityAdapter extends RecyclerView.Adapter<RecordingEntityAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(RecordingEntity item);
    }

    private final List<RecordingEntity> items;
    private int itemLayout;
    private final OnItemClickListener listener;

    public RecordingEntityAdapter(List<RecordingEntity> items, int itemLayout, OnItemClickListener listener){
        this.items = items;
        this.itemLayout = itemLayout;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecordingEntityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecordingEntityAdapter.ViewHolder holder, int position) {
       //String s = items.get(position).getTitle();
       //holder.title_field.setText(s);
       holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title_field = null;
        public ViewHolder(View itemView) {
            super(itemView);
            title_field = (TextView) itemView.findViewById(R.id.list_item_title);
        }

        public void bind( final RecordingEntity item, final OnItemClickListener listener){
            title_field.setText(item.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }

}
