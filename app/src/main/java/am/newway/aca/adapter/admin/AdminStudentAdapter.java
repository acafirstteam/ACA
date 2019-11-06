package am.newway.aca.adapter.admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;

import am.newway.aca.R;
import am.newway.aca.template.Student;
import am.newway.aca.ui.admin.AdminStudentEditActivity;
import am.newway.aca.util.Util;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import static am.newway.aca.ui.admin.student.AdminStudentActivity.PERMISSIONS_REQUEST_CALL_PHONE;
import static am.newway.aca.ui.admin.student.AdminStudentActivity.UPDATE_STUDENT_LIST;

public
class AdminStudentAdapter extends RecyclerView.Adapter<AdminStudentAdapter.ViewHolder> {
    private List<Student> students;
    private Context context;
    private String phone;

    public
    AdminStudentAdapter ( List<Student> students , Context context ) {
        this.students = students;
        this.context = context;
    }

    public
    void setStudents ( List<Student> students ) {
        this.students.clear();
        this.students.addAll( students );

        notifyDataSetChanged();
    }

    public
    void setStudent ( Student student, int index ) {
        this.students.set( index, student );

        notifyItemChanged( index );
    }

    @NonNull
    @Override
    public
    ViewHolder onCreateViewHolder ( @NonNull final ViewGroup parent , int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.admin_student_item , parent , false );

        return new ViewHolder( view );
    }

    @Override
    public
    void onBindViewHolder ( @NonNull ViewHolder holder , int position ) {
        holder.bind( students.get( position ) );
    }

    @Override
    public
    int getItemCount () {
        return students.size();
    }

    public
    void callPhone () {
        Util.callPhone( context, phone );
    }

    //    @SuppressLint ( "MissingPermission" )
//    public
//    void callPhone () {
//
//        View view =
//                LayoutInflater.from( context ).inflate( R.layout.phone_permission_dialog , null );
//        final LottieAnimationView anim = view.findViewById( R.id.animation_view );
//        final TextView text = view.findViewById( R.id.text );
//        anim.setRepeatCount( LottieDrawable.INFINITE );
//        anim.setAnimation( "call.json" );
//        anim.playAnimation();
//        text.setText( R.string.like_to_call );
//
//        AlertDialog.Builder builder = new AlertDialog.Builder( context );
//        builder.setView( view )
//                .setPositiveButton( context.getString( R.string.call ) ,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public
//                            void onClick ( final DialogInterface dialogInterface , final int i ) {
//                                context.startActivity( new Intent( Intent.ACTION_CALL ,
//                                        Uri.parse( "tel:" + phone ) ) );
//                            }
//                        } )
//                .setNegativeButton( context.getString( R.string.no ) , null )
//                .show();
//    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView courseName;
        TextView studentEmail;
        TextView studentPhone;
        TextView studentName;
        TextView studentCourse;

        SimpleDraweeView imageView;
        Student student;

        ViewHolder ( @NonNull final View itemView ) {

            super( itemView );

            courseName = itemView.findViewById( R.id.textDialogCoursesName );
            studentName = itemView.findViewById( R.id.textNameStudentItm );
            studentEmail = itemView.findViewById( R.id.textEmailStudentItm );
            studentPhone = itemView.findViewById( R.id.textPhoneStudentItm );
            studentCourse = itemView.findViewById( R.id.textCourseStudentItm );
            imageView = itemView.findViewById( R.id.image_view );

            studentPhone.setOnClickListener( new View.OnClickListener() {
                @Override
                public
                void onClick ( View view ) {
                    makePhoneCall();
                }
            } );

            studentEmail.setOnClickListener( new View.OnClickListener() {
                @Override
                public
                void onClick ( View view ) {
                    Util.sendEmail(context, studentEmail.getText().toString());
                }
            } );

            itemView.setOnClickListener( new View.OnClickListener() {

                @Override
                public
                void onClick ( View view ) {
                    startActivityAdapter( getAdapterPosition() );
                }
            } );
        }

        @SuppressLint ( "DefaultLocale" )
        void bind ( Student student ) {
            this.student = student;
            studentName.setText( student.getName() );
            studentEmail.setText( student.getEmail() );
            studentPhone.setText(
                    student.getPhone().isEmpty() ? context.getString( R.string.empty_phone )
                            : student.getPhone() );
            studentCourse.setText( student.getCourse().isEmpty() ?
                    context.getString( R.string.empty_course ) : student.getCourse() );
            imageView.setImageURI( student.getPicture() );
        }

        @SuppressLint ( "MissingPermission" )
        private
        void makePhoneCall () {

            if ( !studentPhone.getText().toString().equals( context.getString( R.string.empty_phone ) ) ) {
                phone = studentPhone.getText().toString();
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                    if ( ContextCompat.checkSelfPermission( context ,
                            Manifest.permission.CALL_PHONE ) !=
                            PackageManager.PERMISSION_GRANTED ) {

                        if ( ActivityCompat.shouldShowRequestPermissionRationale(
                                ( Activity ) context , Manifest.permission.CALL_PHONE ) ) {
                            AlertDialog.Builder builder = new AlertDialog.Builder( context );
                            builder.setView( R.layout.phone_permission_dialog )
                                    .setPositiveButton( context.getString( R.string.call ) ,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public
                                                void onClick (
                                                        final DialogInterface dialogInterface ,
                                                        final int i ) {
                                                    ActivityCompat.requestPermissions(
                                                            ( Activity ) context ,
                                                            new String[]{ Manifest.permission.CALL_PHONE } ,
                                                            PERMISSIONS_REQUEST_CALL_PHONE );
                                                }
                                            } )
                                    .setNegativeButton( context.getString( R.string.no ) , null )
                                    .show();
                        }
                        else {
                            // No explanation needed; request the permission
                            ActivityCompat.requestPermissions( ( Activity ) context ,
                                    new String[]{ Manifest.permission.CALL_PHONE } ,
                                    PERMISSIONS_REQUEST_CALL_PHONE );
                        }
                    }
                    else
                        Util.callPhone(context, phone);
                }
                else
                    Util.callPhone(context, phone);
            }
            else
                Toast.makeText( context , R.string.no_phone_number , Toast.LENGTH_SHORT )
                        .show();
        }
    }

    private
    void startActivityAdapter ( int position ) {
        Intent intent = new Intent( context , AdminStudentEditActivity.class );
        Student student = students.get( position );
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> map = mapper.convertValue( student , HashMap.class );
        intent.putExtra( "map" , map );
        intent.putExtra( "index" , position );
        ((Activity)context).startActivityForResult( intent, UPDATE_STUDENT_LIST );
    }
}
