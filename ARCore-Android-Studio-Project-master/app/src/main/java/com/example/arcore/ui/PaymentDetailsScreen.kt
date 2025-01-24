package com.example.arcore.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arcore.database.JsonDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentDetailsScreen(onPaymentComplete: () -> Unit) {
    var cardNumber by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Kart Numarası") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = cvv,
            onValueChange = { cvv = it },
            label = { Text("CVV") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = expiryDate,
            onValueChange = { expiryDate = it },
            label = { Text("Son Kullanma Tarihi (MM/YY)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (cardNumber.isNotEmpty() && cvv.isNotEmpty() && expiryDate.isNotEmpty()) {
                // Ödeme işlemini kontrol et (Sahte işlem)
                val paymentSuccessful = JsonDatabase.processPayment(cardNumber, cvv, expiryDate)
                if (paymentSuccessful) {
                    onPaymentComplete() // Ödeme tamamlandı, yönlendirme yapılır
                } else {
                    errorMessage = "Ödeme başarısız. Lütfen bilgilerinizi kontrol edin."
                }
            } else {
                errorMessage = "Lütfen tüm alanları doldurun."
            }
        }) {
            Text("Pay and Continue")
        }
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}
