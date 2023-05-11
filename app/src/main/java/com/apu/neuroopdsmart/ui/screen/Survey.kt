package com.apu.neuroopdsmart.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.apu.neuroopdsmart.api.model.Adjective
import com.apu.neuroopdsmart.api.model.SurveyResult
import com.apu.neuroopdsmart.api.service.ApiService
import com.apu.neuroopdsmart.ui.widgets.NeuroAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Survey(
    nav: NavController? = null,
    apiService: ApiService,
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    name: String,
    desc: String,
    id: Int,
) {
    var adj by remember { mutableStateOf<List<Adjective>>(emptyList()) }
    LaunchedEffect(Unit) {
        scope.launch {
            apiService.getAdjectivesByCategory() {
                adj = it
            }
        }
    }
    val checked = mutableListOf<Adjective>()
    Column(
        Modifier
            .fillMaxSize(),
    ) {
        NeuroAppBar(content = {
            Spacer(Modifier.width(40.dp))
            Text("Survey", maxLines = 1)
            Spacer(Modifier.width(60.dp))
            Button(content = {
                Text("Отправить ${checked.size}")
            }, onClick = {
                scope.launch {
                    apiService.sendSurveyResult(
                        SurveyResult(
                            1,
                            id,
                            //List(size = checked.size, init = { checked[it].id }),
                        ),
                    )
                }
            }, modifier = Modifier.fillMaxWidth())
        }, onNavButtonClicked = {
            nav?.popBackStack()
        })
        val groupped = adj.groupBy { it.category }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.wrapContentHeight(),
        ) {
            item {
                Text(name, Modifier.padding(8.dp))
                Text(desc, Modifier.padding(8.dp))
                Spacer(Modifier.size(16.dp))
            }
            groupped.forEach { (category, adjectives) ->
                stickyHeader {
                    Header(category)
                }
                items(adjectives) { adjective ->
                    AdjCard(adjective) {
                        //checked.add(adjective)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdjCard(adjective: Adjective, onClick: (Adjective) -> Unit = {}) {
    var isSelected by remember { mutableStateOf(false) }
    InputChip(
        label = { Text(adjective.trait, modifier = Modifier.padding(vertical = 16.dp)) },
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            isSelected = !isSelected
        },
        selected = isSelected,
    )
}

@Composable
fun Header(text: String) {
    Text(
        text,
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) Color.DarkGray else Color.White)
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
    )
}
