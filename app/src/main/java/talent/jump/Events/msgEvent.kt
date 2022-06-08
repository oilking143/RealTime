package talent.jump.Events

class msgEvent internal constructor(WsMsg: String)  {

    private var errorMsg: String = WsMsg
    init {
        this.errorMsg = WsMsg
    }
    internal fun getWsMsg(): String {
        return errorMsg
    }


}