package com.medandev.sspl2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GarSuhuPanasTambah extends AppCompatActivity {
    Spinner spinner_ruang;
    ProgressDialog pDialog;
    AdapterRuang adapter;
    List<DataRuang> listRuang = new ArrayList<DataRuang>();
    EditText edtSV, edtPV, edtTer1Min, edtTer1Max, edtTer2Min, edtTer2Max, edtTer3Min, edtTer3Max;

    TextView tvIDRuang;
    Button btnTambah;

    public static final String url_data_ruang = Server.URL + "garruangpanas/data_ruang.php";
    public static final String url = Server.URL + "garruangpanas/aksi.php?act=tambah";
    private static final String TAG = GarSuhuPanasTambah.class.getSimpleName();

    public static final String TAG_ID = "id";
    public static final String TAG_RUANG = "nama";

    int success;
    ConnectivityManager conMgr;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gar_suhu_panas_tambah);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        spinner_ruang = findViewById(R.id.spinner_ruang);

        edtSV = findViewById(R.id.edtSV);
        edtPV = findViewById(R.id.edtPV);
        edtTer1Min = findViewById(R.id.edtTer1Min);
        edtTer1Max = findViewById(R.id.edtTer1Max);
        edtTer2Min = findViewById(R.id.edtTer2Min);
        edtTer2Max = findViewById(R.id.edtTer2Max);
        edtTer3Min = findViewById(R.id.edtTer3Min);
        edtTer3Max = findViewById(R.id.edtTer3Max);

        tvIDRuang = findViewById(R.id.tvIDRuang);

        btnTambah = findViewById(R.id.btnTambah);

        spinner_ruang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
               tvIDRuang.setText(listRuang.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        adapter = new AdapterRuang(GarSuhuPanasTambah.this, listRuang);
        spinner_ruang.setAdapter(adapter);

        callData();

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sv, pv, ter1min, ter1max, ter2min, ter2max, ter3min, ter3max, idRuang, idPegawai;
                idRuang = tvIDRuang.getText().toString();
                sv = edtSV.getText().toString();
                pv = edtPV.getText().toString();
                ter1min = edtTer1Min.getText().toString();
                ter1max = edtTer1Max.getText().toString();
                ter2min = edtTer2Min.getText().toString();
                ter2max = edtTer2Max.getText().toString();
                ter3min = edtTer3Min.getText().toString();
                ter3max = edtTer3Max.getText().toString();

                idPegawai = MainActivity.id;

                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                    kirimData(idRuang, sv, pv, ter1min, ter1max, ter2min, ter2max, ter3min, ter3max, idPegawai);
                }
                else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void callData() {
        listRuang.clear();

        pDialog = new ProgressDialog(GarSuhuPanasTambah.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        showDialog();

        // Creating volley request obj
        JsonArrayRequest jArr = new JsonArrayRequest(url_data_ruang,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);

                                DataRuang item = new DataRuang();

                                item.setId(obj.getString(TAG_ID));
                                item.setRuang(obj.getString(TAG_RUANG));

                                listRuang.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();

                        hideDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(GarSuhuPanasTambah.this, error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void kirimData(final String idRuang, final String sv, final String pv, final String ter1min, final String ter1max, final String ter2min, final String ter2max, final String ter3min, final String ter3max, final String idPegawai) {
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

                        Intent intent = new Intent(GarSuhuPanasTambah.this, MainActivity.class);

                        intent.putExtra("fragmentActive", "GarSuhuPanas");
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
                params.put("idRuang", idRuang);
                params.put("sv", sv);
                params.put("pv", pv);
                params.put("ter1min", ter1min);
                params.put("ter1max", ter1max);
                params.put("ter2min", ter2min);
                params.put("ter2max", ter2max);
                params.put("ter3min", ter3min);
                params.put("ter3max", ter3max);
                params.put("idPegawai", idPegawai);

                return params;
            }

        };

        // Adding request to request queue
        Log.d("Data", String.valueOf(strReq));
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
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
