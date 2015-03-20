package com.neuroleap.speachandlanguage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.neuroleap.speachandlanguage.Data.ScreeningContract.PicturesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionCategoriesEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningContract.QuestionsEntry;
import com.neuroleap.speachandlanguage.Data.ScreeningDbHelper;
import com.neuroleap.speachandlanguage.Models.Question;
import com.neuroleap.speachandlanguage.Models.QuestionCategory;

import java.util.ArrayList;
import java.util.List;


public class FlowControlActivity extends ActionBarActivity {

    private ScreeningDbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private List<QuestionCategory> mQuestionCategories = new ArrayList<QuestionCategory>();
    private List<Question> mQuestions = new ArrayList<Question>();
    private int mLangage = ENGLISH;

    private static final int ENGLISH = 0;
    private static final int SPANISH = 1;
    private static final String TAG = "## My Info ##";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_control);
        mDbHelper = new ScreeningDbHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        loadLists();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flow_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadLists(){
        String[] CategoryColumns = new String[] {"_ID", QuestionCategoriesEntry.CATEGORY_NAME};
    }

    private void checkDB(){
        String[] columns = new String[] {"_ID", QuestionCategoriesEntry.CATEGORY_NAME, QuestionCategoriesEntry.FACILITATOR_MODE_FRAGMENT,QuestionCategoriesEntry.STUDENT_MODE_FRAGMENT};
        String[] columns2 = new String[] {"_ID", QuestionsEntry.CATEGORY_ID, QuestionsEntry.TEXT_ENGLISH, QuestionsEntry.TEXT_SPANISH,
                QuestionsEntry.AUDIO_ENGLISH, QuestionsEntry.AUDIO_SPANISH, QuestionsEntry.PROMPT_ENGLISH, QuestionsEntry.PROMPT_SPANISH};
        String[] columns3 = new String[] {"_ID", PicturesEntry.QUESTION_ID, PicturesEntry.FILENAME};
        Cursor cursor = mDb.query(QuestionCategoriesEntry.TABLE_NAME, columns, null,null,null,null,null);
        int categoryId, questionId;
        String category;
        String fragName_fm;
        String fragname_sm;
        while (cursor.moveToNext()) {
            categoryId = cursor.getInt(0);
            category = cursor.getString((1));
            fragName_fm = cursor.getString(2);
            fragname_sm = cursor.getString(3);
            Log.i(TAG, "id= " + categoryId + "  category= " + category + "  Facilitator Fragment name= " + fragName_fm +"  Student Fragment Name= " + fragname_sm);
            Log.i(TAG, "########################################################");
            Cursor cursor2 = mDb.query(QuestionsEntry.TABLE_NAME, columns2, QuestionsEntry.CATEGORY_ID + "=" + categoryId, null, null, null, null);
            while (cursor2.moveToNext()){
                questionId = cursor2.getInt(0);
                int category_id = cursor2.getInt(1);
                String text_english = cursor2.getString(2);
                String text_spanish = cursor2.getString(3);
                String audio_english = cursor2.getString(4);
                String audio_spanish = cursor2.getString(5);
                String prompt_english = cursor2.getString(6);
                String prompt_spanish = cursor2.getString(7);
                Log.i(TAG, "questionId= " + questionId +" category id= " + category_id + " Text English= " + text_english +
                        " text Spanish= " + text_spanish + " audio english= " + audio_english +" audio spanish= " + audio_spanish
                        + " prompt english= " + prompt_english + " prompt spanish= " + prompt_spanish);
                Cursor cursor3 = mDb.query(PicturesEntry.TABLE_NAME, columns3, PicturesEntry.QUESTION_ID  + "=" + questionId, null, null, null,null);
                while (cursor3.moveToNext()) {
                    int answerId = cursor3.getInt(0);
                    int questionsId = cursor3.getInt(1);
                    String text = cursor3.getString(2);
                    Log.d(TAG, "picture id= " + answerId + "    question id= " + questionsId +"   filename: " + text);
                }
            }
        }
    }
}
