package com.example.admin.election;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by admin on 01.05.2018.
 */

public class SettingsDialog extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);

        builder.setView(view)
                .setPositiveButton(R.string.apply, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        EditText ip=getDialog().findViewById(R.id.ip);
                        MainActivity.connectionSettings.setIp(ip.getText().toString());
                        dismiss();
                    }
                });

        return builder.create();
    }


    public void onActivityCreated(Bundle savedInstanceState) {



        super.onActivityCreated(savedInstanceState);
    }


}
