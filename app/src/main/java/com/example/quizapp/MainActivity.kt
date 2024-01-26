package com.example.quizapp

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.quizapp.databinding.ActivityMainBinding
import com.example.quizapp.domian.model.Answer
import com.example.quizapp.domian.model.Question
import java.util.Collections

class MainActivity : AppCompatActivity() {
    lateinit var controller: NavController
    lateinit var binding: ActivityMainBinding
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var permissionGuaranty: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        controller = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration.Builder(controller.graph).build()

        NavigationUI.setupWithNavController(binding.toolbar, controller, appBarConfiguration)

        permissionGuaranty = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){

        }

        controller.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener{
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                if (destination.id == R.id.quizFragment || destination.id == R.id.resultFragment)
                    binding.toolbar.apply {
                        setNavigationIconTint(R.drawable.round_arrow_back_ios_24)
                        setNavigationIconTint(resources.getColor(R.color.md_theme_light_surface))
                    }
            }
        })

        permissionGuaranty.launch(arrayOf(READ_EXTERNAL_STORAGE , WRITE_EXTERNAL_STORAGE))
    }

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
}