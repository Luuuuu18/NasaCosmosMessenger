package com.example.nasacosmosmessenger.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.nasacosmosmessenger.component.ChatBubble
import com.example.nasacosmosmessenger.component.InputBar
import com.example.nasacosmosmessenger.data.local.AppDatabase
import com.example.nasacosmosmessenger.data.repository.FavoriteRepository
import com.example.nasacosmosmessenger.viewmodel.FavoriteViewModel
import com.example.nasacosmosmessenger.viewmodel.FavoriteViewModelFactory
import com.example.nasacosmosmessenger.viewmodel.NovaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaChatScreen(
    viewModel: NovaViewModel = viewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val listState = rememberLazyListState()

    val context = LocalContext.current

    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_db"
    ).build()

    val favoriteViewModel: FavoriteViewModel = viewModel(
        factory = FavoriteViewModelFactory(
            FavoriteRepository(db.favoriteDao())
        )
    )

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(messages, key = { it.id }) { message ->

                    ChatBubble(
                        message = message,
                        onLongPress = {
                            favoriteViewModel.addFavorite(message)
                        }
                    )
                }
            }

            InputBar(
                onSend = { text ->
                    viewModel.sendMessage(text)
                }
            )
        }
    }
}
