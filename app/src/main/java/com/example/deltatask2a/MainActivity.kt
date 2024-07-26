package com.example.deltatask2a

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer


import android.os.Bundle
import android.text.Layout
import android.util.AttributeSet

import android.view.MotionEvent
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.Nullable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deltatask2a.Objetcs.highscore
import kotlinx.coroutines.delay
import java.nio.file.Path
import kotlin.io.path.moveTo


class MainActivity : ComponentActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mediaPlayer = MediaPlayer.create(this, R.raw.background)
        mediaPlayer.isLooping = true

        setContent {
            MainScreen(mediaPlayer = mediaPlayer)
        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}

@Composable
fun MainScreen(mediaPlayer: MediaPlayer) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!isPlaying) {
            mediaPlayer.start()
            isPlaying = true
        }
    }

    val color1 = Color(0xFF8B4513)
    val color2 = Color(0xFFFF6F61)
    val backgroundColor = Color(0xFFFFEB3B)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.jerry),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "CHASE GAME",
                fontSize = 35.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(
                onClick = {
                    mediaPlayer.pause()
                    Intent(context, Game::class.java).also { context.startActivity(it) }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.7f)
                    .height(50.dp)
                    .border(2.dp, Color.White, shape = RoundedCornerShape(15.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = color1),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    "Start Game",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    Intent(context, Instruction::class.java).also { context.startActivity(it) }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.7f)
                    .height(50.dp)
                    .border(2.dp, Color.White, shape = RoundedCornerShape(15.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = color2),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    "Instructions",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    mediaPlayer.pause()
                    Intent(context, Hacker::class.java).also { context.startActivity(it) }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.7f)
                    .height(50.dp)
                    .border(2.dp, Color.White, shape = RoundedCornerShape(15.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    "Hacker",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.7f)
                    .background(backgroundColor)
                    .height(50.dp)
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "High Score: ${Objetcs.highscore}",
                    color = Color.Black,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}