package com.philipstudio.pizzaplan.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.utils.NguoiDungUtils;
import com.philipstudio.pizzaplan.view.fragment.DangNhapFragment;
import com.philipstudio.pizzaplan.view.fragment.DangKyFragment;

import lib.kingja.switchbutton.SwitchMultiButton;

public class PizzaPlantActivity extends AppCompatActivity implements DangNhapFragment.OnSignInClickListener {

    SwitchMultiButton switchMultiButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        switchMultiButton = findViewById(R.id.switch_multi_button);

        NguoiDungUtils utils = new NguoiDungUtils(PizzaPlantActivity.this);
        String idNguoiDung = utils.getIdUser();
        if (idNguoiDung != null){
            Intent intent = new Intent(PizzaPlantActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        switchMultiButton.setOnSwitchListener(listener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_container, new DangNhapFragment()).commit();
    }

    private SwitchMultiButton.OnSwitchListener listener = new SwitchMultiButton.OnSwitchListener() {
        @Override
        public void onSwitch(int position, String tabText) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = new DangNhapFragment();
                    break;
                case 1:
                    fragment = new DangKyFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_container, fragment).commit();
        }
    };

    @Override
    public void onSuccess(boolean isSuccess) {
        if (isSuccess){
            Intent intent = new Intent(PizzaPlantActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}