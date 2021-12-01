package com.example.assignment1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.assignment1.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private DatabaseManager mydManager;
    private TextView title;
    private EditText studentId, firstName, lastName, course, age, address, taskName, location,
                    examName, examLocation;
    private ImageView logo;
    private Button addStudent, addStudentRecord, cancelStudentRecord, editStudent, deleteStudentRecord,
            addTask, editTask, addTaskRecord, cancelTaskRecord, deleteTaskRecord,
            picture, showAddress, showExam, addExam, deleteExam, cancelExam, addExamRecord, cancelExamRecord;
    private TableLayout addStudentTable, addTaskTable, addExamTable;
    private ListView studentRec, taskRec, examRec;
    CustomAdapter adapter;
    private boolean studentrecordInserted, studentrecordDeleted;
    private RadioGroup group_gender, group_status;
    private RadioButton selected_gender, selected_status;
    private ImageView profilePicture;
    private byte[] img;
    private Bitmap b;
    private CalendarView calendar;
    private TimePicker timePicker;
    private String examDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mydManager = new DatabaseManager(MainActivity.this);
        title = (TextView) findViewById(R.id.textView_title);
        logo = (ImageView) findViewById(R.id.imageView_logo);
        addStudent = (Button) findViewById(R.id.button_addstudent);
        addStudentTable = (TableLayout) findViewById(R.id.studentrecords_table);
        studentRec = (ListView) findViewById(R.id.studrec);
        editStudent = (Button) findViewById(R.id.button_editstudent);

        addTaskTable = (TableLayout) findViewById(R.id.taskrecords_table);
        taskRec = (ListView) findViewById(R.id.taskrec);
        addTask = (Button) findViewById(R.id.button_addtask);
        editTask = (Button) findViewById(R.id.button_edittask);

        profilePicture = (ImageView) findViewById(R.id.myimage);

        examRec = (ListView) findViewById(R.id.examrec);
        addExam = (Button) findViewById(R.id.button_addexam);
        deleteExam = (Button) findViewById(R.id.button_deleteexam);
        cancelExam = (Button) findViewById(R.id.button_cancelexam);
        addExamTable = (TableLayout) findViewById(R.id.examrecords_table);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_home:
                home();
                break;
            case R.id.action_studentrecords:
                studentRecords();
                break;
            case R.id.action_todotasks:
                todoTasks();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void home() {
        title.setText("Welcome to Student Management App");
        logo.setVisibility(View.VISIBLE);
        addStudentTable.setVisibility(View.GONE);
        addStudent.setVisibility(View.GONE);
        studentRec.setVisibility(View.GONE);
        editStudent.setVisibility(View.GONE);

        addTaskTable.setVisibility(View.GONE);
        addTask.setVisibility(View.GONE);
        editTask.setVisibility(View.GONE);
        taskRec.setVisibility(View.GONE);

        examRec.setVisibility(View.GONE);
        addExamTable.setVisibility(View.GONE);
        addExam.setVisibility(View.GONE);
        deleteExam.setVisibility(View.GONE);
        cancelExam.setVisibility(View.GONE);
    }

    public void studentRecords() {
        title.setText("All Students Records");
        logo.setVisibility(View.GONE);
        addStudent.setVisibility(View.VISIBLE);
        studentRec.setVisibility(View.VISIBLE);
        addStudentTable.setVisibility(View.GONE);
        editStudent.setVisibility(View.VISIBLE);
        addTaskTable.setVisibility(View.GONE);
        addTask.setVisibility(View.GONE);
        editTask.setVisibility(View.GONE);
        examRec.setVisibility(View.GONE);
        addExam.setVisibility(View.GONE);
        deleteExam.setVisibility(View.GONE);
        cancelExam.setVisibility(View.GONE);
        addExamTable.setVisibility(View.GONE);
        viewStudentRec();

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudentTable.setVisibility(View.VISIBLE);
                addStudent.setVisibility(View.GONE);
                studentRec.setVisibility(View.GONE);
                editStudent.setVisibility(View.GONE);
                addTaskTable.setVisibility(View.GONE);
                taskRec.setVisibility(View.GONE);
                addTask.setVisibility(View.GONE);
                editTask.setVisibility(View.GONE);
                title.setText("Add a student to the list");

                addStudentRecord = (Button) findViewById(R.id.add_studentrecord);
                addStudentRecord.setText("Add student");
                addStudentRecord.setOnClickListener(addStudentRec);

                cancelStudentRecord = (Button) findViewById(R.id.cancel_studentrecord);
                cancelStudentRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        studentRecords();
                        clearStudentTable();
                        studentRec.setAdapter(adapter);
                    }
                });

                deleteStudentRecord = (Button) findViewById(R.id.delete_studentrecord);
                deleteStudentRecord.setVisibility(View.GONE);

                picture = (Button) findViewById(R.id.changepicture);
                picture.setText("Add image");
                picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                });

                showAddress = (Button) findViewById(R.id.showaddress);
                showAddress.setVisibility(View.GONE);

                showExam = (Button) findViewById(R.id.showexam);
                showExam.setVisibility(View.GONE);
            }
        });

        editStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudentTable.setVisibility(View.VISIBLE);
                addStudent.setVisibility(View.GONE);
                studentRec.setVisibility(View.GONE);
                editStudent.setVisibility(View.GONE);
                title.setText("Edit a student record");

                boolean[] checkboxes = adapter.getCheckBoxState();
                String studentRow = "";

                for (int i = 0; i < studentRec.getCount(); i++) {
                    if (checkboxes[i] == true) {
                        studentRow = studentRow + adapter.getName(i) + " ";
                        break;
                    }
                }

                mydManager.openReadable();
                img = mydManager.retrieveAPictureStudentRow(studentRow);
                b=BitmapFactory.decodeByteArray(img, 0, img.length);
                profilePicture.setImageBitmap(b);

                studentRow = mydManager.retrieveAStudentRow(studentRow);

                String[] studentRowElement = studentRow.split(", ");

                assignStudentElement();
                profilePicture = (ImageView) findViewById(R.id.myimage);

                studentId.setText(studentRowElement[0]);
                firstName.setText(studentRowElement[1]);
                lastName.setText(studentRowElement[2]);
                course.setText(studentRowElement[3]);
                if (studentRowElement[4].equals("Male")){
                    group_gender.check(R.id.gender_male);
                } else if (studentRowElement[4].equals("Female")){
                    group_gender.check(R.id.gender_female);
                } else {
                    group_gender.check(R.id.gender_others);
                }
                age.setText(studentRowElement[5]);
                address.setText(studentRowElement[6]);

                addStudentRecord = (Button) findViewById(R.id.add_studentrecord);
                addStudentRecord.setText("Edit student");
                addStudentRecord.setOnClickListener(editStudentRec);

                cancelStudentRecord = (Button) findViewById(R.id.cancel_studentrecord);
                cancelStudentRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        studentRecords();
                        clearStudentTable();
                        studentRec.setAdapter(adapter);
                    }
                });

                deleteStudentRecord = (Button) findViewById(R.id.delete_studentrecord);
                deleteStudentRecord.setVisibility(View.VISIBLE);
                deleteStudentRecord.setOnClickListener(deleteStudentRec);

                picture = (Button) findViewById(R.id.changepicture);
                picture.setText("Change image");
                picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                        startActivityForResult(Intent.createChooser(intent, "Select picture"),1);
                    }
                });

                showAddress = (Button) findViewById(R.id.showaddress);
                showAddress.setVisibility(View.VISIBLE);
                showAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        intent.putExtra("Student address", address.getText().toString());
                        startActivity(intent);
                    }
                });

                showExam = (Button) findViewById(R.id.showexam);
                showExam.setVisibility(View.VISIBLE);
                showExam.setOnClickListener(viewExam);
            }
        });
    }

    public boolean viewStudentRec() {
        addStudentTable.setVisibility(View.GONE);
        studentRec.setVisibility(View.VISIBLE);
        addTaskTable.setVisibility(View.GONE);
        taskRec.setVisibility(View.GONE);
        examRec.setVisibility(View.GONE);
        addExam.setVisibility(View.GONE);
        deleteExam.setVisibility(View.GONE);
        cancelExam.setVisibility(View.GONE);
        addExamTable.setVisibility(View.GONE);

        mydManager.openReadable();
        ArrayList<String> tableContent = mydManager.retrieveStudentRows();
        adapter = new CustomAdapter(this, tableContent);
        studentRec = (ListView) findViewById(R.id.studrec);
        studentRec.setAdapter(adapter);

        mydManager.close();

        return true;
    }

    public View.OnClickListener addStudentRec = new View.OnClickListener() {
        public void onClick(View v) {
            mydManager.openReadable();
            assignStudentElement();

            studentrecordInserted = mydManager.addStudentRow(Integer.parseInt(studentId.getText().toString()),
                    firstName.getText().toString(), lastName.getText().toString(),
                    course.getText().toString(), selected_gender.getText().toString(),
                    Integer.parseInt(age.getText().toString()), address.getText().toString(), img);

            dispatchTakePictureIntent();

            addStudent.setVisibility(View.GONE);
            if (studentrecordInserted) {
                Toast.makeText(MainActivity.this, "The row in the student records table is inserted", Toast.LENGTH_SHORT).show();
                studentRecords();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(address.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                mydManager.close();
                clearStudentTable();
                studentRec.setAdapter(adapter);
            } else {
                Toast.makeText(MainActivity.this, "Sorry, errors when inserting to DB. \n Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public View.OnClickListener editStudentRec = new View.OnClickListener() {
        public void onClick(View v) {
            mydManager.openReadable();
            assignStudentElement();

            studentrecordInserted = mydManager.updateStudentRow(Integer.parseInt(studentId.getText().toString()),
                    firstName.getText().toString(), lastName.getText().toString(),
                    course.getText().toString(), selected_gender.getText().toString(),
                    Integer.parseInt(age.getText().toString()), address.getText().toString());

            addStudent.setVisibility(View.GONE);
            if (studentrecordInserted) {
                Toast.makeText(MainActivity.this, "The row in the student records table is editted", Toast.LENGTH_SHORT).show();
                studentRecords();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(address.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                mydManager.close();
                clearStudentTable();
                studentRec.setAdapter(adapter);
            } else {
                Toast.makeText(MainActivity.this, "Sorry, errors when editting this student. \n Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public View.OnClickListener deleteStudentRec = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mydManager.openReadable();
            studentrecordDeleted = mydManager.deleteStudentRow(Integer.parseInt(studentId.getText().toString()));

            if (studentrecordDeleted) {
                Toast.makeText(MainActivity.this, "The row in the student records table is deleted", Toast.LENGTH_SHORT).show();
                studentRecords();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(address.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                mydManager.close();
                clearStudentTable();
                studentRec.setAdapter(adapter);
            } else {
                Toast.makeText(MainActivity.this, "Sorry, errors when deleting this student. \n Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void clearStudentTable() {
        studentId.setText("");
        firstName.setText("");
        lastName.setText("");
        course.setText("");
        group_gender.clearCheck();
        age.setText("");
        address.setText("");
        profilePicture.setImageDrawable(null);
    }

    public void assignStudentElement() {
        studentId = (EditText) findViewById(R.id.studentId);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        course = (EditText) findViewById(R.id.course);
        group_gender = (RadioGroup) findViewById(R.id.radio_gender);
        int selected = group_gender.getCheckedRadioButtonId();
        selected_gender = (RadioButton) findViewById(selected);
        age = (EditText) findViewById(R.id.age);
        address = (EditText) findViewById(R.id.address);
    }

    public void todoTasks() {
        title.setText("All To-do Tasks Records");
        logo.setVisibility(View.GONE);
        addStudentTable.setVisibility(View.GONE);
        addStudent.setVisibility(View.GONE);
        studentRec.setVisibility(View.GONE);
        editStudent.setVisibility(View.GONE);

        addTaskTable.setVisibility(View.GONE);
        addTask.setVisibility(View.VISIBLE);
        editTask.setVisibility(View.VISIBLE);

        examRec.setVisibility(View.GONE);
        addExam.setVisibility(View.GONE);
        deleteExam.setVisibility(View.GONE);
        cancelExam.setVisibility(View.GONE);
        addExamTable.setVisibility(View.GONE);
        viewTaskRec();

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudentTable.setVisibility(View.GONE);
                addStudent.setVisibility(View.GONE);
                studentRec.setVisibility(View.GONE);
                editStudent.setVisibility(View.GONE);
                addTaskTable.setVisibility(View.VISIBLE);
                taskRec.setVisibility(View.GONE);
                addTask.setVisibility(View.GONE);
                editTask.setVisibility(View.GONE);
                title.setText("Add a task to the list");

                addTaskRecord = (Button) findViewById(R.id.add_taskrecord);
                addTaskRecord.setOnClickListener(addTaskRec);

                cancelTaskRecord = (Button) findViewById(R.id.cancel_taskrecord);
                cancelTaskRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        todoTasks();
                        clearTaskTable();
                        taskRec.setAdapter(adapter);
                    }
                });

                deleteTaskRecord = (Button) findViewById(R.id.delete_taskrecord);
                deleteTaskRecord.setVisibility(View.GONE);
            }
        });

        editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskTable.setVisibility(View.VISIBLE);
                addTask.setVisibility(View.GONE);
                taskRec.setVisibility(View.GONE);
                editTask.setVisibility(View.GONE);
                title.setText("Edit a task record");

                boolean[] checkboxes = adapter.getCheckBoxState();
                String taskRow = "";

                for (int i = 0; i < taskRec.getCount(); i++) {
                    if (checkboxes[i] == true) {
                        taskRow = taskRow + adapter.getName(i) + " ";
                        break;
                    }
                }

                mydManager.openReadable();
                taskRow = mydManager.retrieveATaskRow(taskRow);

                String[] taskRowElement = taskRow.split(", ");

                assignTaskElement();

                taskName.setText(taskRowElement[0]);
                location.setText(taskRowElement[1]);
                if (taskRowElement[2].equals("Completed")){
                    group_status.check(R.id.status_completed);
                } else if (taskRowElement[2].equals("Not Completed")){
                    group_status.check(R.id.status_notcompleted);
                }

                addTaskRecord = (Button) findViewById(R.id.add_taskrecord);
                addTaskRecord.setText("Edit task");
                addTaskRecord.setOnClickListener(editTaskRec);

                cancelTaskRecord = (Button) findViewById(R.id.cancel_taskrecord);
                cancelTaskRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        todoTasks();
                        clearTaskTable();
                        taskRec.setAdapter(adapter);
                    }
                });

                deleteTaskRecord = (Button) findViewById(R.id.delete_taskrecord);
                deleteTaskRecord.setVisibility(View.VISIBLE);
                deleteTaskRecord.setOnClickListener(deleteTaskRec);
            }
        });
    }

    public boolean viewTaskRec() {
        addStudentTable.setVisibility(View.GONE);
        addTaskTable.setVisibility(View.GONE);
        studentRec.setVisibility(View.GONE);
        taskRec.setVisibility(View.VISIBLE);
        examRec.setVisibility(View.GONE);
        addExam.setVisibility(View.GONE);
        deleteExam.setVisibility(View.GONE);
        cancelExam.setVisibility(View.GONE);
        addExamTable.setVisibility(View.GONE);

        mydManager.openReadable();
        ArrayList<String> tableContent = mydManager.retrieveTaskRows();
        adapter = new CustomAdapter(this, tableContent);
        taskRec.setAdapter(adapter);

        mydManager.close();

        return true;
    }

    public View.OnClickListener addTaskRec = new View.OnClickListener() {
        public void onClick(View v) {
            mydManager.openReadable();
            assignTaskElement();

            studentrecordInserted = mydManager.addTaskRow(taskName.getText().toString(), location.getText().toString(),
                      selected_status.getText().toString());

            addStudent.setVisibility(View.GONE);

            if (studentrecordInserted) {
                Toast.makeText(MainActivity.this, "The row in the to-do tasks records table is inserted", Toast.LENGTH_SHORT).show();
                todoTasks();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(location.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                mydManager.close();
                clearTaskTable();
                taskRec.setAdapter(adapter);
            } else {
                Toast.makeText(MainActivity.this, "Sorry, errors when inserting to DB. \n Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public View.OnClickListener editTaskRec = new View.OnClickListener() {
        public void onClick(View v) {
            mydManager.openReadable();
            assignTaskElement();

            studentrecordInserted = mydManager.updateTaskRow(taskName.getText().toString(), location.getText().toString(),
                    selected_status.getText().toString());

            addTask.setVisibility(View.GONE);
            //deleteButton.setVisibility(View.GONE);
            if (studentrecordInserted) {
                Toast.makeText(MainActivity.this, "The row in the to-do tasks records table is editted", Toast.LENGTH_SHORT).show();
                todoTasks();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(location.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                mydManager.close();
                clearTaskTable();
                taskRec.setAdapter(adapter);
            } else {
                Toast.makeText(MainActivity.this, "Sorry, errors when editting this student. \n Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public View.OnClickListener deleteTaskRec = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mydManager.openReadable();
            studentrecordDeleted = mydManager.deleteTaskRow(taskName.getText().toString());

            if (studentrecordDeleted) {
                Toast.makeText(MainActivity.this, "The row in the to-do tasks records table is deleted", Toast.LENGTH_SHORT).show();
                todoTasks();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(location.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                mydManager.close();
                clearTaskTable();
                taskRec.setAdapter(adapter);
            } else {
                Toast.makeText(MainActivity.this, "Sorry, errors when deleting this student. \n Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void clearTaskTable() {
        taskName.setText("");
        location.setText("");
        group_status.check(R.id.status_notcompleted);
    }

    public void assignTaskElement() {
        taskName = (EditText) findViewById(R.id.taskName);
        location = (EditText) findViewById(R.id.location);
        group_status = (RadioGroup) findViewById(R.id.radio_status);
        int selected = group_status.getCheckedRadioButtonId();
        selected_status = (RadioButton) findViewById(selected);
    }

    public View.OnClickListener viewExam = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewExamRec();

            addExam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    examRec.setVisibility(View.GONE);
                    addExam.setVisibility(View.GONE);
                    deleteExam.setVisibility(View.GONE);
                    cancelExam.setVisibility(View.GONE);
                    title.setText("");
                    addExamTable.setVisibility(View.VISIBLE);

                    addExamRecord = (Button) findViewById(R.id.add_examrecord);
                    addExamRecord.setOnClickListener(addExamRec);

                    cancelExamRecord = (Button) findViewById(R.id.cancel_examrecord);
                    cancelExamRecord.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewExamRec();
                        }
                    });
                }
            });

            cancelExam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    studentRecords();
                    clearStudentTable();
                }
            });

            deleteExam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean[] checkboxes = adapter.getCheckBoxState();
                    String examRow = "";

                    for (int i = 0; i < examRec.getCount(); i++) {
                        if (checkboxes[i] == true) {
                            examRow = examRow + adapter.getName(i) + " ";
                        }
                    }

                    mydManager.openReadable();

                    String[] examRow1 = examRow.split(" ");

                    for (int i = 0; i < examRow1.length; i++){
                        studentrecordDeleted = mydManager.deleteExamRow(examRow1[i]);
                    }

                    if (studentrecordDeleted) {
                        Toast.makeText(MainActivity.this, "The row in the exam records table is deleted", Toast.LENGTH_SHORT).show();
                        studentRecords();

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(examLocation.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        mydManager.close();
                        examName.setText("");
                        examLocation.setText("");
                        examRec.setAdapter(null);
                    } else {
                        Toast.makeText(MainActivity.this, "Sorry, errors when deleting this student. \n Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };

    public boolean viewExamRec() {
        addStudentTable.setVisibility(View.GONE);
        studentRec.setVisibility(View.GONE);
        addTaskTable.setVisibility(View.GONE);
        taskRec.setVisibility(View.GONE);
        examRec.setVisibility(View.VISIBLE);
        addExam.setVisibility(View.VISIBLE);
        deleteExam.setVisibility(View.VISIBLE);
        cancelExam.setVisibility(View.VISIBLE);
        title.setText("All exam associated with student " + studentId.getText().toString());
        addExamTable.setVisibility(View.GONE);

        mydManager.openReadable();
        ArrayList<String> tableContent = mydManager.retrieveExamRows(studentId.getText().toString());
        adapter = new CustomAdapter(this, tableContent);
        examRec = (ListView) findViewById(R.id.examrec);
        examRec.setAdapter(adapter);

        mydManager.close();
        return true;
    }

    public View.OnClickListener addExamRec = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mydManager.openReadable();
            examName = (EditText) findViewById(R.id.examName);
            examLocation = (EditText) findViewById(R.id.examLocation);
            calendar = (CalendarView) findViewById(R.id.calendarView);
            calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                    examDateTime = String.valueOf(dayOfMonth)+ "/" +String.valueOf(month + 1)+ "/" + String.valueOf(year);
                }
            });
            timePicker = (TimePicker) findViewById(R.id.examTime);
            examDateTime = examDateTime + " " + String.valueOf(timePicker.getHour()) + ":" + String.valueOf(timePicker.getMinute());

            studentrecordInserted = mydManager.addExamRow(examName.getText().toString(), examDateTime,
                    examLocation.getText().toString(), Integer.parseInt(studentId.getText().toString()));

            addStudent.setVisibility(View.GONE);
            if (studentrecordInserted) {
                Toast.makeText(MainActivity.this, "The row in the exam records table is inserted", Toast.LENGTH_SHORT).show();
                viewExamRec();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(studentId.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                mydManager.close();
                examName.setText("");
                examLocation.setText("");
                examRec.setAdapter(adapter);
            } else {
                Toast.makeText(MainActivity.this, "Sorry, errors when inserting to DB. \n Please try again.", Toast.LENGTH_SHORT).show();
            }

        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap b = (Bitmap) extras.get("data");
            profilePicture.setImageBitmap(b);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, bos);
            img = bos.toByteArray();
        }
        else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                try {
                    b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    img = bos.toByteArray();
                    profilePicture.setImageBitmap(b);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            // Error occurred while creating the File
                //Toast.makeText(MainActivity.this, "Error when creating the file", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                galleryAddPic();
            }
        }
    }

}