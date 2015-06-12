package com.neuroleap.speachandlanguage.Utility;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.neuroleap.speachandlanguage.Data.ScreeningContract.AnswerButtonsPressedEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.AnswerIconEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.PicturesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionCategoriesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.ScreeningCategoriesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.ScreeningsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentAnswersEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentAnswersTextEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.StudentsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.ValidAnswersEgEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.ValidAnswersSpEntry;
import com.neuroleap.speachandlanguage.Models.PressedIcon;
import com.neuroleap.speachandlanguage.Models.StudentAnswerText;

import java.util.ArrayList;

/**
 * Created by Karl on 3/22/2015.
 */
public class DbCRUD {

    private static SQLiteDatabase mDB;
    private static final String TAG = "## My Info ##";

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
                                           QuestionCategoriesEntry.FRAGMENT_NAME, QuestionCategoriesEntry.SCREENING_CATEGORY_ID};
        }else{
            categoryColumns = new String[]{"_ID", QuestionCategoriesEntry.CATEGORY_NAME_SP, QuestionCategoriesEntry.CUTOFF_AGE,
                                           QuestionCategoriesEntry.FRAGMENT_NAME, QuestionCategoriesEntry.SCREENING_CATEGORY_ID};
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

    public static Cursor getStudentInfo(long studentId){
        String[] columns = new String[] {   StudentsEntry.FIRST_NAME,
                                            StudentsEntry.LAST_NAME,
                                            StudentsEntry.BIRTHDAY,
                                            StudentsEntry.HEARING_TEST_DATE,
                                            StudentsEntry.HEARING_PASS,
                                            StudentsEntry.VISION_TEST_DATE,
                                            StudentsEntry.VISION_PASS};
        return mDB.query(StudentsEntry.TABLE_NAME, columns, StudentsEntry._ID + "=" + studentId, null, null, null, null);
    }

    public static Cursor getScreeningStudentInfo(long studentId){
        String[] columns = new String[]{    ScreeningsEntry.TEST_DATE,
                                            ScreeningsEntry.AGE,
                                            ScreeningsEntry.ROOM,
                                            ScreeningsEntry.TEACHER,
                                            ScreeningsEntry.GRADE};
        return mDB.query(ScreeningsEntry.TABLE_NAME, columns, ScreeningsEntry.STUDENT_ID + "=" + studentId, null, null, null, null);
    }

    public static long getLongScreeningDate(long screeningId){
        String[] columns = new String [] {ScreeningsEntry.TEST_DATE};
        Cursor c = mDB.query(ScreeningsEntry.TABLE_NAME, columns, ScreeningsEntry._ID + "=" + screeningId, null, null, null, null);
        c.moveToNext();
        long date = c.getLong(0);
        c.close();
        return date;
    }

    public static Cursor getShortScreens (){
        String sql = "Select "
                      + ScreeningsEntry.TABLE_NAME + "._ID , "
                      + ScreeningsEntry.TABLE_NAME + "." +ScreeningsEntry.STUDENT_ID + " , "
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

    public static Cursor getTestModeAndAge(long screeningId){
        String[] columns = new String[] {ScreeningsEntry.TEST_MODE, ScreeningsEntry.AGE};
        return mDB.query(ScreeningsEntry.TABLE_NAME, columns, ScreeningsEntry._ID + "=" + screeningId, null, null, null, null);
    }

    public static Cursor getStudentAnswer(long question_id, long screening_id){
        String sql = "SELECT "
                     + StudentAnswersEntry._ID + " , "
                     + StudentAnswersEntry.CORRECT + " , "
                     + StudentAnswersEntry.SCREENING_CATEGORY_ID
                     + " FROM " + StudentAnswersEntry.TABLE_NAME
                     + " WHERE " + StudentAnswersEntry.QUESTION_ID + "=" + question_id
                     + " AND " + StudentAnswersEntry.SCREENING_ID + "=" + screening_id;

        return mDB.rawQuery(sql, null);
    }

    public static Cursor getStudentAnswersText(long studentAnswerId){
        Log.i(TAG,"get answer text for student answer id= " + studentAnswerId);
        String sql = "SELECT "
                     + StudentAnswersTextEntry.ANSWER_NUMBER + " , "
                     + StudentAnswersTextEntry.TEXT
                     + " FROM " + StudentAnswersTextEntry.TABLE_NAME
                     + " WHERE " + StudentAnswersTextEntry.ANSWER_ID + "=" + studentAnswerId
                     + " ORDER BY " + StudentAnswersTextEntry.ANSWER_NUMBER + " ASC";
        return mDB.rawQuery(sql, null);
    }

    public static Cursor getStudentAnswersIcons(long studentAnswerId){
        Log.i(TAG,"get answer icons for student answer id= " + studentAnswerId);
        String sql ="SELECT "
                    + AnswerButtonsPressedEntry.ANSWER_NUMBER + " , "
                    + AnswerButtonsPressedEntry.ANSWER_ICONS_ID
                    + " FROM " + AnswerButtonsPressedEntry.TABLE_NAME
                    + " WHERE " + AnswerButtonsPressedEntry.ANSWER_ID + " = " + studentAnswerId
                    + " ORDER BY " + AnswerButtonsPressedEntry.ANSWER_NUMBER + " ASC";
        return mDB.rawQuery(sql , null);
    }

    public static Cursor getStudentAnswersForScreeningCategoryId(long screening_id, long screeningCategoryId){
        String sql =  "SELECT "
                + StudentAnswersEntry.CORRECT + " , "
                + StudentAnswersEntry.SCREENING_CATEGORY_ID
                + " FROM " + StudentAnswersEntry.TABLE_NAME
                + " WHERE " + StudentAnswersEntry.SCREENING_CATEGORY_ID + "=" + screeningCategoryId
                + " AND " + StudentAnswersEntry.SCREENING_ID + "=" + screening_id;

        return mDB.rawQuery(sql, null);
    }

    public static Cursor getScreeningCategories(){
        String[] columns = new String[] {ScreeningCategoriesEntry._ID,
                                         ScreeningCategoriesEntry.NAME_EG,
                                         ScreeningCategoriesEntry.NAME_SP,
                                         ScreeningCategoriesEntry.CUT_OFF_AGE};
        return mDB.query(ScreeningCategoriesEntry.TABLE_NAME, columns, null, null, null, null, null);
    }

    public static Cursor getAllCompletedQuestionsIds(long screeningId){
        String [] columns = new String[] {StudentAnswersEntry.QUESTION_ID, StudentAnswersEntry.CORRECT};
        return mDB.query(StudentAnswersEntry.TABLE_NAME, columns, StudentAnswersEntry.SCREENING_ID + "=" + screeningId, null, null, null, null);
    }

    public static int getAge(int screeningId) {
        String[] columns = new String[] {ScreeningsEntry.AGE};
        Cursor c = mDB.query(ScreeningsEntry.TABLE_NAME, columns, "_ID = " + screeningId, null, null, null, null);
        c.moveToNext();
        int age = c.getInt(0);
        c.close();
        return age;
    }

    public static int getStudentId(int screeningId){
        String[] columns = new String[] {ScreeningsEntry.STUDENT_ID};
        Cursor c = mDB.query(ScreeningsEntry.TABLE_NAME, columns, "_ID = " + screeningId, null, null, null, null);
        c.moveToNext();
        int studentId = c.getInt(0);
        c.close();
        return studentId;
    }

    public static Cursor getValidAnswersInCorrectLanguage(long questionId){
        String columns[];
        String tableName;
        String idColumn;
        if (Utilities.getQuestionsLanguage() == Utilities.ENGLISH){
            columns = new String[] {ValidAnswersEgEntry.TEXT};
            tableName = ValidAnswersEgEntry.TABLE_NAME;
            idColumn = ValidAnswersEgEntry.QUESTION_ID;
        }else{
            columns = new String[] {ValidAnswersSpEntry.TEXT};
            tableName = ValidAnswersSpEntry.TABLE_NAME;
            idColumn = ValidAnswersSpEntry.QUESTION_ID;
        }
        return mDB.query(tableName, columns, idColumn + " = " + questionId, null, null, null, null);
    }

    public static String getStudentNameStringFromScreeningId(long screeningId){
        String[] columns = new String[] {ScreeningsEntry.STUDENT_ID};
        Cursor c = mDB.query(ScreeningsEntry.TABLE_NAME, columns, ScreeningsEntry._ID + "=" + screeningId, null, null, null, null);
        c.moveToNext();
        long studentId = c.getLong(0);
        c.close();
        columns = new String[] {StudentsEntry.FIRST_NAME, StudentsEntry.LAST_NAME};
        c = mDB.query(StudentsEntry.TABLE_NAME, columns, StudentsEntry._ID + "=" + screeningId, null, null, null, null);
        c.moveToNext();
        String name = c.getString(0) + " " + c.getString(1);
        c.close();
        return name;
    }

    public static int getScreeningCompletionState(int screeningId){
        String[] columns = new String[] {ScreeningsEntry.COMPLETION_STATE};
        Cursor c = mDB.query(ScreeningsEntry.TABLE_NAME, columns, "_ID = " + screeningId, null, null, null, null);
        c.moveToNext();
        int completionState = c.getInt(0);
        c.close();
        return completionState;
    }

    public static int getFirstQuestion(long questionCategoryId){
        String sql = "SELECT MIN ( _ID)  FROM " + QuestionsEntry.TABLE_NAME +  " WHERE " + QuestionsEntry.CATEGORY_ID + " = " + questionCategoryId;
        Cursor  c = mDB.rawQuery(sql, null);
        c.moveToNext();
        int questionId = c.getInt(0);
        c.close();
        return questionId;
    }

    /*public static int getLastAnsweredQuestionId(long screeningId, int categoryType){
        String sql = "SELECT MAX (" + StudentAnswersEntry.QUESTION_ID + ") FROM " + StudentAnswersEntry.TABLE_NAME +
                     " WHERE " + StudentAnswersEntry.SCREENING_ID + " = " +  screeningId +
                     " AND " + StudentAnswersEntry.CATEGORY_TYPE + " = " + categoryType;
        Cursor c = mDB.rawQuery(sql, null);
        int questionId;
        if (c.getCount() > 0){
            c.moveToNext();
            questionId = c.getInt(0);
            c.close();
            return questionId;
        }else{
            c.close();
            return -1;
        }
    }

    public static int getFirstQuestionOfCategoryType(int categoryType){
        String sql = "SELECT MIN ( _ID) FROM " + QuestionCategoriesEntry.TABLE_NAME + " WHERE "+ QuestionCategoriesEntry.CATEGORY_TYPE + " = " + categoryType;
        Cursor c = mDB.rawQuery(sql , null);
        int categoryId;
        if (c.getCount() > 0){
            c.moveToNext();
            categoryId = c.getInt(0);
        }else{
            categoryId = 0;
        }
        Log.i(TAG, "category id= " + categoryId);
        c.close();
        return getFirstQuestion(categoryId);
    }*/

    public static Cursor getQuestionData(long questionId){
        String[] columns = new String[] {QuestionsEntry.CATEGORY_ID, QuestionsEntry.TEXT_ENGLISH, QuestionsEntry.TEXT_SPANISH,
                                         QuestionsEntry.AUDIO_ENGLISH, QuestionsEntry.AUDIO_SPANISH};
        return mDB.query(QuestionsEntry.TABLE_NAME, columns, "_ID=" +  questionId, null, null, null, null);
    }

    public static Cursor getPictureFilenames(long questionId){
        String[] columns = new String[] {PicturesEntry.FILENAME};
        return mDB.query(PicturesEntry.TABLE_NAME, columns, PicturesEntry.QUESTION_ID + " = " + questionId, null, null, null, null);
    }

    public static Cursor getIconFilenames(long questionId){
        String[] columns = new String [] {AnswerIconEntry._ID, AnswerIconEntry.FILENAME};
        return mDB.query(AnswerIconEntry.TABLE_NAME, columns, AnswerIconEntry.QUESTION_ID + " = " + questionId, null, null, null, null);
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

    public static int getNumberOfQuestionsForScreeningCategoryId(long screeningCategoryId){
        int total = 0;
        String sql;
        String[] columns = new String[] {"_ID"};
        Cursor categoryIds = mDB.query(QuestionCategoriesEntry.TABLE_NAME, columns, QuestionCategoriesEntry.SCREENING_CATEGORY_ID + "=" + screeningCategoryId, null, null, null,null);
        while (categoryIds.moveToNext()){
            sql = "SELECT COUNT(*) FROM " + QuestionsEntry.TABLE_NAME + " WHERE " + QuestionsEntry.CATEGORY_ID + " = " + categoryIds.getInt(0);
            Cursor count = mDB.rawQuery(sql , null);
            count.moveToNext();
            total += count.getInt(0);
            count.close();
        }
        categoryIds.close();
        return total;
    }

    public static long insertStudent(long studentId, String firstName, String lastName, String date_of_birth, String hearingScreenDate,
                                     Boolean hearingPass, String visionScreenDate, boolean visionPass){

        ContentValues cv= new ContentValues();
        cv.put(StudentsEntry.FIRST_NAME , firstName);
        cv.put(StudentsEntry.LAST_NAME, lastName);
        cv.put(StudentsEntry.BIRTHDAY, Utilities.getLongDate(date_of_birth));
        cv.put(StudentsEntry.HEARING_TEST_DATE, Utilities.getLongDate(hearingScreenDate));
        cv.put(StudentsEntry.HEARING_PASS, hearingPass);
        cv.put(StudentsEntry.VISION_TEST_DATE, Utilities.getLongDate(visionScreenDate));
        cv.put(StudentsEntry.VISION_PASS, visionPass);

        if (studentId == -1) {
            return mDB.insert(StudentsEntry.TABLE_NAME, null, cv);
        }else{
            mDB.update(StudentsEntry.TABLE_NAME, cv, StudentsEntry._ID + " = " + studentId, null);
            return studentId;
        }
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

        String sql = "SELECT _ID " +
                     " FROM " + ScreeningsEntry.TABLE_NAME +
                     " WHERE " + ScreeningsEntry.STUDENT_ID + "=" + student_id;
        Cursor c = mDB.rawQuery(sql, null);
        if (c.getCount() == 0) {
            mDB.insert(ScreeningsEntry.TABLE_NAME, null, cv);
        }else{
            c.moveToNext();
            mDB.update(ScreeningsEntry.TABLE_NAME, cv, ScreeningsEntry._ID + "=" + c.getLong(0), null);
        }
    }

    public static void enterAnswerText(long answerId, ArrayList<StudentAnswerText> alAnswerText){
        ContentValues cv = new ContentValues();
        cv.put (StudentAnswersTextEntry.ANSWER_ID, answerId);
        for (int i = 0; i < alAnswerText.size(); i++){
            cv.put (StudentAnswersTextEntry.ANSWER_NUMBER, alAnswerText.get(i).getAnswerNumber());
            cv.put (StudentAnswersTextEntry.TEXT, alAnswerText.get(i).getText());
            mDB.insert(StudentAnswersTextEntry.TABLE_NAME, null, cv);
        }
    }

    public static long enterAnswer(long question_id, long screening_id, boolean correct, long screeningCategoryId){
        ContentValues cv = new ContentValues();
        cv.put(StudentAnswersEntry.QUESTION_ID, question_id);
        cv.put(StudentAnswersEntry.SCREENING_ID, screening_id);
        cv.put(StudentAnswersEntry.CORRECT, correct);
        cv.put(StudentAnswersEntry.SCREENING_CATEGORY_ID, screeningCategoryId);

        String sql = "SELECT _ID " +
                     " FROM " + StudentAnswersEntry.TABLE_NAME +
                     " WHERE " + StudentAnswersEntry.QUESTION_ID + "=" + question_id +
                      " AND " + StudentAnswersEntry.SCREENING_ID + "=" + screening_id;
        Cursor c_Answer = mDB.rawQuery(sql,null);
        long id;
        if (c_Answer.getCount() == 0) {
            id=mDB.insert(StudentAnswersEntry.TABLE_NAME, null, cv);
        }else{
            c_Answer.moveToNext();
            id = c_Answer.getLong(0);
            mDB.delete(StudentAnswersTextEntry.TABLE_NAME, StudentAnswersTextEntry.ANSWER_ID + "=" + id, null);
            mDB.delete(AnswerButtonsPressedEntry.TABLE_NAME, AnswerButtonsPressedEntry.ANSWER_ID + "=" + id, null);
            mDB.update(StudentAnswersEntry.TABLE_NAME, cv, "_ID=" + id, null);
        }
        c_Answer.close();

        updateScreeningCompletionState(screening_id, Utilities.SCREENING_NOT_COMPLETE);
        return id;
    }

    public static void insertAnswerText(long studentAnswerId, ArrayList<StudentAnswerText> alAnswerText){
        String sql = "INSERT INTO " + StudentAnswersTextEntry.TABLE_NAME +
                "(" +
                    StudentAnswersTextEntry.ANSWER_ID + "," +
                    StudentAnswersTextEntry.ANSWER_NUMBER + "," +
                    StudentAnswersTextEntry.TEXT  +
                ") VALUES (?,?,?)";
        SQLiteStatement statement = mDB.compileStatement(sql);
        mDB.beginTransaction();
        for(int i = 0; i < alAnswerText.size(); i++){
            statement.clearBindings();
            statement.bindLong(1 , studentAnswerId);
            statement.bindLong(2, alAnswerText.get(i).getAnswerNumber());
            statement.bindString(3, alAnswerText.get(i).getText());
            statement.execute();
        }
        mDB.setTransactionSuccessful();
        mDB.endTransaction();
    }

    public static void insertAnswerIconsPressed(long studentAnswerId, ArrayList<PressedIcon> alPressedIcons){
        String sql = "INSERT INTO " + AnswerButtonsPressedEntry.TABLE_NAME +
                "(" +
                    AnswerButtonsPressedEntry.ANSWER_ID + "," +
                    AnswerButtonsPressedEntry.ANSWER_ICONS_ID + "," +
                    AnswerButtonsPressedEntry.ANSWER_NUMBER +
                ") VALUES (?,?,?)";
        SQLiteStatement statement = mDB.compileStatement(sql);
        mDB.beginTransaction();
        for (int i=0; i < alPressedIcons.size(); i ++) {
            statement.clearBindings();
            statement.bindLong(1, studentAnswerId);
            statement.bindLong(2, alPressedIcons.get(i).getAnswerIconId());
            statement.bindLong(3, alPressedIcons.get(i).getAnswerNumber());
            statement.execute();
        }
        mDB.setTransactionSuccessful();
        mDB.endTransaction();

    }

    public static void updateScreeningCompletionState(long screening_id, int completion_state){
        Log.i(TAG, "updateScreeningCompletionState, screening id= " + screening_id);
        ContentValues cv = new ContentValues();
        cv.put(ScreeningsEntry.COMPLETION_STATE, completion_state);

        mDB.update(ScreeningsEntry.TABLE_NAME, cv, "_ID=" + screening_id, null);


    }
}

