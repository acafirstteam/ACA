package am.newway.aca;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import am.newway.aca.adapter.CustomSpinnerAdapter;
import am.newway.aca.template.CustomItemSpinner;

public class AdminStudentStatus extends AppCompatActivity implements AdapterView.OnItemClickListener {
Spinner customspinner , customSpinner2;
Button buttonSave;
ArrayList<CustomItemSpinner> customList , customListTwo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_status);
        //Spinners
        customspinner=findViewById(R.id.spinner1);
        customSpinner2 = findViewById(R.id.spinner2);

        buttonSave=findViewById(R.id.btndoneForstatus);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                finish();
            }
        });
        customList=getCustomList();
        customListTwo= getCustomListTwo();

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this,customList);
        CustomSpinnerAdapter adapter2 = new CustomSpinnerAdapter(this,customListTwo);

        if (customspinner!=null) {
            customspinner.setAdapter(adapter);
            customspinner.setOnItemClickListener(this);
        }
        if (customSpinner2!=null) {
            customSpinner2.setAdapter(adapter2);
            customSpinner2.setOnItemClickListener(this);
        }

    }

    private ArrayList<CustomItemSpinner> getCustomList() {
        customList= new ArrayList<>();
        //Spiner for lvl
        customList.add(new CustomItemSpinner("LVL zero junior",R.drawable.ic_school_black_24dp));
        customList.add(new CustomItemSpinner("LVL zero junior",R.drawable.ic_school_green_24dp));
        customList.add(new CustomItemSpinner("LVL one middle",R.drawable.ic_school_middle_24dp));
        customList.add(new CustomItemSpinner("LVL two senior",R.drawable.ic_school_senior_24dp));
        customList.add(new CustomItemSpinner("LVL three master",R.drawable.ic_school_master_24dp));


        return customList;
    }

    private ArrayList<CustomItemSpinner> getCustomListTwo() {
        customListTwo= new ArrayList<>();
        customListTwo.add(new CustomItemSpinner("Introduction to JS",R.drawable.ic_js));
        customListTwo.add(new CustomItemSpinner("Introduction to Java",R.drawable.ic_java));
        customListTwo.add(new CustomItemSpinner("Fundamentals ios",R.drawable.ic_apple));
        customListTwo.add(new CustomItemSpinner("Fundamentals QA",R.drawable.ic_qa_));
        customListTwo.add(new CustomItemSpinner("Fundamentals UI/UX",R.drawable.ic_ux));
        customListTwo.add(new CustomItemSpinner("IT Project Man.",R.drawable.ic_project));
        customListTwo.add(new CustomItemSpinner("Introduction to Python",R.drawable.ic_python));
        customListTwo.add(new CustomItemSpinner("Introduction to Python",R.drawable.ic_c));


        return customListTwo;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CustomItemSpinner itemSpinner =(CustomItemSpinner) adapterView.getSelectedItem();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
