package com.example.localization_in_jetpack_compose


import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.localization_in_jetpack_compose.ui.theme.Localization_in_Jetpack_composeTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val savedLang = getSavedLanguage(this) ?: "en"
        setLocale(this, savedLang)

        enableEdgeToEdge()

        setContent {
            Localization_in_Jetpack_composeTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 50.dp, top = 50.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProjectScreen()
                }
            }
        }
    }
}

@Composable
fun LanguageSelectionScreen() {
    val context = LocalContext.current
    val languages = listOf(
        "en" to "English 🇺🇸",
        "ur" to "اردو 🇵🇰",
        "fr" to "Français 🇫🇷",
        "es" to "Español 🇪🇸"
    )

    var selectedLanguage by rememberSaveable {
        mutableStateOf(getSavedLanguage(context) ?: "en")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.change_language),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            languages.forEach { (code, name) ->
                Card(
                    onClick = {
                        saveLanguage(context, code)
                        selectedLanguage = code
                        setLocale(context, code, recreate = true)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = if (selectedLanguage == code) {
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    } else {
                        CardDefaults.cardColors()
                    },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        if (selectedLanguage == code) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.profile),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Divider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${stringResource(id = R.string.username)}:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(id = R.string.name_value),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${stringResource(id = R.string.email)}:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(id = R.string.email_value),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${stringResource(id = R.string.password)}:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(id = R.string.password_value),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            }
        }
    }
}


fun saveLanguage(context: Context, langCode: String) {
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    prefs.edit().putString("language", langCode).apply()
}

fun getSavedLanguage(context: Context): String? {
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    return prefs.getString("language", null)
}

fun setLocale(context: Context, langCode: String, recreate: Boolean = false) {
    val locale = Locale(langCode)
    Locale.setDefault(locale)
    val config = context.resources.configuration
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
    if (recreate) {
        (context as Activity).recreate()
    }
}

