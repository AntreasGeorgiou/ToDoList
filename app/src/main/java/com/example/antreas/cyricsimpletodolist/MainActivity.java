package com.example.antreas.cyricsimpletodolist;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    String [] SpinnerImportance={"High","Medium","Low"};
    String [] SpinnerCurrent={"Started","Not Started","Finished"};
    String [] SpinnerSort={"by Description", "by Importance State", "by Current State", "by Location"};
    Spinner betterSpinner3;
    private ArrayList<ToDoModel> items;
    private ListAdapter itemsAdapter;
    public ListView lvItems;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,SpinnerImportance);
        MaterialBetterSpinner betterSpinner= findViewById(R.id.ImportanceState);
        betterSpinner.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapter2=new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,SpinnerCurrent);
        MaterialBetterSpinner betterSpinner2= findViewById(R.id.CurrentState);
        betterSpinner2.setAdapter(arrayAdapter2);

        ArrayAdapter<String> arrayAdapter3=new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,SpinnerSort);
        betterSpinner3= findViewById(R.id.spSort);
        betterSpinner3.setAdapter(arrayAdapter3);
        betterSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               Sort(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        lvItems =  findViewById(R.id.lvItems);
        boolean shared = getSharedPreferences("items", MODE_PRIVATE).contains("items");
        if(shared){
            Gson gson=new GsonBuilder().create();
            String json= getSharedPreferences("items", MODE_PRIVATE).getString("items","");
            Type type=new TypeToken<ArrayList<ToDoModel>>(){}.getType();
            items=gson.fromJson(json,type);
        }
        else {
            items = new ArrayList<>();
        }
        itemsAdapter = new ListAdapter(this,R.layout.listview_layout, items);
        lvItems.setAdapter(itemsAdapter);

    }



    public void Add(MenuItem item) {
        MaterialBetterSpinner betterSpinner= findViewById(R.id.ImportanceState);
        MaterialBetterSpinner betterSpinner2= findViewById(R.id.CurrentState);
        EditText txtDesc = findViewById(R.id.Description);
        EditText txtLoc = findViewById(R.id.txtLocation);
        Button btnAdd = findViewById(R.id.btnAddItem);


        betterSpinner.setVisibility(View.VISIBLE);
        betterSpinner2.setVisibility(View.VISIBLE);
        txtDesc.setVisibility(View.VISIBLE);
        btnAdd.setVisibility(View.VISIBLE);
        txtLoc.setVisibility(View.VISIBLE);
        betterSpinner.setText("");
        betterSpinner2.setText("");
        txtDesc.setText("");
        txtLoc.setText("");
    }
    public void Clear(MenuItem item) {
        if(items.size()== 0 ){
            Toast.makeText(MainActivity.this, "Not Added Any Item Yet!", Toast.LENGTH_LONG).show();
        }
        else {
            MaterialBetterSpinner betterSpinner= findViewById(R.id.ImportanceState);
            MaterialBetterSpinner betterSpinner2= findViewById(R.id.CurrentState);
            EditText txtDesc = findViewById(R.id.Description);
            final EditText txtLoca= findViewById(R.id.txtLocation);

            Button btnAdd = findViewById(R.id.btnAddItem);
            Button btnUpdate = findViewById(R.id.btnUpdate);
            btnUpdate.setVisibility(View.INVISIBLE);
            betterSpinner.setVisibility(View.INVISIBLE);
            betterSpinner2.setVisibility(View.INVISIBLE);
            txtDesc.setVisibility(View.INVISIBLE);
            btnAdd.setVisibility(View.INVISIBLE);
            txtLoca.setVisibility(View.INVISIBLE);

            items.clear();
            itemsAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Cleared Successfully!", Toast.LENGTH_LONG).show();

        }
    }

    public void AddItem(View view) {
        ToDoModel model = new ToDoModel();
        EditText txtdescription =  findViewById(R.id.Description);
        EditText txtLoc= findViewById(R.id.txtLocation);
        MaterialBetterSpinner  Importance =  findViewById(R.id.ImportanceState);
        MaterialBetterSpinner  Current =  findViewById(R.id.CurrentState);
        model.setCurrent(Current.getText().toString());
        model.setDescr(txtdescription.getText().toString());
        model.setImportance(Importance.getText().toString());
        model.setLocation(txtLoc.getText().toString());
        itemsAdapter.add(model);
        txtdescription.setText("");
        Importance.setText("");
        Current.setText("");
        txtLoc.setText("");
        Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_LONG).show();
        Sort(betterSpinner3.getSelectedItemPosition());
        save();
    }

    private void Sort(int position){
        if(position==0){
            items.sort(new Comparator<ToDoModel>() {
                @Override
                public int compare(ToDoModel o1, ToDoModel o2) {
                    return o1.getDescr().compareToIgnoreCase(o2.getDescr());
                }
            });
            itemsAdapter.notifyDataSetChanged();
        }
        else if(position==1){
            items.sort(new Comparator<ToDoModel>() {
                @Override
                public int compare(ToDoModel o1, ToDoModel o2) {
                    return o1.getImportance().compareToIgnoreCase(o2.getImportance());
                }
            });
            itemsAdapter.notifyDataSetChanged();

        }
        else if(position==2){
            items.sort(new Comparator<ToDoModel>() {
                @Override
                public int compare(ToDoModel o1, ToDoModel o2) {
                    return o1.getCurrent().compareToIgnoreCase(o2.getCurrent());
                }
            });
            itemsAdapter.notifyDataSetChanged();

        }else if(position==3){
            items.sort(new Comparator<ToDoModel>() {
                @Override
                public int compare(ToDoModel o1, ToDoModel o2) {
                    return o1.getLocation().compareToIgnoreCase(o2.getLocation());
                }
            });
            itemsAdapter.notifyDataSetChanged();

        }
    }
    public void editPop(final int position){
//                Toast.makeText(MainActivity.this,String.valueOf(position),Toast.LENGTH_LONG).show();


    AlertDialog.Builder altdial = new AlertDialog.Builder(MainActivity.this);
    altdial.setMessage("Do you want to Update this item ???").setCancelable(false)
            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            })
            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final MaterialBetterSpinner betterSpinner= findViewById(R.id.ImportanceState);
                    final MaterialBetterSpinner betterSpinner2= findViewById(R.id.CurrentState);
                    final EditText txtDesc = findViewById(R.id.Description);
                    Button btnAdd = findViewById(R.id.btnAddItem);
                    final EditText txtLoca= findViewById(R.id.txtLocation);
                    final Button btnUpdate = findViewById(R.id.btnUpdate);
                    btnAdd.setVisibility(View.INVISIBLE);
                    betterSpinner.setVisibility(View.VISIBLE);
                    betterSpinner2.setVisibility(View.VISIBLE);
                    btnUpdate.setVisibility(View.VISIBLE);
                    txtDesc.setVisibility(View.VISIBLE);
                    txtLoca.setVisibility(View.VISIBLE);

                    ToDoModel item=items.get(position);
                    txtDesc.setText(item.getDescr());
                    txtLoca.setText(item.getLocation());
                    betterSpinner.setText(item.getImportance());
                    betterSpinner2.setText(item.getCurrent());
                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToDoModel model=new ToDoModel();
                            model.setCurrent(betterSpinner2.getText().toString());
                            model.setDescr(txtDesc.getText().toString());
                            model.setImportance(betterSpinner.getText().toString());
                            model.setLocation(txtLoca.getText().toString());
                            items.set(position,model);
                            itemsAdapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this,"Item Updated Successfully",Toast.LENGTH_LONG).show();
                            betterSpinner.setVisibility(View.INVISIBLE);
                            betterSpinner2.setVisibility(View.INVISIBLE);
                            btnUpdate.setVisibility(View.INVISIBLE);
                            txtDesc.setVisibility(View.INVISIBLE);
                            txtLoca.setVisibility(View.INVISIBLE);
                            Sort(betterSpinner3.getSelectedItemPosition());
                            save();

                        }
                    });
//                                txtDesc.setText(itemsText.lastIndexOf(":"));
//                                betterSpinner.setVisibility(View.INVISIBLE);
//                                betterSpinner2.setVisibility(View.INVISIBLE);
//                                txtDesc.setVisibility(View.INVISIBLE);
                }
                // Toast.makeText(MainActivity.this,"Item Updated Successfully",Toast.LENGTH_LONG).show();}
            });

    AlertDialog alert = altdial.create();
    alert.setTitle("Update Item");
    alert.show();

}
    protected void save() {
        SharedPreferences.Editor editor = getSharedPreferences("items", MODE_PRIVATE).edit();
        Gson gson= new GsonBuilder().create();
        editor.putString("items",gson.toJson(items));
        editor.apply();
    }
    public  void deletePop(final int position){

        AlertDialog.Builder altdial = new AlertDialog.Builder(MainActivity.this);
        altdial.setMessage("Do you want to Delete this item ???").setCancelable(false)
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MaterialBetterSpinner betterSpinner=findViewById(R.id.ImportanceState);
                        MaterialBetterSpinner betterSpinner2= findViewById(R.id.CurrentState);
                        EditText txtDesc = findViewById(R.id.Description);
                        final EditText txtLoca= findViewById(R.id.txtLocation);
                        Button btnAdd = findViewById(R.id.btnAddItem);
                        Button btnUpdate = findViewById(R.id.btnUpdate);
                        btnUpdate.setVisibility(View.INVISIBLE);
                        betterSpinner.setVisibility(View.INVISIBLE);
                        betterSpinner2.setVisibility(View.INVISIBLE);
                        txtDesc.setVisibility(View.INVISIBLE);
                        btnAdd.setVisibility(View.INVISIBLE);
                        txtLoca.setVisibility(View.INVISIBLE);

                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this,"Item Deleted Successfully",Toast.LENGTH_LONG).show();}
                });

        AlertDialog alert = altdial.create();
        alert.setTitle("Delete Item");
        alert.show();
    }
}
