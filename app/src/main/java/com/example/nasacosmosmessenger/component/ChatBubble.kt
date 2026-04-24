package com.example.nasacosmosmessenger.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nasacosmosmessenger.R
import com.example.nasacosmosmessenger.model.ChatMessage

@Composable
fun ChatBubble(
    message: ChatMessage,
    onLongPress: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement =
        if (message.isUser) Arrangement.End else Arrangement.Start
    ) {

        if (!message.isUser) {
            Image(
                painter = painterResource(id = R.drawable.ic_bot),
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .padding(end = 6.dp)
            )
        }

        Column(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { onLongPress() }
                    )
                }
                .background(
                    color = if (message.isUser)
                        Color(0xFF4A90E2)
                    else
                        Color(0xFF2C2C2E),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(10.dp)
                .widthIn(max = 280.dp)
        ) {


            if (!message.isUser && message.imageUrl != null) {
                AsyncImage(
                    model = message.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(bottom = 6.dp)
                )
            }


            message.content?.let {
                Text(
                    text = it,
                    color = Color.White
                )
            }


            message.date?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    color = Color.LightGray,
                    fontSize = 10.sp
                )
            }
        }
    }
}