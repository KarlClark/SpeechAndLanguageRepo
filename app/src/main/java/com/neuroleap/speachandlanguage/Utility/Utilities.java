package com.neuroleap.speachandlanguage.Utility;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionCategoriesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionsEntry;


/**
 * Created by Karl on 3/6/2015.
 */
public class Utilities {

    public static void loadDatabase(SQLiteDatabase db) {
        ContentValues  cv= new ContentValues();
        long id;

        cv.put(QuestionCategoriesEntry.CATEGORY_NAME , "Semantics");
        cv.put(QuestionCategoriesEntry.FRAGMENT_NAME , "SemanticsFragment");
        id = db.insert(QuestionCategoriesEntry.TABLE_NAME , null, cv);
        cv.clear();
        cv.put(QuestionsEntry.CATEGORY_ID, id);
        cv.put (QuestionsEntry.TEXT_ENGLISH, "Semantics question 1");
        cv.put (QuestionsEntry.TEXT_SPANISH ,"spanish 1");
        cv.put ( QuestionsEntry.AUDIO_ENGLISH, "audio english 1");
        cv.put (QuestionsEntry.AUDIO_SPANISH, "audio spanish 1");
        db.insert(QuestionsEntry.TABLE_NAME, null, cv);
        cv.put(QuestionsEntry.CATEGORY_ID, id);
        cv.put (QuestionsEntry.TEXT_ENGLISH, "Semantics question 2");
        cv.put (QuestionsEntry.TEXT_SPANISH ,"spanish 2");
        cv.put ( QuestionsEntry.AUDIO_ENGLISH, "audio english 2");
        cv.put (QuestionsEntry.AUDIO_SPANISH, "audio spanish 2");
        db.insert(QuestionsEntry.TABLE_NAME, null, cv);

        cv.clear();
        cv.put(QuestionCategoriesEntry.CATEGORY_NAME , "Categories");
        cv.put(QuestionCategoriesEntry.FRAGMENT_NAME , "CategoryFragment");
        id=db.insert(QuestionCategoriesEntry.TABLE_NAME , null, cv);
        cv.clear();
        cv.put(QuestionsEntry.CATEGORY_ID, id);
        cv.put (QuestionsEntry.TEXT_ENGLISH, "Categories question 1");
        cv.put (QuestionsEntry.TEXT_SPANISH ,"spanish 1");
        cv.put ( QuestionsEntry.AUDIO_ENGLISH, "audio english 1");
        cv.put (QuestionsEntry.AUDIO_SPANISH, "audio spanish 1");
        db.insert(QuestionsEntry.TABLE_NAME, null, cv);
        cv.put(QuestionsEntry.CATEGORY_ID, id);
        cv.put (QuestionsEntry.TEXT_ENGLISH, "Categories question 2");
        cv.put (QuestionsEntry.TEXT_SPANISH ,"spanish 2");
        cv.put ( QuestionsEntry.AUDIO_ENGLISH, "audio english 2");
        cv.put (QuestionsEntry.AUDIO_SPANISH, "audio spanish 2");
        db.insert(QuestionsEntry.TABLE_NAME, null, cv);

        cv.clear();
        cv.put(QuestionCategoriesEntry.CATEGORY_NAME , "Functions & Question Comprehension");
        cv.put(QuestionCategoriesEntry.FRAGMENT_NAME , "ComprehensionFragment");
        id=db.insert(QuestionCategoriesEntry.TABLE_NAME , null, cv);
        cv.clear();
        cv.put(QuestionsEntry.CATEGORY_ID, id);
        cv.put (QuestionsEntry.TEXT_ENGLISH, "Function & questions question 1");
        cv.put (QuestionsEntry.TEXT_SPANISH ,"spanish 1");
        cv.put ( QuestionsEntry.AUDIO_ENGLISH, "audio english 1");
        cv.put (QuestionsEntry.AUDIO_SPANISH, "audio spanish 1");
        db.insert(QuestionsEntry.TABLE_NAME, null, cv);
        cv.put(QuestionsEntry.CATEGORY_ID, id);
        cv.put (QuestionsEntry.TEXT_ENGLISH, "Functions & questions question 2");
        cv.put (QuestionsEntry.TEXT_SPANISH ,"spanish 2");
        cv.put ( QuestionsEntry.AUDIO_ENGLISH, "audio english 2");
        cv.put (QuestionsEntry.AUDIO_SPANISH, "audio spanish 2");
        db.insert(QuestionsEntry.TABLE_NAME, null, cv);
    }
}
