package talent.jump.Events

import talent.jump.data.LoginResponse
import talent.jump.data.ResetResponse

class ResetEvent internal constructor(resetResponse: ResetResponse){
    private var resetResponse: ResetResponse

    init {
        this.resetResponse = resetResponse
    }

    fun GetLoginData(): ResetResponse
    {
        return resetResponse
    }
}

