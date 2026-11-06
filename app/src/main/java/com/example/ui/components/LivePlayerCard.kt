package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playback.PlaybackState
import com.example.ui.theme.BcnsCyan
import com.example.ui.theme.BcnsGold
import com.example.ui.theme.BcnsSuccess

@Composable
fun LivePlayerCard(
    playbackState: PlaybackState,
    volume: Float,
    statusDetail: String,
    sleepTimerMinutes: Int?,
    visualizerAmplitudes: List<Float>,
    currentStreamUrl: String,
    primaryStreamUrl: String,
    backupStreamUrl: String,
    errorMessage: String?,
    onTogglePlay: () -> Unit,
    onVolumeChange: (Float) -> Unit,
    onSetSleepTimer: (Int) -> Unit,
    onCancelSleepTimer: () -> Unit,
    onSwitchStream: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSleepMenu by remember { mutableStateOf(false) }
    var showStreamMenu by remember { mutableStateOf(false) }

    val isPlaying = playbackState == PlaybackState.PLAYING
    val isConnecting = playbackState == PlaybackState.CONNECTING
    val isError = playbackState == PlaybackState.ERROR

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0D283C),
                        Color(0xFF061622)
                    )
                )
            )
            .border(1.dp, Color(0x3397B1C7), RoundedCornerShape(22.dp))
            .padding(18.dp)
    ) {
        Column {
            // Header Row: Studio Live Feed Title, frequency badge & state pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(
                                when (playbackState) {
                                    PlaybackState.PLAYING -> BcnsSuccess
                                    PlaybackState.CONNECTING -> BcnsGold
                                    PlaybackState.ERROR -> Color(0xFFFF5252)
                                    else -> Color(0xFF627D92)
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Live Studio Stream",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Crystal FM 91.3 Minna Feed",
                            color = Color(0xFF8FB0C7),
                            fontSize = 11.sp
                        )
                    }
                }

                // 91.3 FM Frequency Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F3550))
                        .border(1.dp, BcnsGold.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "91.3 FM",
                        color = BcnsGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Center Player Deck: Visualizer Equalizer + Big Play/Pause Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF04111B))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = when (playbackState) {
                            PlaybackState.PLAYING -> "ON AIR • STREAMING LIVE"
                            PlaybackState.CONNECTING -> "CONNECTING TO MINNA..."
                            PlaybackState.PAUSED -> "STREAM PAUSED"
                            PlaybackState.ERROR -> "CONNECTION FAILED"
                            PlaybackState.IDLE -> "READY TO STREAM"
                        },
                        color = when (playbackState) {
                            PlaybackState.PLAYING -> BcnsCyan
                            PlaybackState.CONNECTING -> BcnsGold
                            PlaybackState.ERROR -> Color(0xFFFF7A7A)
                            else -> Color(0xFF90AFC5)
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    VisualizerBars(
                        amplitudes = visualizerAmplitudes,
                        playbackState = playbackState,
                        height = 30.dp,
                        barWidth = 5.dp,
                        spacing = 4.dp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Big Circular Play / Pause Action Button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(66.dp)
                        .shadow(10.dp, CircleShape, spotColor = BcnsCyan)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                if (isError) {
                                    listOf(Color(0xFFE53935), Color(0xFFC62828))
                                } else {
                                    listOf(BcnsCyan, Color(0xFF0072B2))
                                }
                            )
                        )
                        .clickable { onTogglePlay() }
                        .testTag("player_toggle_button")
                ) {
                    if (isConnecting) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(30.dp),
                            strokeWidth = 3.dp
                        )
                    } else if (isError) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Retry playback",
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    } else {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause broadcast" else "Play live broadcast",
                            tint = Color.White,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }
            }

            // Real-time Status Caption
            Text(
                text = statusDetail,
                color = when (playbackState) {
                    PlaybackState.PLAYING -> Color(0xFFC0DEEE)
                    PlaybackState.CONNECTING -> BcnsGold
                    PlaybackState.ERROR -> Color(0xFFFF8585)
                    else -> Color(0xFF7A9BB3)
                },
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 8.dp, start = 2.dp)
            )

            AnimatedVisibility(visible = errorMessage != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x22FF5D5D))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = "Error",
                        tint = Color(0xFFFF5D5D),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = Color(0xFFFFB0B0),
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Volume Control Slider with percentage display
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeDown,
                    contentDescription = "Low Volume",
                    tint = Color(0xFF7A9BB3),
                    modifier = Modifier.size(18.dp)
                )

                Slider(
                    value = volume,
                    onValueChange = onVolumeChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .testTag("volume_slider"),
                    colors = SliderDefaults.colors(
                        thumbColor = BcnsCyan,
                        activeTrackColor = BcnsCyan,
                        inactiveTrackColor = Color(0xFF143046)
                    )
                )

                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "High Volume",
                    tint = Color(0xFF7A9BB3),
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "${(volume * 100).toInt()}%",
                    color = Color(0xFF90AFC5),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action row: Sleep timer, Switch stream server
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sleep timer button
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0A2234))
                            .border(1.dp, Color(0x2297B1C7), RoundedCornerShape(8.dp))
                            .clickable { showSleepMenu = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AvTimer,
                            contentDescription = "Sleep timer",
                            tint = if (sleepTimerMinutes != null) BcnsGold else Color(0xFF8AA9BF),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (sleepTimerMinutes != null) "Timer: ${sleepTimerMinutes}m" else "Sleep Timer",
                            color = if (sleepTimerMinutes != null) BcnsGold else Color(0xFFC7DCED),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    DropdownMenu(
                        expanded = showSleepMenu,
                        onDismissRequest = { showSleepMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("15 minutes") },
                            onClick = {
                                onSetSleepTimer(15)
                                showSleepMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("30 minutes") },
                            onClick = {
                                onSetSleepTimer(30)
                                showSleepMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("45 minutes") },
                            onClick = {
                                onSetSleepTimer(45)
                                showSleepMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("60 minutes") },
                            onClick = {
                                onSetSleepTimer(60)
                                showSleepMenu = false
                            }
                        )
                        if (sleepTimerMinutes != null) {
                            DropdownMenuItem(
                                text = { Text("Turn off timer", color = Color.Red) },
                                onClick = {
                                    onCancelSleepTimer()
                                    showSleepMenu = false
                                }
                            )
                        }
                    }
                }

                // Stream Server Switcher
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0A2234))
                            .border(1.dp, Color(0x2297B1C7), RoundedCornerShape(8.dp))
                            .clickable { showStreamMenu = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Radio,
                            contentDescription = "Stream source",
                            tint = BcnsCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (currentStreamUrl == primaryStreamUrl) "Primary Feed" else "Backup Feed",
                            color = Color(0xFFC7DCED),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    DropdownMenu(
                        expanded = showStreamMenu,
                        onDismissRequest = { showStreamMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Primary (bcnsradioniger.org.ng)") },
                            onClick = {
                                onSwitchStream(primaryStreamUrl)
                                showStreamMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Backup (Spectra Radio 8064)") },
                            onClick = {
                                onSwitchStream(backupStreamUrl)
                                showStreamMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
}
