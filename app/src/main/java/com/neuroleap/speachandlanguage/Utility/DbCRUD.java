package com.neuroleap.speachandlanguage.Utility;

import android.database.Cursor;

import com.neuroleap.speachandlanguage.Data.ScreeningContract;

/**
 * Created by Karl on 3/22/2015.
 */
public class DbCRUD {

    public static  Cursor getQuestionCategories(){
        String[] categoryColumns;
        if (Utilities.getLanguage() == Utilities.ENGLISH) {
            categoryColumns = new String[]{"_ID", ScreeningContract.QuestionCategoriesEntry.CATEGORY_NAME_EG};
        }else{
            categoryColumns = new String[]{"_ID", ScreeningContract.QuestionCategoriesEntry.CATEGORY_NAME_SP};
        }

        return Utilities.getDatabase().query(ScreeningContract.QuestionCategoriesEntry.TABLE_NAME, categoryColumns, null, null, null, null, null);
    }

    public static Cursor getQuestionsPrompts(int questionCategoryId){
        String[] questionColumns;
        if (Utilities.getLanguage() == Utilities.ENGLISH) {
            questionColumns = new String[] {"_ID", ScreeningContract.QuestionsEntry.CATEGORY_ID, ScreeningContract.QuestionsEntry.PROMPT_ENGLISH};
        }else{
            questionColumns = new String[] {"_ID", ScreeningContract.QuestionsEntry.CATEGORY_ID, ScreeningContract.QuestionsEntry.PROMPT_SPANISH};
        }
        return Utilities.getDatabase().query(ScreeningContract.QuestionsEntry.TABLE_NAME, questionColumns,
                ScreeningContract.QuestionsEntry.CATEGORY_ID + "=" +questionCategoryId , null, null, null, null);
    }
}
