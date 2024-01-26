package com.example.quizapp.presentation.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import com.example.quizapp.R
import com.example.quizapp.commons.getCachedObj
import com.example.quizapp.databinding.FragmentHomeBinding
import com.example.quizapp.presentation.AppViewModel
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat
import java.util.Date

class HomeFragment : Fragment() {
    private val appViewModel: AppViewModel by viewModels()
    lateinit var binding: FragmentHomeBinding
    lateinit var controller: NavController
    lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = Navigation.findNavController(requireView())

        binding.quizBtn.setOnClickListener {
            controller.navigate(R.id.action_homeFragment_to_quizFragment)
        }

        val result = getCachedObj(requireContext())
        if (result != null)
            binding.subtext.text = "Your Last Record is ${result.score}, you take it on\n ${
                SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(Date(result.date))
            }"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


}