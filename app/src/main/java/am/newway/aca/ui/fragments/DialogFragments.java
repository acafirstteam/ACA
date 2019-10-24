package am.newway.aca.ui.fragments;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import am.newway.aca.R;
import am.newway.aca.template.Course;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public
class DialogFragments extends DialogFragment implements View.OnClickListener {

    private final String LOG_TAG = getClass().getSimpleName();
    private Course course;
    private String lang;
    private Context context;

    public
    View onCreateView ( LayoutInflater inflater , ViewGroup container ,
            Bundle savedInstanceState ) {
        View v = inflater.inflate( R.layout.dialog_fragmen_for_recycler2 , container );

        final TextView textName = v.findViewById( R.id.textDialogCoursesName );
        final TextView textDescription = v.findViewById( R.id.textDialogCoursesDescription );
        final SimpleDraweeView imageView = v.findViewById( R.id.imageView );

        v.findViewById( R.id.btnGoWebsec ).setOnClickListener( this );

        imageView.setImageURI( course.getUrl() );
        textName.setText( course.getName() );
        textDescription.setText( course.getDescription().get( lang ).toString() );
        textDescription.setMovementMethod( new ScrollingMovementMethod() );

        Dialog dialog = getDialog();
        if ( dialog != null ) {
            Window window = dialog.getWindow();
            if ( window != null ) {
                window.setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
            }
        }
        return v;
    }

    @Override
    public
    void onDismiss ( final DialogInterface dialog ) {
        super.onDismiss( dialog );
        Fragment parentFragment = getParentFragment();
        if ( parentFragment instanceof DialogInterface.OnDismissListener ) {
            ( ( DialogInterface.OnDismissListener ) parentFragment ).onDismiss( dialog );
        }
    }

    public
    void setCourse ( Course course ) {
        this.course = course;
    }

    public
    void setContext ( Context context ) {
        this.context = context;
    }

    public
    void setLanguage ( String lang ) {
        this.lang = lang;
    }

    public
    void onClick ( View v ) {

        try {
            if ( !URLUtil.isValidUrl( course.getLink() ) ) {
                Toast.makeText( context , " This is not a valid link" , Toast.LENGTH_LONG ).show();
            }
            else {
                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setData( Uri.parse( course.getLink() ) );
                context.startActivity( intent );
            }
        } catch ( ActivityNotFoundException e ) {
            Toast.makeText( context , " You don't have any browser to open web page" ,
                    Toast.LENGTH_LONG ).show();
        }
        dismiss();
    }

    public
    void onCancel ( DialogInterface dialog ) {
        super.onCancel( dialog );
        Log.d( LOG_TAG , "Dialog 1: onCancel" );
    }
}
