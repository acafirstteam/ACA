package am.newway.aca;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Course;

public class AdminEditCourseActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private final String ADD = "add";
    private final String UPDATE = "update";
    private final int RESULT_LOAD_IMG = 5;

    private EditText editCourseName;
    private EditText editLecturer;
    private EditText editGroupNameEng;
    private EditText editGroupNameArm;
    private EditText editDescriptionEng;
    private EditText editDescriptionArm;
    private EditText editLink;
    private EditText editGroupType;
    private ImageView setImage;
    private Button saveButton;
    private Bundle bundle;
    private ArrayList<Course> courseItems;
    private int position;
    private Map<String, Object> groupName;
    private Map<String, Object> description;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_course_layout);

        editCourseName = (EditText) findViewById(R.id.admin_edit_courseName_id);
        editLecturer = (EditText) findViewById(R.id.admin_edit_lecturerEng_id);
        editGroupNameEng = (EditText) findViewById(R.id.admin_edit_groupNameEng_id);
        editGroupNameArm = (EditText) findViewById(R.id.admin_edit_groupNameArm_id);
        editDescriptionEng = (EditText) findViewById(R.id.admin_edit_DescriptionEng_id);
        editDescriptionArm = (EditText) findViewById(R.id.admin_edit_DescriptionArm_id);
        editLink = (EditText) findViewById(R.id.admin_edit_Link_id);
        editGroupType = (EditText) findViewById(R.id.admin_edit_groupType_id);
        saveButton = (Button) findViewById(R.id.admin_edit_Save_btn_id);
        setImage = (ImageView) findViewById(R.id.admin_edit_imageView_id);
        setImage.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        bundle = getIntent().getExtras();
        String action = bundle.getString("action");
        Log.d(TAG, "action: " + action);
        if (action.equals(ADD)) {
            gotActionAddCourse();
        } else {
            gotActionUpdateCourse();
        }


    }

    public void gotActionUpdateCourse() {
        position = bundle.getInt("pos", 0);

        FIRESTORE.getCuorces(new Firestore.OnCourseReadListener() {
            @Override
            public void OnCourseRead(List<Course> courses) {
                courseItems = new ArrayList<Course>(courses);
                Log.d(TAG, "-----------------------------Courses count = " + courseItems.size());

                editCourseName.setText(courseItems.get(position).getName());
                editLecturer.setText(courseItems.get(position).getLecturer());
                editGroupNameEng.setText(courseItems.get(position).getGroup_name().get("en").toString());
                editGroupNameArm.setText(courseItems.get(position).getGroup_name().get("hy").toString());
                editDescriptionEng.setText(courseItems.get(position).getDescription().get("en").toString());
                editDescriptionArm.setText(courseItems.get(position).getDescription().get("hy").toString());
                editLink.setText(courseItems.get(position).getLink());
                setImage.setImageURI(Uri.parse(courseItems.get(position).getUrl()));
            }
        });


    }

    public void gotActionAddCourse() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.admin_edit_Save_btn_id:

                groupName.put(editGroupNameEng.getText().toString(), "en");
                groupName.put(editGroupNameArm.getText().toString(), "hy");
                description.put(editDescriptionEng.getText().toString(), "en");
                description.put(editDescriptionArm.getText().toString(), "hy");

                String url = "url";
                Course course = new Course(
                        editCourseName.getText().toString(),
                        editLink.getText().toString(),
                        false,
                        Integer.parseInt(editGroupType.getText().toString()),
                        url
                );

                course.setGroup(Integer.parseInt(editGroupType.getText().toString()));
                course.setGroup_name(groupName);
                course.setDescription(description);
                course.setLecturer(editLecturer.getText().toString());

                break;

            case R.id.admin_edit_imageView_id:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                setImage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_SHORT).show();
        }
    }
}
