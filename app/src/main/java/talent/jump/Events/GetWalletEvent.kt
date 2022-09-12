package talent.jump.Events

import talent.jump.data.FansListResponse
import talent.jump.data.WalletResponse

class GetWalletEvent internal constructor(walletResponse: WalletResponse){
    private var walletResponse: WalletResponse

    init {
        this.walletResponse = walletResponse
    }

    fun GetWallet(): WalletResponse
    {
        return walletResponse
    }
}

