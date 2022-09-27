package com.beok.qrcode_sample

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.beok.qrcode_sample.ui.theme.QrcodesampleTheme
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : ComponentActivity() {

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) return@registerForActivityResult
        Toast.makeText(this, "Scanned: ${result.contents}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QrcodesampleTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var text by remember { mutableStateOf("") }
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                    var qrcode: ImageBitmap by remember {
                        mutableStateOf(
                            ImageBitmap(
                                width = WIDTH_HEIGHT,
                                height = WIDTH_HEIGHT,
                                config = ImageBitmapConfig.Rgb565
                            )
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(onClick = {
                            qrcode = qrCode(text) ?: return@Button
                        }) {
                            Text(text = "QR생성")
                        }
                        Button(onClick = {
                            barcodeLauncher.launch(
                                ScanOptions()
                                    .setPrompt("QR코드 딱 대!")
                                    .setBeepEnabled(true)
                            )
                        }) {
                            Text(text = "QR스캔")
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                    Image(
                        modifier = Modifier.wrapContentSize(),
                        bitmap = qrcode,
                        contentDescription = null
                    )
                }
            }
        }
    }

    private fun qrCode(text: String): ImageBitmap? = runCatching {
        BarcodeEncoder()
            .encodeBitmap(
                /* contents = */ text,
                /* format = */ BarcodeFormat.QR_CODE,
                /* width = */ WIDTH_HEIGHT,
                /* height = */ WIDTH_HEIGHT
            )
            .asImageBitmap()
    }.getOrNull()

    companion object {
        private const val WIDTH_HEIGHT = 700
    }
}
