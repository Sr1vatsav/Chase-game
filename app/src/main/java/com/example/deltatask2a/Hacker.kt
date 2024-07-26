package com.example.deltatask2a

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deltatask2a.Objetcs.o1
import com.example.deltatask2a.Objetcs.o2
import com.example.deltatask2a.Objetcs.o3
import kotlinx.coroutines.delay
import kotlin.random.Random

class Hacker : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Gamescreen()
        }
    }
}

@Composable
fun Gamescreen() {
    val brown = Color(0xFF8B4513)
    val game = RememberGameState()
    val isPowerUpActive = remember { mutableStateOf(false) }
    val powerUpType = remember { mutableStateOf(PowerupEffectType.none) }
    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.tap).apply {
            setOnCompletionListener {
                reset()
                setDataSource(context, Uri.parse("android.resource://${context.packageName}/${R.raw.tap}"))
                prepare()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    if (!game.value.gameLost) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(10)
                ColumnMove(game)
                if (game.value.score % 100 == 0 && game.value.score != 0) {
                    game.value.player.y -= 80f
                }
                game.value.score += 1

                if (!isPowerUpActive.value) {
                    for (powerup in game.value.powerups) {
                        if (powerup.effectType != PowerupEffectType.none) {
                            powerUpType.value = powerup.effectType
                            isPowerUpActive.value = true
                            powerup.effectType = PowerupEffectType.none
                        }
                    }
                }
            }
        }

        HandlePowerUpEffects(
            isPowerUpActive = isPowerUpActive,
            powerUpType = powerUpType,
            game = game
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(5.dp, color = Color.Black)
        ) {
            Text(
                text = "Score: ${game.value.score}",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Black,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            if (mediaPlayer.isPlaying) {
                                mediaPlayer.pause()
                                mediaPlayer.seekTo(0)
                            }
                            mediaPlayer.start()

                            val tappedPowerup = game.value.powerups.find { powerup ->
                                hit(offset.x, offset.y, 10f, powerup.x, powerup.y, powerup.size, powerup.size)
                            }
                            if (tappedPowerup != null) {
                                tappedPowerup.effectType = if (Random.nextBoolean()) PowerupEffectType.MoveForward else PowerupEffectType.RemoveObstacles
                            }

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
                    }
            ) {
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
                    newObstacles += GenerateObstacles()
                }

                drawCircle(
                    color = brown,
                    radius = game.value.player.size,
                    center = Offset(game.value.player.x, game.value.player.y)
                )

                drawCircle(
                    color = Color.Gray,
                    radius = game.value.tom.size,
                    center = Offset(game.value.tom.x, game.value.tom.y)
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
                val powerups = game.value.powerups.map { powerup ->
                    val Y = powerup.y + game.value.player.speed / 10f
                    if (Y > size.height) {
                        null
                    } else {
                        powerup.copy(y = Y)
                    }
                }.filterNotNull().toMutableList()
                if (powerups.isEmpty() || powerups.last().y >= columnHeight.toFloat()) {
                    powerups += powerups(game)
                }
                game.value = game.value.copy(powerups = powerups)
                powerups.forEach { powerup ->
                    drawCircle(
                        color = Color.Yellow,
                        center = Offset(powerup.x, powerup.y),
                        radius = powerup.size
                    )
                }
            }
        }
    } else {
        Objetcs.score = game.value.score
        LaunchedEffect(Unit) {
            context.startActivity(Intent(context, Win2::class.java))
            if (context is Activity) {
                context.finish()
            }
        }
    }
}

@Composable
fun HandlePowerUpEffects(
    isPowerUpActive: MutableState<Boolean>,
    powerUpType: MutableState<PowerupEffectType>,
    game: MutableState<Game_State>
) {
    LaunchedEffect(isPowerUpActive.value) {
        if (isPowerUpActive.value) {
            when (powerUpType.value) {
                PowerupEffectType.MoveForward -> {
                    game.value.tom.y += 3f
                }
                PowerupEffectType.RemoveObstacles -> {
                    game.value = game.value.copy(obstacles = emptyList())
                    delay(5000)
                    game.value = game.value.copy(obstacles = GenerateObstacles())
                }
                PowerupEffectType.none -> {
                    // Do nothing
                }
            }
            isPowerUpActive.value = false
            powerUpType.value = PowerupEffectType.none
        }
    }
}


fun ColumnMove(game: MutableState<Game_State>) {
    val columnSpeed = 5f
    val newColumns = game.value.columns.map { it.copy(y = it.y + columnSpeed) }
    val newObstacles = game.value.obstacles.map { it.copy(y = it.y + columnSpeed) }
    val PowerUps=game.value.powerups.map { it.copy(y = it.y + columnSpeed) }

    game.value = game.value.copy(columns = newColumns, obstacles = newObstacles, powerups =PowerUps)
    for (powerup in game.value.powerups){
        if(hit(game.value.player.x,
                game.value.player.y,
                game.value.player.size,
                powerup.x,powerup.y,
                powerup.size,
                powerup.size)){
            val effectType = if (Random.nextBoolean()) PowerupEffectType.MoveForward else PowerupEffectType.RemoveObstacles
            powerup.effectType=effectType
        }
    }



    for (obstacle in game.value.obstacles) {
        if (hit(
                game.value.player.x,
                game.value.player.y,
                game.value.player.size,
                obstacle.x,
                obstacle.y,
                obstacle.width,
                obstacle.height
            )
        ) {
            game.value.tom.y -= 10f
        }
    }





    if (game.value.tom.y - game.value.player.y <= 175f) {
        game.value.gameLost = true
    }
}

@Composable
fun RememberGameState(): MutableState<Game_State> {
    return remember {
        mutableStateOf(
            Game_State(
                player = Jerry(x = 550f, y = 1300f, size = 75f, speed = 192),
                tom = Tom(x = 550f, y = 1920f, size = 100f, distanceToJerry = 445f, speed = 192),
                obstacles = GenerateObstacles(),
                isCollision = false,
                columns = listOf(
                    Column(x = 0f, y = 0f, width = 100f, height = 300f),
                    Column(x = 200f, y = 0f, width = 100f, height = 400f),
                    Column(x = 400f, y = 0f, width = 100f, height = 350f)
                ),
                score = 0,
                jerryHitCount = 0,
                gameLost = false,
                powerups = emptyList()
            )
        )
    }
}

fun GenerateObstacles(): List<Obstacle> {
    val obstacles = mutableListOf<Obstacle>()
    val y1 = Random.nextInt(0, 550)
    val y2 = Random.nextInt(0, 550)
    val y3 = Random.nextInt(400, 550)
    o1=y1
    o2=y2
    o3=y3
    obstacles.add(Obstacle(x = 850f, y = y2.toFloat(), width = 170f, height = 170f))
    obstacles.add(Obstacle(x = 490f, y = y1.toFloat(), width = 170f, height = 170f))
    obstacles.add(Obstacle(x = 100f, y = y3.toFloat(), width = 170f, height = 170f))
    return obstacles
}



fun powerups(game: MutableState<Game_State>): List<Powerup> {
    val powerups = mutableListOf<Powerup>()
    val y = Random.nextInt(0, 550)
    val x = listOf(920f, 560f, 170f).random()
    for (obstacle in game.value.obstacles) {
        if (!hit(x, y.toFloat(), 50f, obstacle.x, obstacle.y, obstacle.width, obstacle.height)) {
            powerups.add(Powerup(x = x, y = y.toFloat(), size = 50f, effectType = PowerupEffectType.none))
        }
    }
    return powerups
}




fun hit(x1: Float, y1: Float, size1: Float, x2: Float, y2: Float, width2: Float, height2: Float): Boolean {
    return x1 < x2 + width2 && x1 + size1 > x2 && y1 < y2 + height2 && y1 + size1 > y2
}

data class Powerup(
    var x: Float,
    var y: Float,
    val size: Float,
    var effectType: PowerupEffectType
)
enum class PowerupEffectType {
    MoveForward,
    RemoveObstacles,
    none
}


data class Game_State(
    var player: Jerry,
    var tom: Tom,
    var obstacles: List<Obstacle>,
    var isCollision: Boolean,
    var columns: List<Column>,
    var score: Int,
    var jerryHitCount: Int,
    var gameLost: Boolean,
    var powerups: List<Powerup>
)
