package co.tz.mtangoo.devfestdar24messenger

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import co.tz.mtangoo.devfestdar24messenger.ui.AppViewModel
import co.tz.mtangoo.devfestdar24messenger.ui.screens.LoginScreen
import co.tz.mtangoo.devfestdar24messenger.ui.screens.Progressbar
import co.tz.mtangoo.devfestdar24messenger.ui.screens.SingleChatScreen
import co.tz.mtangoo.devfestdar24messenger.ui.theme.DevFestDar24MessengerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevFestDar24MessengerTheme {
                val viewModel: AppViewModel = viewModel()
                val isLoggedIn = viewModel.isLoggedIn.collectAsStateWithLifecycle(false)
                val isLoading = viewModel.isLoading.collectAsStateWithLifecycle(false)
                val message = viewModel.message.collectAsStateWithLifecycle("")

                val context = LocalContext.current

                if(isLoading.value){
                    Progressbar(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else{
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        if(isLoggedIn.value) {
                            SingleChatScreen(
                                viewModel = viewModel,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        } else {
                            LoginScreen(
                                onLogin = { username, password ->
                                    viewModel.login(username, password)
                                }
                            )
                        }
                    }
                }

                LaunchedEffect(message.value) {
                    if(message.value.isNotEmpty()) {
                        Toast.makeText(context, message.value, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}