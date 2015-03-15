package com.neuroleap.speachandlanguage.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.ScreeningsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionCategoriesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.ValidAnswersEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.PicturesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentAnswersEntry;

/**
 * Created by Karl on 3/5/2015.
 */
public class ScreeningDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "screening.db";
    private static final String TAG = "## My Info ##";
    public ScreeningDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_STUDENTS_TABLE = "CREATE TABLE " + StudentsEntry.TABLE_NAME + " (" +
                StudentsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StudentsEntry.FIRST_NAME + " TEXT NOT NULL, " +
                StudentsEntry.LAST_NAME + " TEXT NOT NULL, " +
                StudentsEntry.TEACHER + " TEXT, " +
                StudentsEntry.BIRTHDAY + " TEXT, " +
                StudentsEntry.HEARING_TEST_DATE + " TEXT, " +
                StudentsEntry.VISION_TEST_DATE + " TEXT, " +
                StudentsEntry.HEARING_PASS + " BOOLEAN, " +
                StudentsEntry.VISION_PASS + " BOOLEAN" +
                " );";

        final String SQL_CREATE_SCREENINGS_TABLE = "CREATE TABLE " + ScreeningsEntry.TABLE_NAME + " (" +
                ScreeningsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ScreeningsEntry.STUDENT_ID  + " INTEGER NOT NULL, " +
                ScreeningsEntry.TEST_DATE + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + ScreeningsEntry.STUDENT_ID + ") REFERENCES " +
                StudentsEntry.TABLE_NAME +" (" + StudentsEntry._ID + ") " +
                " );";

        final String SQL_CREATE_QUESTION_CATEGORIES_TABLE = "CREATE TABLE " + QuestionCategoriesEntry.TABLE_NAME + " (" +
                QuestionCategoriesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionCategoriesEntry.CATEGORY_NAME + " TEXT NOT NULL, " +
                QuestionCategoriesEntry.FRAGMENT_NAME + " TEXT NOT NULL" +
                " );";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " + QuestionsEntry.TABLE_NAME + " (" +
                QuestionsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsEntry.CATEGORY_ID + " INTEGER NOT NULL, " +
                QuestionsEntry.TEXT_ENGLISH + " TEXT NOT NULL, " +
                QuestionsEntry.TEXT_SPANISH + " TEXT, " +
                QuestionsEntry.AUDIO_ENGLISH + " TEXT, " +
                QuestionsEntry.AUDIO_SPANISH + " TEXT, " +
                " FOREIGN KEY (" + QuestionsEntry.CATEGORY_ID +") REFERENCES " +
                QuestionCategoriesEntry.TABLE_NAME + " (" + QuestionCategoriesEntry._ID + ")" +
                " );";

        final String SQL_CREATE_VALID_ANSWERS_TABLE = "CREATE TABLE " + ValidAnswersEntry.TABLE_NAME + " (" +
                ValidAnswersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ValidAnswersEntry.QUESTION_ID + " INTEGER NOT NULL, " +
                ValidAnswersEntry.TEXT_ENGLISH + " TEXT NOT NULL, " +
                ValidAnswersEntry.TEXT_SPANISH + "TEXT, " +
                " FOREIGN KEY (" + ValidAnswersEntry.QUESTION_ID +") REFERENCES " +
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
        sqLiteDatabase.execSQL(SQL_CREATE_VALID_ANSWERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PICTURES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STUDENT_ANSWERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StudentsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
