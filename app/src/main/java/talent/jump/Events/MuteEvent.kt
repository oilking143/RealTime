package talent.jump.Events

import talent.jump.data.heartResponse
import talent.jump.data.muteResponse

class MuteEvent internal constructor(muteResponse: muteResponse)  {

    private var muteResponse: muteResponse
    init {
        this.muteResponse = muteResponse
    }
    internal fun getGeartResponse(): muteResponse {
        return muteResponse
    }


}