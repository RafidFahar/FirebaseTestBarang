package com.example.testbarang;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterLihatBarang extends RecyclerView.Adapter<AdapterLihatBarang.ViewHolder> {

    FirebaseDataListener listener;
    private ArrayList<Barang> daftarBarang;
    private Context context;
    public AdapterLihatBarang(ArrayList<Barang> barangs, Context ctx){

        daftarBarang = barangs;
        context = ctx;
        listener = (LihatBarang) ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent,
                        false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String name = daftarBarang.get(position).getNama();
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_view);
                dialog.setTitle("Pilihan");
                dialog.show();

                Button editButton = (Button) dialog.findViewById(
                        R.id.btnEditData
                );
                Button delButton = (Button) dialog.findViewById(
                        R.id.btnDeleteData
                );

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        context.startActivity(TambahData.getActIntent((Activity) context).putExtra("data", daftarBarang.get(position)));
                    }
                });

                delButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        listener.onDeleteData(daftarBarang.get(position), position);
                    }
                });
                return true;
            }
        });
        holder.tvTitle.setText(name);
    }

    @Override
    public int getItemCount() {
        return daftarBarang.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tv_namabarang);
        }
    }

    public interface FirebaseDataListener{
        void onDeleteData(Barang barang, int position);
    }
}
