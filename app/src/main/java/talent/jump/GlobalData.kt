package talent.jump

import android.app.Application
import okhttp3.Cookie
import talent.jump.data.LoginData

open class GlobalData: Application()  {

    companion object {
        var Cookies = mutableListOf<Cookie>()
        var loginToken:LoginData=LoginData("",0,"","")
        var CacheCode=""
        var access_token=""
        var refresh_token=""
        var token_type=""
        var expire_at:Long = 0
    }

}