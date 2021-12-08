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

public class GarKutipKch2Tambah extends AppCompatActivity {
    static final String TAG = GarKutipKch2Tambah.class.getSimpleName();
    EditText edtNoKutipKch, edtJumlahKantong, edtJumlahNormal, edtJumlahAfkir;
    ProgressBar progressBar;
    Button btnTambah;
    ProgressDialog pDialog;

    int success;
    ConnectivityManager conMgr;

    private String url = Server.URL + "garkutipkch2/aksi.php?act=tambah";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gar_kutip_kch2_tambah);

        edtJumlahKantong = findViewById(R.id.edtJumlahKantong);
        edtJumlahNormal = findViewById(R.id.edtJumlahNormal);
        edtJumlahAfkir = findViewById(R.id.edtJumlahAfkir);
        edtNoKutipKch = findViewById(R.id.edtNoKutipKch);

        progressBar = findViewById(R.id.progressBar);
        btnTambah = findViewById(R.id.btnTambah);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        progressBar.setVisibility(View.GONE);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jumlahKantong, jumlahNormal, noKutipKch,  jumlahAfkir, idPegawai;

                jumlahKantong = edtJumlahKantong.getText().toString();
                jumlahNormal = edtJumlahNormal.getText().toString();
                noKutipKch = edtNoKutipKch.getText().toString();
                jumlahAfkir = edtJumlahAfkir.getText().toString();
                idPegawai = MainActivity.id;

                if ((noKutipKch.trim().length() > 0) && jumlahKantong.trim().length()>0) {
                    if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                        kirimData(jumlahKantong, jumlahNormal, noKutipKch, jumlahAfkir, idPegawai);
                    }
                    else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext() ,"No. Kutip KCH dan jumlah kantong  wajib diisi", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void kirimData(final String jumlahKantong, final String jumlahNormal, final String noKutipKch, final String jumlahAfkir, final String idPegawai) {
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

                        Intent intent = new Intent(GarKutipKch2Tambah.this, MainActivity.class);

                        intent.putExtra("fragmentActive", "GarKutipKch2");
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
                Log.d("Error", error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("jumlahKantong", jumlahKantong);
                params.put("jumlahNormal", jumlahNormal);
                params.put("noKutipKch", noKutipKch);
                params.put("jumlahAfkir", jumlahAfkir);
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