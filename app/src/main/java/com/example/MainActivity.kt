package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.playback.PlaybackState
import com.example.playback.RadioManager
import com.example.ui.components.StationHeader
import com.example.ui.components.VisualizerBars
import com.example.ui.screens.AdvertRatesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NewsroomScreen
import com.example.ui.screens.ScheduleScreen
import com.example.ui.screens.StationInfoScreen
import com.example.ui.theme.BcnsCyan
import com.example.ui.theme.BcnsGold
import com.example.ui.theme.BcnsNightBg
import com.example.ui.theme.BcnsSuccess
import com.example.ui.theme.MyApplicationTheme

enum class Screen {
    HOME,
    SCHEDULE,
    NEWSROOM,
    ADVERT_RATES,
    STATION_INFO
}

class MainActivity : ComponentActivity() {

    private lateinit var radioManager: RadioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        radioManager = RadioManager.getInstance(applicationContext)

        setContent {
            MyApplicationTheme {
                MainApp(radioManager = radioManager)
            }
        }
    }
}

@Composable
fun MainApp(radioManager: RadioManager) {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }

    val playbackState by radioManager.state.collectAsStateWithLifecycle()
    val volume by radioManager.volume.collectAsStateWithLifecycle()
    val sleepTimerMinutes by radioManager.sleepTimerMinutes.collectAsStateWithLifecycle()
    val visualizerAmplitudes by radioManager.visualizerAmplitudes.collectAsStateWithLifecycle()
    val currentStreamUrl by radioManager.streamUrl.collectAsStateWithLifecycle()
    val errorMessage by radioManager.errorMessage.collectAsStateWithLifecycle()

    BackHandler(enabled = currentScreen != Screen.HOME) {
        currentScreen = Screen.HOME
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BcnsNightBg),
        topBar = {
            StationHeader(modifier = Modifier.statusBarsPadding())
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                // Persistent Mini Player when browsing other screens and audio is active
                AnimatedVisibility(
                    visible = (currentScreen != Screen.HOME) && (playbackState == PlaybackState.PLAYING || playbackState == PlaybackState.CONNECTING),
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it }
                ) {
                    MiniPlayerBar(
                        playbackState = playbackState,
                        visualizerAmplitudes = visualizerAmplitudes,
                        onTogglePlay = { radioManager.togglePlayPause() },
                        onBarClick = { currentScreen = Screen.HOME }
                    )
                }

                // Material 3 Navigation Bar (Home, Schedule, Newsroom, Rates, About)
                NavigationBar(
                    containerColor = Color(0xFF040E16),
                    contentColor = Color(0xFF90AFC5),
                    tonalElevation = 8.dp,
                    modifier = Modifier.border(
                        1.dp,
                        Color(0x2297B1C7),
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                ) {
                    NavigationBarItem(
                        selected = currentScreen == Screen.HOME,
                        onClick = { currentScreen = Screen.HOME },
                        icon = { Icon(Icons.Default.Radio, contentDescription = "Radio") },
                        label = { Text("Radio", fontSize = 10.sp) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_home")
                    )

                    NavigationBarItem(
                        selected = currentScreen == Screen.SCHEDULE,
                        onClick = { currentScreen = Screen.SCHEDULE },
                        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Schedule") },
                        label = { Text("Schedule", fontSize = 10.sp) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_schedule")
                    )

                    NavigationBarItem(
                        selected = currentScreen == Screen.NEWSROOM,
                        onClick = { currentScreen = Screen.NEWSROOM },
                        icon = { Icon(Icons.Default.Newspaper, contentDescription = "Newsroom") },
                        label = { Text("Newsroom", fontSize = 10.sp) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_newsroom")
                    )

                    NavigationBarItem(
                        selected = currentScreen == Screen.ADVERT_RATES,
                        onClick = { currentScreen = Screen.ADVERT_RATES },
                        icon = { Icon(Icons.Default.Campaign, contentDescription = "Advert Rates") },
                        label = { Text("Rates", fontSize = 10.sp) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_rates")
                    )

                    NavigationBarItem(
                        selected = currentScreen == Screen.STATION_INFO,
                        onClick = { currentScreen = Screen.STATION_INFO },
                        icon = { Icon(Icons.Default.Info, contentDescription = "Station Info") },
                        label = { Text("About", fontSize = 10.sp) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_station_info")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BcnsNightBg)
        ) {
            when (currentScreen) {
                Screen.HOME -> {
                    HomeScreen(
                        playbackState = playbackState,
                        volume = volume,
                        sleepTimerMinutes = sleepTimerMinutes,
                        visualizerAmplitudes = visualizerAmplitudes,
                        currentStreamUrl = currentStreamUrl,
                        primaryStreamUrl = radioManager.primaryStreamUrl,
                        backupStreamUrl = radioManager.backupStreamUrl,
                        errorMessage = errorMessage,
                        onTogglePlay = { radioManager.togglePlayPause() },
                        onVolumeChange = { radioManager.setVolume(it) },
                        onSetSleepTimer = { radioManager.setSleepTimer(it) },
                        onCancelSleepTimer = { radioManager.cancelSleepTimer() },
                        onSwitchStream = { radioManager.switchStream(it) },
                        onNavigateToSchedule = { currentScreen = Screen.SCHEDULE },
                        onNavigateToNews = { currentScreen = Screen.NEWSROOM },
                        onNavigateToRates = { currentScreen = Screen.ADVERT_RATES }
                    )
                }

                Screen.SCHEDULE -> {
                    ScheduleScreen(
                        playbackState = playbackState,
                        onListenLiveClick = {
                            radioManager.play()
                            currentScreen = Screen.HOME
                        }
                    )
                }

                Screen.NEWSROOM -> {
                    NewsroomScreen()
                }

                Screen.ADVERT_RATES -> {
                    AdvertRatesScreen()
                }

                Screen.STATION_INFO -> {
                    StationInfoScreen()
                }
            }
        }
    }
}

@Composable
private fun MiniPlayerBar(
    playbackState: PlaybackState,
    visualizerAmplitudes: List<Float>,
    onTogglePlay: () -> Unit,
    onBarClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF091E2E))
            .border(1.dp, Color(0x3397B1C7))
            .clickable { onBarClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (playbackState == PlaybackState.PLAYING) BcnsSuccess else BcnsGold)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Crystal FM 91.3 Minna Live",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = if (playbackState == PlaybackState.PLAYING) "Streaming live feed • Tap to view" else "Connecting...",
                        color = BcnsCyan,
                        fontSize = 10.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                VisualizerBars(
                    amplitudes = visualizerAmplitudes,
                    playbackState = playbackState,
                    height = 20.dp,
                    barWidth = 3.dp,
                    spacing = 2.dp,
                    modifier = Modifier.padding(end = 12.dp)
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BcnsCyan)
                        .clickable { onTogglePlay() },
                    contentAlignment = Alignment.Center
                ) {
                    if (playbackState == PlaybackState.CONNECTING) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = if (playbackState == PlaybackState.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Toggle stream",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun navItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = BcnsCyan,
    selectedTextColor = BcnsCyan,
    indicatorColor = Color(0xFF0F324C),
    unselectedIconColor = Color(0xFF7597AF),
    unselectedTextColor = Color(0xFF7597AF)
)
