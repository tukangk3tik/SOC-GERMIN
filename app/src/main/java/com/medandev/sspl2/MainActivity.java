package com.medandev.sspl2;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.medandev.sspl2.packaging.kotak.KotakListFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    Fragment fragment = null;
    TextView tvNama, tvEmail;

    SharedPreferences sharedpreferences;
    static String id;
    String username;
    String nama;
    String email;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_EMAIL = "email";

    private static final int ZXING_CAMERA_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        }

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
       /* if(LoginActivity.session == false) {
            id = getIntent().getStringExtra(TAG_ID);
            username = getIntent().getStringExtra(TAG_USERNAME);
            nama = getIntent().getStringExtra(TAG_NAMA);
            email = getIntent().getStringExtra(TAG_EMAIL);
        }
        else{
            id= LoginActivity.TAG_ID;
            username=LoginActivity.TAG_USERNAME;
            nama=LoginActivity.TAG_NAMA;
            email=LoginActivity.TAG_EMAIL;
        }*/


        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);
        email = sharedpreferences.getString(TAG_EMAIL, null);

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);



        tvNama = header.findViewById(R.id.tvNama);
        tvEmail = header.findViewById(R.id.tvEmail);

        tvNama.setText(nama);
        tvEmail.setText(email);


        String fragmentActive = getIntent().getStringExtra("fragmentActive");
        if(fragmentActive!= null){
            //Log.d("Fragment", fragmentActive);
            if(fragmentActive.equals(("GarApung"))){
                fragment = new GarApung();
                callFragment(fragment);
            }
            else if(fragmentActive.equals(("GarApungAfkir"))){
                fragment = new GarApungAfkir();
                callFragment(fragment);
            }
            else if(fragmentActive.equals(("GarApungKantong"))){
                fragment = new GarApungKantong();
                callFragment(fragment);
            }
            else if(fragmentActive.equals(("GarRendam"))){
                fragment = new GarRendam();
                callFragment(fragment);
            }
            else if(fragmentActive.equals(("GarFungisida"))){
                fragment = new GarFungisida();
                callFragment(fragment);
            }
            else if(fragmentActive.equals(("GarFungisidaAfkir"))){
                fragment = new GarFungisidaAfkir();
                callFragment(fragment);
            }
            else if(fragmentActive.equals(("GarKeringAngin"))){
                fragment = new GarKeringAngin();
                callFragment(fragment);
            }
            else if(fragmentActive.equals(("GarSuhuPanas"))){
                fragment = new GarSuhuPanas();
                callFragment(fragment);
            }
            else if(fragmentActive.equals(("GarPanas"))){
                fragment = new GarPanas();
                callFragment(fragment);
            }

            else if(fragmentActive.equals(("GarPreheating"))){
                fragment = new GarPreheating();
                callFragment(fragment);
            }

            else if(fragmentActive.equals(("GarRendam2"))){
                fragment = new GarRendam2();
                callFragment(fragment);
            }

            else if(fragmentActive.equals(("GarFungisida2"))){
                fragment = new GarFungisida2();
                callFragment(fragment);
            }

            else if(fragmentActive.equals(("GarKeringAngin2"))){
                fragment = new GarKeringAngin2();
                callFragment(fragment);
            }

            else if(fragmentActive.equals(("GarGelap"))){
                fragment = new GarGelap();
                callFragment(fragment);
            }

            else if(fragmentActive.equals(("GarGelap2"))){
                fragment = new GarGelap2();
                callFragment(fragment);
            }

            else if(fragmentActive.equals(("GarKutip1"))){
                fragment = new GarKutip1();
                callFragment(fragment);
            }

            else if(fragmentActive.equals(("GarKutip2"))){
                fragment = new GarKutip2();
                callFragment(fragment);
            }

            else if(fragmentActive.equals(("GarKutip3"))){
                fragment = new GarKutip3();
                callFragment(fragment);
            }

            else if(fragmentActive.equals(("GarKutip4"))){
                fragment = new GarKutip4();
                callFragment(fragment);
            }

            else if(fragmentActive.equals(("GarKutip5"))){
                fragment = new GarKutip5();
                callFragment(fragment);
            }

            else if(fragmentActive.equals(("GarKutipKch1"))){
                fragment = new GarKutipKch1();
                callFragment(fragment);
            }

            else if(fragmentActive.equals(("GarKutipKch2"))){
                fragment = new GarKutipKch2();
                callFragment(fragment);
            }

            else if(fragmentActive.equals(("Kotak"))){
                fragment = new KotakListFragment();
                callFragment(fragment);
            }
        }
        else{
            fragment = new Root();
            callFragment(fragment);
        }
        /*
        // tampilan default awal ketika aplikasii dijalankan
        if (savedInstanceState == null) {
            fragment = new Root();
            callFragment(fragment);
        }
        else{
            String fragmentActive = getIntent().getStringExtra("fragmentActive");
            if(fragmentActive=="LBKTimbang"){
                fragment = new LBKTimbang();
                callFragment(fragment);
            }
        }
        */
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(LoginActivity.session_status, false);
            editor.putString(TAG_ID, null);
            editor.putString(TAG_USERNAME, null);
            editor.apply();

            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            finish();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        //PROSES I
        if(id == R.id.nav_gar_apung){
            fragment = new GarApung();
            callFragment(fragment);
        }

        else if(id == R.id.nav_gar_apungafkir){
            fragment = new GarApungAfkir();
            callFragment(fragment);
        }
        /*
        else if(id == R.id.nav_gar_apungkantong){
            fragment = new GarApungKantong();
            callFragment(fragment);
        }
        */
        else if(id == R.id.nav_gar_rendam){
            fragment = new GarRendam();
            callFragment(fragment);
        }

        else if(id == R.id.nav_gar_fungisida){
            fragment = new GarFungisida();
            callFragment(fragment);
        }
        /*
        else if(id == R.id.nav_gar_fungisidaafkir){
            fragment = new GarFungisidaAfkir();
            callFragment(fragment);
        }
        */
        else if(id == R.id.nav_gar_keringangin){
            fragment = new GarKeringAngin();
            callFragment(fragment);
        }

        else if(id == R.id.nav_gar_suhupanas){
            fragment = new GarSuhuPanas();
            callFragment(fragment);
        }
        else if(id == R.id.nav_gar_panas){
            fragment = new GarPanas();
            callFragment(fragment);
        }

        //PROSES II
        else if(id == R.id.nav_gar_preheating){
            fragment = new GarPreheating();
            callFragment(fragment);
        }
        else if(id == R.id.nav_gar_rendam2){
            fragment = new GarRendam2();
            callFragment(fragment);
        }
        else if(id == R.id.nav_gar_fungisida2){
            fragment = new GarFungisida2();
            callFragment(fragment);
        }
        else if(id == R.id.nav_gar_keringangin2){
            fragment = new GarKeringAngin2();
            callFragment(fragment);
        }
        else if(id == R.id.nav_gar_gelap){
            fragment = new GarGelap();
            callFragment(fragment);
        }
        else if(id == R.id.nav_gar_gelap2){
            fragment = new GarGelap2();
            callFragment(fragment);
        }
        else if(id == R.id.nav_gar_kutip1){
            fragment = new GarKutip1();
            callFragment(fragment);
        }

        else if(id == R.id.nav_gar_kutip2){
            fragment = new GarKutip2();
            callFragment(fragment);
        }

        else if(id == R.id.nav_gar_kutip3){
            fragment = new GarKutip3();
            callFragment(fragment);
        }
        else if(id == R.id.nav_gar_kutip4){
            fragment = new GarKutip4();
            callFragment(fragment);
        }
        else if(id == R.id.nav_gar_kutip5){
            fragment = new GarKutip5();
            callFragment(fragment);
        }
        else if(id == R.id.nav_gar_kutipkch1){
            fragment = new GarKutipKch1();
            callFragment(fragment);
        }

        else if(id == R.id.nav_kotak){
            fragment = new KotakListFragment();
            callFragment(fragment);
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null)
                .commit();
    }
}
