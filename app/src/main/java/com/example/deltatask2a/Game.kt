package com.example.deltatask2a

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.Px
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deltatask2a.Objetcs.highscore
import com.example.deltatask2a.Objetcs.score
import kotlinx.coroutines.delay



import kotlin.random.Random

class Game:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent {
            GameScreen()
        }

    }
}

data class Jerry(var x: Float, var y: Float, val size: Float,var speed:Int)
data class Tom(var x: Float, var y: Float, val size: Float, var distanceToJerry: Float,var speed:Int)
data class Obstacle(val x: Float, var y: Float, val width: Float, val height: Float)
data class Column(var x: Float, var y: Float, val width: Float, val height: Float)

data class GameState(
    var player: Jerry,
    var tom: Tom,
    var obstacles: List<Obstacle>,
    var isCollision: Boolean,
    var columns: List<Column>,
    var score: Int,
    var jerryHitCount: Int
    ,var gameLost:Boolean
    )


@Composable
fun GameScreen() {
    val brown = Color(0xFF8B4513)
    val game = rememberGameState()

    if(!game.value.gameLost) {
        LaunchedEffect(Unit) {
            while (true) {
                columnMove(game)
                delay(10)
                game.value.score+=1
            }
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)) {
            Text(
                text = "Score: ${game.value.score}",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Black, fontSize = 25.sp
            )

            Canvas(modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (game.value.player.x == 550f) {
                            if (offset.x < size.width / 2) {
                                game.value.player.x = 160f

                            } else {
                                game.value.player.x = 910f
                            }
                            game.value.tom.x = game.value.player.x
                        } else if (game.value.player.x == 910f) {
                            if (offset.x < size.width) {
                                game.value.player.x = 550f
                            }
                            game.value.tom.x = game.value.player.x
                        } else {
                            game.value.player.x = 550f
                            game.value.tom.x = game.value.player.x

                        }
                    }
                }) {
                val columnWidth = size.width / 3
                val columnHeight = size.height.toInt()
                val stripeWidth = 10.dp.toPx()
                for (i in 0..2) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(x = i * (columnWidth + stripeWidth), y = 0f),
                        size = Size(width = columnWidth, height = size.height),
                        style = Fill
                    )
                }

                for (i in 0 until 2) {
                    drawRect(
                        color = Color.White,
                        topLeft = Offset(x = (i + 1) * columnWidth + i * stripeWidth, y = 0f),
                        size = Size(width = stripeWidth, height = size.height),
                        style = Fill
                    )
                }
                val newObstacles = game.value.obstacles.map { obstacle ->
                    val Y = obstacle.y + game.value.player.speed / 10f
                    if (Y > size.height) {
                        null
                    } else {
                        obstacle.copy(y = Y)
                    }
                }.filterNotNull().toMutableList()
                if (newObstacles.isEmpty() || newObstacles.last().y >= columnHeight.toFloat()) {
                    newObstacles += generateObstacles()
                }
                drawCircle(
                    color = brown,
                    radius = game.value.player.size,
                    Offset(game.value.player.x, game.value.player.y)
                )

                drawCircle(
                    color = Color.Gray,
                    radius = game.value.tom.size,
                    Offset(game.value.tom.x, game.value.tom.y)
                )

                game.value = game.value.copy(obstacles = newObstacles)
                newObstacles.forEach { obstacle ->
                    drawRect(
                        color = Color.Red,
                        topLeft = Offset(obstacle.x, obstacle.y),
                        size = Size(obstacle.width, obstacle.height),
                        style = Fill
                    )
                }
            }

        }

    }

    else{
        score=game.value.score
       val context= LocalContext.current
        LaunchedEffect(Unit) {
            context.startActivity(Intent(context, Win::class.java))
            if (context is Activity) {
                context.finish()
            }
        }



    }

}

fun columnMove(game: MutableState<GameState>) {

    val columnSpeed = 5f
    val newColumns = game.value.columns.map { it.copy(y = it.y + columnSpeed) }
    val newObstacles = game.value.obstacles.map { it.copy(y = it.y + columnSpeed) }

    if (game.value.obstacles.isNotEmpty() && game.value.obstacles.last().y >= game.value.player.size) {
        game.value = game.value.copy(obstacles = newObstacles)
    } else {
        game.value = game.value.copy(columns = newColumns, obstacles = newObstacles)
    }
    for (obstacle in game.value.obstacles) {
        if (Hit(game.value.player.x, game.value.player.y, game.value.player.size, obstacle.x, obstacle.y, obstacle.width, obstacle.height)) {
            game.value.tom.y-=10f
        }
    }
    if(game.value.tom.y-game.value.player.y<=175f){
        game.value.gameLost=true
    }
}

@Composable
fun rememberGameState(): MutableState<GameState> {
    return remember {
        mutableStateOf(
            GameState(
                player = Jerry(x = 550f, y = 1300f, size = 75f, speed = 192),
                tom = Tom(x = 550f, y = 1920f, size = 100f, distanceToJerry = 445f, speed = 192),
                obstacles = generateObstacles(),
                isCollision = false,
                columns = listOf(
                    Column(x = 0f, y = 0f, width = 100f, height = 300f),
                    Column(x = 200f, y = 0f, width = 100f, height = 400f),
                    Column(x = 400f, y = 0f, width = 100f, height = 350f)
                ),
                score = 0,
                jerryHitCount = 0,gameLost = false
            )
        )
    }
}

fun generateObstacles(): List<Obstacle> {
    val obstacles = mutableListOf<Obstacle>()
    val  y1=Random.nextInt(0,1000)
    val y2=Random.nextInt(0,200)
    val y3=Random.nextInt(400,550)
    obstacles.add(Obstacle(x = 850f, y =y2.toFloat(), width = 170f, height = 170f))
    obstacles.add(Obstacle(x = 490f, y = y1.toFloat(), width = 170f, height = 170f))
    obstacles.add(Obstacle(x = 100f, y =y3.toFloat() ,width = 170f, height = 170f))
    return obstacles
}
fun Hit(x1: Float, y1: Float, size1: Float, x2: Float, y2: Float, width2: Float, height2: Float): Boolean {
    return x1 < x2 + width2 && x1 + size1 > x2 && y1 < y2 + height2 && y1 + size1 > y2
}

