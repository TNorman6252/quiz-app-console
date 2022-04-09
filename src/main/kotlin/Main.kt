import controllers.QuestionAPI
import models.Question
import utils.ScannerInput
import utils.ScannerInput.readNextLine
import java.lang.Integer.parseInt

private val qAPI = QuestionAPI();

fun main(args: Array<String>) {
    runMenu();
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt("""
        > ----------------------------------
        > |        QUIZ APPLICATION        |
        > ----------------------------------
        > | QUIZ OPTIONS:                  |
        > |   1) Play Quiz Game            |
        > |   2) Add a Question            |   
        > |   3) Delete a Question         |  
        > |   4) Number of Questions in App |         
        > |   5) Update a Question          |
        > |   6) List Questions - (Sub-menu) |
        > |   7) Save Question(s) (NOT IMPLEMENTED YET) |
        > |   8) Load Question(s) (NOT IMPLEMENTED YET) |
        > ----------------------------------
        > |   0) Exit                      |
        > ----------------------------------
        > ==>> """.trimMargin(">"))
}

fun runMenu() {
    do {
        val option = mainMenu();
        when(option) {
       //     1 -> playGame();
            2 -> addQuestion();
        //    3 -> deleteQuestion();
         //   4 -> numOfQuestions();
         //   5 -> updateQuestion();
            6 -> listQuestions();
         //   7 -> saveQuestions();
        //    8 -> loadQuestions();
          //  0 -> exitApp();
            else -> println("Invalid choice entered: $option. Please try again!");
        }
    } while(true);
}

fun addQuestion() {
    while(true) {
        println("----- Adding a Question -----\n");

        var questionNumber = 0;
        while (true) {
            try {
                questionNumber = parseInt(readNextLine("Enter the Question Number here: "));
                break;
            } catch (nfe: NumberFormatException) {
                println("Please enter a number!");
            } catch (e : Exception) {
                println("Something went wrong!");
            }
        }

        var theQuestion = readNextLine("Enter the Question here: ");

        var possibleAnswers: Array<String?> = arrayOfNulls(4);

        for (i in 0 until possibleAnswers.size-1) {
            println("Possible Answer (" + (i + 1) + " of 3): ");
            println("------------------------------------------");
            possibleAnswers[i] = readNextLine("Enter possible answer here: ");
        }

        var answer = readNextLine("Enter the answer to the question here: ");

        var questionDifficultyLevel = 0;
        while (true) {
            try {
                questionDifficultyLevel = parseInt(readNextLine("Enter question difficulty level here (1-5):"));
                break;
            } catch (nfe: NumberFormatException) {
                println("Please enter a number!");
            } catch (e: Exception) {
                println("Something went wrong!");
            }
        }

        var isAdded =
            qAPI.addQuestion(Question(questionNumber, theQuestion, possibleAnswers, answer, questionDifficultyLevel));

        if (isAdded) {
            println("Successfully Added Question to System!");
        } else {
            println("Something went wrong! Please try again!");
        }

        println("------------------------------------------------------");

        var addAnotherQuestionAnswer = readNextLine("Would you like to add another question? (Y/N):");
        if(addAnotherQuestionAnswer.uppercase() != "Y") {
            println("Heading back to the main menu!");
            println("--------------------------------------")
            break;
        }
    }
}

fun listQuestions() {
    while(true) {
        println("QUIZ SUB-MENU: ");
        println("--------------------------------------");
        println(" 1) List all Questions");
        println(" 2) List Questions by difficulty (ascending order)")
        println(" 3) List Questions by difficulty (descending order)");
        println("---------------------------------------");
        println(" 0) Exit Sub-Menu");
        println("---------------------------------------");

        var userChoice = 0;
        try {
             userChoice = parseInt(readNextLine("Enter Choice [1-3]: "));
        } catch(nfe : NumberFormatException) {
            println("Please Enter a Number!");
        } catch(e : Exception) {
            println("Something went wrong!");
        }
        when (userChoice) {
            1 -> println("---You chose: List All Questions---\n" + qAPI.listAllQuestions());
            2 -> println("---You chose: List by Ascending Order---\n" + qAPI.allQuestionsByAscendingDifficulty());
            3 -> println("---You chose: List by Descending Order---\n" + qAPI.allQuestionsByDescendingDifficulty());
            0 -> break;
            else -> println("Invalid choice entered!");
        }
    }

}






