package com.example.quizapp.domian.model

import java.io.Serializable

data class Result(val score:Int , val date: Long , val selectoions : List<Int>) : Serializable