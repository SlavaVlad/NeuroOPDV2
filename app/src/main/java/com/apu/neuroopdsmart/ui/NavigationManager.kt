package com.apu.neuroopdsmart.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.apu.neuroopdsmart.api.service.ApiService
import com.apu.neuroopdsmart.maketag
import com.apu.neuroopdsmart.ui.screen.AddProfession
import com.apu.neuroopdsmart.ui.screen.Professions
import com.apu.neuroopdsmart.ui.screen.Screen
import com.apu.neuroopdsmart.ui.screen.Survey
import com.apu.neuroopdsmart.ui.screen.SurveyResults
import com.apu.neuroopdsmart.ui.screen.TestContainer
import kotlinx.coroutines.CoroutineScope

class NavigationManager(private val scope: CoroutineScope) {
    private val apiService = ApiService()
    lateinit var nav: NavHostController

    @Composable
    fun Navigation(): NavHostController {
        nav = rememberNavController()
        NavHost(navController = nav, startDestination = Screen.Professions.route) {
            composable(Screen.Professions.route) {
                Professions(nav, apiService, scope)
            }
            composable(Screen.AddProfession.route) {
                AddProfession(nav, apiService)
            }
            composable(
                Screen.Survey.route +
                    "/{prof_id}/{prof_name}/{prof_desc}",
                Screen.Survey.arguments,
            ) { entry ->
                Survey(
                    nav,
                    apiService,
                    scope,
                    entry.arguments?.getString("prof_name")!!,
                    entry.arguments?.getString("prof_desc")!!,
                    entry.arguments?.getInt("prof_id")!!,
                )
            }
            composable(Screen.Surveys.route) {
                SurveyResults(nav, apiService, scope)
            }
            composable(Screen.Test.route + "/{test_id}", Screen.Test.arguments) {
                TestContainer(nav, apiService, it.arguments?.getInt("test_id"))
            }
        }

        Log.d(maketag(this), "Navigation: ${nav.graph.nodes}")
        return nav
    }
}
