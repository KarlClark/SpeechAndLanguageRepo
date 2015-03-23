package com.neuroleap.speachandlanguage.Utility;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.neuroleap.speachandlanguage.Data.ScreeningContract;

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
            categoryColumns = new String[]{"_ID", ScreeningContract.QuestionCategoriesEntry.CATEGORY_NAME_EG};
        }else{
            categoryColumns = new String[]{"_ID", ScreeningContract.QuestionCategoriesEntry.CATEGORY_NAME_SP};
        }

        return mDB.query(ScreeningContract.QuestionCategoriesEntry.TABLE_NAME, categoryColumns, null, null, null, null, null);
    }

    public static Cursor getQuestionsPrompts(long questionCategoryId){
        String[] questionColumns;
        if (Utilities.getLanguage() == Utilities.ENGLISH) {
            questionColumns = new String[] {"_ID", ScreeningContract.QuestionsEntry.CATEGORY_ID, ScreeningContract.QuestionsEntry.PROMPT_ENGLISH};
        }else{
            questionColumns = new String[] {"_ID", ScreeningContract.QuestionsEntry.CATEGORY_ID, ScreeningContract.QuestionsEntry.PROMPT_SPANISH};
        }
        return mDB.query(ScreeningContract.QuestionsEntry.TABLE_NAME, questionColumns,
                ScreeningContract.QuestionsEntry.CATEGORY_ID + "=" +questionCategoryId , null, null, null, null);
    }

    public static Cursor getFacilitatorModeFragmentName(long questionCategoryId) {
        String[] columns = new String[] {ScreeningContract.QuestionCategoriesEntry.FACILITATOR_MODE_FRAGMENT};
        return mDB.query(ScreeningContract.QuestionCategoriesEntry.TABLE_NAME, columns, "_ID=" + questionCategoryId, null, null, null, null);
    }

    public static Cursor getStudentModeFragmentName(long questionCategoryId) {
        String[] columns = new String[] {ScreeningContract.QuestionCategoriesEntry.STUDENT_MODE_FRAGMENT};
        return mDB.query(ScreeningContract.QuestionCategoriesEntry.TABLE_NAME, columns, "_ID=" + questionCategoryId, null, null, null, null);
    }

}
