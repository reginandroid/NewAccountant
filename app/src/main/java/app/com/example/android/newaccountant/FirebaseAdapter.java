package app.com.example.android.newaccountant;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by Ковтун on 28.12.2017.
 */

public class FirebaseAdapter extends FirebaseRecyclerAdapter<Balance_Item, FirebaseAdapter.MyViewHolder> {
    Context c;
    ArrayList<Balance_Item> list;
    private  static ClickListener mClickListener;

    public FirebaseAdapter(Context context, Class<Balance_Item> modelClass, int modelLayout, Class<MyViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.c =context;
    }



    @Override
    protected void populateViewHolder(MyViewHolder viewHolder, final Balance_Item model, final int position) {
        viewHolder.date.setText(model.getDate());
        viewHolder.name.setText(model.getItem_name());
        viewHolder.sum.setText(String.valueOf(model.getSum()));

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView date, name, sum;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            date = (TextView) view.findViewById(R.id.date);
            name = (TextView) view.findViewById(R.id.name);
            sum = (TextView) view.findViewById(R.id.sum);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());

                    return true;
                }
            });

        }
    }

        public interface ClickListener{
            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(ClickListener clickListener){
            mClickListener = clickListener;
        }
    }


