package talent.jump

import android.app.Application
import okhttp3.Cookie
import talent.jump.Fragment.LiveFragment
import talent.jump.data.LoginData
import talent.jump.data.UserData
import talent.jump.data.UserResponse
import talent.jump.data.VerifyCodeData

open class GlobalData: Application()  {

    companion object {
        var Cookies = mutableListOf<Cookie>()
        var loginToken:LoginData=LoginData("",0,"","")
        lateinit var userData:UserResponse
        var CacheCode=""
        var access_token=""
        var refresh_token=""
        var token_type=""
        var expire_at:Long = 0
        var user_ID=""
        var sliderItem= 0
    }

}