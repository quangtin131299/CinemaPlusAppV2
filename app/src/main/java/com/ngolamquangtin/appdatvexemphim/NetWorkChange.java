package com.ngolamquangtin.appdatvexemphim;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Button;

public class NetWorkChange extends BroadcastReceiver {

    static Button button = null;
    static Dialog dialog = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (dialog == null) {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_nowifi);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            if (button == null) {
                button = dialog.findViewById(R.id.btnOK);
            }
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isOnline(context) == false) {
//                        dialog.dismiss();
//                        dialog.show();
//                    } else {
//                        dialog.dismiss();
//                    }
//                }
//            });

//            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialogInterface) {
//                    if (isOnline(context) == false) {
//                        dialog.show();
//                    } else {
//                        dialog.dismiss();
//                    }
//                }
//            });
        }
        if (isOnline(context)) {
            dialog.dismiss();
        } else {
            dialog.show();
        }

    }

    public static boolean isOnline(Context context) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
        return connected;
    }
}
