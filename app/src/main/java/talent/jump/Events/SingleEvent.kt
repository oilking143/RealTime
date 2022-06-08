package talent.jump.Events

class SingleEvent internal constructor(dataMsg: String)  {

    private var dataMsg: String = dataMsg
    init {
        this.dataMsg = dataMsg
    }
    internal fun getMsg(): String {
        return dataMsg
    }


}