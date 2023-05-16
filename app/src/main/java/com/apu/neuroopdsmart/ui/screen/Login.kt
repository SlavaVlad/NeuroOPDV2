package com.apu.neuroopdsmart.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.apu.neuroopdsmart.api.service.ApiService
import com.apu.neuroopdsmart.ui.theme.padding
import com.apu.neuroopdsmart.ui.theme.widePadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun Login(nav: NavController, apiService: ApiService, scope: CoroutineScope) {
    fun checkLoggedIn() {
        nav.navigate("home") {
            popUpTo("login") { inclusive = true }
        }
    }

    // checkLoggedIn() // - always request login

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var login by remember { mutableStateOf(true) }
    var isCompetent by remember { mutableStateOf(false) }

    Column(Modifier.padding(widePadding)) {
        var isError by remember { mutableStateOf(false) }
        if (!login) {
            TextField(
                value = username,
                onValueChange = { username = it; isError = false },
                label = { Text("Username") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.colors(
                    errorContainerColor = MaterialTheme.colorScheme.errorContainer
                ),
                isError = isError
            )
        }
        Spacer(Modifier.padding(padding))
        TextField(
            value = email,
            onValueChange = { email = it; isError = false },
            label = { Text("E-Mail") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                errorContainerColor = MaterialTheme.colorScheme.errorContainer
            ),
            isError = isError
        )
        Spacer(Modifier.padding(padding))
        TextField(
            value = password,
            onValueChange = { password = it; isError = false },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.colors(
                errorContainerColor = MaterialTheme.colorScheme.errorContainer
            ),
            isError = isError
        )
        Spacer(Modifier.padding(widePadding))
        if (login) {
            Row {
                Button(content = {
                    Text("Login")
                }, onClick = {
                    scope.launch {
                        apiService.login(email, password, onSuccess = {
                            launch(Dispatchers.Main) {
                                nav.navigate(Screen.Professions.route)
                            }
                        }, onError = {
                            isError = true
                        })
                    }
                })
                Spacer(Modifier.padding(horizontal = 8.dp))
                OutlinedButton(content = {
                    Text("Зарегистрироваться", color = Color.Blue)
                }, onClick = {
                    login = false
                }, border = BorderStroke(0.dp, color = Color.Transparent))
            }
        } else {
            var isMale by remember { mutableStateOf(true) }

            Text("Мужчина")
            RadioButton(isMale, onClick = { isMale = true })

            Text("Женщина")
            RadioButton(!isMale, onClick = { isMale = false })

            Row {
                Text("Есть профильное образование")
                Spacer(Modifier.padding(horizontal = 8.dp))
                Checkbox(isCompetent, onCheckedChange = { isCompetent = it })
            }
            Row {
                Button(content = {
                    Text("Register")
                }, onClick = {
                    scope.launch {
                        apiService.register(username, email, password, isMale, isCompetent, onSuccess = {
                            launch(Dispatchers.Main) {
                                nav.navigate(Screen.Professions.route)
                            }
                        }, onError = {
                            isError = true
                        })
                    }
                })
                Spacer(Modifier.padding(horizontal = 8.dp))
                OutlinedButton(content = {
                    Text("Есть аккаунт", color = Color.Blue)
                }, onClick = {
                    login = true
                }, border = BorderStroke(0.dp, color = Color.Transparent))
            }
        }
    }
}
