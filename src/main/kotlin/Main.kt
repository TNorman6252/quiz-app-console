import controllers.QuestionAPI
import models.Question
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextLine
import java.io.File
import java.io.LineNumberReader
import java.lang.Integer.parseInt
import java.lang.System.currentTimeMillis
import java.lang.System.exit

//private val qAPI = QuestionAPI(XMLSerializer(File("questions.xml")));
private val qAPI = QuestionAPI(XMLSerializer(File("questions.json")));


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
        > |   6) List Questions - (Sub-menu)|
        > |   7) Save Question(s)           |
        > |   8) Load Question(s)           |
        > ----------------------------------
        > |   0) Exit                       |
        > ----------------------------------
        > ==>> """.trimMargin(">"))
}

fun runMenu() {
    do {
        val option = mainMenu();
        when(option) {
            1 -> playGame();
            2 -> addQuestion();
            3 -> deleteQuestion();
            4 -> numOfQuestions();
            5 -> updateQuestion();
            6 -> listQuestions();
            7 -> save();
            8 -> load();
            0 -> exitApp();
            else -> println("Invalid choice entered: $option. Please try again!");
        }
    } while(true);
}

fun playGame() {

    if(qAPI.numberOfQuestions() == 0) {
        println("There are no Questions in the System. Add one!");
        return;
    }

    println("---------------");
    println("PLAY GAME!!");
    println("---------------");

    try {

        var numOfQuestions = 0;

            do {
                numOfQuestions = parseInt(readNextLine("Enter how many questions you want to be asked? (1-10): "));

                if(numOfQuestions <= 0) {
                    println("Number is too low. Please try again!");
                } else if(numOfQuestions > 10) {
                    println("Number is too high. Please try again!");
                } else if(qAPI.numberOfQuestions() < numOfQuestions) {
                    println("There are/is only ${qAPI.numberOfQuestions()} question(s) in the System. You will need to choose a lower number for each question to be unique!");
                }
                else {
                    println("GET READY TO PLAY THE GAME!");
                    println("-----------------------------------");

                    Thread.sleep(1000);
                    println("3!")
                    Thread.sleep(1000);
                    println("2!");
                    Thread.sleep(1000);
                    println("1!");
                    println("GO!")
                    println("-----------------------------------");


                    var numberOfCorrectAnswers = 0;
                    var timeBeforeGuess = 0;
                    var timeAfterGuess = 0;

                    for(i in 1..numOfQuestions) {
                        var question = qAPI.randomQuestion();
                        println("Question: $i");
                        println("----------------------------");
                        if (question != null) {
                            println("Q: " + question.theQuestion);
                            println("Difficulty Level: " + question.questionDifficultyLevel);
                            for(i in 0..3) {
                                println("Possible Answer " + (i+1) + ": " + question.possibleAnswers[i]);
                            }

                                timeBeforeGuess = currentTimeMillis().toInt();
                                var userGuess = readNextLine("Enter Question Guess (Word-for-Word): ");
                                timeAfterGuess = currentTimeMillis().toInt();

                                if(userGuess == question.questionAnswer) {
                                    numberOfCorrectAnswers++;
                                    println("CORRECT!");
                                    println("It took you: ${(timeAfterGuess - timeBeforeGuess) / 1000} seconds to answer that question!");
                                    qAPI.removeQuestion(question.questionNumber-1); // removes question to stop multiple occurrences
                                    println("------------------------------------");
                                } else {
                                    println("INCORRECT, SORRY!");
                                    qAPI.removeQuestion(question.questionNumber-1); // removes question to stop multiple occurrences
                                    println("------------------------------------");
                                }
                            println("Moving onto the next question....");
                            println("------------------------------------");

                        } else {
                            println("Something went wrong!");
                        }

                    }

                    println("You got a total of $numberOfCorrectAnswers / $numOfQuestions");
                    println("---------------------------------------------------------------");
                }

            } while(numOfQuestions <= 0 || numOfQuestions > 10);


    } catch(nfe: NumberFormatException) {
        println("Please enter a Number!");
    } catch(e : Exception) {
        println("Something went wrong!");
    }
}

fun addQuestion() {
    while(true) {
        println("----- Adding a Question -----\n");

        var questionNumber = 0;
        while (true) {
            try {
               // questionNumber = parseInt(readNextLine("Enter the Question Number here: "));
                questionNumber = qAPI.numberOfQuestions()+1;
                break;
            } catch (nfe: NumberFormatException) {
                println("Please enter a number!");
            } catch (e : Exception) {
                println("Something went wrong!");
            }
        }

        var theQuestion = "";
        while(theQuestion.isEmpty()) {
             theQuestion = readNextLine("Enter the Question here: ");
        }
        var possibleAnswers: Array<String?> = arrayOfNulls(4);

        for (i in 0 until possibleAnswers.size) {
            println("Possible Answer (" + (i + 1) + " of 4): ");
            println("------------------------------------------");
            possibleAnswers[i] = readNextLine("Enter possible answer here: ");

            while(possibleAnswers[i]?.isEmpty() == true) {
                possibleAnswers[i] = readNextLine("Please enter something for Possible Answer " + (i+1) + ": ");
            }
        }

        var answer = "";
        while(answer.isEmpty()) {
             answer = readNextLine("Enter the answer to the question here: ");
        }

        var questionDifficultyLevel = 0;
        while (true) {
            try {
                questionDifficultyLevel = parseInt(readNextLine("Enter question difficulty level here (1-5):"));
                if(questionDifficultyLevel < 1 || questionDifficultyLevel > 5 || questionDifficultyLevel == 0) { // if it equals zero, that means the user didn't enter a value and won't break out of the loop (user validation)
                    println("Please enter a number between 1 and 5!");
                } else {
                    break;
                }
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


fun deleteQuestion() {

    if(qAPI.numberOfQuestions() == 0) {
        println("There are no questions in the System. Add one!");
        return;
    }

    while(true) {
    println("--- Deleting a Question ---");
    println("---------------------------------");
    println("List of all Questions: ");
    println(qAPI.listAllQuestions());
    println("---------------------------------");

        var choice = 0;
    try {
         choice = parseInt(readNextLine("Enter Question Number to be deleted: "));

        if(qAPI.removeQuestion(choice-1) != null) {
            println("Question has been successfully deleted!");

            if(qAPI.numberOfQuestions() == 0) {
                break;
            } else {
                var option = readNextLine("Would you like to delete another question? (Y/N): ");
                if(option.uppercase() != "Y") {
                    break;
                }
            }

        } else {
            println("Invalid question number was entered. Please try again!");
        }

    } catch(nfe: NumberFormatException) {
        println("Please Enter a Number!");
    } catch(e : Exception) {
        println("Something went wrong!");
    }


   }

}

fun numOfQuestions() {
    println("Number of Questions stored in app: ${qAPI.numberOfQuestions()}");
    println("-----------------------------------------------------------------");
}

fun updateQuestion() {

    if(qAPI.numberOfQuestions() == 0) {
        println("There are no Questions in the System. Add one!");
    } else {

        println("All questions listed below:\n ${qAPI.updateQuestionObjectFormat()}");

        try {
            var indexToUpdate = parseInt(readNextLine("Enter the index of the Question to update here: "));

            if (qAPI.isValidIndex(indexToUpdate-1)) {

                var questionObject = qAPI.findQuestion(indexToUpdate-1);
             //   var questionNumber = parseInt(readNextLine("Enter question number to update: "));
                var questionNumber = questionObject?.questionNumber;

                var question = "";
                while(question.isEmpty()) {
                    question = readNextLine("Enter question to update: ");
                }

                var possibleAnswers: Array<String?> = arrayOfNulls(4);

                for (i in 0 until possibleAnswers.size) {
                    println("Possible Answer (" + (i + 1) + " of 4): ");
                    println("------------------------------------------");
                    possibleAnswers[i] = readNextLine("Enter possible answer to update: ");

                    while(possibleAnswers[i]?.isEmpty() == true) {
                        possibleAnswers[i] = readNextLine("Please enter something for Possible Answer " + (i+1) + ": ");
                    }
                }

                var answer = "";
                while(answer.isEmpty()) {
                     answer = readNextLine("Enter the answer to update: ");
                }

                var questionDifficultyLevel = 0;

                while(true) {

                    try {

                        questionDifficultyLevel = parseInt(readNextLine("Enter difficulty level (1-5) to update: "));

                        if (questionDifficultyLevel < 1 || questionDifficultyLevel > 5 || questionDifficultyLevel == 0) { // if it equals zero, that means the user didn't enter a value and won't break out of the loop (user validation)
                            println("Please enter a number between 1 and 5!");
                        } else {
                            break;
                        }
                    } catch (nfe: NumberFormatException) {
                        println("Please enter a Number!");
                    } catch (e: Exception) {
                        println("Something went wrong!");
                    }
                }




                        if (qAPI.updateQuestion(
                                indexToUpdate - 1,
                                questionNumber?.let { Question(it, question, possibleAnswers, answer, questionDifficultyLevel) }
                            )
                        ) {
                            println("Successfully updated Question!");
                        } else {
                            println("Something went wrong!");
                        }



            } else {
                println("Invalid Question Index. Returning to Main Menu!");
            }

        } catch (nfe: NumberFormatException) {
            println("Please Enter a Number!");
        } catch (e: java.lang.Exception) {
            println("Something went wrong!");
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


fun save() {
    try {
        qAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        qAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}


fun exitApp() {
    println("Exiting application. We hope to see you again soon!");
    Thread.sleep(1000);
    println("3");
    Thread.sleep(1000);
    println("2");
    Thread.sleep(1000);
    println("1");
    println("-----------------------------------------------------");
    exit(0);
}






