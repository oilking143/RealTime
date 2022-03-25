package talent.jump.Events

import talent.jump.data.LoginResponse

class LoginEvent internal constructor(LoginData: LoginResponse){
    private var loginData: LoginResponse = LoginData

    init {
        this.loginData = LoginData
    }

    fun GetLoginData(): LoginResponse
    {
        return loginData
    }
}

