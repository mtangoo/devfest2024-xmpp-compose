package co.tz.mtangoo.devfestdar24messenger.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.tz.mtangoo.devfestdar24messenger.data.Constants
import co.tz.mtangoo.devfestdar24messenger.data.ChatMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.XMPPException.XMPPErrorException
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Localpart

class AppViewModel : ViewModel() {
    private val _isLoggedIn = MutableSharedFlow<Boolean>(replay = 0)
    val isLoggedIn: SharedFlow<Boolean> = _isLoggedIn

    private val _isLoading = MutableSharedFlow<Boolean>(replay = 0)
    val isLoading: SharedFlow<Boolean> = _isLoading

    private val _message = MutableSharedFlow<String>(replay = 0)
    val message: SharedFlow<String> = _message

    private val _currentUser = MutableStateFlow<String>(Constants.DEFAULT_CHAT_USER)
    val currentUser: StateFlow<String> = _currentUser

    private val _messageList = MutableStateFlow(mutableStateListOf<ChatMessage>())
    val messageList: StateFlow<SnapshotStateList<ChatMessage>> = _messageList

    private val _chat = MutableStateFlow<Chat?>(null)
    val chat: StateFlow<Chat?> = _chat

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.message?.let {
            viewModelScope.launch {
                _message.emit(it)
                _isLoading.emit(false)
            }
        }
    }

    private val connection by lazy {
        val conf = XMPPTCPConnectionConfiguration.builder()
            .setXmppDomain(Constants.HOST)
            .setHost(Constants.IP)
            .setCompressionEnabled(true)
            .setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible)
            .enableDefaultDebugger()
            .build()

        return@lazy XMPPTCPConnection(conf)
    }

    fun login(username: String, password: String) =
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _isLoading.emit(true)

            if (!connection.isConnected) {
                connection.connect()
            }

            //if it crashes possibly user exists
            try {
                // create XMPP user
                val accountManager = AccountManager.getInstance(connection)
                accountManager.sensitiveOperationOverInsecureConnection(true)
                accountManager.createAccount(Localpart.from(username), password)
            } catch (e: XMPPErrorException) {
                // user exists, silently ignore
                e.printStackTrace()
            }

            if (!connection.isAuthenticated) {
                connection.login(username, password)
                changeChatUser(currentUser.value)

                ChatManager.getInstanceFor(connection).addIncomingListener { from, message, chat ->
                    _messageList.value.add(
                        ChatMessage(
                            id = message.stanzaId + System.currentTimeMillis(),
                            sender = from.asBareJid().toString(),
                            text = message.body
                        )
                    )
                }

                ChatManager.getInstanceFor(connection).addOutgoingListener { to, message, chat ->
                    _messageList.value.add(
                        ChatMessage(
                            id = message.stanzaId,
                            sender = connection.user.asBareJid().toString(),
                            text = message.body
                        )
                    )
                }
            }

            _isLoggedIn.emit(connection.isAuthenticated)
            _isLoading.emit(false)
        }

    fun changeChatUser(user: String) = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
        _chat.emit(
            ChatManager.getInstanceFor(connection)
                .chatWith(JidCreate.entityBareFrom("$user@${Constants.HOST}"))
        )
        _currentUser.emit(user)
    }
}