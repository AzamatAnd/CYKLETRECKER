package com.example.cycletracker.ui.backup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cycletracker.backup.BackupManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val backupManager = remember { BackupManager(context) }
    
    var isCreatingBackup by remember { mutableStateOf(false) }
    var isRestoringBackup by remember { mutableStateOf(false) }
    var showRestoreDialog by remember { mutableStateOf(false) }
    var backupStatus by remember { mutableStateOf("") }
    
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                scope.launch {
                    try {
                        isRestoringBackup = true
                        backupStatus = "Восстановление данных..."
                        val backupData = backupManager.importFromFile(uri)
                        backupManager.restoreBackup(backupData)
                        backupStatus = "Данные успешно восстановлены!"
                    } catch (e: Exception) {
                        backupStatus = "Ошибка восстановления: ${e.message}"
                    } finally {
                        isRestoringBackup = false
                    }
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Резервное копирование",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFE3F2FD)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status card
            if (backupStatus.isNotEmpty()) {
                StatusCard(
                    status = backupStatus,
                    isError = backupStatus.contains("Ошибка")
                )
            }
            
            // Backup options
            BackupOptionsCard(
                isCreatingBackup = isCreatingBackup,
                isRestoringBackup = isRestoringBackup,
                onExportLocal = {
                    scope.launch {
                        try {
                            isCreatingBackup = true
                            backupStatus = "Создание резервной копии..."
                            val backupData = backupManager.createBackup()
                            val uri = backupManager.exportToFile(backupData)
                            backupStatus = "Резервная копия сохранена: ${uri.lastPathSegment}"
                        } catch (e: Exception) {
                            backupStatus = "Ошибка создания резервной копии: ${e.message}"
                        } finally {
                            isCreatingBackup = false
                        }
                    }
                },
                onImportLocal = {
                    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "application/json"
                    }
                    filePickerLauncher.launch(intent)
                },
                onShowRestoreDialog = { showRestoreDialog = true }
            )
            
            // Cloud backup options
            CloudBackupCard()
            
            // Backup info
            BackupInfoCard()
        }
    }
    
    if (showRestoreDialog) {
        RestoreConfirmationDialog(
            onDismiss = { showRestoreDialog = false },
            onConfirm = {
                showRestoreDialog = false
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "application/json"
                }
                filePickerLauncher.launch(intent)
            }
        )
    }
}

@Composable
fun StatusCard(
    status: String,
    isError: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isError) Color(0xFFFFEBEE) else Color(0xFFE8F5E8)
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = if (isError) Icons.Default.Error else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (isError) Color(0xFFD32F2F) else Color(0xFF4CAF50),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = status,
                fontSize = 14.sp,
                color = if (isError) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BackupOptionsCard(
    isCreatingBackup: Boolean,
    isRestoringBackup: Boolean,
    onExportLocal: () -> Unit,
    onImportLocal: () -> Unit,
    onShowRestoreDialog: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Локальное резервное копирование",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Создайте резервную копию ваших данных на устройстве или восстановите из существующего файла.",
                fontSize = 14.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Export button
            Button(
                onClick = onExportLocal,
                enabled = !isCreatingBackup && !isRestoringBackup,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                if (isCreatingBackup) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isCreatingBackup) "Создание..." else "Экспорт данных",
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Import button
            Button(
                onClick = onImportLocal,
                enabled = !isCreatingBackup && !isRestoringBackup,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                if (isRestoringBackup) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Icon(
                    imageVector = Icons.Default.Upload,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isRestoringBackup) "Восстановление..." else "Импорт данных",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun CloudBackupCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Облачное резервное копирование",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Синхронизация с Google Drive для автоматического резервного копирования.",
                fontSize = 14.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Google Drive button
            Button(
                onClick = { /* TODO: Implement Google Drive integration */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
            ) {
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Синхронизировать с Google Drive",
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Auto backup toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Автоматическое резервное копирование",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Switch(
                    checked = false, // TODO: Implement auto backup setting
                    onCheckedChange = { /* TODO: Save setting */ }
                )
            }
        }
    }
}

@Composable
fun BackupInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Информация о резервном копировании",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            val infoItems = listOf(
                "• Резервные копии содержат все ваши данные о циклах",
                "• Включены заметки, настроение и лекарства",
                "• Файлы сохраняются в формате JSON",
                "• Данные зашифрованы для безопасности",
                "• Рекомендуется создавать резервные копии еженедельно"
            )
            
            infoItems.forEach { item ->
                Text(
                    text = item,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun RestoreConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Восстановление данных") },
        text = { 
            Text(
                "Восстановление данных заменит все текущие данные. " +
                "Рекомендуется создать резервную копию перед восстановлением. " +
                "Продолжить?"
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFD32F2F))
            ) {
                Text("Восстановить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
