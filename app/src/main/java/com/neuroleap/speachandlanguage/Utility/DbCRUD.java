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
        if (Utilities.getQuestionsLanguage() == Utilities.ENGLISH) {
            categoryColumns = new String[]{"_ID", QuestionCategoriesEntry.CATEGORY_NAME_EG, QuestionCategoriesEntry.CUTOFF_AGE,
                                           QuestionCategoriesEntry.FRAGMENT_NAME};
        }else{
            categoryColumns = new String[]{"_ID", QuestionCategoriesEntry.CATEGORY_NAME_SP, QuestionCategoriesEntry.CUTOFF_AGE,
                                           QuestionCategoriesEntry.FRAGMENT_NAME};
        }

        return mDB.query(QuestionCategoriesEntry.TABLE_NAME, categoryColumns, null, null, null, null, null);
    }

    public static Cursor getQuestionsPrompts(long questionCategoryId){
        String[] questionColumns;
        if (Utilities.getQuestionsLanguage() == Utilities.ENGLISH) {
            questionColumns = new String[] {"_ID", QuestionsEntry.CATEGORY_ID, QuestionsEntry.PROMPT_ENGLISH};
        }else{
            questionColumns = new String[] {"_ID", QuestionsEntry.CATEGORY_ID, QuestionsEntry.PROMPT_SPANISH};
        }
        return mDB.query(QuestionsEntry.TABLE_NAME, questionColumns, QuestionsEntry.CATEGORY_ID + "=" +questionCategoryId , null, null, null, null);
    }


    public static Cursor getShortScreens (){
        String sql = "Select "
                      + ScreeningsEntry.TABLE_NAME + "._ID , "
                      + StudentsEntry.TABLE_NAME + "." +StudentsEntry.FIRST_NAME + " , "
                      + StudentsEntry.TABLE_NAME + "." + StudentsEntry.LAST_NAME + " , "
                      + ScreeningsEntry.TABLE_NAME + "." + ScreeningsEntry.AGE + " , "
                      + ScreeningsEntry.TABLE_NAME + "." + ScreeningsEntry.TEACHER + " ,  "
                      + ScreeningsEntry.TABLE_NAME + "." + ScreeningsEntry.TEST_DATE + " , "
                      + ScreeningsEntry.TABLE_NAME + "." + ScreeningsEntry.COMPLETION_STATE
                      + " FROM " + StudentsEntry.TABLE_NAME + " , " + ScreeningsEntry.TABLE_NAME
                      + " WHERE " + StudentsEntry.TABLE_NAME + "."+ StudentsEntry._ID + " = "
                      + ScreeningsEntry.TABLE_NAME + "." + ScreeningsEntry.STUDENT_ID
                      + " ORDER BY " + ScreeningsEntry.TABLE_NAME + "." + ScreeningsEntry.TEST_DATE + " DESC";
        return mDB.rawQuery(sql,null);
    }

    public static Cursor getAnswer(long question_id, long screening_id){
        String sql = "SELECT "
                     + StudentAnswersEntry.ANSWER_TEXT + " , "
                     + StudentAnswersEntry.CORRECT
                     + " FROM " + StudentAnswersEntry.TABLE_NAME
                     + " WHERE " + StudentAnswersEntry.QUESTION_ID + "=" + question_id
                     + " AND " + StudentAnswersEntry.SCREENING_ID + "=" + screening_id;

        return mDB.rawQuery(sql, null);
    }

    public static int getFirstQuestion(long questionCategoryId){
        String sql = "SELECT MIN ( _ID)  FROM " + QuestionsEntry.TABLE_NAME +  " WHERE " + QuestionsEntry.CATEGORY_ID + " = " + questionCategoryId;
        Cursor  c = mDB.rawQuery(sql, null);
        c.moveToNext();
        int questionId = c.getInt(0);
        c.close();
        return questionId;
    }

    public static Cursor getQuestionData(long questionId){
        String[] columns = new String[] {QuestionsEntry.CATEGORY_ID, QuestionsEntry.TEXT_ENGLISH, QuestionsEntry.TEXT_SPANISH,
                                         QuestionsEntry.AUDIO_ENGLISH, QuestionsEntry.AUDIO_SPANISH};
        return mDB.query(QuestionsEntry.TABLE_NAME, columns, "_ID=" +  questionId, null, null, null, null);
    }

    public static Cursor getPictureFilenames(long questionId){
        String[] columns = new String[] {PicturesEntry.FILENAME};
        return mDB.query(PicturesEntry.TABLE_NAME, columns, PicturesEntry.QUESTION_ID + " = " + questionId, null, null, null, null);
    }

    public static int getQuestionCategory(int questionId){
        String columns[] = new String[] {QuestionsEntry.CATEGORY_ID};
        Cursor c = mDB.query(QuestionsEntry.TABLE_NAME, columns, "_ID=" + questionId, null, null, null, null);
        c.moveToNext();
        int id = c.getInt(0);
        c.close();
        return id;
    }

    public static Cursor getFragmentName(long questionCategoryId){
        String[] columns = new String[] {QuestionCategoriesEntry.FRAGMENT_NAME};
        return mDB.query(QuestionCategoriesEntry.TABLE_NAME, columns,  "_ID=" + questionCategoryId, null, null, null, null);
    }

    public static int getQuestionCount(){
        Cursor c = mDB.rawQuery("SELECT COUNT(*) FROM " + QuestionsEntry.TABLE_NAME , null);
        c.moveToNext();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    public static long insertStudent(String firstName, String lastName, String date_of_birth, String hearingScreenDate,
                                     Boolean hearingPass, String visionScreenDate, boolean visionPass){

        ContentValues cv= new ContentValues();
        cv.put(StudentsEntry.FIRST_NAME , firstName);
        cv.put(StudentsEntry.LAST_NAME, lastName);
        cv.put(StudentsEntry.BIRTHDAY, Utilities.getLongDate(date_of_birth));
        cv.put(StudentsEntry.HEARING_TEST_DATE, Utilities.getLongDate(hearingScreenDate));
        cv.put(StudentsEntry.HEARING_PASS, hearingPass);
        cv.put(StudentsEntry.VISION_TEST_DATE, Utilities.getLongDate(visionScreenDate));
        cv.put(StudentsEntry.VISION_PASS, visionPass);

        return mDB.insert(StudentsEntry.TABLE_NAME, null, cv);
    }

    public static void insertScreening(long student_id, String testDate, int mode, int language, int age, String room, int grade, String teacher){
        ContentValues cv = new ContentValues();
        cv.put(ScreeningsEntry.STUDENT_ID , student_id);
        cv.put(ScreeningsEntry.TEST_DATE, Utilities.getLongDate(testDate));
        cv.put(ScreeningsEntry.TEST_MODE, mode);
        cv.put(ScreeningsEntry.LANGUAGE, language);
        cv.put(ScreeningsEntry.AGE, age);
        cv.put(ScreeningsEntry.ROOM, room);
        cv.put(ScreeningsEntry.GRADE, grade);
        cv.put(ScreeningsEntry.TEACHER, teacher);
        cv.put(ScreeningsEntry.COMPLETION_STATE, Utilities.SCREENING_NOT_STARTED);

        mDB.insert(ScreeningsEntry.TABLE_NAME, null, cv);
    }

    public static void enterAnswer(long question_id, long screening_id, String answer_text, boolean correct){
        ContentValues cv = new ContentValues();
        cv.put(StudentAnswersEntry.QUESTION_ID, question_id);
        cv.put(StudentAnswersEntry.SCREENING_ID, screening_id);
        cv.put(StudentAnswersEntry.ANSWER_TEXT, answer_text);
        cv.put(StudentAnswersEntry.CORRECT, correct);

        String sql = "SELECT _ID " +
                     " FROM " + StudentAnswersEntry.TABLE_NAME +
                     " WHERE " + StudentAnswersEntry.QUESTION_ID + "=" + question_id +
                      " AND " + StudentAnswersEntry.SCREENING_ID + "=" + screening_id;
        Cursor c = mDB.rawQuery(sql,null);
        if (c.getCount() == 0) {
            mDB.insert(StudentAnswersEntry.TABLE_NAME, null, cv);
        }else{
            c.moveToNext();
            mDB.update(StudentAnswersEntry.TABLE_NAME, cv, "_ID=" + c.getLong(0), null);
        }
        c.close();

        updateScreeningCompletionState(screening_id, Utilities.SCREENING_NOT_COMPLETE);
    }

    public static void updateScreeningCompletionState(long screening_id, int completion_state){
        ContentValues cv = new ContentValues();
        cv.put(ScreeningsEntry.COMPLETION_STATE, completion_state);

        mDB.update(ScreeningsEntry.TABLE_NAME, cv, "_ID=" + screening_id, null);
    }
}

