package com.example.assignment1;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.util.ArrayList;

public class DatabaseManager {
    public static final String DB_NAME = "StudentsManagement";
    public static final String DB_TABLE1 = "StudentInfo";
    public static final String DB_TABLE2 = "ToDoTasks";
    public static final String DB_TABLE3 = "ExamInfo";
    public static final int DB_VERSION = 1;
    private static final String CREATE_TABLE1 = "CREATE TABLE " + DB_TABLE1
            + " (StudentID INTEGER PRIMARY KEY, FirstName TEXT, LastName TEXT,"
            + "Gender TEXT, CourseStudy TEXT, Age INTEGER, Address TEXT, Picture BLOB);";
    private static final String CREATE_TABLE2 = "CREATE TABLE " + DB_TABLE2
            + " (TaskName TEXT PRIMARY KEY, Location TEXT, Status TEXT);";
    private static final String CREATE_TABLE3 = "CREATE TABLE " + DB_TABLE3
            + " (ExamName TEXT PRIMARY KEY, ExamDateTime TEXT, ExamLocation TEXT, StudentId INTEGER)";
    private SQLHelper helper;
    private SQLiteDatabase db;
    private Context context;


    public DatabaseManager(Context c) {
        this.context = c;
        helper = new SQLHelper(c);
        this.db = helper.getWritableDatabase();
    }

    public DatabaseManager openReadable() throws android.database.SQLException {
        helper = new SQLHelper(context);
        db = helper.getReadableDatabase();
        return this;
    }

    public void close() {
        helper.close();
    }

    public boolean addStudentRow(Integer id, String fn, String ln, String g,
                          String course, Integer age, String add, byte[] image) {
        synchronized(this.db) {
            ContentValues newStudent = new ContentValues();
            newStudent.put("StudentID", id);
            newStudent.put("FirstName", fn);
            newStudent.put("LastName", ln);
            newStudent.put("Gender", g);
            newStudent.put("CourseStudy", course);
            newStudent.put("Age", age);
            newStudent.put("Address", add);
            newStudent.put("Picture", image);
            try {
                db.insertOrThrow(DB_TABLE1, null, newStudent);
            } catch (Exception e) {
                Log.e("Error in inserting rows", e.toString());
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    public ArrayList<String> retrieveStudentRows() {
        ArrayList<String> studentRows = new ArrayList<String>();
        String[] columns = new String[] {"StudentID", "FirstName", "LastName", "Gender",
                                         "CourseStudy", "Age", "Address", "Picture"};
        Cursor cursor = db.query(DB_TABLE1, columns, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            studentRows.add(Integer.toString(cursor.getInt(0)) + ", "
                    + cursor.getString(1) + ", " + cursor.getString(2) + ", "
                    + cursor.getString(3) + ", " + cursor.getString(4) + ", "
                    + Integer.toString(cursor.getInt(5)) + ", " + cursor.getString(6) + ", "
                    + String.valueOf(cursor.getBlob(7)));
            cursor.moveToNext();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return studentRows;
    }

    public String retrieveAStudentRow(String whereClause) {
        String studentRow = "";
        String[] columns = new String[] {"StudentID", "FirstName", "LastName", "Gender",
                "CourseStudy", "Age", "Address"};
        Cursor cursor = db.query(DB_TABLE1, columns, "StudentID"
                        + "=" + whereClause,null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            studentRow = Integer.toString(cursor.getInt(0)) + ", "
                    + cursor.getString(1) + ", " + cursor.getString(2) + ", "
                    + cursor.getString(3) + ", " + cursor.getString(4) + ", "
                    + Integer.toString(cursor.getInt(5)) + ", " + cursor.getString(6);
            cursor.moveToNext();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return studentRow;
    }

    public byte[] retrieveAPictureStudentRow(String whereClause) {
        byte[] image = new byte[0];
        String[] columns = new String[] {"Picture"};
        Cursor cursor = db.query(DB_TABLE1, columns, "StudentID"
                + "=" + whereClause,null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            image = cursor.getBlob(0);
            cursor.moveToNext();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return image;
    }

    public boolean deleteStudentRow(int id) {
        db = helper.getWritableDatabase();

        return db.delete(DB_TABLE1, "StudentID"
                + "=" + id, null) > 0;
    }

    public boolean updateStudentRow(Integer id, String fn, String ln, String g,
                                    String course, Integer age, String add) {
        synchronized(this.db) {

            ContentValues newStudent = new ContentValues();
            newStudent.put("StudentID", id);
            newStudent.put("FirstName", fn);
            newStudent.put("LastName", ln);
            newStudent.put("Gender", g);
            newStudent.put("CourseStudy", course);
            newStudent.put("Age", age);
            newStudent.put("Address", add);
            try {
                db.update(DB_TABLE1, newStudent, "StudentID" + "=" + id, null);
            } catch (Exception e) {
                Log.e("Error in deleting rows", e.toString());
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    public boolean addTaskRow(String tn, String loc, String stt) {
        synchronized(this.db) {
            ContentValues newTask = new ContentValues();
            newTask.put("TaskName", tn);
            newTask.put("Location", loc);
            newTask.put("Status", stt);
            try {
                db.insertOrThrow(DB_TABLE2, null, newTask);
            } catch (Exception e) {
                Log.e("Error in inserting rows", e.toString());
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    public ArrayList<String> retrieveTaskRows() {
        ArrayList<String> taskRows = new ArrayList<String>();
        String[] columns = new String[] {"TaskName", "Location", "Status"};
        Cursor cursor = db.query(DB_TABLE2, columns, null, null, null, null, "Status");
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            taskRows.add(cursor.getString(0) + ", " + cursor.getString(1) + ", "
                    + cursor.getString(2));
            cursor.moveToNext();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return taskRows;
    }

    public String retrieveATaskRow(String whereClause) {
        String taskRow = "";
        String[] columns = new String[] {"TaskName", "Location", "Status"};
        Cursor cursor = db.query(DB_TABLE2, columns, "TaskName" + "= '"+whereClause.trim()+"'",
                null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            taskRow = cursor.getString(0) + ", " + cursor.getString(1) + ", "
                    + cursor.getString(2);
            cursor.moveToNext();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return taskRow;
    }

    public boolean updateTaskRow(String tn, String loc, String stt) {
        synchronized(this.db) {
            ContentValues newTask = new ContentValues();
            newTask.put("TaskName", tn);
            newTask.put("Location", loc);
            newTask.put("Status", stt);
            try {
                db.update(DB_TABLE2, newTask, "TaskName" + "= '"+tn.trim()+"'", null);
            } catch (Exception e) {
                Log.e("Error in deleting rows", e.toString());
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    public boolean deleteTaskRow(String tn) {
        db = helper.getWritableDatabase();

        return db.delete(DB_TABLE2, "TaskName" + "= '"+tn.trim()+"'", null) > 0;
    }

    public boolean addExamRow(String en, String dt, String loc, Integer stuId) {
        synchronized(this.db) {
            ContentValues newExam = new ContentValues();
            newExam.put("ExamName", en);
            newExam.put("ExamDateTime", dt);
            newExam.put("ExamLocation", loc);
            newExam.put("StudentId", stuId);
            try {
                db.insertOrThrow(DB_TABLE3, null, newExam);
            } catch (Exception e) {
                Log.e("Error in inserting rows", e.toString());
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    public ArrayList<String> retrieveExamRows(String whereClause) {
        ArrayList<String> ExamRows = new ArrayList<String>();
        String[] columns = new String[] {"ExamName", "ExamDateTime", "ExamLocation"};
        Cursor cursor = db.query(DB_TABLE3, columns, "StudentId"
                + "=" + whereClause, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            ExamRows.add(cursor.getString(0) + ", " + cursor.getString(1) + ", "
                    + cursor.getString(2));
            cursor.moveToNext();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return ExamRows;
    }

    public boolean deleteExamRow(String en) {
        db = helper.getWritableDatabase();

        return db.delete(DB_TABLE3, "ExamName" + "= '"+en.trim()+"'", null) > 0;
    }

    public void clearTable() {
        db = helper.getWritableDatabase();
        db.delete(DB_TABLE1, null, null);
        db.delete(DB_TABLE2, null, null);
        db.delete(DB_TABLE3, null, null);
    }

    public class SQLHelper extends SQLiteOpenHelper {
        public SQLHelper (Context c) {
            super(c, DB_NAME, null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE1);
            db.execSQL(CREATE_TABLE2);
            db.execSQL(CREATE_TABLE3);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Student records table", "Upgrading database i.e. dropping table and recreating it");
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE1);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE2);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE3);
            onCreate(db);
        }
    }
}
