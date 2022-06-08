package talent.jump.Events

import talent.jump.data.AuthRegistResponse
import talent.jump.data.ForgetPassResponse

class AuthRegisterEvent internal constructor(authRegister: AuthRegistResponse){
    private var authRegister: AuthRegistResponse = authRegister

    init {
        this.authRegister = authRegister
    }

    fun GetAuthRegist(): AuthRegistResponse
    {
        return authRegister
    }
}

