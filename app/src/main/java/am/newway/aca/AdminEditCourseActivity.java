package am.newway.aca;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Course;
import am.newway.aca.ui.admin.AdminCourseActivity;

public class AdminEditCourseActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private final String ADD = "add";
    private final String UPDATE = "update";
    private final int RESULT_LOAD_IMG = 5;
    private final int MY_PERMISSION_REQUEST = 1;

    private EditText editCourseName;
    private EditText editLecturer;
    private EditText editGroupNameEng;
    private EditText editGroupNameArm;
    private EditText editDescriptionEng;
    private EditText editDescriptionArm;
    private EditText editLink;
    private ImageView setImage;
    private RadioGroup groupTypeRadioGroup;
    private RadioButton typeBeginner, typeSpecialized, typeAdvanced;
    private Button saveButton;
    private Bundle bundle;
    private ArrayList<Course> courseItems;
    private int position;
    private Map<String, Object> groupName;
    private Map<String, Object> description;
    private String action = null;
    private String imageURI;
    private Uri imagePath;
    private String url;
    private boolean imagePicked = false;
    private int radioState;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_course_layout);
        initNavigationBar(2);


        editCourseName = (EditText) findViewById(R.id.admin_edit_courseName_id);
        editLecturer = (EditText) findViewById(R.id.admin_edit_lecturerEng_id);
        editGroupNameEng = (EditText) findViewById(R.id.admin_edit_groupNameEng_id);
        editGroupNameArm = (EditText) findViewById(R.id.admin_edit_groupNameArm_id);
        editDescriptionEng = (EditText) findViewById(R.id.admin_edit_DescriptionEng_id);
        editDescriptionArm = (EditText) findViewById(R.id.admin_edit_DescriptionArm_id);
        editLink = (EditText) findViewById(R.id.admin_edit_Link_id);
        groupTypeRadioGroup = (RadioGroup) findViewById(R.id.admin_edit_radioGroup_id);
        typeBeginner = (RadioButton) findViewById(R.id.admin_edit_radio_0);
        typeSpecialized = (RadioButton) findViewById(R.id.admin_edit_radio_1);
        typeAdvanced = (RadioButton) findViewById(R.id.admin_edit_radio_2);
        saveButton = (Button) findViewById(R.id.admin_edit_Save_btn_id);
        setImage = (ImageView) findViewById(R.id.admin_edit_imageView_id);
        setImage.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        groupTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.admin_edit_radio_0:
                        radioState = 0;
                        break;
                    case R.id.admin_edit_radio_1:
                        radioState = 1;
                        break;
                    case R.id.admin_edit_radio_2:
                        radioState = 2;
                        break;
                }
            }
        });


//GET ACTION AND POSITION FROM BUNDLE
        bundle = getIntent().getExtras();
        action = bundle.getString("action");
        Log.d(TAG, "action: " + action);

        switch (action) {
            case ADD:
                setTitle(R.string.add_course);
                gotActionAddCourse();
                break;
            case UPDATE:
                setTitle(R.string.edit_course);
                gotActionUpdateCourse();
                break;
        }


    }
//Action Update Course
    public void gotActionUpdateCourse() {
        position = bundle.getInt("pos", 0);

        FIRESTORE.getCourses(new Firestore.OnCourseReadListener() {
            @Override
            public void OnCourseRead(List<Course> courses) {
                courseItems = new ArrayList<Course>(courses);
                Log.d(TAG, "-----------------------------Courses count = " + courseItems.size());

                if (courseItems.get(position).getUrl().toString().trim().isEmpty()){
                    setImage.setImageResource(R.drawable.ic_add_image_view);
                }else{
                    setImage.setImageURI(Uri.parse(courseItems.get(position).getUrl()));
                }

                editCourseName.setText(courseItems.get(position).getName());
                editCourseName.setEnabled(false);
                editLecturer.setText(courseItems.get(position).getLecturer());
                editGroupNameEng.setText(courseItems.get(position).getGroup_name().get("en").toString());
                editGroupNameArm.setText(courseItems.get(position).getGroup_name().get("hy").toString());
                switch (courseItems.get(position).getGroup()){
                    case 0:
                        typeBeginner.setChecked(true);
                        radioState = 0;
                        break;
                    case 1:
                        typeSpecialized.setChecked(true);
                        radioState = 1;
                        break;
                    case 2:
                        typeAdvanced.setChecked(true);
                        radioState = 2;
                        break;
                }
                editDescriptionEng.setText(courseItems.get(position).getDescription().get("en").toString());
                editDescriptionArm.setText(courseItems.get(position).getDescription().get("hy").toString());
                editLink.setText(courseItems.get(position).getLink());

            }
        });


    }
//Action Add Course
    public void gotActionAddCourse() {
        FIRESTORE.getCourses(new Firestore.OnCourseReadListener() {
            @Override
            public void OnCourseRead(List<Course> courses) {
                setImage.setImageResource(R.drawable.ic_add_image_view);
                courseItems = new ArrayList<>(courses);
            }
        });
    }

//On Click
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

//Save Button clicked
            case R.id.admin_edit_Save_btn_id:

                if (editCourseName.getText().toString().trim().isEmpty() ||
                        groupTypeRadioGroup.getCheckedRadioButtonId() == -1 ||
                        editGroupNameEng.getText().toString().trim().isEmpty() ||
                        editGroupNameArm.getText().toString().trim().isEmpty() ||
                        editDescriptionEng.getText().toString().trim().isEmpty() ||
                        editDescriptionArm.getText().toString().trim().isEmpty() ||
                        editLink.getText().toString().trim().isEmpty()
                ) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();

                } else {
                    switch (action) {
    //CASE ADD
                        case ADD:
                            if (imagePicked == false){   //IMAGE NOT PICKED
                                Toast.makeText(getApplicationContext(), "Please Pick Course Image",Toast.LENGTH_SHORT).show();
                            }else {                      //IMAGE PICKED
                                Log.d(TAG, "---------------------- Case ADD");
                                FIRESTORE.uploadImage(imagePath, editCourseName.getText().toString(), new Firestore.OnImageUploadListener() {
                                    @Override
                                    public void OnImageUploaded(String uri) {
                                        imageURI = uri;
                                        ArrayList<Course> courses = new ArrayList<>();
                                        courses.add(createCourse());
                                        FIRESTORE.addCourses(courses);
                                        Log.d(TAG, "----------------------URI: " + imageURI);
                                        Toast.makeText(getApplicationContext(), "New Course Added", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void OnImageUploadFailed(String error) {
                                        Toast.makeText(getApplicationContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Intent intent = new Intent(this, AdminCourseActivity.class );
                                startActivity(intent);
                            }
    //CASE UPDATE
                            break;
                        case UPDATE:
                            Log.d(TAG, "---------------------- Case UPDATE");
                    //IMAGE PICKED
                            if (imagePicked == true){
                                FIRESTORE.uploadImage(imagePath, editCourseName.getText().toString(), new Firestore.OnImageUploadListener() {
                                    @Override
                                    public void OnImageUploaded(String uri) {
                                        imageURI = uri;
                                        FIRESTORE.updateCourse(createCourse(), new Firestore.OnCourseUpdateListener() {
                                            @Override
                                            public void OnCourseUpdated() {
                                                Toast.makeText(getApplicationContext(),"Course Updated",Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void OnCourseUpdateFailed() {
                                                Toast.makeText(getApplicationContext(),"Course update failed",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void OnImageUploadFailed(String error) {

                                    }
                                });
                            }else{
                    //IMAGE NOT PICKED
                                FIRESTORE.updateCourse(createCourse(), new Firestore.OnCourseUpdateListener() {
                                    @Override
                                    public void OnCourseUpdated() {
                                        Toast.makeText(getApplicationContext(),"Course Updated",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void OnCourseUpdateFailed() {
                                        Toast.makeText(getApplicationContext(),"Course update failed",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            Intent intent = new Intent(this, AdminCourseActivity.class );
                            startActivity(intent);
                            break;
                    }

                }
                break;

//ImageView Clicked
            case R.id.admin_edit_imageView_id:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSION_REQUEST);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                }
                break;

        }
    }
//Activity Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                setImage.setImageBitmap(selectedImage);
                imagePath = imageUri;
                imagePicked = true;
                Log.d(TAG, "------------------------------Image picked");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_SHORT).show();
        }
    }

//Create Course object
    public Course createCourse() {

        groupName = new HashMap<>();
        description = new HashMap<>();
        groupName.put("en", editGroupNameEng.getText().toString());
        groupName.put("hy", editGroupNameArm.getText().toString());
        description.put("en", editDescriptionEng.getText().toString());
        description.put( "hy", editDescriptionArm.getText().toString());

        if (imagePicked == true){
            url = imageURI;
        }else{
            url = courseItems.get(position).getUrl();
        }

        Course newCourse = new Course(
                editCourseName.getText().toString(),
                editLink.getText().toString(),
                false,
                radioState,
                url
        );

        newCourse.setGroup_name(groupName);
        newCourse.setDescription(description);
        newCourse.setLecturer(editLecturer.getText().toString());

        return newCourse;

    }
//Nav Button
    @Override
    public
    boolean onOptionsItemSelected ( @NonNull final MenuItem item ) {
        if ( item.getItemId() == android.R.id.home ) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

}
