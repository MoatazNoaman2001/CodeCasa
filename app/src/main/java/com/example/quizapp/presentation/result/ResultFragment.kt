package com.example.quizapp.presentation.result

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizapp.R
import com.example.quizapp.commons.CachingResultForFutureShow
import com.example.quizapp.databinding.FragmentResultBinding
import com.example.quizapp.domian.model.Result
import com.example.quizapp.presentation.AppViewModel
import com.google.android.material.appbar.MaterialToolbar
import java.util.Date
import java.util.stream.IntStream


private const val TAG = "ResultFragment"

class ResultFragment : Fragment() {
    lateinit var binding: FragmentResultBinding
    lateinit var controller: NavController
    private val viewModel: AppViewModel by viewModels()
    lateinit var toolbar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(layoutInflater)
        toolbar = requireActivity().findViewById(R.id.toolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = Navigation.findNavController(requireView())
        setOnBackPressHandler()
        setResultFromViewModel()
    }

    private fun setResultFromViewModel() {
        val correctResultIndex = listOf(1, 2, 2, 3, 0)
//        Log.d(TAG, "setResultFromViewModel: ${viewModel.selections.joinToString()}")
//        val predicted = viewModel.quizList.map { question ->
//            question.answers.map {
//                if (it.selectd) question.answers.indexOf(it) else -1
//            }.first { it != -1 }
//        }.toList()

        val predicted = controller.previousBackStackEntry?.savedStateHandle?.get<ArrayList<Int>>("selections")
        Log.d(TAG, "setResultFromViewModel: ${predicted?.joinToString()}")
        var result = 0
        for ((index, i) in predicted?.withIndex()!!) {
            if (i == correctResultIndex[index])
                result++
        }
        if (result > 3) {
            binding.headLine.text = "Congratulation"
            binding.subtitle?.text = "your current achievement is $result/5"
        } else {
            binding.headLine.text = "Bad Luck"
            binding.logo?.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.fail, requireActivity().theme))
            binding.subtitle?.text = "your current achievement is $result/5"
        }

        CachingResultForFutureShow(requireContext() , Result(result , Date(System.currentTimeMillis()).time , predicted))
    }

    private fun setOnBackPressHandler() {
        toolbar.setNavigationOnClickListener {
            controller.popBackStack()
            controller.popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    controller.popBackStack()
                    controller.popBackStack()
                }
            })
    }
}