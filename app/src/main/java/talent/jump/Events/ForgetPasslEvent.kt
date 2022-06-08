package talent.jump.Events

import talent.jump.data.ForgetPassResponse

class ForgetPassEvent internal constructor(forgetPass: ForgetPassResponse){
    private var ForgetPass: ForgetPassResponse = forgetPass

    init {
        this.ForgetPass = forgetPass
    }

    fun GetforgetLetter(): ForgetPassResponse
    {
        return ForgetPass
    }
}

