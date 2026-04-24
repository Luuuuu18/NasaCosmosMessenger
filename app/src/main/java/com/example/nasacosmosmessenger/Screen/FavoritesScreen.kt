package com.example.nasacosmosmessenger.Screen

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.TypedValue
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.ImageRequest.*
import com.example.nasacosmosmessenger.component.FavoriteCard
import com.example.nasacosmosmessenger.component.ShareCard
import com.example.nasacosmosmessenger.data.local.AppDatabaseProvider
import com.example.nasacosmosmessenger.data.local.FavoriteEntity
import com.example.nasacosmosmessenger.data.repository.FavoriteRepository
import com.example.nasacosmosmessenger.util.shareImage
import com.example.nasacosmosmessenger.util.viewToBitmap
import com.example.nasacosmosmessenger.viewmodel.FavoriteViewModel
import com.example.nasacosmosmessenger.viewmodel.FavoriteViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen() {

    val context = LocalContext.current
    val parentComposition = rememberCompositionContext()
    val currentView = LocalView.current

    // 🔥 1. 新增 CoroutineScope，用來處理非同步的等待
    val coroutineScope = rememberCoroutineScope()

    val db = remember { AppDatabaseProvider.getDatabase(context) }
    val repository = remember { FavoriteRepository(db.favoriteDao()) }

    val viewModel: FavoriteViewModel = viewModel(
        factory = FavoriteViewModelFactory(repository)
    )

    val list by viewModel.favorites.collectAsState(initial = emptyList())
    var selectedItem by remember { mutableStateOf<FavoriteEntity?>(null) }

    fun handleShare(item: FavoriteEntity) {
        coroutineScope.launch {
            try {
                // 1. 強制 Coil 抓取非硬體加速的圖片 (確保有星星圖)
                val request = ImageRequest.Builder(context)
                    .data(item.url)
                    .allowHardware(false)
                    .memoryCachePolicy(coil.request.CachePolicy.DISABLED)
                    .build()

                val result = context.imageLoader.execute(request)
                val drawable = result.drawable

                val bitmapFromCoil = if (drawable is android.graphics.drawable.BitmapDrawable) {
                    drawable.bitmap
                } else if (drawable != null) {
                    val bmp = Bitmap.createBitmap(
                        drawable.intrinsicWidth.takeIf { it > 0 } ?: 1000,
                        drawable.intrinsicHeight.takeIf { it > 0 } ?: 1000,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = android.graphics.Canvas(bmp)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)
                    bmp
                } else {
                    null
                }
                val preloadedBitmap = bitmapFromCoil?.asImageBitmap()

                // 2. 建立 ComposeView
                val composeView = ComposeView(context).apply {
                    setViewTreeLifecycleOwner(currentView.findViewTreeLifecycleOwner())
                    setViewTreeSavedStateRegistryOwner(currentView.findViewTreeSavedStateRegistryOwner())
                    setViewTreeViewModelStoreOwner(currentView.findViewTreeViewModelStoreOwner())

                    setContent {
                        MaterialTheme {
                            ShareCard(item = item, preloadedImage = preloadedBitmap)
                        }
                    }
                }

                // 將 dp 轉換成精準的 px 尺寸
                val displayMetrics = context.resources.displayMetrics
                val widthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, displayMetrics).toInt()
                val heightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500f, displayMetrics).toInt()

                // 🌟 關鍵神技開始 🌟
                val params = android.widget.FrameLayout.LayoutParams(widthPx, heightPx)

                // 把它推到螢幕右邊 10000 像素的地方（使用者絕對看不到）
                composeView.translationX = 10000f

                // 取得當前 APP 的最底層佈局 (RootView)，並把我們的分享卡片硬塞進去
                val rootView = currentView.rootView as android.view.ViewGroup
                rootView.addView(composeView, params)

                // ComposeView 終於發現自己在螢幕上了，開始認真排版畫圖
                // 我們給它 0.3 秒的時間畫完
                composeView.postDelayed({
                    try {
                        // 3. 拍照截圖！
                        val finalBitmap = viewToBitmap(composeView)

                        // 4. 呼叫你原本寫得很完美的 shareImage
                        shareImage(context, finalBitmap)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        rootView.removeView(composeView)
                    }
                }, 300)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("收藏") }
            )
        }
    ) { padding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(list) { item ->
                FavoriteCard(
                    item = item,
                    onDelete = { viewModel.deleteFavorite(item) },
                    onClick = { selectedItem = item },
                    onShare = { handleShare(item) }
                )
            }
        }
    }

    selectedItem?.let { item ->
        Dialog(
            onDismissRequest = {
                selectedItem = null
            }
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column {

                    AsyncImage(
                        model = item.url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = item.explanation,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}