package hgh.my_changeskin.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hgh.my_changeskin.R;

/**
 * Created by 3046 on 2017/1/17.
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder>
{
    private final View.OnClickListener mSimpleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
       /*     Context context = v.getContext();
           Intent intent = new Intent(context, DetailActivity.class);
            context.startActivity(intent);*/
        }
    };

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.item_first_fragment, parent, false);
        return new SimpleViewHolder(rootView);
    }



    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.itemView.setOnClickListener(mSimpleClickListener);
    }

    @Override
    public int getItemCount() {
        return 200;
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
