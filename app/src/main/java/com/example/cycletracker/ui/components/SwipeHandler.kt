package com.example.cycletracker.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
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
                detectDragGestures(
                    onDragEnd = { },
                    onDrag = { change, _ ->
                        val deltaX = change.position.x - change.previousPosition.x
                        val deltaY = change.position.y - change.previousPosition.y
                        
                        when {
                            abs(deltaX) > abs(deltaY) && abs(deltaX) > thresholdPx -> {
                                if (deltaX > 0) {
                                    onSwipeRight()
                                } else {
                                    onSwipeLeft()
                                }
                            }
                            abs(deltaY) > abs(deltaX) && abs(deltaY) > thresholdPx -> {
                                if (deltaY > 0) {
                                    onSwipeDown()
                                } else {
                                    onSwipeUp()
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
