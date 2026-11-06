package com.example.playback

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.CountDownTimer
import android.os.PowerManager
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class PlaybackState {
    IDLE,
    CONNECTING,
    PLAYING,
    PAUSED,
    ERROR
}

class RadioManager private constructor(private val appContext: Context) {

    private val tag = "RadioManager"

    val primaryStreamUrl = "https://bcnsradioniger.org.ng/stream"
    val backupStreamUrl = "http://spectraradio.ng:8064/stream"

    private val _streamUrl = MutableStateFlow(primaryStreamUrl)
    val streamUrl: StateFlow<String> = _streamUrl.asStateFlow()

    private val _state = MutableStateFlow(PlaybackState.IDLE)
    val state: StateFlow<PlaybackState> = _state.asStateFlow()

    private val _volume = MutableStateFlow(0.85f)
    val volume: StateFlow<Float> = _volume.asStateFlow()

    private val _statusDetail = MutableStateFlow("Tap Play to listen to 91.3 FM live")
    val statusDetail: StateFlow<String> = _statusDetail.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _sleepTimerMinutes = MutableStateFlow<Int?>(null)
    val sleepTimerMinutes: StateFlow<Int?> = _sleepTimerMinutes.asStateFlow()

    private val _visualizerAmplitudes = MutableStateFlow(listOf(0.2f, 0.4f, 0.3f, 0.5f, 0.3f, 0.6f, 0.4f, 0.5f))
    val visualizerAmplitudes: StateFlow<List<Float>> = _visualizerAmplitudes.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var wifiLock: WifiManager.WifiLock? = null
    private var sleepTimer: CountDownTimer? = null
    private var visualizerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private var audioFocusRequest: AudioFocusRequest? = null

    init {
        initLocks()
        startVisualizerLoop()
    }

    private fun initLocks() {
        try {
            val powerManager = appContext.getSystemService(Context.POWER_SERVICE) as? PowerManager
            wakeLock = powerManager?.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "BCNSRadio:PlaybackWakeLock")

            val wifiManager = appContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
            wifiLock = wifiManager?.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "BCNSRadio:PlaybackWifiLock")
        } catch (e: Exception) {
            Log.w(tag, "Failed to initialize wake locks: ${e.message}")
        }
    }

    fun play() {
        if (_state.value == PlaybackState.PLAYING) return

        if (_state.value == PlaybackState.PAUSED && mediaPlayer != null) {
            try {
                requestAudioFocus()
                mediaPlayer?.start()
                _state.value = PlaybackState.PLAYING
                _statusDetail.value = "Broadcasting Live • 91.3 FM Minna"
                _errorMessage.value = null
                acquireLocks()
                RadioPlaybackService.start(appContext)
                return
            } catch (e: Exception) {
                Log.e(tag, "Resume failed, reinitializing", e)
            }
        }

        initAndStartPlayer(_streamUrl.value)
    }

    fun pause() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
            _state.value = PlaybackState.PAUSED
            _statusDetail.value = "Broadcast Paused"
            releaseLocks()
            abandonAudioFocus()
        } catch (e: Exception) {
            Log.e(tag, "Error pausing playback", e)
            _state.value = PlaybackState.IDLE
            _statusDetail.value = "Tap Play to listen live"
        }
    }

    fun stop() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            Log.e(tag, "Error stopping playback", e)
        } finally {
            _state.value = PlaybackState.IDLE
            _statusDetail.value = "Station Offline • Tap Play to connect"
            releaseLocks()
            abandonAudioFocus()
            RadioPlaybackService.stop(appContext)
        }
    }

    fun togglePlayPause() {
        when (_state.value) {
            PlaybackState.PLAYING -> pause()
            PlaybackState.CONNECTING -> stop()
            else -> play()
        }
    }

    fun setVolume(vol: Float) {
        val clamped = vol.coerceIn(0f, 1f)
        _volume.value = clamped
        try {
            mediaPlayer?.setVolume(clamped, clamped)
        } catch (e: Exception) {
            Log.w(tag, "Error setting volume: ${e.message}")
        }
    }

    fun switchStream(url: String) {
        _streamUrl.value = url
        if (_state.value == PlaybackState.PLAYING || _state.value == PlaybackState.CONNECTING) {
            stop()
            play()
        }
    }

    fun setSleepTimer(minutes: Int) {
        sleepTimer?.cancel()
        if (minutes <= 0) {
            _sleepTimerMinutes.value = null
            return
        }

        _sleepTimerMinutes.value = minutes
        val totalMs = minutes * 60 * 1000L
        sleepTimer = object : CountDownTimer(totalMs, 60000L) {
            override fun onTick(millisUntilFinished: Long) {
                val minsLeft = (millisUntilFinished / 60000L).toInt() + 1
                _sleepTimerMinutes.value = minsLeft
            }

            override fun onFinish() {
                _sleepTimerMinutes.value = null
                stop()
            }
        }.start()
    }

    fun cancelSleepTimer() {
        sleepTimer?.cancel()
        sleepTimer = null
        _sleepTimerMinutes.value = null
    }

    private fun initAndStartPlayer(url: String) {
        stopPlayerInternal()
        _state.value = PlaybackState.CONNECTING
        _statusDetail.value = "Connecting to Crystal FM 91.3 Minna..."
        _errorMessage.value = null

        requestAudioFocus()
        RadioPlaybackService.start(appContext)
        acquireLocks()

        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build()
                )

                // Add standard browser & streaming headers to prevent 403/405 drops
                val headers = HashMap<String, String>().apply {
                    put("User-Agent", "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Mobile Safari/537.36")
                    put("Accept", "*/*")
                    put("Icy-MetaData", "1")
                    put("Connection", "keep-alive")
                }

                setDataSource(appContext, Uri.parse(url), headers)

                val currentVol = _volume.value
                setVolume(currentVol, currentVol)

                setOnPreparedListener { mp ->
                    Log.d(tag, "Stream prepared: $url")
                    try {
                        mp.start()
                        _state.value = PlaybackState.PLAYING
                        _statusDetail.value = "Broadcasting Live • 91.3 FM Minna"
                        _errorMessage.value = null
                    } catch (e: Exception) {
                        Log.e(tag, "Error starting MediaPlayer after prepare", e)
                        handlePlaybackError("Failed to start playback.")
                    }
                }

                setOnBufferingUpdateListener { _, percent ->
                    if (_state.value == PlaybackState.CONNECTING) {
                        _statusDetail.value = "Buffering stream ($percent%)..."
                    }
                }

                setOnInfoListener { _, what, _ ->
                    when (what) {
                        MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                            _statusDetail.value = "Buffering audio feed..."
                        }
                        MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                            if (_state.value == PlaybackState.PLAYING) {
                                _statusDetail.value = "Broadcasting Live • 91.3 FM Minna"
                            }
                        }
                    }
                    true
                }

                setOnErrorListener { _, what, extra ->
                    Log.e(tag, "MediaPlayer error: what=$what extra=$extra")
                    // If primary stream fails, auto try backup stream
                    if (url == primaryStreamUrl) {
                        Log.w(tag, "Primary stream failed, attempting backup stream: $backupStreamUrl")
                        _statusDetail.value = "Reconnecting via backup transmitter..."
                        scope.launch {
                            delay(1000)
                            switchStream(backupStreamUrl)
                        }
                    } else {
                        handlePlaybackError("Connection error. Tap Play or switch server.")
                    }
                    true
                }

                prepareAsync()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            Log.e(tag, "Error preparing player: ${e.message}", e)
            handlePlaybackError("Unable to reach stream: ${e.localizedMessage ?: "Network issue"}")
        }
    }

    private fun handlePlaybackError(message: String) {
        _state.value = PlaybackState.ERROR
        _statusDetail.value = "Stream Disconnected"
        _errorMessage.value = message
        releaseLocks()
        abandonAudioFocus()
    }

    private fun stopPlayerInternal() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
        } catch (e: Exception) {
            Log.w(tag, "Error releasing player: ${e.message}")
        } finally {
            mediaPlayer = null
        }
    }

    private fun acquireLocks() {
        try {
            if (wakeLock?.isHeld == false) wakeLock?.acquire(3 * 60 * 60 * 1000L)
            if (wifiLock?.isHeld == false) wifiLock?.acquire()
        } catch (e: Exception) {
            Log.w(tag, "Error acquiring locks: ${e.message}")
        }
    }

    private fun releaseLocks() {
        try {
            if (wakeLock?.isHeld == true) wakeLock?.release()
            if (wifiLock?.isHeld == true) wifiLock?.release()
        } catch (e: Exception) {
            Log.w(tag, "Error releasing locks: ${e.message}")
        }
    }

    private fun requestAudioFocus() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    .setOnAudioFocusChangeListener { focusChange ->
                        when (focusChange) {
                            AudioManager.AUDIOFOCUS_LOSS -> pause()
                            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pause()
                            AudioManager.AUDIOFOCUS_GAIN -> {
                                if (_state.value == PlaybackState.PAUSED) play()
                            }
                        }
                    }
                    .build()
                audioFocusRequest?.let { audioManager?.requestAudioFocus(it) }
            } else {
                @Suppress("DEPRECATION")
                audioManager?.requestAudioFocus(
                    null,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
                )
            }
        } catch (e: Exception) {
            Log.w(tag, "Audio focus request failed: ${e.message}")
        }
    }

    private fun abandonAudioFocus() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioFocusRequest?.let { audioManager?.abandonAudioFocusRequest(it) }
            } else {
                @Suppress("DEPRECATION")
                audioManager?.abandonAudioFocus(null)
            }
        } catch (_: Exception) {}
    }

    private fun startVisualizerLoop() {
        visualizerJob?.cancel()
        visualizerJob = scope.launch {
            val basePattern = listOf(0.35f, 0.75f, 0.5f, 0.95f, 0.65f, 0.85f, 0.4f, 0.8f)
            var step = 0
            while (isActive) {
                if (_state.value == PlaybackState.PLAYING) {
                    val animated = basePattern.mapIndexed { idx, base ->
                        val wave = kotlin.math.sin((step * 0.4 + idx * 1.3))
                        ((base + wave * 0.35).toFloat()).coerceIn(0.18f, 1.0f)
                    }
                    _visualizerAmplitudes.value = animated
                    step++
                } else if (_state.value == PlaybackState.CONNECTING) {
                    val p = ((kotlin.math.sin(step * 0.3).toFloat() + 1f) / 2f) * 0.5f + 0.25f
                    _visualizerAmplitudes.value = List(8) { p }
                    step++
                } else {
                    _visualizerAmplitudes.value = List(8) { 0.12f }
                }
                delay(100)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: RadioManager? = null

        fun getInstance(context: Context): RadioManager {
            return instance ?: synchronized(this) {
                instance ?: RadioManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
