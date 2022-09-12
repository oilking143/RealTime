package talent.jump.Events

import talent.jump.data.VerifyMailCodeResponse

class GetVerifyMailCodeEvent internal constructor(VerifyCodeResponse: VerifyMailCodeResponse){
    private var verifyMailResponse: VerifyMailCodeResponse = VerifyCodeResponse

    init {
        this.verifyMailResponse = VerifyCodeResponse
    }

    fun GetVerifyMailCodeData(): VerifyMailCodeResponse
    {
        return verifyMailResponse
    }
}

