package talent.jump.Events

import talent.jump.data.heartResponse

class HeartEvent internal constructor(heartResponse: heartResponse)  {

    private var heartResponse: heartResponse =heartResponse
    init {
        this.heartResponse = heartResponse
    }
    internal fun getGeartResponse(): heartResponse {
        return heartResponse
    }


}