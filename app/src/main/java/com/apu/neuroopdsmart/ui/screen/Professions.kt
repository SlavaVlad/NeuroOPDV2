package com.apu.neuroopdsmart.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.apu.neuroopdsmart.R
import com.apu.neuroopdsmart.api.model.Profession
import com.apu.neuroopdsmart.api.service.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Professions(nav: NavController, apiService: ApiService, scope: CoroutineScope) {
    var profs by remember { mutableStateOf<List<Profession>>(emptyList()) }
    LaunchedEffect(Unit) {
        scope.launch {
            apiService.getProfessions("1") {
                profs = it
            }
        }
    }
    Box(Modifier.fillMaxSize()) {
        LazyColumn() {
            items(profs.size) { index ->
                val profession = profs[index]
                ElevatedCard(
                    onClick = {
                        nav.navigate(
                            Screen.Survey
                                .withArgs(
                                    profession.occupation_id.toString(),
                                    profession.occupation_name,
                                    profession.description,
                                ),
                        )
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .heightIn(min = 70.dp),
                ) {
                    Box(Modifier.fillMaxSize()) {
                        Column(Modifier.padding(8.dp)) {
                            Text(profession.occupation_name, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(2.dp))
                            Text(profession.description, maxLines = 2)
                        }
                    }
                }
            }
        }
        FloatingActionButton(onClick = {
            nav.navigate(Screen.AddProfession.route)
        }, Modifier.padding(32.dp).align(Alignment.BottomEnd)) {
            Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = null)
        }

        FloatingActionButton(onClick = {
            nav.navigate(Screen.Test.withArgs("0"))
        }, Modifier.padding(32.dp).align(Alignment.BottomStart)) {
            Icon(painter = painterResource(id = R.drawable.ic_qa), contentDescription = null)
        }
    }
}
