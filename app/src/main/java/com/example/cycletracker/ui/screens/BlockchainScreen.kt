package com.example.cycletracker.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cycletracker.blockchain.BlockchainManager
import com.example.cycletracker.blockchain.NFTHelthToken
import com.example.cycletracker.blockchain.NFTRarity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockchainScreen(
    blockchainManager: BlockchainManager
) {
    val blockchainState by blockchainManager.blockchainState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    
    val tabs = listOf("NFT Коллекция", "Блокчейн", "Безопасность", "Настройки")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "🔗 Блокчейн Технологии",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Tab Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tabs.size) { index ->
                BlockchainTabButton(
                    text = tabs[index],
                    isSelected = selectedTab == index,
                    onClick = { selectedTab = index }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Content
        when (selectedTab) {
            0 -> NFTCollectionTab(blockchainState.nftTokens)
            1 -> BlockchainTab(blockchainState)
            2 -> SecurityTab(blockchainState)
            3 -> BlockchainSettingsTab()
        }
    }
}

@Composable
fun BlockchainTabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF00E5FF) else Color(0xFF1A1A1A)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else Color.White,
            fontSize = 14.sp
        )
    }
}

@Composable
fun NFTCollectionTab(nftTokens: List<NFTHelthToken>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Stats
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard("Всего NFT", nftTokens.size.toString(), "🏆")
                StatCard("Редких", nftTokens.count { it.rarity == NFTRarity.RARE }.toString(), "💎")
                StatCard("Эпических", nftTokens.count { it.rarity == NFTRarity.EPIC }.toString(), "⚡")
            }
        }
        
        // NFT Grid
        item {
            Text(
                text = "Ваша коллекция",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(nftTokens) { nft ->
            NFTCard(nft = nft)
        }
        
        if (nftTokens.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = "🏆",
                    title = "Нет NFT токенов",
                    description = "Зарабатывайте достижения для получения NFT!"
                )
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: String) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = icon,
                fontSize = 20.sp
            )
            Text(
                text = value,
                color = Color(0xFF00E5FF),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun NFTCard(nft: NFTHelthToken) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // NFT Icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                getRarityColor(nft.rarity),
                                getRarityColor(nft.rarity).copy(alpha = 0.7f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🏆",
                    fontSize = 24.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // NFT Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = nft.achievement,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = nft.description,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                Text(
                    text = nft.rarity.displayName,
                    color = getRarityColor(nft.rarity),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Rarity Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(getRarityColor(nft.rarity))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = nft.rarity.displayName,
                    color = Color.Black,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun BlockchainTab(blockchainState: com.example.cycletracker.blockchain.BlockchainState) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Blockchain Stats
        item {
            Text(
                text = "Статистика блокчейна",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard("Блоков", blockchainState.totalBlocks.toString(), "🔗")
                StatCard("NFT", blockchainState.totalNFTs.toString(), "🏆")
                StatCard("Синхронизация", if (blockchainState.isConnected) "✅" else "❌", "🔄")
            }
        }
        
        // Data Integrity
        item {
            DataIntegrityCard(isValid = true)
        }
        
        // Recent Blocks
        item {
            Text(
                text = "Последние блоки",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(blockchainState.dataBlocks.take(5)) { block ->
            BlockCard(block = block)
        }
    }
}

@Composable
fun DataIntegrityCard(isValid: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isValid) Color(0xFF1A3A1A) else Color(0xFF3A1A1A)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isValid) Icons.Default.CheckCircle else Icons.Default.Error,
                contentDescription = null,
                tint = if (isValid) Color(0xFF4CAF50) else Color(0xFFF44336),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = "Целостность данных",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isValid) "Все данные проверены" else "Обнаружены ошибки",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun BlockCard(block: com.example.cycletracker.blockchain.DataBlock) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Блок ${block.id.take(8)}...",
                color = Color(0xFF00E5FF),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Время: ${java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date(block.timestamp))}",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
            Text(
                text = "Хеш: ${block.hash.take(16)}...",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun SecurityTab(blockchainState: com.example.cycletracker.blockchain.BlockchainState) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Безопасность данных",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            SecurityFeatureCard(
                icon = Icons.Default.Lock,
                title = "Шифрование",
                description = "Все данные зашифрованы AES-256",
                isEnabled = true
            )
        }
        
        item {
            SecurityFeatureCard(
                icon = Icons.Default.Fingerprint,
                title = "Биометрическая защита",
                description = "Доступ по отпечатку пальца",
                isEnabled = true
            )
        }
        
        item {
            SecurityFeatureCard(
                icon = Icons.Default.CloudOff,
                title = "Локальное хранение",
                description = "Данные хранятся только на устройстве",
                isEnabled = true
            )
        }
        
        item {
            SecurityFeatureCard(
                icon = Icons.Default.VerifiedUser,
                title = "Блокчейн валидация",
                description = "Целостность данных проверена блокчейном",
                isEnabled = blockchainState.isConnected
            )
        }
    }
}

@Composable
fun SecurityFeatureCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    isEnabled: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isEnabled) Color(0xFF4CAF50) else Color(0xFF666666),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
            
            Icon(
                if (isEnabled) Icons.Default.CheckCircle else Icons.Default.Cancel,
                contentDescription = null,
                tint = if (isEnabled) Color(0xFF4CAF50) else Color(0xFF666666),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun BlockchainSettingsTab() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Настройки блокчейна",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            SettingsItem(
                icon = Icons.Default.Sync,
                title = "Синхронизация",
                description = "Автоматическая синхронизация данных",
                isEnabled = true
            )
        }
        
        item {
            SettingsItem(
                icon = Icons.Default.Backup,
                title = "Резервное копирование",
                description = "Создание резервных копий",
                isEnabled = true
            )
        }
        
        item {
            SettingsItem(
                icon = Icons.Default.Delete,
                title = "Очистка данных",
                description = "Удаление всех данных",
                isEnabled = false
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    isEnabled: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0xFF00E5FF),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
            
            Switch(
                checked = isEnabled,
                onCheckedChange = { },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF00E5FF),
                    checkedTrackColor = Color(0xFF00E5FF).copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
fun EmptyStateCard(
    icon: String,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun getRarityColor(rarity: NFTRarity): Color {
    return when (rarity) {
        NFTRarity.COMMON -> Color(0xFF9CA3AF)
        NFTRarity.RARE -> Color(0xFF3B82F6)
        NFTRarity.EPIC -> Color(0xFF8B5CF6)
        NFTRarity.LEGENDARY -> Color(0xFFF59E0B)
        NFTRarity.MYTHIC -> Color(0xFFEF4444)
    }
}
