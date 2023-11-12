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
    private final String loginUrl = "http://lgn.bjut.edu.cn";
    private WebView loginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init vars
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        loginPage = binding.loginPage;
        loginPage.getSettings().setJavaScriptEnabled(true);
        setContentView(binding.getRoot());
        SharedPreferences sP = getSharedPreferences("IdAndPw", Context.MODE_PRIVATE);
        String id = sP.getString("id", "");
        String pw = sP.getString("pw", "");

        if (id.equals("")) { // empty
            showDialog();
        } else { // not empty
            // label
            binding.idLabel.setText(getString(R.string.label, sP.getString("id", "")));
            performLogin(id, pw);
        }

        // reset button
        Button resetBtn = binding.resetBtn;
        resetBtn.setOnClickListener(view -> {
            sP.edit().remove("id").remove("pw").apply();
            binding.idLabel.setText("账号信息已清除！");
            showDialog();
        });

        Button refreshBtn = binding.refreshBtn;
        refreshBtn.setOnClickListener(v -> {
            loginPage.reload();
        });
    }

    private void showDialog() {
        DialogFragment dialog = new GetInfoDialog();
        dialog.show(getSupportFragmentManager(), null);
    }

    private void performLogin(String id, String pw) {
        String js = "document.getElementsByName('upass')[0].value='" + pw + "';document.getElementsByName('DDDDD')[0].value='" + id + "';document.getElementsByName('0MKKey')[0].click();";
        loginPage.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.evaluateJavascript(js, null); // inject js
//                loginPage.setWebViewClient(new WebViewClient()); //avoid infinite loop
            }
        });
        loginPage.loadUrl(loginUrl);
    }

    public void confirmInput(String newId, String newPw) {
        // save to sp
        SharedPreferences sP = getSharedPreferences("IdAndPw", Context.MODE_PRIVATE);
        sP.edit().putString("id", newId).putString("pw", newPw).apply();
        // label
        binding.idLabel.setText(getString(R.string.label, newId));
        // perform
        performLogin(newId, newPw);
    }
}