package controllers

import models.Question
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.XMLSerializer
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class QuestionAPITest {

    private var testQuestion: Question? = null;
    private var testQuestion2: Question? = null;
    private var testQuestion3: Question? = null;

    private var possibleAnswers1 = arrayOf<String?>("8 million", "16 million", "4.8 billion", "500 million");
    private var possibleAnswers2 = arrayOf<String?>("8 million", "16 million", "13.8 billion", "500 million");
    private var possibleAnswers3 = arrayOf<String?>("8 million", "16 million", "4.8 billion", "500 million");





    private var questionStore: QuestionAPI? = QuestionAPI(XMLSerializer(File("questions.xml")));
    private var questionStoreEmpty: QuestionAPI? = QuestionAPI(XMLSerializer(File("questions.xml")));
    private var questionStoreListing: QuestionAPI? = QuestionAPI(XMLSerializer(File("questions.xml")));


    @BeforeEach
    fun setup() {
        testQuestion = Question(1, "How old is Earth?", possibleAnswers1, "4.8 billion", 2);
        testQuestion2 = Question(2, "How old is the universe?", possibleAnswers2, "13.8 billion", 4);
        testQuestion3 = Question(3, "What is the capital of Ireland?", possibleAnswers3, "Dublin", 1);

        //adding Question objects to QuestionAPI
        questionStore!!.addQuestion(testQuestion!!);
        questionStore!!.addQuestion(testQuestion2!!);
        questionStore!!.addQuestion(testQuestion3!!);
    }

    @AfterEach
    fun tearDown() {
        testQuestion = null;
        testQuestion2 = null;
        testQuestion3 = null;
        questionStore = null;
        questionStoreEmpty = null;
    }


    @Nested
    inner class AddQuestion {
        @Test
        fun `adding A Question Object to a populated ArrayList`() {
            var possibleAnswers4 = arrayOf<String?>("Paris", "London", "Tokyo", "Melbourne");
            val testQuestion4 = Question(1, "What is the capital of France?", possibleAnswers4, "Paris", 1);

            assertEquals(3, questionStore!!.numberOfQuestions());
            assertTrue(questionStore!!.addQuestion(testQuestion4));
            assertEquals(4, questionStore!!.numberOfQuestions());
        }

        @Test
        fun `adding A Question Object to an empty ArrayList`() {
            var possibleAnswers4 = arrayOf<String?>("Paris", "London", "Tokyo", "Melbourne");
            val testQuestion4 = Question(1, "What is the capital of France?", possibleAnswers4, "Paris", 1);

            assertEquals(0, questionStoreEmpty!!.numberOfQuestions());
            assertTrue(questionStoreEmpty!!.addQuestion(testQuestion4));
            assertEquals(1, questionStoreEmpty!!.numberOfQuestions());
        }
    }


    @Nested
    inner class RemoveQuestion {
        @Test
        fun `removing A Question Object from a populated ArrayList`() {
            var possibleAnswers4 = arrayOf<String?>("Paris", "London", "Tokyo", "Melbourne");
            val testQuestion4 = Question(1, "What is the capital of France?", possibleAnswers4, "Paris", 1);

            assertEquals(3, questionStore!!.numberOfQuestions());
            assertTrue(questionStore!!.addQuestion(testQuestion4));
            assertEquals(4, questionStore!!.numberOfQuestions());
            //important line below:
            assertNotNull(questionStore!!.removeQuestion(0));
            assertEquals(3, questionStore!!.numberOfQuestions());
        }
    }

    @Nested
    inner class ListingQuestions {
        @Test
        fun `listing questions in empty array`() {
            var possibleAnswers4 = arrayOf<String?>("Paris", "London", "Tokyo", "Melbourne");
            val testQuestion4 = Question(1, "What is the capital of France?", possibleAnswers4, "Paris", 1);
            var result : String = "There are no Questions in the System. Add one!";
            assertEquals(result, questionStoreListing!!.listAllQuestions());
        }
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `persistence app crashing test method with an empty file`() {
            // Saving an empty questions.XML file.
            val questionStoring = QuestionAPI(XMLSerializer(File("questions.xml")))
            questionStoring.store()

            //Loading the empty questions.xml file into a new object
            val loadedQuestions = QuestionAPI(XMLSerializer(File("questions.xml")))
            loadedQuestions.load()

            assertEquals(0, questionStoring.numberOfQuestions())
            assertEquals(0, loadedQuestions.numberOfQuestions())
            assertEquals(questionStoring.numberOfQuestions(), loadedQuestions.numberOfQuestions())
        }


    }

}