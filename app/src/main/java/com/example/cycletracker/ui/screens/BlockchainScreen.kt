package com.example.cycletracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Блокчейн экран для децентрализованного хранения медицинских данных
 * Реализует Web3 интеграцию и NFT токены для данных здоровья
 */
@Composable
fun BlockchainScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F0F23),
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Blockchain Header
        BlockchainHeader()

        Spacer(modifier = Modifier.height(16.dp))

        // NFT Health Token
        HealthNFTToken()

        Spacer(modifier = Modifier.height(16.dp))

        // Blockchain Features
        BlockchainControlButtons()

        Spacer(modifier = Modifier.height(16.dp))

        // Decentralized Health Features
        BlockchainFeaturesList()

        Spacer(modifier = Modifier.height(16.dp))

        // Health Data Marketplace
        HealthDataMarketplace()

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun BlockchainHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF8A2BE2).copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⛓️ БЛОКЧЕЙН ЗДОРОВЬЕ 2025",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8A2BE2),
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ДЕЦЕНТРАЛИЗОВАННЫЕ МЕДИЦИНСКИЕ ДАННЫЕ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun HealthNFTToken() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // NFT Token Visualization
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF8A2BE2),
                                Color(0xFF6366F1)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🏥",
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "HEALTH NFT #2025-001",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00F5FF),
                letterSpacing = 1.sp
            )

            Text(
                text = "Блок: 0x1a2b...3c4d",
                fontSize = 12.sp,
                color = Color(0xFFCAC4D0)
            )
        }
    }
}

@Composable
private fun BlockchainControlButtons() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BlockchainButton(
                "🔐 Зашифровать данные",
                "Квантовое шифрование",
                Color(0xFF8A2BE2),
                modifier = Modifier.weight(1f)
            )
            BlockchainButton(
                "📤 Синхронизировать",
                "IPFS хранение",
                Color(0xFFFF6B35),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BlockchainButton(
                "👥 Поделиться с врачом",
                "Безопасный доступ",
                Color(0xFF14B8A6),
                modifier = Modifier.weight(1f)
            )
            BlockchainButton(
                "🏆 NFT Награды",
                "За здоровый образ жизни",
                Color(0xFF00FF88),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BlockchainButton(title: String, subtitle: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { /* Handle blockchain action */ },
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun BlockchainFeaturesList() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "⛓️ ДЕЦЕНТРАЛИЗОВАННЫЕ ВОЗМОЖНОСТИ",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00F5FF),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            BlockchainFeatureItem("Неизменяемые медицинские записи")
            BlockchainFeatureItem("Кросс-платформенная совместимость")
            BlockchainFeatureItem("Монетизация данных с согласия пользователя")
            BlockchainFeatureItem("Глобальный доступ к истории здоровья")
            BlockchainFeatureItem("Криптографическая защита приватности")
        }
    }
}

@Composable
private fun BlockchainFeatureItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(Color(0xFF8A2BE2), CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color(0xFFCAC4D0)
        )
    }
}

@Composable
private fun HealthDataMarketplace() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "🏪 МАРКЕТПЛЕЙС ДАННЫХ ЗДОРОВЬЯ",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00F5FF),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ваши данные = Ваш актив",
                fontSize = 12.sp,
                color = Color(0xFFCAC4D0),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MarketplaceButton("🎯 Исследования", Color(0xFF6366F1))
                MarketplaceButton("🏥 Клиники", Color(0xFF14B8A6))
                MarketplaceButton("💊 Фармацевтика", Color(0xFFFF006E))
            }
        }
    }
}

@Composable
private fun MarketplaceButton(text: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}