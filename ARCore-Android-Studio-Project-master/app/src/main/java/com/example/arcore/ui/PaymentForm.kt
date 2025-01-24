package com.example.arcore.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentForm(onPaymentSuccess: () -> Unit) {
    var cardNumber by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Bu efekt, butona tıklama sonrasında ödeme işlemini başlatmak için kullanılır.
    LaunchedEffect(isProcessing) {
        if (isProcessing) {
            delay(2000) // 2 saniye bekleyelim (simülasyon)
            if (processPayment(cardNumber, cvv, expiryDate)) {
                onPaymentSuccess() // Başarıyla ödeme işlemi sonlandırıldığında başarı ekranına geç
            } else {
                // Ödeme başarısız olduğunda hata mesajı göster
                Toast.makeText(context, "Payment Failed", Toast.LENGTH_SHORT).show()
            }
            isProcessing = false // İşlem tamamlandığında işleme son ver
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter Payment Details")
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Card Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = cvv,
            onValueChange = { cvv = it },
            label = { Text("CVV") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = expiryDate,
            onValueChange = { expiryDate = it },
            label = { Text("Expiry Date (MM/YY)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isProcessing = true // İşleme başla
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isProcessing
        ) {
            Text(if (isProcessing) "Processing..." else "Submit Payment")
        }
    }
}

fun processPayment(cardNumber: String, cvv: String, expiryDate: String): Boolean {
    // Simülasyon için ödeme işlemi: Geçerli kart numarası, CVV ve son kullanma tarihi kontrolü
    return cardNumber.length == 16 && cvv.length == 3 && expiryDate.isNotEmpty()
}
