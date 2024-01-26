package com.example.quizapp.presentation.quiz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuizBinding
import com.example.quizapp.domian.model.Question
import com.example.quizapp.presentation.AppViewModel
import com.google.android.material.appbar.MaterialToolbar

private const val TAG = "QuizFragment"

class QuizFragment : Fragment() {

    lateinit var controller: NavController
    lateinit var toolbar: MaterialToolbar
    lateinit var binding: FragmentQuizBinding
    private val viewModel: AppViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(layoutInflater)
        toolbar = requireActivity().findViewById(R.id.toolbar)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = Navigation.findNavController(requireView())
        setQuestion(viewModel.quizList[viewModel.currentQuestionIndex], binding)

        viewModel.currentTime = {
            Log.d(TAG, "onViewCreated: $it")
            setTimeAndProgress(it)
            if (it == 60L) {
                nextQuestion()
            }
        }


        binding.RadioGroup.setOnCheckedChangeListener { target, checkedId ->
            Log.d(TAG, "onViewCreated: $checkedId")
            if (checkedId != -1) {
                binding.continueBtn.isVisible = true
                viewModel.quizList[viewModel.currentQuestionIndex].answers[
                    target.indexOfChild(requireActivity().findViewById<RadioButton>(checkedId))
                ].selectd = true


                Log.d(TAG, "onViewCreated: ${viewModel.selections.joinToString()}")
                binding.continueBtn.setOnClickListener {
                    viewModel.selections.add(
                        target.indexOfChild(
                            requireActivity().findViewById<RadioButton>(
                                checkedId
                            )
                        )
                    )
                    nextQuestion()
                    binding.RadioGroup.clearCheck()
                }
            }
        }

        setSelection()
        addBackBtnListener()
    }

    private fun nextQuestion() {
        if (viewModel.quizList.last() == viewModel.quizList[viewModel.currentQuestionIndex]) {
            controller.currentBackStackEntry?.savedStateHandle?.set(
                "selections",
                viewModel.selections
            )
            viewModel.timer?.cancel()
            controller.navigate(R.id.action_quizFragment_to_resultFragment)
        } else {
            viewModel.currentQuestionIndex++
            setQuestion(viewModel.quizList[viewModel.currentQuestionIndex], binding)
            binding.continueBtn.isVisible = false
            resetTime()
        }
    }

    private fun addBackBtnListener() {
        toolbar.setNavigationOnClickListener {
            handleBackBtnClicked()
        }
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackBtnClicked()
                }
            })
    }

    fun handleBackBtnClicked() {
        if (0 == viewModel.currentQuestionIndex) {
            controller.popBackStack()
        } else {
            viewModel.currentQuestionIndex--
            setQuestion(viewModel.quizList[viewModel.currentQuestionIndex], binding)
            setSelection()
        }
    }


    private fun setSelection() {
        for ((index, answer) in viewModel.quizList[viewModel.currentQuestionIndex].answers.withIndex()) {
            if (answer.selectd)
                when (index) {
                    0 -> binding.ans1.isChecked = true
                    1 -> binding.ans2.isChecked = true
                    2 -> binding.ans3.isChecked = true
                    3 -> binding.ans4.isChecked = true
                }
        }
    }

    private fun resetTime() {
        viewModel.time = 0L
        viewModel.initiateTime()
    }

    private fun setTimeAndProgress(it: Long) {
        val sec = it % 60
        val min = it / 60

        binding.currentTime.text =
            "${if (min > 9) min else "0$min"}:${if (sec > 9) sec else "0$sec"}"
        binding.timeProgress.max = 60
        binding.timeProgress.progress = it.toInt()
    }

    private fun setQuestion(question: Question, binding: FragmentQuizBinding) {
        binding.questionPanel.text = question.QuestionText
        binding.ans1.text = question.answers[0].answ.toString()
        binding.ans2.text = question.answers[1].answ.toString()
        binding.ans3.text = question.answers[2].answ.toString()
        binding.ans4.text = question.answers[3].answ.toString()
        binding.headLine?.text = "${question.questionNumber}\t${question.headLine}"

        viewModel.initiateTime()
    }
}