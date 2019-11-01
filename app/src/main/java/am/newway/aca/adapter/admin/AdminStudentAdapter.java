package am.newway.aca.adapter.admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
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
import am.newway.aca.ui.admin.AdminStudentActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public
class AdminStudentAdapter extends RecyclerView.Adapter<AdminStudentAdapter.ViewHolder> {
    private List<Student> students;
    private Context context;
    private static final int REQUEST_CALL = 1;
    private static final int REQUEST_EMAIL = 1;

    public
    AdminStudentAdapter ( List<Student> students , Context context ) {
        this.students = students;
        this.context = context;
    }

    public
    void setStudents ( List<Student> students ) {
        this.students.clear();
        this.students.addAll( students );
        Log.e( "@@@@@@@@@@@@@@@@@@" , "onComplete: " + students.size() );

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public
    ViewHolder onCreateViewHolder ( @NonNull final ViewGroup parent , int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.student_item , parent , false );

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

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCourseName;
        TextView textEmailStudentItm;
        TextView textPhoneStudentItm;
        TextView textNameStudentItm;

        SimpleDraweeView imageView;
        Student student;


        ViewHolder ( @NonNull final View itemView ) {
            super( itemView );

            textViewCourseName = itemView.findViewById( R.id.textDialogCoursesName );
            textNameStudentItm = itemView.findViewById( R.id.textNameStudentItm );
            textEmailStudentItm = itemView.findViewById( R.id.textEmailStudentItm );
            textPhoneStudentItm = itemView.findViewById( R.id.textPhoneStudentItm );
            imageView = itemView.findViewById( R.id.imageViewStudentItem );


            textPhoneStudentItm.setOnClickListener( new View.OnClickListener() {
                @Override
                public
                void onClick ( View view ) {

                    makePhoneCall();


                }
            } );
            textEmailStudentItm.setOnClickListener( new View.OnClickListener() {
                @Override
                public
                void onClick ( View view ) {

                    sendEmail();

                }
            } );


            imageView.setOnClickListener( new View.OnClickListener() {
                // DialogFragments dialogFragments = new DialogFragments();

                @Override
                public
                void onClick ( View view ) {
       /*             FragmentManager fragmentManager = ((AppCompatActivity) context)
                            .getSupportFragmentManager()
                            .getFragments().get(0).getChildFragmentManager();*/


                }
            } );

            itemView.setOnClickListener( new View.OnClickListener() {

                @Override
                public
                void onClick ( View view ) {
                    startActivityAdapter(getAdapterPosition());

                }
            } );
        }

        @SuppressLint ( "DefaultLocale" )
        void bind ( Student student ) {
            this.student = student;
            textNameStudentItm.setText( student.getName() );
            textEmailStudentItm.setText( student.getEmail() );
            textPhoneStudentItm.setText( student.getPhone() );
            imageView.setImageURI( student.getPicture() );
            //imageView.setImageURI(Uri.parse(course.getRes()));
        }

        private
        void makePhoneCall () {
            String number = textPhoneStudentItm.getText().toString();
            if ( number.trim().length() > 0 ) {
                if ( ContextCompat.checkSelfPermission( context ,
                        Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED ) {

                    ActivityCompat.requestPermissions( ( Activity ) context ,
                            new String[]{ Manifest.permission.CALL_PHONE } , REQUEST_CALL );

                }
                else {
                    String dial = "tel:" + number;

                    context.startActivity( new Intent( Intent.ACTION_CALL , Uri.parse( dial ) ) );
                }
            }
            else {
                Toast.makeText( context , "Student has not any number" , Toast.LENGTH_SHORT )
                        .show();
            }


        }

        private
        void sendEmail () {
            String sendEmail = textEmailStudentItm.getText().toString();
            if ( sendEmail.length() > 0 ) {

                {
                    if ( ContextCompat.checkSelfPermission( context ,
                            Manifest.permission.INTERNET ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( ( Activity ) context ,
                                new String[]{ Manifest.permission.INTERNET } , REQUEST_EMAIL );

                    }
                    else {
                        Intent intent = new Intent( Intent.ACTION_SEND );
                        intent.putExtra( Intent.EXTRA_EMAIL , sendEmail );
                        intent.setType( "message/rfc822" );
                        context.startActivity(
                                Intent.createChooser( intent , "choose an email client" ) );
                    }

                }

            }
            else {
                Toast.makeText( context , "Student's email does not right" , Toast.LENGTH_SHORT )
                        .show();
            }


        }

       /* @Override
        public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
            if (requestCode==REQUEST_CALL){
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    makePhoneCall();
                }else {
                    Toast.makeText(context,"permission Denied",Toast.LENGTH_SHORT).show();
                }

            }
        }*/
    }

    private
    void startActivityAdapter (int position) {
        // startActivity(new Intent(this, AdminStudentActivity.class));
        Intent intent = new Intent( context , AdminStudentActivity.class );
        Student student = students.get( position );
        ObjectMapper maper = new ObjectMapper(  );
        HashMap<String, String> map = maper.convertValue( student, HashMap.class );

        intent.putExtra( "map", map );
        context.startActivity( intent );
    }
}