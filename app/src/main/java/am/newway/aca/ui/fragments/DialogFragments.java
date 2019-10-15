package am.newway.aca.ui.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.facebook.drawee.view.SimpleDraweeView;

import am.newway.aca.R;

public class DialogFragments extends DialogFragment implements View.OnClickListener {


    final String LOG_TAG = getClass().getSimpleName();
    private SimpleDraweeView imageView;
    private Uri uri;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragmen_for_recycler2, container);
        v.findViewById(R.id.textDialogCoursesName);
        v.findViewById(R.id.textDialogCoursesDescription);
        v.findViewById(R.id.btnGoWebsec).setOnClickListener(this);
        imageView = v.findViewById(R.id.imageView);
        imageView.setImageURI(uri);

        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
        return v;
    }

    public void setImageUrl(Uri uri){
        this.uri = uri;
    }

    public void onClick(View v) {
        Log.d(LOG_TAG, "Dialog 1: " + ((Button) v).getText());
        dismiss();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 1: onCancel");
    }
}
