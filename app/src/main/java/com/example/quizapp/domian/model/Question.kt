package com.example.quizapp.domian.model

data class Question(
    val questionNumber: Int,
    val headLine: String,
    val QuestionText: String,
    val answers: List<Answer>
)

data class Answer(
    val answ : Any,
    var selectd : Boolean = false
)