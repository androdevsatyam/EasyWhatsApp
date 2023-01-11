package com.androdevsatyam.easywhatsapp.adapter;

import static com.androdevsatyam.easywhatsapp.helpers.GlobalData.FormatNumbers;
import static com.androdevsatyam.easywhatsapp.helpers.GlobalData.convertArrayToString;
import static com.androdevsatyam.easywhatsapp.helpers.GlobalData.makeToast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.androdevsatyam.easywhatsapp.GlobalData;
import com.androdevsatyam.easywhatsapp.GroupScrapper;
import com.androdevsatyam.easywhatsapp.R;

import java.util.ArrayList;


public class GroupNumbers extends RecyclerView.Adapter<GroupNumbers.ListHolder> {
    ArrayList<String> number, name, selectnumber, selectname, searchnum, searchname;

    private Context context;
    String Name;

    public GroupNumbers(Context context, String Name, ArrayList<String> number, ArrayList<String> name) {
        this.context = context;
        this.number = number;
        this.name = name;
        this.Name = Name;

        selectname = new ArrayList<>();
        selectnumber = new ArrayList<>();

        searchname = name;
        searchnum = number;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        view = inflater.inflate(R.layout.final_list, parent, false);

        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
//        if(context instanceof GroupScrapper || context instanceof Records || context )
        holder.txtName.setText(name.get(position));
        holder.txtnum.setText(FormatNumbers(number.get(position)).trim());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectname.contains(name.get(position)));

        holder.delete.setOnClickListener(v -> {
            number.remove(number.get(holder.getAdapterPosition())/*holder.txtnum.getText().toString()*/);
            name.remove(holder.txtName.getText().toString());

            if (number.size() > 0 && name.size()>0) {

                notifyDataSetChanged();
            }
            else {
                GlobalData.makeToast("Group must have at least one contact", context, Toast.LENGTH_SHORT);
               if(context instanceof GroupScrapper) {
                    ((GroupScrapper) context).toolbar.setSubtitle("No Contacts");
                    ((GroupScrapper) context).create.setVisibility(View.GONE);
                }
            }
        });

        holder.edit.setOnClickListener(v -> showEdit(holder));
    }

    private void showEdit(ListHolder adapterPosition) {
       makeToast("showEdit",context,Toast.LENGTH_SHORT);
    }


    @Override
    public int getItemCount() {
        return number.size();
    }

    public void filter(String text) {
        ArrayList<String> sname, snumber;
        sname = new ArrayList<>();
        snumber = new ArrayList<>();
        for (String d : searchname) {
            if (d.toLowerCase().contains(text)) {
                sname.add(d);
                snumber.add(searchnum.get(searchname.indexOf(d)));
            }
        }
        //update recyclerview
        if (sname.size() > 1)
            updateList(sname, snumber);
        else
            GlobalData.makeToast("No match found..", context, Toast.LENGTH_SHORT);
    }

    private void updateList(ArrayList<String> sname, ArrayList<String> snumber) {
        name = sname;
        number = snumber;
        notifyDataSetChanged();
    }

    class ListHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtnum;
        RelativeLayout view;
        ImageButton delete, edit;
        CheckBox checkBox;

        ListHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.row);
            txtName = itemView.findViewById(R.id.name);
            txtnum = itemView.findViewById(R.id.number);
            delete = itemView.findViewById(R.id.ic_delete);
            edit = itemView.findViewById(R.id.ic_edit);
            checkBox = itemView.findViewById(R.id.select);

            if(context instanceof GroupScrapper)
                edit.setVisibility(View.GONE);
        }
    }
}
