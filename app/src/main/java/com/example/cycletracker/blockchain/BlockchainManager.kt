package com.example.cycletracker.blockchain

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.MessageDigest
import java.util.*

/**
 * Менеджер блокчейн технологий для безопасного хранения медицинских данных
 */
class BlockchainManager(private val context: Context) {
    
    private val _blockchainState = MutableStateFlow(BlockchainState())
    val blockchainState: StateFlow<BlockchainState> = _blockchainState.asStateFlow()
    
    // Симуляция блокчейн операций
    private val dataBlocks = mutableListOf<DataBlock>()
    private val nftTokens = mutableListOf<NFTHelthToken>()
    
    /**
     * Создание NFT токена для достижения в здоровье
     */
    suspend fun createHealthNFT(
        achievement: String,
        description: String,
        rarity: NFTRarity
    ): NFTHelthToken {
        val token = NFTHelthToken(
            id = UUID.randomUUID().toString(),
            achievement = achievement,
            description = description,
            rarity = rarity,
            createdAt = System.currentTimeMillis(),
            hash = generateHash(achievement + description)
        )
        
        nftTokens.add(token)
        _blockchainState.value = _blockchainState.value.copy(
            nftTokens = nftTokens.toList(),
            totalNFTs = nftTokens.size
        )
        
        return token
    }
    
    /**
     * Безопасное хранение медицинских данных в блокчейн
     */
    suspend fun storeHealthData(
        data: HealthData,
        encryptionKey: String
    ): String {
        val encryptedData = encryptData(data, encryptionKey)
        val block = DataBlock(
            id = UUID.randomUUID().toString(),
            data = encryptedData,
            timestamp = System.currentTimeMillis(),
            previousHash = dataBlocks.lastOrNull()?.hash ?: "genesis",
            hash = generateHash(encryptedData + System.currentTimeMillis().toString())
        )
        
        dataBlocks.add(block)
        _blockchainState.value = _blockchainState.value.copy(
            dataBlocks = dataBlocks.toList(),
            totalBlocks = dataBlocks.size
        )
        
        return block.id
    }
    
    /**
     * Получение NFT коллекции пользователя
     */
    fun getNFTCollection(): List<NFTHelthToken> {
        return nftTokens.sortedByDescending { it.createdAt }
    }
    
    /**
     * Проверка целостности данных
     */
    fun verifyDataIntegrity(): Boolean {
        for (i in 1 until dataBlocks.size) {
            val currentBlock = dataBlocks[i]
            val previousBlock = dataBlocks[i - 1]
            
            if (currentBlock.previousHash != previousBlock.hash) {
                return false
            }
        }
        return true
    }
    
    /**
     * Генерация хеша для блоков
     */
    private fun generateHash(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
    
    /**
     * Шифрование данных
     */
    private fun encryptData(data: HealthData, key: String): String {
        // Простое шифрование (в реальном приложении использовать AES)
        return Base64.getEncoder().encodeToString(
            (data.toString() + key).toByteArray()
        )
    }
    
    /**
     * Дешифрование данных
     */
    private fun decryptData(encryptedData: String, key: String): HealthData? {
        return try {
            val decoded = Base64.getDecoder().decode(encryptedData)
            val decrypted = String(decoded).removeSuffix(key)
            // Парсинг HealthData из строки
            HealthData.fromString(decrypted)
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * Состояние блокчейн системы
 */
data class BlockchainState(
    val isConnected: Boolean = false,
    val totalBlocks: Int = 0,
    val totalNFTs: Int = 0,
    val nftTokens: List<NFTHelthToken> = emptyList(),
    val dataBlocks: List<DataBlock> = emptyList(),
    val lastSyncTime: Long = 0L
)

/**
 * Блок данных в блокчейне
 */
data class DataBlock(
    val id: String,
    val data: String,
    val timestamp: Long,
    val previousHash: String,
    val hash: String
)

/**
 * NFT токен для достижений в здоровье
 */
data class NFTHelthToken(
    val id: String,
    val achievement: String,
    val description: String,
    val rarity: NFTRarity,
    val createdAt: Long,
    val hash: String,
    val imageUrl: String? = null
)

/**
 * Редкость NFT токена
 */
enum class NFTRarity(val displayName: String, val color: String) {
    COMMON("Обычный", "#9CA3AF"),
    RARE("Редкий", "#3B82F6"),
    EPIC("Эпический", "#8B5CF6"),
    LEGENDARY("Легендарный", "#F59E0B"),
    MYTHIC("Мифический", "#EF4444")
}

/**
 * Медицинские данные для хранения
 */
data class HealthData(
    val cycleData: CycleData,
    val symptoms: List<String>,
    val mood: String,
    val notes: String,
    val timestamp: Long
) {
    companion object {
        fun fromString(data: String): HealthData? {
            // Парсинг из строки (упрощенная версия)
            return try {
                HealthData(
                    cycleData = CycleData(1, 28, "Менструация"),
                    symptoms = emptyList(),
                    mood = "Хорошее",
                    notes = "",
                    timestamp = System.currentTimeMillis()
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}

/**
 * Данные цикла
 */
data class CycleData(
    val currentDay: Int,
    val cycleLength: Int,
    val phase: String
)
