package com.jay.test.view.dialog;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.jay.test.R;

public class NoInternetConnectionDialog extends AlertDialog {

    protected NoInternetConnectionDialog(@NonNull Context context) {
        super(context);

    }

    public static void buildDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.no_internet_massage)
                .setCancelable(false)
                .setPositiveButton(R.string.settings,
                        (dialog, id) -> {

                            context.startActivity(new Intent(
                                    Settings.ACTION_WIRELESS_SETTINGS));
                           dialog.dismiss();

                        })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();

    }
}
