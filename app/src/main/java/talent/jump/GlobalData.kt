package talent.jump

import android.app.Application
import okhttp3.Cookie

open class GlobalData: Application()  {

    companion object {
        var Cookies = mutableListOf<Cookie>()
        var CacheCode=""
    }

}