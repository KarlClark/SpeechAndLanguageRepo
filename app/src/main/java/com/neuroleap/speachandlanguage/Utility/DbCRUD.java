package com.neuroleap.speachandlanguage.Utility;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.neuroleap.speachandlanguage.Data.ScreeningContract.*;

/**
 * Created by Karl on 3/22/2015.
 */
public class DbCRUD {

    private static SQLiteDatabase mDB;

    public static SQLiteDatabase getDatabase() {
        return mDB;
    }

    public static void setDatabase(SQLiteDatabase db) {
        mDB = db;
    }

    public static  Cursor getQuestionCategories(){
        String[] categoryColumns;
        if (Utilities.getLanguage() == Utilities.ENGLISH) {
            categoryColumns = new String[]{"_ID", QuestionCategoriesEntry.CATEGORY_NAME_EG};
        }else{
            categoryColumns = new String[]{"_ID", QuestionCategoriesEntry.CATEGORY_NAME_SP};
        }

        return mDB.query(QuestionCategoriesEntry.TABLE_NAME, categoryColumns, null, null, null, null, null);
    }

    public static Cursor getQuestionsPrompts(long questionCategoryId){
        String[] questionColumns;
        if (Utilities.getLanguage() == Utilities.ENGLISH) {
            questionColumns = new String[] {"_ID", QuestionsEntry.CATEGORY_ID, QuestionsEntry.PROMPT_ENGLISH};
        }else{
            questionColumns = new String[] {"_ID", QuestionsEntry.CATEGORY_ID, QuestionsEntry.PROMPT_SPANISH};
        }
        return mDB.query(QuestionsEntry.TABLE_NAME, questionColumns, QuestionsEntry.CATEGORY_ID + "=" +questionCategoryId , null, null, null, null);
    }

    public static Cursor getFacilitatorModeFragmentName(long questionCategoryId) {
        String[] columns = new String[] {QuestionCategoriesEntry.FACILITATOR_MODE_FRAGMENT};
        return mDB.query(QuestionCategoriesEntry.TABLE_NAME, columns, "_ID=" + questionCategoryId, null, null, null, null);
    }

    public static Cursor getStudentModeFragmentName(long questionCategoryId) {
        String[] columns = new String[] {QuestionCategoriesEntry.STUDENT_MODE_FRAGMENT};
        return mDB.query(QuestionCategoriesEntry.TABLE_NAME, columns, "_ID=" + questionCategoryId, null, null, null, null);
    }

    public static Cursor getShortScreens (){
        String sql = "Select " + StudentsEntry.TABLE_NAME + "." +StudentsEntry.FIRST_NAME + " , "
                      + StudentsEntry.TABLE_NAME + "." + StudentsEntry.LAST_NAME + " , "
                      + ScreeningsEntry.TABLE_NAME + "." + ScreeningsEntry.TEACHER + " ,  "
                      + ScreeningsEntry.TABLE_NAME + "." + ScreeningsEntry.TEST_DATE + " , "
                      + ScreeningsEntry.TABLE_NAME + "." + ScreeningsEntry.COMPLETION_STATE
                      + " FROM " + StudentsEntry.TABLE_NAME + " , " + ScreeningsEntry.TABLE_NAME
                      + " WHERE " + StudentsEntry.TABLE_NAME + "."+ StudentsEntry._ID + " = "
                      + ScreeningsEntry.TABLE_NAME + "." + ScreeningsEntry.STUDENT_ID
                      +" GROUP BY " + StudentsEntry.TABLE_NAME + "." + StudentsEntry.FIRST_NAME + " , "
                      + StudentsEntry.TABLE_NAME + "." + StudentsEntry.LAST_NAME + " , "
                      + ScreeningsEntry.TABLE_NAME + "." + ScreeningsEntry.TEACHER + " ,  "
                      + ScreeningsEntry.TABLE_NAME + "." + ScreeningsEntry.TEST_DATE + " , "
                      + ScreeningsEntry.TABLE_NAME + "." + ScreeningsEntry.COMPLETION_STATE
                      + " ORDER BY " + ScreeningsEntry.TABLE_NAME + "." + ScreeningsEntry.TEST_DATE + " DESC";
        return mDB.rawQuery(sql,null);
    }

    public static long insertStudent(String firstName, String lastName, String date_of_birth, String hearingScreenDate,
                                     Boolean hearingPass, String visionScreenDate, boolean visionPass){

        ContentValues cv= new ContentValues();
        cv.put(StudentsEntry.FIRST_NAME , firstName);
        cv.put(StudentsEntry.LAST_NAME, lastName);
        cv.put(StudentsEntry.BIRTHDAY, date_of_birth);
        cv.put(StudentsEntry.HEARING_TEST_DATE, hearingScreenDate);
        cv.put(StudentsEntry.HEARING_PASS, hearingPass);
        cv.put(StudentsEntry.VISION_TEST_DATE, visionScreenDate);
        cv.put(StudentsEntry.VISION_PASS, visionPass);

        return mDB.insert(StudentsEntry.TABLE_NAME, null, cv);
    }

}
