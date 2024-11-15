package com.example.proyectokotlinense

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


//Revisar las versiones de android
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //JetPassComposse enlazado
        val vistadesdeelcomposse = findViewById<ComposeView>(R.id.holadesdelavistacomposse)
        vistadesdeelcomposse.setContent {
            HolaconJetPackCompose()
        }

    }
    @Preview (showSystemUi = true)
    @Composable
    private fun HolaconJetPackCompose() {
        Text(text ="Hola con JetPack Compose" , modifier =  Modifier.wrapContentWidth(Alignment.CenterHorizontally))

    }
}
