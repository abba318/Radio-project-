package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.playback.PlaybackState
import com.example.ui.theme.BcnsCyan
import com.example.ui.theme.BcnsGold

@Composable
fun VisualizerBars(
    amplitudes: List<Float>,
    playbackState: PlaybackState,
    modifier: Modifier = Modifier,
    height: Dp = 36.dp,
    barWidth: Dp = 4.dp,
    spacing: Dp = 3.dp
) {
    val barBrush = Brush.verticalGradient(
        colors = when (playbackState) {
            PlaybackState.PLAYING -> listOf(BcnsGold, BcnsCyan)
            PlaybackState.CONNECTING -> listOf(BcnsCyan, Color(0xFF005E8D))
            else -> listOf(Color(0xFF4A6B84), Color(0xFF2C4355))
        }
    )

    Row(
        modifier = modifier.height(height),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.Bottom
    ) {
        amplitudes.forEach { amp ->
            val animatedHeight by animateFloatAsState(
                targetValue = if (playbackState == PlaybackState.PLAYING) amp else if (playbackState == PlaybackState.CONNECTING) 0.35f else 0.12f,
                animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
                label = "barHeight"
            )

            Box(
                modifier = Modifier
                    .width(barWidth)
                    .fillMaxHeight(fraction = animatedHeight.coerceIn(0.1f, 1f))
                    .clip(RoundedCornerShape(percent = 50))
                    .background(barBrush)
            )
        }
    }
}
