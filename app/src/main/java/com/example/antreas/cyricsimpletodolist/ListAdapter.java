package com.example.antreas.cyricsimpletodolist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<ToDoModel>{
    private MainActivity context;
    private  List<ToDoModel> data;
    public ListAdapter(@NonNull Activity context, int resource, @NonNull List<ToDoModel> objects) {
        super(context, resource, objects);
        this.data=objects;
        this.context=(MainActivity)context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       final ToDoModel model=data.get(position);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.listview_layout, null, false);
        TextView txtDescr=rowView.findViewById(R.id.txtDescr);
        TextView txtLoca= rowView.findViewById(R.id.txtLoca);
        TextView txtCurrr=rowView.findViewById(R.id.txtCurr);
        TextView txtImpo=rowView.findViewById(R.id.txtImport);
        ImageButton imgLoc=rowView.findViewById(R.id.imgLoc);
        imgLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri=Uri.parse("geo:0,0?q="+model.getLocation());
                Intent i=new Intent(Intent.ACTION_VIEW,uri);
                i.setPackage("com.google.android.apps.maps");
                context.startActivity(i);
            }
        });
        ImageButton imgDel=rowView.findViewById(R.id.imgDelete);
        imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.deletePop(position);
            }

            });


        ImageButton imgEdit=rowView.findViewById(R.id.imgEdit);
        imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.editPop(position);
                }
                });

        txtDescr.setText(model.getDescr());
        txtCurrr.setText(model.getCurrent());
        txtImpo.setText(model.getImportance());
        txtLoca.setText(model.getLocation());
        return rowView;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ToDoModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
