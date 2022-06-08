package talent.jump.Events

import talent.jump.data.VerifyMailReaponse

class GetVerifyMailEvent internal constructor(VerifyResponse: VerifyMailReaponse){
    private var userResponse: VerifyMailReaponse = VerifyResponse

    init {
        this.userResponse = VerifyResponse
    }

    fun GetVerifyMailData(): VerifyMailReaponse
    {
        return userResponse
    }
}

