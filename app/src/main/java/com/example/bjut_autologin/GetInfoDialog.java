package com.example.bjut_autologin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class GetInfoDialog extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getLayoutInflater().inflate(R.layout.dialog_signin, new LinearLayout(getContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView confirmBtn = view.findViewById(R.id.confirm_button);
        TextView idTv = view.findViewById(R.id.userid);
        TextView pwTv = view.findViewById(R.id.password);
        confirmBtn.setOnClickListener(v -> {
            String id = idTv.getText().toString();
            String pw = pwTv.getText().toString();
            if (id.equals("")) { // empty
                Toast.makeText(getContext(), "账号为空！", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sP = getActivity().getSharedPreferences("IdAndPw", Context.MODE_PRIVATE);
                sP.edit().putString("id", id).putString("pw", pw).apply();
                Toast.makeText(getContext(), "已保存账号：" + id, Toast.LENGTH_SHORT).show();
                MainActivity activity = (MainActivity) getActivity();
                activity.confirmInput(id);
                this.dismiss();
            }
        });
    }


}