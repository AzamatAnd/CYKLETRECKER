<<<<<<< HEAD
package com.example.cycletracker.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cycletracker.audio.CycleSoundManager
import com.example.cycletracker.ui.animations.*
import com.example.cycletracker.ui.components.*
import com.example.cycletracker.ui.icons.AppEmojis
import com.example.cycletracker.ui.icons.AppIcons

@Composable
fun SoundSettingsScreen(
    soundManager: CycleSoundManager,
    modifier: Modifier = Modifier
) {
    var soundEnabled by remember { mutableStateOf(soundManager.isSoundEnabled()) }
    var ambientEnabled by remember { mutableStateOf(soundManager.isAmbientEnabled()) }
    var volume by remember { mutableStateOf(soundManager.getVolume()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        FadeInAnimation(delay = 200) {
            Text(
                text = "🔊 Настройки звука",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Основные настройки звука
        ScaleInAnimation(delay = 400) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "🎵 Основные звуки",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Включить звуки",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = { 
                                soundEnabled = it
                                soundManager.setEnabled(it)
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Громкость: ${(volume * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Slider(
                        value = volume,
                        onValueChange = { 
                            volume = it
                            soundManager.setVolume(it)
                        },
                        valueRange = 0f..1f,
                        steps = 9
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Фоновые звуки
        ScaleInAnimation(delay = 600) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "🌿 Фоновые звуки",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Включить фоновые звуки",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = ambientEnabled,
                            onCheckedChange = { 
                                ambientEnabled = it
                                soundManager.setAmbientEnabled(it)
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { soundManager.playAmbientNature() },
                            enabled = ambientEnabled
                        ) {
                            Text("🌿 Природа")
                        }
                        
                        Button(
                            onClick = { soundManager.playAmbientRain() },
                            enabled = ambientEnabled
                        ) {
                            Text("🌧️ Дождь")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Тестовые звуки
        ScaleInAnimation(delay = 800) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "🎧 Тестовые звуки",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Звуки UI
                    Text(
                        text = "Звуки интерфейса:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { soundManager.playButtonClick() },
                            enabled = soundEnabled
                        ) {
                            Text("Кнопка")
                        }
                        
                        Button(
                            onClick = { soundManager.playSuccess() },
                            enabled = soundEnabled
                        ) {
                            Text("Успех")
                        }
                        
                        Button(
                            onClick = { soundManager.playError() },
                            enabled = soundEnabled
                        ) {
                            Text("Ошибка")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Звуки циклов
                    Text(
                        text = "Звуки циклов:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { soundManager.playCycleStart() },
                            enabled = soundEnabled
                        ) {
                            Text("🌸 Цикл")
                        }
                        
                        Button(
                            onClick = { soundManager.playPeriodStart() },
                            enabled = soundEnabled
                        ) {
                            Text("🩸 Период")
                        }
                        
                        Button(
                            onClick = { soundManager.playOvulationPredicted() },
                            enabled = soundEnabled
                        ) {
                            Text("✨ Овуляция")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Звуки настроения
                    Text(
                        text = "Звуки настроения:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { soundManager.playMoodSound(3) },
                            enabled = soundEnabled
                        ) {
                            Text("😔 Плохое")
                        }
                        
                        Button(
                            onClick = { soundManager.playMoodSound(6) },
                            enabled = soundEnabled
                        ) {
                            Text("😐 Нейтральное")
                        }
                        
                        Button(
                            onClick = { soundManager.playMoodSound(9) },
                            enabled = soundEnabled
                        ) {
                            Text("😊 Хорошее")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Достижения
                    Button(
                        onClick = { soundManager.playAchievement() },
                        enabled = soundEnabled,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🏆 Достижение")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Информация о звуках
        FadeInAnimation(delay = 1000) {
            GlassmorphismCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "ℹ️ О звуках",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Все звуки созданы специально для CycleTracker с использованием алгоритмической генерации. Каждый звук отражает естественные процессы женского цикла.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "🌸 Цветок распускается - начало цикла\n🩸 Капля воды - период\n✨ Звездочка - овуляция\n🌿 Лесной шепот - фоновые звуки",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
=======
package com.example.cycletracker.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cycletracker.audio.CycleSoundManager
import com.example.cycletracker.ui.animations.*
import com.example.cycletracker.ui.components.*
import com.example.cycletracker.ui.icons.AppEmojis
import com.example.cycletracker.ui.icons.AppIcons

@Composable
fun SoundSettingsScreen(
    soundManager: CycleSoundManager,
    modifier: Modifier = Modifier
) {
    var soundEnabled by remember { mutableStateOf(soundManager.isSoundEnabled()) }
    var ambientEnabled by remember { mutableStateOf(soundManager.isAmbientEnabled()) }
    var volume by remember { mutableStateOf(soundManager.getVolume()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        FadeInAnimation(delay = 200) {
            Text(
                text = "🔊 Настройки звука",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Основные настройки звука
        ScaleInAnimation(delay = 400) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "🎵 Основные звуки",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Включить звуки",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = { 
                                soundEnabled = it
                                soundManager.setEnabled(it)
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Громкость: ${(volume * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Slider(
                        value = volume,
                        onValueChange = { 
                            volume = it
                            soundManager.setVolume(it)
                        },
                        valueRange = 0f..1f,
                        steps = 9
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Фоновые звуки
        ScaleInAnimation(delay = 600) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "🌿 Фоновые звуки",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Включить фоновые звуки",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = ambientEnabled,
                            onCheckedChange = { 
                                ambientEnabled = it
                                soundManager.setAmbientEnabled(it)
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { soundManager.playAmbientNature() },
                            enabled = ambientEnabled
                        ) {
                            Text("🌿 Природа")
                        }
                        
                        Button(
                            onClick = { soundManager.playAmbientRain() },
                            enabled = ambientEnabled
                        ) {
                            Text("🌧️ Дождь")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Тестовые звуки
        ScaleInAnimation(delay = 800) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "🎧 Тестовые звуки",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Звуки UI
                    Text(
                        text = "Звуки интерфейса:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { soundManager.playButtonClick() },
                            enabled = soundEnabled
                        ) {
                            Text("Кнопка")
                        }
                        
                        Button(
                            onClick = { soundManager.playSuccess() },
                            enabled = soundEnabled
                        ) {
                            Text("Успех")
                        }
                        
                        Button(
                            onClick = { soundManager.playError() },
                            enabled = soundEnabled
                        ) {
                            Text("Ошибка")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Звуки циклов
                    Text(
                        text = "Звуки циклов:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { soundManager.playCycleStart() },
                            enabled = soundEnabled
                        ) {
                            Text("🌸 Цикл")
                        }
                        
                        Button(
                            onClick = { soundManager.playPeriodStart() },
                            enabled = soundEnabled
                        ) {
                            Text("🩸 Период")
                        }
                        
                        Button(
                            onClick = { soundManager.playOvulationPredicted() },
                            enabled = soundEnabled
                        ) {
                            Text("✨ Овуляция")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Звуки настроения
                    Text(
                        text = "Звуки настроения:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { soundManager.playMoodSound(3) },
                            enabled = soundEnabled
                        ) {
                            Text("😔 Плохое")
                        }
                        
                        Button(
                            onClick = { soundManager.playMoodSound(6) },
                            enabled = soundEnabled
                        ) {
                            Text("😐 Нейтральное")
                        }
                        
                        Button(
                            onClick = { soundManager.playMoodSound(9) },
                            enabled = soundEnabled
                        ) {
                            Text("😊 Хорошее")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Достижения
                    Button(
                        onClick = { soundManager.playAchievement() },
                        enabled = soundEnabled,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🏆 Достижение")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Информация о звуках
        FadeInAnimation(delay = 1000) {
            GlassmorphismCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "ℹ️ О звуках",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Все звуки созданы специально для CycleTracker с использованием алгоритмической генерации. Каждый звук отражает естественные процессы женского цикла.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "🌸 Цветок распускается - начало цикла\n🩸 Капля воды - период\n✨ Звездочка - овуляция\n🌿 Лесной шепот - фоновые звуки",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
>>>>>>> 7eb7955987a0da95e1b119ccbbfc2bcdc4522c76
