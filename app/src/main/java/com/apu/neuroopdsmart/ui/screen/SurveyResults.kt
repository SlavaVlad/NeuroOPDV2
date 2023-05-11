package com.apu.neuroopdsmart.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.apu.neuroopdsmart.api.model.SurveyProfession
import com.apu.neuroopdsmart.api.service.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SurveyResults(
    nav: NavController? = null,
    apiService: ApiService,
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) {
    var results by remember { mutableStateOf<List<SurveyProfession>>(emptyList()) }
    LaunchedEffect(Unit) {
        scope.launch {
            apiService.getSurveyResult() {
                results = it!!
            }
        }
    }
    val mapResults = getAdjectivesByProfession(results)
    LazyColumn {
        items(count = mapResults.size) {
            val name = mapResults.keys.toList()[it]
            val adjectives = mapResults.values.toList()[it]
            SurveyItem(name, adjectives.toList())
        }
    }
}

@Composable
fun SurveyItem(profession: String, adjectives: List<String>) {
    ElevatedCard(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .heightIn(min = 70.dp),
    ) {
        Text(profession)
        Spacer(modifier = Modifier.height(8.dp))
        adjectives.forEach {
            Column(modifier = Modifier.padding(4.dp)) {
                Text(it)
            }
        }
    }
}

fun getAdjectivesByProfession(surveyProfessions: List<SurveyProfession>): Map<String, Set<String>> {
    val adjectivesMap = mutableMapOf<String, MutableSet<String>>()

    for (surveyProfession in surveyProfessions) {
        val professionName = surveyProfession.profName
        val adjectiveName = surveyProfession.adjName

        if (!adjectivesMap.containsKey(professionName)) {
            adjectivesMap[professionName] = mutableSetOf(adjectiveName)
        } else {
            adjectivesMap[professionName]?.add(adjectiveName)
        }
    }

    return adjectivesMap
}
