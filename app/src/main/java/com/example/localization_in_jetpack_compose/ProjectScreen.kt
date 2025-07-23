package com.example.localization_in_jetpack_compose

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import java.util.Locale
import androidx.core.content.edit

@Composable
fun ProjectScreen() {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val (selectedLanguage, setSelectedLanguage, localizedContext) = rememberLanguagePreference()

    val languages = mapOf(
        "en" to "English 🇺🇸",
        "ur" to "اردو 🇵🇰",
        "fr" to "Français 🇫🇷",
        "es" to "Español 🇪🇸"
    )

    CompositionLocalProvider(LocalContext provides localizedContext) {
        val safeSelectedLanguage = if (languages.containsKey(selectedLanguage)) selectedLanguage else "en"
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Text(
                    text = stringResource(id = R.string.welcome_back),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(id = R.string.sign_in_continue),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(stringResource(id = R.string.email1)) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(stringResource(id = R.string.password1)) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(12.dp)
                )

                Text(
                    text = stringResource(id = R.string.forgot_password),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 24.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(id = R.string.select_language))
                }

                Text(
                    text = stringResource(id = R.string.select_language) + ": " + languages[safeSelectedLanguage],
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(id = R.string.dont_have_account))
                    TextButton(onClick = { }) {
                        Text(stringResource(id = R.string.sign_up))
                    }
                }
            }

            if (showDialog) {
                var tempSelectedLanguage by rememberSaveable { mutableStateOf(safeSelectedLanguage) }

                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            setSelectedLanguage(tempSelectedLanguage)
                            showDialog = false
                        }) {
                            Text(stringResource(id = R.string.ok))
                        }
                    },
                    title = {
                        Text(stringResource(id = R.string.select_language))
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            languages.forEach { (code, label) ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            tempSelectedLanguage = code
                                        }
                                        .padding(4.dp)
                                ) {
                                    RadioButton(
                                        selected = tempSelectedLanguage == code,
                                        onClick = { tempSelectedLanguage = code }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(label)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun rememberLanguagePreference(): Triple<String, (String) -> Unit, Context> {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    var language by rememberSaveable {
        mutableStateOf(
            prefs.getString("selected_language", "en").takeIf {
                it in listOf("en", "ur", "fr", "es")
            } ?: "en"
        )
    }

    var updatedContext by remember {
        mutableStateOf(context.updateLocale(language))
    }

    LaunchedEffect(language) {
        updatedContext = context.updateLocale(language)
    }

    val setLanguage: (String) -> Unit = { newLang ->
        language = newLang
        prefs.edit { putString("selected_language", newLang) }
    }

    return Triple(language, setLanguage, updatedContext)
}


fun Context.updateLocale(languageCode: String): Context {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val config = Configuration(resources.configuration)
    config.setLocale(locale)
    return createConfigurationContext(config)
}

