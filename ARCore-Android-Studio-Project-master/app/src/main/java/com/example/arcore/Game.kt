package com.example.arcore

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.google.ar.core.Config
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.math.Position
import kotlin.random.Random

class Game : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ARGameScreen(onPaymentRequired = {
                // Burada ödeme yapılacak sayfaya yönlendirme yapılabilir
                Toast.makeText(this, "Ödeme sayfasına yönlendiriliyorsunuz", Toast.LENGTH_SHORT).show()
                // Ödeme sayfasına yönlendirme kodunu buraya ekleyebilirsiniz
            })
        }

    }
}

const val MAX_NUMBER = 99
const val TARGET_BUBBLES = 5

fun generateRandomOperation(current: Int): Pair<String, Int> {
    val operations = listOf("+", "-", "*", "/")
    var operation: String
    var operand: Int

    do {
        operation = operations.random()
        operand = when (operation) {
            "+" -> Random.nextInt(1, 10)
            "-" -> Random.nextInt(1, current + 1)
            "*" -> Random.nextInt(1, 10)
            "/" -> Random.nextInt(1, 10).takeIf { current % it == 0 } ?: 1
            else -> 1
        }
    } while (!isResultValid(current, operation, operand))

    return operation to operand
}

fun calculateOperation(current: Int, operation: Pair<String, Int>): Int {
    return when (operation.first) {
        "+" -> current + operation.second
        "-" -> current - operation.second
        "*" -> current * operation.second
        "/" -> current / operation.second
        else -> current
    }
}


fun isResultValid(current: Int, operation: String, operand: Int): Boolean {
    val result = when (operation) {
        "+" -> current + operand
        "-" -> current - operand
        "*" -> current * operand
        "/" -> if (operand != 0 && current % operand == 0) current / operand else 0
        else -> current
    }
    return result in 1..MAX_NUMBER
}
fun createCombinedNumberModel(
    number: Int,
    sceneView: ArSceneView,
    position: Position,
    scale: Float = 0.5f,
    spacing: Float = 1.5f
): List<ArModelNode> {
    val nodes = mutableListOf<ArModelNode>()
    val digits = number.toString().map { it.toString() } // Basamakları çıkar

    // Basamakları soldan sağa sıralı olacak şekilde oluştur
    digits.forEachIndexed { index, digit ->
        val digitPosition = Position(
            position.x - ((digits.size - 1) / 2f * spacing) + index * spacing, // Ortalayarak sıralama
            position.y,
            position.z
        )
        val digitNode = create3DModel(digit, sceneView, digitPosition, scale)
        nodes.add(digitNode)
    }

    return nodes
}


fun create3DModel(
    modelName: String,
    sceneView: ArSceneView,
    position: Position,
    scale: Float = 0.5f
): ArModelNode {
    val glbFile = when (modelName) {
        "+" -> "operation_plus.glb"
        "-" -> "operation_minus.glb"
        "*" -> "operation_carp.glb"
        "/" -> "operation_bol.glb"
        else -> "number_$modelName.glb" // Sayılar için
    }
    Log.d("ARGame", "3D Model Loaded: $glbFile at $position")
    return ArModelNode(
        sceneView.engine,
        modelGlbFileLocation = "models/$glbFile",
        scaleToUnits = scale,
        centerOrigin = position
    )
}


fun create3DWinMessage(sceneView: ArSceneView): ArModelNode {
    return ArModelNode(
        sceneView.engine,
        modelGlbFileLocation = "models/win_message.glb",
        scaleToUnits = 1.5f, // Daha büyük ölçek
        centerOrigin = Position(0f, 0f, -2f) // Daha merkezi bir pozisyon
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ARGameScreen(onPaymentRequired: () -> Unit) { // Burada onPaymentRequired parametresini ekledik
    val context = LocalContext.current
    val nodes = remember { mutableStateListOf<ArModelNode>() }
    var currentNumber by remember { mutableStateOf((1..10).random()) }
    var currentOperation by remember { mutableStateOf(generateRandomOperation(currentNumber)) }
    var userInput by remember { mutableStateOf("") }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var correctAnswers by remember { mutableStateOf(0) }
    var gameEnded by remember { mutableStateOf(false) }
    var arSceneView: ArSceneView? by remember { mutableStateOf(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        // AR Scene Setup
        ARScene(
            modifier = Modifier.fillMaxSize(),
            nodes = nodes,
            planeRenderer = true,
            onCreate = { arSceneViewInstance ->
                arSceneView = arSceneViewInstance
                arSceneViewInstance.lightEstimationMode = Config.LightEstimationMode.DISABLED
                arSceneViewInstance.planeRenderer.isShadowReceiver = false
            }
        )

        val MODEL_SPACING = 3f

        // AR Model Güncellemesi
        LaunchedEffect(currentNumber, currentOperation, gameEnded) {
            arSceneView?.let { sceneView ->
                if (!gameEnded) {
                    nodes.forEach { it.destroy() }
                    nodes.clear()

                    val leftNodes = createCombinedNumberModel(currentNumber, sceneView, Position(-MODEL_SPACING, 0f, -1f))
                    nodes.addAll(leftNodes)

                    val operationNode = create3DModel(currentOperation.first, sceneView, Position(0f, 0f, -1f))
                    nodes.add(operationNode)

                    val rightNodes = createCombinedNumberModel(currentOperation.second, sceneView, Position(MODEL_SPACING, 0f, -1f))
                    nodes.addAll(rightNodes)
                } else {
                    nodes.forEach { it.destroy() }
                    nodes.clear()

                    val winNode = create3DWinMessage(sceneView)
                    nodes.add(winNode)
                }
            }
        }

        // Kullanıcı girişi ve doğrulama
        if (!gameEnded) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TextField(
                        value = userInput,
                        onValueChange = { input -> userInput = input.filter { it.isDigit() } },
                        label = { Text("Sonuç Girin") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            submitAnswer(userInput, currentNumber, currentOperation,
                                onCorrect = {
                                    toastMessage = "Doğru! Yeni sayınız: $it"
                                    currentNumber = it
                                    currentOperation = generateRandomOperation(currentNumber)
                                    correctAnswers++

                                    if (correctAnswers >= TARGET_BUBBLES) {
                                        gameEnded = true
                                    }
                                },
                                onIncorrect = {
                                    toastMessage = "Yanlış cevap, tekrar deneyin."
                                    correctAnswers = 0
                                }
                            )
                            userInput = "" // Girişi sıfırla
                        })
                    )

                    Button(onClick = {
                        submitAnswer(userInput, currentNumber, currentOperation,
                            onCorrect = {
                                toastMessage = "Doğru! Yeni sayınız: $it"
                                currentNumber = it
                                currentOperation = generateRandomOperation(currentNumber)
                                correctAnswers++

                                if (correctAnswers >= TARGET_BUBBLES) {
                                    gameEnded = true
                                }
                            },
                            onIncorrect = {
                                toastMessage = "Yanlış cevap, tekrar deneyin."
                                correctAnswers = 0
                            }
                        )
                        userInput = "" // Girişi sıfırla
                    }) {
                        Text("Onayla")
                    }
                }
            }
        } else {
            // Oyun bittiğinde ödeme sayfasına yönlendiren buton ekleniyor
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = {
                    // Ödeme sayfasına yönlendir
                    onPaymentRequired()
                }) {
                    Text("Ödeme Yap ve Devam Et")
                }
            }
        }

        // Toast mesajı
        LaunchedEffect(toastMessage) {
            toastMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                toastMessage = null
            }
        }
    }
}

// Cevabı değerlendirme fonksiyonu
fun submitAnswer(
    userInput: String,
    currentNumber: Int,
    currentOperation: Pair<String, Int>,
    onCorrect: (Int) -> Unit,
    onIncorrect: () -> Unit
) {
    val userAnswer = userInput.toIntOrNull()
    if (userAnswer == null) {
        return
    }
    val correctAnswer = calculateOperation(currentNumber, currentOperation)
    if (userAnswer == correctAnswer) {
        onCorrect(correctAnswer)
    } else {
        onIncorrect()
    }
}
