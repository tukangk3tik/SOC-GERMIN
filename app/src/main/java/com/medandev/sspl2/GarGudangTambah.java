package com.medandev.sspl2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GarGudangTambah extends AppCompatActivity {
    static final String TAG = GarGudangTambah.class.getSimpleName();
    EditText edtNoGoni, edtRak, edtBaris, edtKolom, edtKeterangan;
    ProgressBar progressBar;
    Button btnTambah;
    ProgressDialog pDialog;

    int success;
    ConnectivityManager conMgr;

    private String url = Server.URL + "gargudang/aksi_gargudang.php?act=tambah";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gar_gudang_tambah);

        edtNoGoni = findViewById(R.id.edtNoGoni);
        edtRak = findViewById(R.id.edtRak);
        edtBaris = findViewById(R.id.edtBaris);
        edtKolom = findViewById(R.id.edtKolom);
        edtKeterangan = findViewById(R.id.edtKeterangan);

        progressBar = findViewById(R.id.progressBar);
        btnTambah = findViewById(R.id.btnTambah);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        String barcode = getIntent().getStringExtra("code");

        Log.d("Kode", barcode);
        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(getApplicationContext(), "Barcode is empty!", Toast.LENGTH_LONG).show();
            finish();
        }

        edtNoGoni.setText(barcode);

        progressBar.setVisibility(View.GONE);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noGoni, rak, baris, kolom, keterangan, idPegawai;
                noGoni = edtNoGoni.getText().toString();
                rak = edtRak.getText().toString();
                baris = edtBaris.getText().toString();
                kolom = edtKolom.getText().toString();
                keterangan = edtKeterangan.getText().toString();
                idPegawai = MainActivity.id;

                if (noGoni.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                        kirimData(noGoni, rak, baris, kolom, keterangan, idPegawai);
                    }
                    else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext() ,"No. Goni wajib diisi", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void kirimData(final String noGoni, final String rak, final String baris, final String kolom, final String keterangan, final String idPegawai) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        // Memanggil main activity
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(GarGudangTambah.this, MainActivity.class);

                        intent.putExtra("fragmentActive", "GarGudang");
                        finish();
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("noGoni", noGoni);
                params.put("rak", rak);
                params.put("baris", baris);
                params.put("kolom", kolom);
                params.put("keterangan", keterangan);
                params.put("idPegawai", idPegawai);

                return params;
            }

        };

        // Adding request to request queue
        Log.d("Data", String.valueOf(strReq));
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void transparentToolbar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}