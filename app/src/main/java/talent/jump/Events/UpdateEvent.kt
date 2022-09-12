package talent.jump.Events

import talent.jump.data.UpdateResponse

class UpdateEvent internal constructor(updateUser: UpdateResponse)  {

    private var updateUser: UpdateResponse = updateUser
    init {
        this.updateUser = updateUser
    }
    internal fun getUpdate(): UpdateResponse {
        return updateUser
    }


}