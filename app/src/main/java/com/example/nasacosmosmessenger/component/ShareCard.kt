package com.example.nasacosmosmessenger.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nasacosmosmessenger.data.local.FavoriteEntity

@Composable
fun ShareCard(
    item: FavoriteEntity,
    preloadedImage: ImageBitmap? = null
) {
    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
            .width(300.dp)
    ) {
        // 🔥 如果有預載好的圖片，就用一般的 Image 直接畫；否則用 AsyncImage
        if (preloadedImage != null) {
            Image(
                bitmap = preloadedImage,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            AsyncImage(
                model = item.url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.title,
            color = Color.White
        )

        Text(
            text = item.date,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}