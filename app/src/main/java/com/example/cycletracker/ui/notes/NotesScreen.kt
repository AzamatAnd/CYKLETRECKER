package com.example.cycletracker.ui.notes

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cycletracker.ui.CycleViewModel
import com.example.cycletracker.data.Note
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(viewModel: CycleViewModel, onNavigateBack: () -> Unit) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showAddDialog by remember { mutableStateOf(false) }
    val notes by viewModel.getAllNotes().collectAsState(initial = emptyList())
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Заметки и симптомы") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE91E63),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFFFF0F5),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFFE91E63)
            ) {
                Icon(Icons.Default.Add, "Добавить", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Date selector
            DateSelectorCard(
                selectedDate = selectedDate,
                onDateChange = { selectedDate = it }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Quick symptom buttons
            Text(
                text = "Быстрое добавление",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE91E63)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            QuickSymptomGrid()
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Today's notes
            Text(
                text = "Записи за ${selectedDate.format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru")))}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE91E63)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Notes list
            NotesList()
        }
    }
    
    if (showAddDialog) {
        AddNoteDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { noteData ->
                val note = Note(
                    date = LocalDate.now(),
                    title = noteData.title,
                    content = noteData.content
                )
                viewModel.addNote(note)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun DateSelectorCard(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onDateChange(selectedDate.minusDays(1)) }) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    "Предыдущий день",
                    tint = Color(0xFFE91E63)
                )
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                if (selectedDate == LocalDate.now()) {
                    Text(
                        text = "Сегодня",
                        fontSize = 12.sp,
                        color = Color(0xFFE91E63)
                    )
                }
            }
            
            IconButton(onClick = { onDateChange(selectedDate.plusDays(1)) }) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    "Следующий день",
                    tint = Color(0xFFE91E63)
                )
            }
        }
    }
}

@Composable
fun QuickSymptomGrid() {
    val symptoms = listOf(
        SymptomItem("😊", "Отличное", Color(0xFF4CAF50)),
        SymptomItem("😐", "Нормально", Color(0xFFFF9800)),
        SymptomItem("😔", "Плохо", Color(0xFFE91E63)),
        SymptomItem("💧", "Выделения", Color(0xFF2196F3)),
        SymptomItem("🤕", "Боль", Color(0xFFF44336)),
        SymptomItem("😴", "Усталость", Color(0xFF9C27B0)),
        SymptomItem("🍽️", "Аппетит", Color(0xFFFF5722)),
        SymptomItem("💤", "Сон", Color(0xFF673AB7))
    )
    
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        symptoms.chunked(4).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { symptom ->
                    QuickSymptomButton(
                        symptom = symptom,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill empty spaces
                repeat(4 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun QuickSymptomButton(
    symptom: SymptomItem,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .clickable {
                isPressed = !isPressed
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isPressed) symptom.color else Color.White
        ),
        elevation = CardDefaults.cardElevation(if (isPressed) 8.dp else 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = symptom.emoji,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = symptom.label,
                fontSize = 10.sp,
                color = if (isPressed) Color.White else Color.Black,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
fun NotesList() {
    val sampleNotes = remember {
        listOf(
            NoteData("Отличное настроение весь день! 😊", "10:30", Color(0xFF4CAF50)),
            NoteData("Небольшая головная боль", "14:15", Color(0xFFFF9800)),
            NoteData("Легкая усталость к вечеру", "18:45", Color(0xFF9C27B0))
        )
    }
    
    if (sampleNotes.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📝",
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Пока нет записей",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Нажмите + чтобы добавить",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleNotes) { note ->
                NoteCard(note)
            }
        }
    }
}

@Composable
fun NoteCard(note: NoteData) {
    var isExpanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(note.color)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = note.content,
                    fontSize = 14.sp,
                    color = Color.Black,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            IconButton(onClick = { viewModel.deleteNote(note.id) }) {
                Icon(
                    Icons.Default.Delete,
                    "Удалить",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteDialog(
    onDismiss: () -> Unit,
    onAdd: (NoteData) -> Unit
) {
    var noteText by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf<String?>(null) }
    
    AlertDialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Добавить запись",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE91E63)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Mood selector
                Text(
                    text = "Настроение",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("😊", "😐", "😔", "😢", "😡").forEach { emoji ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (selectedMood == emoji) Color(0xFFE91E63)
                                    else Color(0xFFF5F5F5)
                                )
                                .clickable { selectedMood = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = emoji,
                                fontSize = 24.sp
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Note text field
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Заметка") },
                    placeholder = { Text("Опишите своё самочувствие...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE91E63),
                        focusedLabelColor = Color(0xFFE91E63),
                        cursorColor = Color(0xFFE91E63)
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена", color = Color.Gray)
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = { 
                            onAdd(NoteData(
                                title = "Запись",
                                content = noteText,
                                color = Color(0xFFE91E63)
                            ))
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE91E63)
                        ),
                        enabled = noteText.isNotBlank()
                    ) {
                        Text("Добавить")
                    }
                }
            }
        }
    }
}

data class SymptomItem(
    val emoji: String,
    val label: String,
    val color: Color
)

data class NoteData(
    val title: String,
    val content: String,
    val color: Color
)
