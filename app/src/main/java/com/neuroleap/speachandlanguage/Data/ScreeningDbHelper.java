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
import com.neuroleap.speachandlanguage.Data.ScreeningContract.AnswerIconEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.AnswerButtonsPressedEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentAnswersTextEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.ScreeningCategoriesEntry;
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
                StudentsEntry.BIRTHDAY + " LONG NOT NULL, " +
                StudentsEntry.HEARING_TEST_DATE + " LONG NOT NULL, " +
                StudentsEntry.VISION_TEST_DATE + " LONG NOT NULL, " +
                StudentsEntry.HEARING_PASS + " BOOLEAN, " +
                StudentsEntry.VISION_PASS + " BOOLEAN" +
                " );";

        final String SQL_CREATE_SCREENINGS_TABLE = "CREATE TABLE " + ScreeningsEntry.TABLE_NAME + " (" +
                ScreeningsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ScreeningsEntry.STUDENT_ID  + " INTEGER NOT NULL, " +
                ScreeningsEntry.TEST_DATE + " LONG NOT NULL, " +
                ScreeningsEntry.AGE +" INTEGER NOT NULL, " +
                ScreeningsEntry.MENTAL_AGE +" INTEGER, " +
                ScreeningsEntry.TEACHER + " TEXT, " +
                ScreeningsEntry.GRADE + " INTEGER, " +
                ScreeningsEntry.ROOM + " TEXT, " +
                ScreeningsEntry.TEST_MODE + " INTEGER NOT NULL, " +
                ScreeningsEntry.COMPLETION_STATE + " INTEGER NOT NULL, " +
                ScreeningsEntry.LANGUAGE + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + ScreeningsEntry.STUDENT_ID + ") REFERENCES " +
                StudentsEntry.TABLE_NAME +" (" + StudentsEntry._ID + ") " +
                " );";

        final String SQL_CREATE_SCREENING_CATEGORIES_TABEL = "CREATE TABLE " + ScreeningContract.ScreeningCategoriesEntry.TABLE_NAME + " (" +
                ScreeningCategoriesEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ScreeningCategoriesEntry.NAME_EG + " TEXT NOT NULL, " +
                ScreeningCategoriesEntry.NAME_SP + " TEXT, " +
                ScreeningCategoriesEntry.LOW_CUT_OFF_AGE + " INTEGER NOT NULL, " +
                ScreeningCategoriesEntry.HIGH_CUT_OFF_AGE + " INTEGER NOT NULL " +
                " );";

        final String SQL_CREATE_QUESTION_CATEGORIES_TABLE = "CREATE TABLE " + QuestionCategoriesEntry.TABLE_NAME + " (" +
                QuestionCategoriesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionCategoriesEntry.SCREENING_CATEGORY_ID + " INTEGER NOT NULL, " +
                QuestionCategoriesEntry.CATEGORY_NAME_EG + " TEXT NOT NULL, " +
                QuestionCategoriesEntry.CATEGORY_NAME_SP + " TEXT, " +
                QuestionCategoriesEntry.FRAGMENT_NAME + " TEXT NOT NULL, " +
                QuestionCategoriesEntry.LOW_CUTOFF_AGE + " INTEGER NOT NULL, " +
                QuestionCategoriesEntry.HIGH_CUTOFF_AGE + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + QuestionCategoriesEntry.SCREENING_CATEGORY_ID + ") REFERENCES " +
                ScreeningCategoriesEntry.TABLE_NAME + " (" + ScreeningCategoriesEntry._ID + ")" +
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
                QuestionsEntry.UNIQUE_TEXT_ENGLISH + " TEXT, " +
                QuestionsEntry.UNIQUE_TEXT_SPANISH + " TEXT, " +
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

        final String SQL_CREATE_ANSWER_ICONS_TABLE = "CREATE TABLE " + AnswerIconEntry.TABLE_NAME + " (" +
                AnswerIconEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AnswerIconEntry.QUESTION_ID + " INTEGER NOT NULL, " +
                AnswerIconEntry.DESCRIPTION + " TEXT NOT NULL, " +
                AnswerIconEntry.FILENAME + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + AnswerIconEntry.QUESTION_ID + ") REFERENCES " +
                QuestionsEntry.TABLE_NAME + " (" + QuestionsEntry._ID + ")" +
                " );";

        final String SQL_CREATE_STUDENT_ANSWERS_TABLE = "CREATE TABLE " + StudentAnswersEntry.TABLE_NAME + " (" +
                StudentAnswersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StudentAnswersEntry.QUESTION_ID + " INTEGER NOT NULL, " +
                StudentAnswersEntry.SCREENING_ID + " INTEGER NOT NULL, " +
                StudentAnswersEntry.SCREENING_CATEGORY_ID + " INTEGER NOT NULL, " +
                StudentAnswersEntry.CORRECT +" BOOLEAN, " +
                " FOREIGN KEY (" + StudentAnswersEntry.QUESTION_ID +") REFERENCES " +
                QuestionsEntry.TABLE_NAME + " (" + QuestionsEntry._ID + "), " +
                " FOREIGN KEY (" + StudentAnswersEntry.SCREENING_ID + ") REFERENCES " +
                ScreeningsEntry.TABLE_NAME + " (" + ScreeningsEntry._ID + ")" +
                " );";

        final String SQL_CREATE_STUDENT_ANSWERS_TEXT_TABLE = "CREATE TABLE " + StudentAnswersTextEntry.TABLE_NAME + " (" +
                StudentAnswersTextEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StudentAnswersTextEntry.ANSWER_ID + " INTEGER NOT NULL, " +
                StudentAnswersTextEntry.ANSWER_NUMBER + " INTEGER NOT NULL, " +
                StudentAnswersTextEntry.TEXT + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + StudentAnswersTextEntry.ANSWER_ID + ") REFERENCES " +
                StudentAnswersEntry.TABLE_NAME + " (" + StudentAnswersEntry._ID + ")"+
                " );";

        final String SQL_CREATE_ANSWER_BUTTONS_PRESSED_TABLE = "CREATE TABLE " + AnswerButtonsPressedEntry.TABLE_NAME + " (" +
                AnswerButtonsPressedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AnswerButtonsPressedEntry.ANSWER_ID + " INTEGER NOT NULL, " +
                AnswerButtonsPressedEntry.ANSWER_ICONS_ID + " INTEGER NOT NULL, " +
                AnswerButtonsPressedEntry.ANSWER_NUMBER + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + AnswerButtonsPressedEntry.ANSWER_ID + ") REFERENCES " +
                StudentAnswersEntry.TABLE_NAME + " (" + StudentAnswersEntry._ID + "), " +
                " FOREIGN KEY (" + AnswerButtonsPressedEntry.ANSWER_ICONS_ID + ") REFERENCES " +
                AnswerIconEntry.TABLE_NAME + " (" + AnswerIconEntry._ID + ")" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_STUDENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SCREENINGS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SCREENING_CATEGORIES_TABEL);
        sqLiteDatabase.execSQL(SQL_CREATE_QUESTION_CATEGORIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VALID_ANSWERS_EG_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VALID_ANSWERS_SP_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PICTURES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STUDENT_ANSWERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ANSWER_ICONS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ANSWER_BUTTONS_PRESSED_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STUDENT_ANSWERS_TEXT_TABLE);

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
        long screening_Category_Id = 0;
        long categoryId=0;
        long categoryType=0;
        long quetionId=0;
        String cvsLine;
        int i=0;
        try {
            whileLoop:
                while((cvsLine = reader.readLine()) != null){
                    Log.d(TAG, "Loop= " + ++i);
                    Log.i(TAG,"cvsLine= @" + cvsLine + "  substring = " + cvsLine.substring(0,1));
                    if ( ! cvsLine.substring(0,1).equals("%")) {

                        String[] row = cvsLine.split(",");
                        for (int j = 0; j < row.length; j++){
                            Log.i(TAG, "j= " + j +"  row[j] " + row[j]);
                        }
                        cv.clear();

                        if ( ! row[0].equals("")) { //First column has something in it so this must be screening category row
                            cv.put(ScreeningCategoriesEntry.NAME_EG , row[0]);
                            if ( ! row[1].equals("") ){
                                cv.put(ScreeningCategoriesEntry.NAME_SP , row[1]);
                            }
                            cv.put(ScreeningCategoriesEntry.LOW_CUT_OFF_AGE, Long.parseLong(row[2]));
                            cv.put(ScreeningCategoriesEntry.HIGH_CUT_OFF_AGE, Long.parseLong(row[3]));
                            screening_Category_Id = db.insert(ScreeningCategoriesEntry.TABLE_NAME, null, cv);
                            continue whileLoop;
                        }

                        if ( ! row[1].equals("")) {  // First column was null but 2nd column has something in it so this must be question category row
                            cv.put(QuestionCategoriesEntry.SCREENING_CATEGORY_ID, screening_Category_Id);
                            cv.put(QuestionCategoriesEntry.CATEGORY_NAME_EG , row[1]);
                            if ( ! row[1].equals("")){
                                cv.put(QuestionCategoriesEntry.CATEGORY_NAME_SP , row[2]);
                            }
                            cv.put(QuestionCategoriesEntry.FRAGMENT_NAME , row[3]);
                            cv.put(QuestionCategoriesEntry.LOW_CUTOFF_AGE, Long.parseLong(row[4]));
                            cv.put(QuestionCategoriesEntry.HIGH_CUTOFF_AGE, Long.parseLong(row[5]));
                            categoryId = db.insert(QuestionCategoriesEntry.TABLE_NAME , null, cv);
                            continue whileLoop;
                        }

                        if ( ! row[2].equals("")) { // First 2 column were null but 3rd column has something so this must be a question row.
                            cv.put(QuestionsEntry.CATEGORY_ID , categoryId);
                            cv.put(QuestionsEntry.TEXT_ENGLISH , row[2]);
                            Log.i(TAG,"question = " + row[2]);
                            if ( ! row[2].equals("")  ) {
                                cv.put(QuestionsEntry.TEXT_SPANISH , row[3]);
                            }
                            if (! row[3].equals("")){
                                cv.put(QuestionsEntry.AUDIO_ENGLISH , row[4]);
                            }
                            if (! row[4].equals("")) {
                                cv.put(QuestionsEntry.AUDIO_SPANISH , row[5]);
                            }

                            cv.put(QuestionsEntry.PROMPT_ENGLISH, row[6]);

                            if (row.length > 7 && ! row[7].equals("") ){
                                cv.put(QuestionsEntry.PROMPT_SPANISH, row[7]);
                            }
                            if (row.length > 8 && ! row[8].equals("") ){
                                cv.put(QuestionsEntry.UNIQUE_TEXT_ENGLISH, row[8]);
                            }
                            if (row.length > 9){
                                cv.put(QuestionsEntry.UNIQUE_TEXT_SPANISH, row[9]);
                            }


                            quetionId = db.insert(QuestionsEntry.TABLE_NAME, null, cv);
                            continue whileLoop;
                        }

                        if ( ! row[3].equals("")){ // First three columns are null but 4th columns has something so this must be an English answer.
                            cv.put(ValidAnswersEgEntry.QUESTION_ID , quetionId);
                            cv.put(ValidAnswersEgEntry.TEXT , row[3]);
                            db.insert(ValidAnswersEgEntry.TABLE_NAME, null, cv);
                            continue whileLoop;
                        }

                        if ( row.length > 4 && ! row[4].equals("")){// First 4 columns null so this is a Spanish answer.
                            cv.put(ValidAnswersSpEntry.QUESTION_ID, quetionId);
                            cv.put(ValidAnswersSpEntry.TEXT , row [4]);
                            db.insert(ValidAnswersSpEntry.TABLE_NAME, null, cv);
                            continue whileLoop;
                        }

                        if (row.length > 5 && ! row[5].equals("")) { // first 5 columns null so this must be picture filename
                            cv.put(PicturesEntry.QUESTION_ID , quetionId);
                            cv.put(PicturesEntry.FILENAME , row[5]);
                            db.insert(PicturesEntry.TABLE_NAME, null, cv);
                            continue whileLoop;
                        }

                        if (row.length > 6) { // first 6 columns null so this must be an answer icon filename
                            cv.put(AnswerIconEntry.QUESTION_ID, quetionId);
                            cv.put(AnswerIconEntry.FILENAME, row[6]);
                            cv.put(AnswerIconEntry.DESCRIPTION, row[7]);
                            db.insert(AnswerIconEntry.TABLE_NAME, null, cv);
                        }
                    }
                }
        } catch (IOException e) {
            Log.e(TAG, "Error reading csv file: " + e);
            throw new RuntimeException("Error in reading csv file: " + e);

        }
    }
}
