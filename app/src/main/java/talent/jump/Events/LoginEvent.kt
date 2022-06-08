package talent.jump.Events

import talent.jump.data.LoginResponse

class LoginEvent internal constructor(LoginResponse: LoginResponse){
    private var loginResponse: LoginResponse = LoginResponse

    init {
        this.loginResponse = LoginResponse
    }

    fun GetLoginData(): LoginResponse
    {
        return loginResponse
    }
}

