package talent.jump.Events

import talent.jump.data.RegisterResponse

class RegisterEvent internal constructor(RegisterData: RegisterResponse){
    private var register: RegisterResponse = RegisterData

    init {
        this.register = RegisterData
    }

    fun GetRegisterData(): RegisterResponse
    {
        return register
    }
}

