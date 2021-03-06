package com.example.testbarang;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TambahData extends AppCompatActivity  {
    private DatabaseReference database;

    private Button btSubmit;
    private EditText etKode;
    private EditText etNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);

        etKode = (EditText) findViewById(R.id.editNo);
        etNama = (EditText) findViewById(R.id.editNama);
        btSubmit = (Button) findViewById(R.id.btnOk);

        database= FirebaseDatabase.getInstance().getReference();

        final Barang barang = (Barang) getIntent().getSerializableExtra("data");

        if (barang != null) {
            etKode.setText(barang.getKode());
            etNama.setText(barang.getNama());
            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    barang.setKode(etKode.getText().toString());
                    barang.setNama(etNama.getText().toString());

                    updateBarang(barang);

                    finish();
                }
            });
        } else {
            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (! (etKode.getText().toString().isEmpty()) &&
                            !(etNama.getText().toString().isEmpty()) )
                        submitBrg(new Barang(etKode.getText().toString(),
                                etNama.getText().toString()));
                    else
                        Toast.makeText(getApplicationContext (), "Data tidak boleh kosong", Toast.LENGTH_LONG).show();

                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etKode.getWindowToken(), 0);
                }
            });
        }
    }

    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }


    private void updateBarang(Barang barang) {

        database = FirebaseDatabase.getInstance().getReference();

        final String getKey = getIntent().getExtras().getString("key");

        database.child("Barang")
                .child(barang.getKey())
                .setValue(barang)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext (), "Data berhasil diupdate", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void submitBrg(Barang brg){

        database.child("Barang").push().setValue(brg).addOnSuccessListener(this,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        etKode.setText("");
                        etNama.setText("");
                        Toast.makeText(getApplicationContext (), "Data berhasil ditambahkan", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, TambahData.class);
    }

}
