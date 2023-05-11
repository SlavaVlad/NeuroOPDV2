package com.apu.neuroopdsmart.ui.screen

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Professions : Screen("professions")
    object AddProfession : Screen("add_profession")
    object Survey : Screen("survey") {
        val arguments = listOf(
            navArgument("prof_id") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument("prof_name") {
                type = NavType.StringType
                defaultValue = "Not Found"
            },
            navArgument("prof_desc") {
                type = NavType.StringType
                defaultValue = "Not Found"
            },
        )
    }
    object Surveys : Screen("surveys")
    object Test : Screen("test") {
        val arguments = listOf(
            navArgument("test_id") {
                type = NavType.IntType
                defaultValue = 0
            },
        )
    }

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
