package com.example.quizapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domian.model.Answer
import com.example.quizapp.domian.model.Question
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Collections
import java.util.Date
import java.util.Timer
import kotlin.concurrent.timer

class AppViewModel : ViewModel() {
    var quizList = prepareQuizData()
    var currentTime: ((Long) -> Unit)? = null
    var time: Long = 0L
    var timer : Timer? = null
    var currentQuestionIndex = 0
    var selections = ArrayList<Int>()

    private fun prepareQuizData(): List<Question> {
        return Collections.unmodifiableList(
            listOf(
                Question(
                    1, "Space", "How Many Plant in our solar system?", listOf(
                        Answer(4), Answer(8), Answer(10),
                        Answer(6)
                    )
                ),
                Question(
                    2, "Genomics", "how many phase in metabolic reaction?", listOf(
                        Answer(7), Answer(9), Answer(3),
                        Answer(1)
                    )
                ),
                Question(
                    3, "Sport", "What is the result of egypt vs Cape-verd match?",
                    listOf(
                        Answer("2:0"),
                        Answer("8:1"),
                        Answer("2:2"),
                        Answer("2:3")
                    )
                ),
                Question(
                    4,
                    "Algorithm",
                    "what is the time complexity for naive suffix array implementation?",
                    listOf(
                        Answer("o(nLog(n))"),
                        Answer("o(o^2 Log(n))"),
                        Answer("o(o^2 Log^2(n))"),
                        Answer("o(oLog^2 (n))")
                    )
                ),
                Question(
                    5, "Algorithm", "What is the time Complexity for Bellman-ford search?", listOf(
                        Answer("o(v^3) , v is the number of vertices(nodes)"),
                        Answer("o(v^2LogV) , v is the number of vertices(nodes)"),
                        Answer("o(v^2) , v is the number of vertices(nodes)"),
                        Answer("o(vLogV) , v is the number of vertices(nodes)"),
                    )
                )
            )
        )
    }

    inline fun startCoroutineTimer(
        crossinline action: () -> Unit
    ) = CoroutineScope(viewModelScope.coroutineContext).launch {
        while (true) {
            action()
            delay(1000)
        }
    }


    fun initiateTime() {
        timer?.cancel()
        timer = timer(initialDelay = 1000L, period = 1000L) {
            CoroutineScope(viewModelScope.coroutineContext).launch {
                time++
                currentTime?.invoke(time)
            }
        }
    }
}