package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.StationSchedule
import com.example.playback.PlaybackState
import com.example.ui.components.LivePlayerCard
import com.example.ui.theme.BcnsCyan
import com.example.ui.theme.BcnsGold
import com.example.ui.theme.BcnsNightBg
import com.example.ui.theme.BcnsSuccess

@Composable
fun HomeScreen(
    playbackState: PlaybackState,
    volume: Float,
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
    onNavigateToSchedule: () -> Unit,
    onNavigateToNews: () -> Unit,
    onNavigateToRates: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentShow = remember { StationSchedule.getCurrentProgram() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BcnsNightBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            // Website Hero Card with Studio Graphic
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0C2538), Color(0xFF06141F))
                        )
                    )
                    .border(1.dp, Color(0x3397B1C7), RoundedCornerShape(22.dp))
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bcns_hero),
                            contentDescription = "BCNS Radio Studio",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Gradient overlay for smooth contrast
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color(0xEE06141F))
                                    )
                                )
                        )

                        // On Air badge overlay
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xDDFF1744))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "ON AIR • 91.3 FM",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Radio,
                                contentDescription = null,
                                tint = BcnsCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PUBLIC SERVICE BROADCASTING FOR NIGER STATE",
                                color = BcnsCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "A sharper, stronger radio experience for listeners, partners, and the community.",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 23.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "BCNS Crystal FM 91.3 Minna delivers trusted news, educational programmes, live conversation, culture, music, and community updates designed for audiences across Niger State and beyond.",
                            color = Color(0xFF9AB7CC),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick buttons matching website hero action buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onTogglePlay,
                                modifier = Modifier.weight(1.3f),
                                colors = ButtonDefaults.buttonColors(containerColor = BcnsCyan),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Headphones,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (playbackState == PlaybackState.PLAYING) "Listening Live" else "Start Listening",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = onNavigateToSchedule,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFCBE3F5)),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x5597B1C7)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Schedule", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }

                            OutlinedButton(
                                onClick = onNavigateToRates,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = BcnsGold),
                                border = androidx.compose.foundation.BorderStroke(1.dp, BcnsGold.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Rates", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        // Live Audio Player Section (matches website popup player)
        item {
            LivePlayerCard(
                playbackState = playbackState,
                volume = volume,
                sleepTimerMinutes = sleepTimerMinutes,
                visualizerAmplitudes = visualizerAmplitudes,
                currentStreamUrl = currentStreamUrl,
                primaryStreamUrl = primaryStreamUrl,
                backupStreamUrl = backupStreamUrl,
                errorMessage = errorMessage,
                onTogglePlay = onTogglePlay,
                onVolumeChange = onVolumeChange,
                onSetSleepTimer = onSetSleepTimer,
                onCancelSleepTimer = onCancelSleepTimer,
                onSwitchStream = onSwitchStream
            )
        }

        // "Now On Air" Program Box
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF091F30))
                    .border(1.dp, Color(0x3397B1C7), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                tint = BcnsGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "NOW ON AIR",
                                color = BcnsGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Text(
                            text = currentShow.timeSlot,
                            color = Color(0xFF8EB0C8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentShow.title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Anchor: ${currentShow.host} • ${currentShow.category}",
                        color = BcnsCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currentShow.description,
                        color = Color(0xFFA5C3D7),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Digital Streaming Audience & Analytics (from website section "Verified Digital Streaming Audience")
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF071B2A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x2297B1C7))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            tint = BcnsCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Verified Digital Streaming Reach",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Broadcasting real-time audio across Niger State, national networks, and worldwide diaspora channels.",
                        color = Color(0xFF90AFC5),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatBox(
                            number = "91.3 FM",
                            label = "Minna Flagship",
                            modifier = Modifier.weight(1f)
                        )
                        StatBox(
                            number = "25 LGAs",
                            label = "State Coverage",
                            modifier = Modifier.weight(1f)
                        )
                        StatBox(
                            number = "Global",
                            label = "Online Feed",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Core Station Services (matching website "Core services" section)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF091E2E))
                    .border(1.dp, Color(0x2297B1C7), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = BcnsGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Core Station Services",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ServiceRow(
                    icon = Icons.Default.Newspaper,
                    title = "Central Newsroom & Bulletins",
                    desc = "Authoritative hourly news reports and reviews in English and Hausa."
                )
                ServiceRow(
                    icon = Icons.Default.RecordVoiceOver,
                    title = "Public Awareness & Community Dialogue",
                    desc = "Matters of State, governance discussions, and live listener phone-ins."
                )
                ServiceRow(
                    icon = Icons.Default.Campaign,
                    title = "Commercial Advert Jingles & Hypes",
                    desc = "Strategic prime-time commercial spots to grow regional businesses."
                )
                ServiceRow(
                    icon = Icons.Default.CellTower,
                    title = "Live Outside Broadcast (OB Van)",
                    desc = "On-site live mobile coverage for conferences, summits, and festivals."
                )
            }
        }

        // WhatsApp Studio Interaction Card (matches website section)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF063321), Color(0xFF09241B))
                        )
                    )
                    .border(1.dp, Color(0x3312B886), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Send a Studio Request or Greeting",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Prepare a shout-out or message directly for the on-air presenters.",
                            color = Color(0xFFA5DEC5),
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = {
                            openWhatsAppShoutout(context)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BcnsSuccess),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatBox(
    number: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF0D2536))
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = number,
                color = BcnsGold,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                color = Color(0xFF90AFC5),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun ServiceRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color(0xFF0F324C)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BcnsCyan,
                modifier = Modifier.size(15.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(desc, color = Color(0xFF8AA8BF), fontSize = 11.sp, lineHeight = 14.sp)
        }
    }
}

private fun openWhatsAppShoutout(context: Context) {
    try {
        val msg = "Hello BCNS Crystal FM 91.3 Minna studio! I am listening live to the station."
        val url = "https://api.whatsapp.com/send?phone=2348036914274&text=" + Uri.encode(msg)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (_: Exception) {}
}
