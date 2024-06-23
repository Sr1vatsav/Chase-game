package com.example.deltatask2a

import android.content.Context
import android.content.Intent


import android.os.Bundle
import android.text.Layout
import android.util.AttributeSet

import android.view.MotionEvent
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.Nullable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deltatask2a.Objetcs.highscore
import java.nio.file.Path
import kotlin.io.path.moveTo


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val color = Color(0xFF00CED1)
            val salmon = Color(0xFFFF6F61)
            val brown = Color(0xFF8B4513)


            Box(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {

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
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        "CHASE GAME",
                        fontSize = 35.sp,
                        color = Color(0xFF000080),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Button(
                        onClick = {
                            Intent(
                                applicationContext,
                                Game::class.java
                            ).also { startActivity(it) }

                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(0.7f)
                            .height(50.dp)
                            .border(2.dp, Color.White, shape = RoundedCornerShape(15.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = brown),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text("Start Game", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {Intent(
                            applicationContext,
                            Instruction::class.java
                        ).also { startActivity(it) }


                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(0.7f)
                            .height(50.dp)
                            .border(2.dp, Color.White, shape = RoundedCornerShape(15.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = salmon),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text("Instructions", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("High Score:$highscore",
                        color=Color.Black,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

