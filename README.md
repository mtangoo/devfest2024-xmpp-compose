# DevFestDar 2024: Real Time Messaging with Compose and XMPP

### Install XMPP Server
To start working with XMPP you need a server. You can install it from various servers. Here are my top3
1. [eJabberd](https://www.ejabberd.im/)
2. [OpenFire](https://www.igniterealtime.org/projects/openfire/)
3. [Prosody](https://prosody.im/)
There are myriads of XMPP server. Please check full list at protocol site with what they offer. The list is [here](https://docs.ejabberd.im/admin/install/)
In this demo I will use eJabberd. Please find [instructions on how to install it here](https://docs.ejabberd.im/admin/install/). If you choose something different, It won't make any difference as long as it is correctly configured.

### Configure eJabberd
1. Move to [this](https://docs.ejabberd.im/admin/configuration/) page and click Basic Configuration. Configure your `ejabberd.yml` effectively. For *nix systems you can use `find / -iname ejabberd.yml` to find where your config file is. For ejabberd, you can start server in live mode and it will let you know where your config lives.
2. Under Listen:
   - make sure port is 5222. If it is different  change it, for sake of uniformity.
   - starttls_required must be false to just suppress certificate errors and make demo simple. In practice, you ensure maximum security so this is better when it is on.

### Register Users
XMPP Servers provides tools to manage users. You can use the provided REST APIs, command-line tools or XMPP protocol itself to create users. In this talk we will use eJabberd's commandline tool to manage things. The name of the tool is `ejabberdctl` and its documentation is found [here](https://docs.ejabberd.im/admin/guide/managing/#ejabberdctl-commands).
We are going to create two users, Kii and Lii who will talk to with our apps. Let's do that:
1. open one terminal tab and key in `ejabberdctl live`. That will start ejabberd in live mode, showing all logs on console. Leave that open and add new tab
2. In the new tab, run `ejabberdctl help commands`. That should give you something to start with. We want help for registering, so accounts is what we want
3. List accounts with `ejabberdctl registered_users localhost`
4. Now we will add our two users to our localhost domain
   -   `ejabberdctl register kii localhost 123456`
   -   `ejabberdctl register lii localhost 123456`
   -   You should get successfully registered message. You can cross-check with `ejabberdctl registered_users localhost`
     
That is all you need to have your server ready. Please follow respective server registration process if you used different server

### Setup Android App
1. Clone this repository
2. Open your Android Studio. I use Ladybug, but I'm not sure if it makes much difference as I use basic features. Fo easy of life, you can make it to that version. It hurts nothing. Whatever, just have a working version of Studio that is not ancient! Android Studio is [Downloadable here](https://developer.android.com/studio)
3. Open Demo folder in your Android Studio and make sure gradle syncs successfully
4. Try to run the application

### Libraries prominently used
1. [Kotlin coroutine](https://kotlinlang.org/docs/coroutines-overview.html)
2. [Jetpack Compose](https://developer.android.com/compose)
3. [Smack](https://github.com/igniterealtime/Smack)

Happy DevFestDar 2024!
