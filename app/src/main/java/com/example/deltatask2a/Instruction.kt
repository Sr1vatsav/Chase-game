package com.example.deltatask2a

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Instruction:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Spacer(modifier=Modifier.height(100.dp))
                        Text(
                            text = "Tom is chasing Jerry, but it can’t just run away as it’s faced with obstacles and traps in front of it. " +
                                    "It has to tackle by moving left and right so that it doesn’t slow down by hitting an obstacle or possibly die by getting caught in the trap. " +
                                    "Tom nears Jerry everytime it hits an obstacle, paving way for it to catch it in the next opportunity.",
                            color = Color.Black,
                            fontSize = 30.sp,
                            lineHeight = 36.sp,
                            textAlign = TextAlign.Justify,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            Intent(
                                applicationContext,
                                MainActivity::class.java
                            ).also { startActivity(it) }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Back", fontSize = 30.sp)
                    }
                }
            }


        }
    }
}
