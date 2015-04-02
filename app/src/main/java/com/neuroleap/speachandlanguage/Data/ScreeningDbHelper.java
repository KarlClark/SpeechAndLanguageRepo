package com.neuroleap.speachandlanguage.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.neuroleap.speachandlanguage.Data.ScreeningContract.PicturesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionCategoriesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.ScreeningsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentAnswersEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.ValidAnswersEgEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.ValidAnswersSpEntry;
import com.neuroleap.speachandlanguage.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Karl on 3/5/2015.
 */
public class ScreeningDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "screening.db";
    private static final String TAG = "## My Info ##";
    private Context context;
    public ScreeningDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_STUDENTS_TABLE = "CREATE TABLE " + StudentsEntry.TABLE_NAME + " (" +
                StudentsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StudentsEntry.FIRST_NAME + " TEXT NOT NULL, " +
                StudentsEntry.LAST_NAME + " TEXT NOT NULL, " +
                StudentsEntry.TEACHER + " TEXT, " +
                StudentsEntry.BIRTHDAY + " TEXT, " +
                StudentsEntry.AGE + " INTEGER NOT NULL, " +
                StudentsEntry.GRADE + " INTEGER, " +
                StudentsEntry.ROOM + " TEXT, " +
                StudentsEntry.HEARING_TEST_DATE + " TEXT, " +
                StudentsEntry.VISION_TEST_DATE + " TEXT, " +
                StudentsEntry.HEARING_PASS + " BOOLEAN, " +
                StudentsEntry.VISION_PASS + " BOOLEAN" +
                " );";

        final String SQL_CREATE_SCREENINGS_TABLE = "CREATE TABLE " + ScreeningsEntry.TABLE_NAME + " (" +
                ScreeningsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ScreeningsEntry.STUDENT_ID  + " INTEGER NOT NULL, " +
                ScreeningsEntry.TEST_DATE + " TEXT NOT NULL, " +
                ScreeningsEntry.AGE +" INTEGER NOT NULL, " +
                ScreeningsEntry.TEST_MODE + " INTEGER NOT NULL, " +
                ScreeningsEntry.LANGUAGE + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + ScreeningsEntry.STUDENT_ID + ") REFERENCES " +
                StudentsEntry.TABLE_NAME +" (" + StudentsEntry._ID + ") " +
                " );";

        final String SQL_CREATE_QUESTION_CATEGORIES_TABLE = "CREATE TABLE " + QuestionCategoriesEntry.TABLE_NAME + " (" +
                QuestionCategoriesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionCategoriesEntry.CATEGORY_NAME_EG + " TEXT NOT NULL, " +
                QuestionCategoriesEntry.CATEGORY_NAME_SP + " TEXT, " +
                QuestionCategoriesEntry.FACILITATOR_MODE_FRAGMENT + " TEXT NOT NULL, " +
                QuestionCategoriesEntry.STUDENT_MODE_FRAGMENT + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " + QuestionsEntry.TABLE_NAME + " (" +
                QuestionsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsEntry.CATEGORY_ID + " INTEGER NOT NULL, " +
                QuestionsEntry.TEXT_ENGLISH + " TEXT NOT NULL, " +
                QuestionsEntry.TEXT_SPANISH + " TEXT, " +
                QuestionsEntry.AUDIO_ENGLISH + " TEXT, " +
                QuestionsEntry.AUDIO_SPANISH + " TEXT, " +
                QuestionsEntry.PROMPT_ENGLISH + " TEXT NOT NULL, " +
                QuestionsEntry.PROMPT_SPANISH + " TEXT, " +
                " FOREIGN KEY (" + QuestionsEntry.CATEGORY_ID +") REFERENCES " +
                QuestionCategoriesEntry.TABLE_NAME + " (" + QuestionCategoriesEntry._ID + ")" +
                " );";

        final String SQL_CREATE_VALID_ANSWERS_EG_TABLE = "CREATE TABLE " + ValidAnswersEgEntry.TABLE_NAME + " (" +
                ValidAnswersEgEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ValidAnswersEgEntry.QUESTION_ID + " INTEGER NOT NULL, " +
                ValidAnswersEgEntry.TEXT + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + ValidAnswersEgEntry.QUESTION_ID +") REFERENCES " +
                QuestionsEntry.TABLE_NAME + " (" + QuestionsEntry._ID + ")" +
                " );";


        final String SQL_CREATE_VALID_ANSWERS_SP_TABLE = "CREATE TABLE " + ValidAnswersSpEntry.TABLE_NAME + " (" +
                ValidAnswersSpEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ValidAnswersSpEntry.QUESTION_ID + " INTEGER NOT NULL, " +
                ValidAnswersSpEntry.TEXT + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + ValidAnswersSpEntry.QUESTION_ID +") REFERENCES " +
                QuestionsEntry.TABLE_NAME + " (" + QuestionsEntry._ID + ")" +
                " );";

        final String SQL_CREATE_PICTURES_TABLE = "CREATE TABLE " + PicturesEntry.TABLE_NAME + " (" +
                PicturesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PicturesEntry.QUESTION_ID + " INTEGER NOT NULL, " +
                PicturesEntry.FILENAME + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + PicturesEntry.QUESTION_ID +") REFERENCES " +
                QuestionsEntry.TABLE_NAME + " (" + QuestionsEntry._ID + ")" +
                " );";

        final String SQL_CREATE_STUDENT_ANSWERS_TABLE = "CREATE TABLE " + StudentAnswersEntry.TABLE_NAME + " (" +
                StudentAnswersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StudentAnswersEntry.QUESTION_ID + " INTEGER NOT NULL, " +
                StudentAnswersEntry.SCREENING_ID + " INTEGER NOT NULL, " +
                StudentAnswersEntry.ANSWER_TEXT + " TEXT, " +
                StudentAnswersEntry.CORRECT +" BOOLEAN, " +
                " FOREIGN KEY (" + StudentAnswersEntry.QUESTION_ID +") REFERENCES " +
                QuestionsEntry.TABLE_NAME + " (" + QuestionsEntry._ID + "), " +
                " FOREIGN KEY (" + StudentAnswersEntry.SCREENING_ID + ") REFERENCES " +
                ScreeningsEntry.TABLE_NAME + " (" + ScreeningsEntry._ID + ")" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_STUDENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SCREENINGS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_QUESTION_CATEGORIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VALID_ANSWERS_EG_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VALID_ANSWERS_SP_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PICTURES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STUDENT_ANSWERS_TABLE);

        InitializeDatabase(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StudentsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    private void InitializeDatabase(SQLiteDatabase db){
        //Read data from the CVS file and put it in the database.
        Log.d(TAG, "InitializeDatabase called");
        InputStream inputStream = context.getResources().openRawResource(R.raw.questions);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ContentValues cv= new ContentValues();
        long categoryId=0;
        long quetionId=0;
        String cvsLine;
        int i=0;
        try {
            whileLoop:
                while((cvsLine = reader.readLine()) != null){
                    Log.d(TAG, "Loop= " + ++i);
                    if ( ! cvsLine.substring(0,1).equals("%")) {

                        String[] row = cvsLine.split(",");
                        cv.clear();

                        if ( ! row[0].equals("")) {  // First column has something in it so this must be question category row
                            cv.put(QuestionCategoriesEntry.CATEGORY_NAME_EG , row[0]);
                            if ( ! row[1].equals("")){
                                cv.put(QuestionCategoriesEntry.CATEGORY_NAME_SP , row[1]);
                            }
                            cv.put(QuestionCategoriesEntry.FACILITATOR_MODE_FRAGMENT , row[2]);
                            cv.put(QuestionCategoriesEntry.STUDENT_MODE_FRAGMENT , row[3]);
                            categoryId = db.insert(QuestionCategoriesEntry.TABLE_NAME , null, cv);
                            continue whileLoop;
                        }

                        if ( ! row[1].equals("")) { // First column was null but 2nd column has something so this must be a question row.
                            cv.put(QuestionsEntry.CATEGORY_ID , categoryId);
                            cv.put(QuestionsEntry.TEXT_ENGLISH , row[1]);
                            Log.i(TAG,"question = " + row[1]);
                            if ( ! row[2].equals("")  ) {
                                cv.put(QuestionsEntry.TEXT_SPANISH , row[2]);
                            }
                            if (! row[3].equals("")){
                                cv.put(QuestionsEntry.AUDIO_ENGLISH , row[3]);
                            }
                            if (! row[4].equals("")) {
                                cv.put(QuestionsEntry.AUDIO_SPANISH , row[4]);
                            }

                            cv.put(QuestionsEntry.PROMPT_ENGLISH, row[5]);

                            if (row.length > 6) {
                                cv.put(QuestionsEntry.PROMPT_SPANISH, row[6]);
                            }

                            quetionId = db.insert(QuestionsEntry.TABLE_NAME, null, cv);
                            continue whileLoop;
                        }

                        if ( ! row[2].equals("")){ // First two columns are null but 3rd columns has something so this must be an English answer.
                            cv.put(ValidAnswersEgEntry.QUESTION_ID , quetionId);
                            cv.put(ValidAnswersEgEntry.TEXT , row[2]);
                            db.insert(ValidAnswersEgEntry.TABLE_NAME, null, cv);
                            continue whileLoop;
                        }

                        if ( row.length > 3 && ! row[3].equals("")){// First 3 columns null so this is a Spanish answer.
                            cv.put(ValidAnswersSpEntry.QUESTION_ID, quetionId);
                            cv.put(ValidAnswersSpEntry.TEXT , row [3]);
                            db.insert(ValidAnswersSpEntry.TABLE_NAME, null, cv);
                            continue whileLoop;
                        }

                        if (row.length > 4) { // first 4 columns null so this must be picture filename
                            cv.put(PicturesEntry.QUESTION_ID , quetionId);
                            cv.put(PicturesEntry.FILENAME , row[4]);
                            db.insert(PicturesEntry.TABLE_NAME, null, cv);
                        }
                    }
                }
        } catch (IOException e) {
            Log.e(TAG, "Error reading csv file: " + e);
            throw new RuntimeException("Error in reading csv file: " + e);

        }
    }
}
