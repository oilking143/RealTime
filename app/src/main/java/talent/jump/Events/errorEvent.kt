package talent.jump.Events

class errorEvent internal constructor(errorMsg: String)  {

    private var errorMsg: String = errorMsg
    init {
        this.errorMsg = errorMsg
    }
    internal fun getInedx(): String {
        return errorMsg
    }


}