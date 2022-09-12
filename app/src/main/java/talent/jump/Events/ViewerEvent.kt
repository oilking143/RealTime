package talent.jump.Events

import talent.jump.data.ViewersResponse

class ViewerEvent internal constructor(viewersResponse: ViewersResponse)  {

    private var viewersResponse: ViewersResponse =viewersResponse
    init {
        this.viewersResponse = viewersResponse
    }
    internal fun getViewerResponse(): ViewersResponse {
        return viewersResponse
    }


}