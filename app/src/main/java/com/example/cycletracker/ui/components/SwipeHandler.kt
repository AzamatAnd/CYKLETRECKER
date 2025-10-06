package com.example.cycletracker.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun SwipeHandler(
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    onSwipeUp: () -> Unit = {},
    onSwipeDown: () -> Unit = {},
    swipeThreshold: Float = 100f,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val thresholdPx = with(density) { swipeThreshold.dp.toPx() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                var totalDragX = 0f
                var totalDragY = 0f
                var hasTriggered = false

                detectDragGestures(
                    onDragStart = {
                        totalDragX = 0f
                        totalDragY = 0f
                        hasTriggered = false
                    },
                    onDragEnd = {
                        totalDragX = 0f
                        totalDragY = 0f
                        hasTriggered = false
                    },
                    onDrag = { change, dragAmount ->
                        totalDragX += dragAmount.x
                        totalDragY += dragAmount.y

                        if (!hasTriggered) {
                            when {
                                abs(totalDragX) > abs(totalDragY) && abs(totalDragX) > thresholdPx -> {
                                    hasTriggered = true
                                    if (totalDragX > 0) {
                                        onSwipeRight()
                                    } else {
                                        onSwipeLeft()
                                    }
                                }
                                abs(totalDragY) > abs(totalDragX) && abs(totalDragY) > thresholdPx -> {
                                    hasTriggered = true
                                    if (totalDragY > 0) {
                                        onSwipeDown()
                                    } else {
                                        onSwipeUp()
                                    }
                                }
                            }
                        }
                    }
                )
            }
    ) {
        content()
    }
}
