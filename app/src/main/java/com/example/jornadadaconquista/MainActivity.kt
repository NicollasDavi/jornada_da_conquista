package com.example.jornadadaconquista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.saveable.rememberSaveable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var clicks by rememberSaveable { mutableStateOf(0) }
            var isGameStarted by rememberSaveable { mutableStateOf(false) }  // Indica se o jogo já começou
            var totalClicks by rememberSaveable { mutableStateOf(0) }  // Total de cliques aleatórios
            var phaseClicks by rememberSaveable { mutableStateOf(0) }  // Cliques por fase (gerado ao iniciar o jogo)
            var gameState by rememberSaveable { mutableStateOf(GameState.Playing) }
            var currentPhase by rememberSaveable { mutableStateOf(0) }  // Fase inicial
            var message by rememberSaveable { mutableStateOf("Clique no coração para iniciar a jornada!") }
            var imageResource by rememberSaveable { mutableStateOf(R.drawable.fundo) }

            JourneyApp(
                clicks = clicks,
                totalClicks = totalClicks,
                phaseClicks = phaseClicks,
                isGameStarted = isGameStarted,
                currentPhase = currentPhase,
                gameState = gameState,
                imageResource = imageResource,
                message = message,
                onClicksUpdate = { clicks = it },
                onGameStart = { isGameStarted = it },
                onTotalClicksUpdate = { totalClicks = it },
                onPhaseUpdate = { currentPhase = it },
                onGameStateUpdate = { gameState = it },
                onImageResourceUpdate = { imageResource = it },
                onMessageUpdate = { message = it },
                onPhaseClicksUpdate = { phaseClicks = it }
            )
        }
    }
}

@Composable
fun JourneyApp(
    clicks: Int,
    totalClicks: Int,
    phaseClicks: Int,
    isGameStarted: Boolean,
    currentPhase: Int,
    gameState: GameState,
    imageResource: Int,
    message: String,
    onClicksUpdate: (Int) -> Unit,
    onGameStart: (Boolean) -> Unit,
    onTotalClicksUpdate: (Int) -> Unit,
    onPhaseUpdate: (Int) -> Unit,
    onGameStateUpdate: (GameState) -> Unit,
    onImageResourceUpdate: (Int) -> Unit,
    onMessageUpdate: (String) -> Unit,
    onPhaseClicksUpdate: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Text(
                text = message,
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (gameState) {
                GameState.Playing -> ActionButtons(
                    isGameStarted, clicks, totalClicks, phaseClicks, currentPhase,
                    onHeartClick = {
                        if (!isGameStarted) {
                            // O jogo começa: gerar cliques totais e por fase
                            onGameStart(true)
                            val randomTotalClicks = (15..30).random()
                            onTotalClicksUpdate(randomTotalClicks)
                            onPhaseClicksUpdate(randomTotalClicks / 4)  // Dividir os cliques por fase
                            onMessageUpdate("Você conheceu a Gal Gadot!")
                            onImageResourceUpdate(R.drawable._1)
                        } else {
                            onClicksUpdate(clicks + 1)
                            checkPhase(clicks + 1, totalClicks, phaseClicks, currentPhase, onPhaseUpdate, onImageResourceUpdate, onMessageUpdate)
                        }
                    },
                    onCrossClick = { onGameStateUpdate(GameState.Restarting) }
                )
                GameState.Restarting -> RestartButtons(
                    onRestart = {
                        onClicksUpdate(0)
                        onGameStart(false)  // Reiniciar como se o jogo não tivesse começado
                        onPhaseUpdate(0)  // Volta para a fase inicial (fundo padrão)
                        onMessageUpdate("Clique no coração para iniciar a jornada!")
                        onImageResourceUpdate(R.drawable.fundo)
                        onGameStateUpdate(GameState.Playing)
                    }
                )
            }
        }
    }
}

@Composable
fun ActionButtons(
    isGameStarted: Boolean, clicks: Int, totalClicks: Int, phaseClicks: Int, currentPhase: Int, onHeartClick: () -> Unit, onCrossClick: () -> Unit
) {
    Row {
        Button(
            onClick = onCrossClick,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Text("×", fontSize = 36.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = onHeartClick,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Text(if (isGameStarted) "♥" else "Iniciar", fontSize = 36.sp)
        }
    }
}

@Composable
fun RestartButtons(onRestart: () -> Unit) {
    Row {
        Button(
            onClick = onRestart,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Text("Sim", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = { /* Não faz nada ao clicar em "Não" */ },
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Text("Não", fontSize = 18.sp)
        }
    }
}

fun checkPhase(
    clicks: Int, totalClicks: Int, phaseClicks: Int, currentPhase: Int,
    onPhaseUpdate: (Int) -> Unit, onUpdateImage: (Int) -> Unit, onUpdateMessage: (String) -> Unit
) {
    when {
        clicks >= totalClicks -> {
            onPhaseUpdate(4)
            onUpdateImage(R.drawable._4)
            onUpdateMessage("Parabéns! Você conquistou a Gal Gadot!")
        }
        clicks >= phaseClicks * 3 -> {
            onPhaseUpdate(3)
            onUpdateImage(R.drawable._3)
            onUpdateMessage("Ela está gostando de você!")
        }
        clicks >= phaseClicks * 2 -> {
            onPhaseUpdate(2)
            onUpdateImage(R.drawable._2)
            onUpdateMessage("Você a chamou pra sair!")
        }
        clicks >= phaseClicks -> {
            onPhaseUpdate(1)
            onUpdateImage(R.drawable._1)
            onUpdateMessage("Você conheceu a Gal Gadot!")
        }
    }
}

enum class GameState {
    Playing, Restarting
}
