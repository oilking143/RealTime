package talent.jump.Events

class ChatEvent internal constructor(dataChat: String)  {

    private var dataChat: String = dataChat
    init {
        this.dataChat = dataChat
    }
    internal fun getChat(): String {
        return dataChat
    }


}