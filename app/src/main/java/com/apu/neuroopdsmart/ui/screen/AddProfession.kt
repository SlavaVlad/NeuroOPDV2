package com.apu.neuroopdsmart.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.apu.neuroopdsmart.api.model.Profession
import com.apu.neuroopdsmart.api.service.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddProfession(nav: NavController, apiService: ApiService) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp)) {
            var prof_name by remember { mutableStateOf("") }
            var prof_desc by remember { mutableStateOf("") }
            Text("Add Profession")
            TextField(
                value = prof_name,
                label = { Text(text = "Name") },
                singleLine = true,
                onValueChange = { text ->
                    prof_name = text
                    Log.d("NavAddProf", "AddProfession: $text,   $prof_name")
                },
            )
            TextField(
                value = prof_desc,
                label = { Text(text = "Description") },
                onValueChange = { text ->
                    prof_desc = text
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val prof = Profession(
                    occupation_name = prof_name,
                    description = prof_desc,
                    rating = "Not Rated",
                )
                CoroutineScope(Dispatchers.IO).launch {
                    apiService.createProfession(prof)
                }
                nav.popBackStack()
            }) {
                Text(text = "Add Profession")
            }
        }
    }
}