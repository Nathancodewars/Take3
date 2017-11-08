package com.example.nath.take3app.activity.recycleView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nath.take3app.R;

import java.util.ArrayList;

/**
 * Created by nath on 24-Oct-17.
 */

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder>{
    Context context;

    private ArrayList<TextboxChecker> values;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mainTV;
        public View layout;
        public CheckBox mCheckBox;
        public EditText editText;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            mainTV = (TextView) itemView.findViewById(R.id.row_layout_main_display);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.row_layout_checkBox);
            editText = (EditText) itemView.findViewById(R.id.row_layout_qty_input);
        }
    }


    public ViewAdapter(Context context, ArrayList<TextboxChecker> myDataset) {
        this.context = context;
        values = myDataset;
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = values.get(position).getText();
        String qty;
        if(values.get(position).getQty() != null){
            qty = values.get(position).getQty().toString();
            holder.editText.setText(qty);

        }

        holder.mCheckBox.setChecked(values.get(position).isSelected());

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                values.get(holder.getAdapterPosition()).setSelected(isChecked);
            }
        });

        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("ViewAdapter", "afterTextChanged: s.toString" + s.toString());
                try{
                    values.get(holder.getAdapterPosition()).setQty(Integer.parseInt(s.toString()));

                }catch(Exception e){
                    Log.d("ViewAdapter", "catught except e: " + e);
                }


            }
        });
        holder.mainTV.setText(name);


    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @Override
    public ViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

}
