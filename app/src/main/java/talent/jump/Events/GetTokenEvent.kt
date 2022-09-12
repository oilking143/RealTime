package talent.jump.Events

import talent.jump.data.FansListResponse
import talent.jump.data.TokenResponse
import talent.jump.data.WalletResponse

class GetTokenEvent internal constructor(tokenResponse: TokenResponse){
    private var tokenResponse: TokenResponse

    init {
        this.tokenResponse = tokenResponse
    }

    fun GetToken(): TokenResponse
    {
        return tokenResponse
    }
}

