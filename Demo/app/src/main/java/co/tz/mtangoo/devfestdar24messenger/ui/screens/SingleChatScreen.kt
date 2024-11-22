package co.tz.mtangoo.devfestdar24messenger.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.tz.mtangoo.devfestdar24messenger.ui.AppViewModel
import java.text.SimpleDateFormat

@Composable
fun SingleChatScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        val currentUser = viewModel.currentUser.collectAsStateWithLifecycle()
        val messages = viewModel.messageList.collectAsStateWithLifecycle()
        val chat = viewModel.chat.collectAsStateWithLifecycle()

        val user = remember { mutableStateOf(currentUser.value) }
        val currentMessage = remember { mutableStateOf("") }

        val keyboardController = LocalSoftwareKeyboardController.current

        Text(
            text = "Chatting with ${currentUser.value}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextField(
                value = user.value,
                onValueChange = { user.value = it },
                label = { Text("Who do you want to chat with?") },
                modifier = Modifier.weight(1f)
            )

            Button(
                modifier = Modifier.padding(8.dp),
                onClick = { viewModel.changeChatUser(user.value) }
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Send"
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f),
        ) {
            items(messages.value.size) { index ->
                ChatMessageScreen(
                    message = messages.value[index],
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextField(
                value = currentMessage.value,
                onValueChange = { currentMessage.value = it },
                label = { Text("What do you think?") },
                modifier = Modifier.weight(1f)
            )

            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    chat.value?.send(currentMessage.value)
                    currentMessage.value = ""
                    keyboardController?.hide()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send"
                )
            }
        }
    }
}
