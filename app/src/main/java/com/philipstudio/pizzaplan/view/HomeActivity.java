package com.philipstudio.pizzaplan.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.view.fragment.CuaHangFragment;
import com.philipstudio.pizzaplan.view.fragment.LichSuThanhToanFragment;
import com.philipstudio.pizzaplan.view.fragment.MenuOrderFragment;
import com.philipstudio.pizzaplan.view.fragment.TrangCaNhanFragment;
import com.philipstudio.pizzaplan.view.fragment.TimKiemFragment;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationViewEx bottomNavigationViewEx;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();

        bottomNavigationViewEx.setOnNavigationItemSelectedListener(listener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_container, new MenuOrderFragment())
                .commit();
    }

    private BottomNavigationViewEx.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.menu:
                    fragment = new MenuOrderFragment();
                    break;
                case R.id.tim_kiem:
                    fragment = new TimKiemFragment();
                    break;
                case R.id.cua_hang:
                    fragment = new CuaHangFragment();
                    break;
                case R.id.lich_su_thanh_toan:
                    fragment = new LichSuThanhToanFragment();
                    break;
                case R.id.trang_ca_nhan:
                    fragment = new TrangCaNhanFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_container, fragment).commit();
            return true;
        }
    };

    private void initView() {
        bottomNavigationViewEx = findViewById(R.id.bottom_navigation_viewEx);
        frameLayout = findViewById(R.id.frame_layout_container);
    }
}