package talent.jump.Events

import talent.jump.data.CreatePostResponse
import talent.jump.data.DeleteResponse

class DeletePostEvent internal constructor(deletepost: DeleteResponse){
    private var deletepost: DeleteResponse

    init {
        this.deletepost = deletepost
    }

    fun GetDelete(): DeleteResponse
    {
        return deletepost
    }
}

