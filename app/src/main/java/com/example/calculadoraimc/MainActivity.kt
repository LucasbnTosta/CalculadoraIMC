package com.example.calculadoraimc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraimc.ui.theme.CalculadoraIMCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraIMCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculadoraIMCScreen()
                }
            }
        }
    }
}

@Composable
fun CalculadoraIMCScreen() {
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("") }
    var historico by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Calculadora de IMC",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso (kg)") },
            placeholder = { Text("Ex: 70.5") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = altura,
            onValueChange = { altura = it },
            label = { Text("Altura (m)") },
            placeholder = { Text("Ex: 1.75") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val pesoNum = peso.trim()
                val alturaNum = altura.trim()

                if (pesoNum.isNotEmpty() && alturaNum.isNotEmpty()) {
                    try {
                        val imc = pesoNum.toFloat() / (alturaNum.toFloat() * alturaNum.toFloat())
                        val classificacao = classificarIMC(imc)
                        val emojiIcon = emojiParaIMC(imc)
                        resultado = "Seu IMC: %.2f\nClassificação: %s".format(imc, classificacao)
                        emoji = emojiIcon

                        historico = listOf("IMC: %.2f - %s %s".format(imc, classificacao, emojiIcon)) + historico
                    } catch (e: Exception) {
                        resultado = "Insira valores numéricos válidos."
                        emoji = "❌"
                    }
                } else {
                    resultado = "Preencha todos os campos!"
                    emoji = "⚠️"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Calcular IMC", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (resultado.isNotEmpty()) {
            Text(
                text = resultado,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = emoji,
                fontSize = 32.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (historico.isNotEmpty()) {
            Text(
                text = "Histórico:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                items(historico) { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .background(Color(0xFFF1F1F1))
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

private fun classificarIMC(imc: Float): String {
    return when {
        imc < 18.5 -> "Abaixo do peso"
        imc < 24.9 -> "Peso normal"
        imc < 29.9 -> "Sobrepeso"
        imc < 34.9 -> "Obesidade Grau I"
        imc < 39.9 -> "Obesidade Grau II"
        else -> "Obesidade Grau III"
    }
}

private fun emojiParaIMC(imc: Float): String {
    return when {
        imc < 18.5 -> "😟"
        imc < 24.9 -> "😃"
        imc < 29.9 -> "😐"
        imc < 34.9 -> "😟"
        imc < 39.9 -> "😧"
        else -> "😱"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalculadoraIMC() {
    CalculadoraIMCTheme {
        CalculadoraIMCScreen()
    }
}
