package talent.jump.Events

import talent.jump.data.PostListResponse

class GetPostListEvent internal constructor(postListResponse: PostListResponse){
    private var postListResponse: PostListResponse

    init {
        this.postListResponse = postListResponse
    }

    fun GetPostList(): PostListResponse
    {
        return postListResponse
    }
}

