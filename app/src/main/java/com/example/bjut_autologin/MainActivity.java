package com.example.bjut_autologin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.bjut_autologin.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String loginUrl = "http://lgn.bjut.edu.cn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sP = getSharedPreferences("IdAndPw", Context.MODE_PRIVATE);

        if (sP.getString("id", "").equals("")) { // empty
            showDialog();
        } else { // not empty
            performLogin();
            // label
            binding.idLabel.setText(getString(R.string.label, sP.getString("id", "")));
            // reset button
            Button resetBtn = binding.resetBtn;
            resetBtn.setOnClickListener(view -> {
                sP.edit().remove("id").remove("pw").apply();
                showDialog();
            });
        }
    }

    private void showDialog() {
        DialogFragment dialog = new GetInfoDialog();
        dialog.show(getSupportFragmentManager(), null);
    }

    private void performLogin() {
        // retrieve id and pw
        SharedPreferences sP = getSharedPreferences("IdAndPw", Context.MODE_PRIVATE);
        String id = sP.getString("id", "");
        String pw = sP.getString("pw", "");
        // perform
        WebView loginPage = binding.loginPage;
        loginPage.getSettings().setJavaScriptEnabled(true);
        String js = "document.getElementsByName('upass')[0].value='" + pw + "';document.getElementsByName('DDDDD')[0].value='" + id + "';document.getElementsByName('0MKKey')[0].click();";
        loginPage.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.evaluateJavascript(js, null);
            }
        });
        loginPage.loadUrl(loginUrl);
    }

    public void confirmInput(String newId) {
        binding.idLabel.setText(getString(R.string.label, newId));
        performLogin();
    }
}