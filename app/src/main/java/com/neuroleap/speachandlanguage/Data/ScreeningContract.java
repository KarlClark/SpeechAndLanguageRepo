package com.neuroleap.speachandlanguage.Data;

import android.provider.BaseColumns;

/**
 * Created by Karl on 3/5/2015.
 */
public class ScreeningContract {

    /* Inner class that defines the table contents of the Students table */
    public static final class StudentsEntry implements BaseColumns {

        public static final String TABLE_NAME = "students";

        public static final String FIRST_NAME ="first_name";
        public static final String LAST_NAME = "last_name";
        public static final String TEACHER ="teacher";
        public static final String BIRTHDAY = "birthday";
        public static final String HEARING_TEST_DATE = "hearing_test_date";
        public static final String VISION_TEST_DATE = "vision_test_date";
        public static final String HEARING_PASS = "hearing_pass";
        public static final String VISION_PASS = "vision_pass";
    }

    public static final class ScreeningsEntry implements BaseColumns {

        public static final String TABLE_NAME = "screenings";

        public static final String STUDENT_ID = "student_id";
        public static final String TEST_DATE = "test_date";
    }

    public static final class StudentAnswersEntry implements BaseColumns {

        public static final String TABLE_NAME = "student_answers";

        public static final String QUESTION_ID = "question_id";
        public static final String SCREENING_ID = "screening_id";
        public static final String ANSWER_TEXT = "answer_text";
        public static final String CORRECT = "correct";
    }

    public static final class QuestionCategoriesEntry implements BaseColumns{

        public static final String TABLE_NAME = "question_categories";

        public static final String CATEGORY_NAME = "category_name";
        public static final String FRAGMENT_NAME ="fragment_name";
    }

    public static final class QuestionsEntry implements BaseColumns {

        public static final String TABLE_NAME = "questions";

        public static final String CATEGORY_ID = "category";
        public static final String TEXT_ENGLISH = "text_english";
        public static final String TEXT_SPANISH = "test_spanish";
        public static final String AUDIO_ENGLISH = "audio_english";
        public static final String AUDIO_SPANISH = "audio_spanish";
    }

    public static final class ValidAnswersEgEntry implements BaseColumns {

        public static final String TABLE_NAME ="valid_answers_eg";

        public static final String QUESTION_ID ="question_id";
        public static final String TEXT ="text";
    }

    public static final class ValidAnswersSpEntry implements BaseColumns {

        public static final String TABLE_NAME ="valid_answers_sp";

        public static final String QUESTION_ID ="question_id";
        public static final String TEXT ="text";
    }

    public static final class PicturesEntry implements BaseColumns {

        public static final String TABLE_NAME = "pictures";

        public static final String QUESTION_ID = "question_id";
        public static final String FILENAME = "filename";
    }
}
