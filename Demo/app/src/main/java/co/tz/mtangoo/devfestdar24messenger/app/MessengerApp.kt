package co.tz.mtangoo.devfestdar24messenger.app

import android.app.Application
import org.jivesoftware.smack.android.AndroidSmackInitializer

class MessengerApp: Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidSmackInitializer.initialize(applicationContext)
    }
}