package com.example.arcore

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.arcore.ui.theme.ARCoreTheme
import com.google.ar.core.Config
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import androidx.compose.ui.platform.LocalContext
import com.google.android.filament.Engine
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ARCoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        ARGameScreen()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ARGameScreen() {
    val context = LocalContext.current
    val nodes = remember { mutableStateListOf<ArModelNode>() }
    var currentNumber by remember { mutableStateOf(17) }
    var currentOperation by remember { mutableStateOf(generateRandomOperation(currentNumber)) }
    var userInput by remember { mutableStateOf("") }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var bubbles by remember { mutableStateOf(0) } // Baloncuk sayısı
    var gameEnded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        var engine: Engine? = null // Global bir değişken tanımlayın.

        ARScene(
            modifier = Modifier.fillMaxSize(),
            nodes = nodes,
            planeRenderer = true,
            onCreate = { arSceneView ->
                engine = arSceneView.engine // `Engine` nesnesini burada alın.
                arSceneView.lightEstimationMode = Config.LightEstimationMode.DISABLED
                arSceneView.planeRenderer.isShadowReceiver = false
            },
            onSessionCreate = {
                planeRenderer.isVisible = false
            }
        )

        // 3 Boyutlu Mevcut Sayıyı Göster
        LaunchedEffect(currentNumber) {
            nodes.clear()
            engine?.let { engineInstance -> // `engine` null değilse çağır
                val numberNode = create3DNumberModel(engineInstance, currentNumber) // Hem Engine hem de number geçiliyor
                nodes.add(numberNode)
            }

        }

        // 3 Boyutlu İşlemi Göster
        LaunchedEffect(currentOperation) {
            engine?.let { engineInstance ->
                val operationNode = create3DOperationModel(engineInstance, currentOperation)
                nodes.add(operationNode)
            }

        }

        // Kullanıcı cevabını değerlendirme
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            androidx.compose.material3.TextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { androidx.compose.material3.Text("Sonuç Girin") }
            )
            androidx.compose.material3.Button(onClick = {
                if (gameEnded) return@Button

                val correctAnswer = calculateOperation(currentNumber, currentOperation)
                if (userInput.toIntOrNull() == correctAnswer) {
                    // Doğru cevap
                    toastMessage = "Doğru! Yeni sayınız: $correctAnswer"
                    currentNumber = correctAnswer
                    currentOperation = generateRandomOperation(currentNumber)
                    bubbles++

                    // Baloncuk modeli ekle
                    engine?.let { engineInstance ->
                        val bubbleNode = create3DBubbleModel(engineInstance, bubbles)
                        nodes.add(bubbleNode)
                    }


                    if (bubbles >= 5) {
                        gameEnded = true
                        engine?.let { engineInstance ->
                            val winMessageNode = create3DWinMessage(engineInstance)
                            nodes.add(winMessageNode)
                        }

                    }
                } else {
                    // Yanlış cevap
                    toastMessage = "Yanlış cevap, tekrar deneyin."
                    if (bubbles > 0) {
                        bubbles--
                        nodes.removeLastOrNull() // Son baloncuğu kaldır
                    }
                }
                userInput = ""
            }) {
                androidx.compose.material3.Text("Onayla")
            }
        }

        // Toast Mesajını Göster
        LaunchedEffect(toastMessage) {
            toastMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                toastMessage = null
            }
        }
    }
}

// Rastgele işlem üreten fonksiyon
fun generateRandomOperation(current: Int): Pair<String, Int> {
    val operations = listOf("+", "-", "*", "/")
    var operation: String
    var operand: Int

    do {
        operation = operations.random()
        operand = when (operation) {
            "+" -> Random.nextInt(1, 100 - current)
            "-" -> Random.nextInt(1, current)
            "*" -> Random.nextInt(2, minOf(10, 100 / current))
            "/" -> Random.nextInt(2, 10).takeIf { current % it == 0 } ?: 1
            else -> 1
        }
    } while (!isResultValid(current, operation, operand))

    return operation to operand
}

// İşlemi hesaplayan fonksiyon
fun calculateOperation(current: Int, operation: Pair<String, Int>): Int {
    return when (operation.first) {
        "+" -> current + operation.second
        "-" -> current - operation.second
        "*" -> current * operation.second
        "/" -> current / operation.second
        else -> current
    }
}

// İşlem sonucunun geçerli olup olmadığını kontrol eden fonksiyon
fun isResultValid(current: Int, operation: String, operand: Int): Boolean {
    val result = when (operation) {
        "+" -> current + operand
        "-" -> current - operand
        "*" -> current * operand
        "/" -> if (operand != 0 && current % operand == 0) current / operand else 0
        else -> current
    }
    return result in 1..99
}
fun create3DNumberModel(engine: Engine, number: Int): ArModelNode {
    return ArModelNode(
        engine = engine,
        modelGlbFileLocation = "models/number_$number.glb",
        scaleToUnits = 0.5f
    )
}

fun create3DOperationModel(engine: Engine, operation: Pair<String, Int>): ArModelNode {
    val operationName = when (operation.first) {
        "*" -> "carp" // Çarpma operatörü için "carp"
        "/" -> "bol"  // Bölme operatörü için "bol"
        else -> operation.first // "+" veya "-" olduğu gibi kullanılır
    }

    return ArModelNode(
        engine = engine,
        modelGlbFileLocation = "models/operation_$operationName.glb",
        scaleToUnits = 0.5f
    )
}



fun create3DBubbleModel(engine: Engine, index: Int): ArModelNode {
    return ArModelNode(
        engine = engine,
        modelGlbFileLocation = "models/bubble.glb",
        scaleToUnits = 0.3f
    )
}

fun create3DWinMessage(engine: Engine): ArModelNode {
    return ArModelNode(
        engine = engine,
        modelGlbFileLocation = "models/win_message.glb",
        scaleToUnits = 1.0f
    )
}

