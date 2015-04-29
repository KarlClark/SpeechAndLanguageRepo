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
        public static final String AGE = "age";
        public static final String MENTAL_AGE = "mental_age";
        public static final String GRADE = "grade";
        public static final String TEACHER ="teacher";
        public static final String ROOM = "room";
        public static final String TEST_MODE = "test_mode";
        public static final String COMPLETION_STATE = "completion_state";
        public static final String LANGUAGE = "language";
    }

    public static final class StudentAnswersEntry implements BaseColumns {

        public static final String TABLE_NAME = "student_answers";

        public static final String QUESTION_ID = "question_id";
        public static final String SCREENING_ID = "screening_id";
        public static final String CORRECT = "correct";
        public static final String CATEGORY_TYPE = "category_type";
    }

    public static final class StudentAnswersTextEntry implements BaseColumns{

        public static final String TABLE_NAME = "student_answers_text";

        public static final String ANSWER_ID = "answer_id";
        public static final String ANSWER_NUMBER = "answer_number";
        public static final String TEXT = "text";
    }

    public static final class AnswerButtonsPressedEntry implements BaseColumns {

        public static final String TABLE_NAME = "answer_buttons_pressed";

        public static final String ANSWER_ID = "answer_id";
        public static final String ANSWER_ICONS_ID = "answer_icons_id";
        public static final String ANSWER_NUMBER = "answer_number";
    }

    public static final class QuestionCategoriesEntry implements BaseColumns{

        public static final int SEMANTICS = 0;
        public static final int PROCESSING = 1;
        public static final int INFERENCES = 2;
        public static final int IDIOMS = 3;
        public static final int  SYNTAX = 4;
        public static final int AUDITORY_PROCESSING = 5;
        public static final int AUDITORY_MEMORY = 6;
        public static final int UNKNOWN = 7;

        public static final String TABLE_NAME = "question_categories";

        public static final String CATEGORY_NAME_EG = "category_name_eg";
        public static final String CATEGORY_NAME_SP = "category_name_sp";
        public static final String FRAGMENT_NAME ="fragment_name";
        public static final String CUTOFF_AGE = "cutoff_age";
        public static final String CATEGORY_TYPE = "category_type";
    }

    public static final class QuestionsEntry implements BaseColumns {

        public static final String TABLE_NAME = "questions";

        public static final String CATEGORY_ID = "category_id";
        public static final String TEXT_ENGLISH = "text_english";
        public static final String TEXT_SPANISH = "test_spanish";
        public static final String AUDIO_ENGLISH = "audio_english";
        public static final String AUDIO_SPANISH = "audio_spanish";
        public static final String PROMPT_ENGLISH = "prompt_english";
        public static final String PROMPT_SPANISH = "prompt_spanish";
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

    public static final class AnswerIconEntry implements BaseColumns {

        public static final String TABLE_NAME = "answer_icons";

        public static final String QUESTION_ID = "question_id";
        public static final String DESCRIPTION = "description";
        public static final String FILENAME = "filename";

    }
}
