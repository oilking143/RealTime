package talent.jump.Events

import talent.jump.data.ListPostCommentResponse

class GetCommentListEvent internal constructor(listCommentResponse: ListPostCommentResponse){
    private var listCommentResponse: ListPostCommentResponse

    init {
        this.listCommentResponse = listCommentResponse
    }

    fun GetCommentList(): ListPostCommentResponse
    {
        return listCommentResponse
    }
}

