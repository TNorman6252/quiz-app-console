package models

data class Question(
    var questionNumber: Int,
    var theQuestion: String,
    var possibleAnswers : Array<String>,
    var questionAnswer: String,
    var questionDifficultyLevel: Int
){
}