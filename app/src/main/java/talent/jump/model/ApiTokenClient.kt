package talent.jump.model

import android.util.Log
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import talent.jump.BuildConfig
import talent.jump.Events.*
import talent.jump.GlobalData
import talent.jump.GlobalData.Companion.Cookies
import talent.jump.data.*
import java.util.*
import java.util.concurrent.TimeUnit

class ApiTokenClient {

    var retrofit= Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
        .client(getOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()


    var api: ApiRequest =retrofit.create(ApiRequest::class.java)


    private fun getOkHttpClient(): OkHttpClient? {
        //日志显示级别
        val level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY
        //新建log拦截器
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("Response", "OkHttp====Message:$message")
            }
        })
        loggingInterceptor.setLevel(level)
        //定制OkHttp

        val cookie = object : CookieJar {

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {

                if(cookies!=null)
                {
                    for (element in cookies)
                    {
                        Cookies.add(element)
                    }

                }

            }
            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                return Cookies
            }
        }

        val httpClientBuilder = OkHttpClient.Builder()
        //OkHttp进行添加拦截器loggingInterceptor
        httpClientBuilder.addInterceptor(Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", GlobalData.loginToken.access_token)
                .build()
            chain.proceed(newRequest)
        }).addInterceptor(Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            Log.d("responseCode",response.code.toString())

            response
        }).addInterceptor(loggingInterceptor).cookieJar(cookie) .connectTimeout(15, TimeUnit.SECONDS)
        return httpClientBuilder.protocols(Collections.singletonList(Protocol.HTTP_1_1)).build()
    }


    fun Register(data: JsonObject)
    {
        GlobalScope.launch(Dispatchers.IO) {


            api.MembershipRegister(data).enqueue(object:retrofit2.Callback<RegisterResponse>{
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: RegisterResponse =response.body()!!
                        EventBus.getDefault().post(RegisterEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }

    fun createStreams(data: JsonObject )
    {
        GlobalScope.launch(Dispatchers.IO) {
            api.createStreams(data).enqueue(object:retrofit2.Callback<CreateStreamResponse>{
                override fun onResponse(
                    call: Call<CreateStreamResponse>,
                    response: Response<CreateStreamResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data =response.body()
                        EventBus.getDefault().post(CreateStreamEvent(data!!))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<CreateStreamResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })
        }
    }


    fun upLoadPhoto(data: JsonObject )
    {
        GlobalScope.launch(Dispatchers.IO) {
            api.upLoadPhoto(data).enqueue(object:retrofit2.Callback<String>{
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    if(response.isSuccessful)
                    {
                        val data =response.body()
                        EventBus.getDefault().post(PhotoEvent(data!!))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })
        }
    }

    fun getSocketViewer(id:String,data: JsonObject )
    {
        GlobalScope.launch(Dispatchers.IO) {
            api.getViewer(id,data).enqueue(object:retrofit2.Callback<ViewersResponse>{
                override fun onResponse(
                    call: Call<ViewersResponse>,
                    response: Response<ViewersResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data =response.body()
                        EventBus.getDefault().post(ViewerEvent(data!!))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<ViewersResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })
        }
    }

    fun giveHearts(id:String,data: JsonObject )
    {
        GlobalScope.launch(Dispatchers.IO) {
            api.giveheart(id,data).enqueue(object:retrofit2.Callback<heartResponse>{
                override fun onResponse(
                    call: Call<heartResponse>,
                    response: Response<heartResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data =response.body()
                        EventBus.getDefault().post(HeartEvent(data!!))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<heartResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })
        }
    }

    fun muteuser(id:String,data: JsonObject )
    {
        GlobalScope.launch(Dispatchers.IO) {
            api.muteUser(id,data).enqueue(object:retrofit2.Callback<muteResponse>{
                override fun onResponse(
                    call: Call<muteResponse>,
                    response: Response<muteResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data =response.body()
                        EventBus.getDefault().post(MuteEvent(data!!))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<muteResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })
        }
    }

    fun CreateMsg(id:String,data: JsonObject )
    {
        GlobalScope.launch(Dispatchers.IO) {
            api.createmessages(id,data).enqueue(object:retrofit2.Callback<VerifyMailReaponse>{
                override fun onResponse(
                    call: Call<VerifyMailReaponse>,
                    response: Response<VerifyMailReaponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data =response.body()
                        EventBus.getDefault().post(GetVerifyMailEvent(data!!))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<VerifyMailReaponse>, t: Throwable) {
                    Log.d("msg",t.message.toString())
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })
        }
    }

    fun VerifyPersonalMailSend()
    {
        GlobalScope.launch(Dispatchers.IO) {
            api.verifyPersonalMailSend().enqueue(object:retrofit2.Callback<String>{
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    if(response.isSuccessful)
                    {
                        val data =response.body()
                        EventBus.getDefault().post(SingleEvent(data!!))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })
        }
    }

    fun postMedia(files : MultipartBody.Part)
    {
        GlobalScope.launch(Dispatchers.IO) {


            api.PostMedias(files).enqueue(object:retrofit2.Callback<PostmediaResponse>{
                override fun onResponse(
                    call: Call<PostmediaResponse>,
                    response: Response<PostmediaResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: PostmediaResponse =response.body()!!
                        EventBus.getDefault().post(PostMediaEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<PostmediaResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent(call.request().toString()))
                }

            })


        }
    }

    fun createPost(data: JsonObject)
    {
        GlobalScope.launch(Dispatchers.IO) {


            api.createPost(data).enqueue(object:retrofit2.Callback<CreatePostResponse>{
                override fun onResponse(
                    call: Call<CreatePostResponse>,
                    response: Response<CreatePostResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: CreatePostResponse =response.body()!!
                        EventBus.getDefault().post(CreatePostEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<CreatePostResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent(call.request().toString()))
                }

            })


        }
    }

    fun UpdatePassword(data: JsonObject)
    {
        GlobalScope.launch(Dispatchers.IO) {


            api.updateOldPassword(data).enqueue(object:retrofit2.Callback<UpdateResponse>{
                override fun onResponse(
                    call: Call<UpdateResponse>,
                    response: Response<UpdateResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: UpdateResponse =response.body()!!
                        EventBus.getDefault().post(UpdateEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }

    fun UpdateUser(data: JsonObject)
    {
        GlobalScope.launch(Dispatchers.IO) {


            api.updateUser(data).enqueue(object:retrofit2.Callback<UpdateResponse>{
                override fun onResponse(
                    call: Call<UpdateResponse>,
                    response: Response<UpdateResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: UpdateResponse =response.body()!!
                        EventBus.getDefault().post(UpdateEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }

    fun DeleteComment(id: String)
    {
        GlobalScope.launch(Dispatchers.IO) {


            api.deleteComments(id).enqueue(object:retrofit2.Callback<DeleteResponse>{
                override fun onResponse(
                    call: Call<DeleteResponse>,
                    response: Response<DeleteResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: DeleteResponse =response.body()!!
                        EventBus.getDefault().post(DeletePostEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }

    fun CreateComment(data: JsonObject)
    {
        GlobalScope.launch(Dispatchers.IO) {


            api.createComments(data).enqueue(object:retrofit2.Callback<CommentResponse>{
                override fun onResponse(
                    call: Call<CommentResponse>,
                    response: Response<CommentResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: CommentResponse =response.body()!!
                        EventBus.getDefault().post(CreateCommentEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }

    fun ResrtPassword(data: JsonObject)
    {
        GlobalScope.launch(Dispatchers.IO) {


            api.resetPassword(data).enqueue(object:retrofit2.Callback<ResetResponse>{
                override fun onResponse(
                    call: Call<ResetResponse>,
                    response: Response<ResetResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: ResetResponse =response.body()!!
                        EventBus.getDefault().post(ResetEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<ResetResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }

    fun StreamAuth()
    {
        GlobalScope.launch(Dispatchers.IO) {
            api.streamAuth().enqueue(object:retrofit2.Callback<AuthRegistResponse>{
                override fun onResponse(
                    call: Call<AuthRegistResponse>,
                    response: Response<AuthRegistResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: AuthRegistResponse =response.body()!!
                        EventBus.getDefault().post(AuthRegisterEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<AuthRegistResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }


    fun getMe()
    {
        GlobalScope.launch(Dispatchers.IO) {

            api.getMe().enqueue(object:retrofit2.Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: UserResponse =response.body()!!
                        EventBus.getDefault().post(GetMeEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }

    fun getFans(page:String,per_page:String)
    {
        GlobalScope.launch(Dispatchers.IO) {

            api.getFans(page,per_page).enqueue(object:retrofit2.Callback<FansListResponse>{
                override fun onResponse(
                    call: Call<FansListResponse>,
                    response: Response<FansListResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: FansListResponse =response.body()!!
                        EventBus.getDefault().post(GetFansEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<FansListResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }

    fun getListPostComments(id:String,before_id:String,after_id:String,number:String)
    {
        GlobalScope.launch(Dispatchers.IO) {

            api.getListPosts(id,before_id,after_id,number).enqueue(object:retrofit2.Callback<ListPostCommentResponse>{
                override fun onResponse(
                    call: Call<ListPostCommentResponse>,
                    response: Response<ListPostCommentResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: ListPostCommentResponse =response.body()!!
                        EventBus.getDefault().post(GetCommentListEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<ListPostCommentResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })
        }
    }

    fun getFollows(page:String,per_page:String)
    {
        GlobalScope.launch(Dispatchers.IO) {

            api.getFollows(page,per_page).enqueue(object:retrofit2.Callback<FollowersResponse>{
                override fun onResponse(
                    call: Call<FollowersResponse>,
                    response: Response<FollowersResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: FollowersResponse =response.body()!!
                        EventBus.getDefault().post(GetFollowerEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<FollowersResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }


    fun getWallet()
    {
        GlobalScope.launch(Dispatchers.IO) {

            api.getwallet().enqueue(object:retrofit2.Callback<WalletResponse>{
                override fun onResponse(
                    call: Call<WalletResponse>,
                    response: Response<WalletResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: WalletResponse =response.body()!!
                        EventBus.getDefault().post(GetWalletEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<WalletResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }


    fun getTokens()
    {
        GlobalScope.launch(Dispatchers.IO) {

            api.getTokens().enqueue(object:retrofit2.Callback<TokenResponse>{
                override fun onResponse(
                    call: Call<TokenResponse>,
                    response: Response<TokenResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: TokenResponse =response.body()!!
                        EventBus.getDefault().post(GetTokenEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }


    fun getListStream(page:String,per_page:String,type:String,version:String)
    {
        GlobalScope.launch(Dispatchers.IO) {

            api.getLiveStream(page,per_page,type,version).enqueue(object:retrofit2.Callback<LiveStreamListResponse>{
                override fun onResponse(
                    call: Call<LiveStreamListResponse>,
                    response: Response<LiveStreamListResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: LiveStreamListResponse =response.body()!!
                        EventBus.getDefault().post(GetStreamEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<LiveStreamListResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }

    fun getPostList(user_id:String,number:String,before_id:String)
    {
        GlobalScope.launch(Dispatchers.IO) {

            api.getPosts(user_id,number,before_id).enqueue(object:retrofit2.Callback<PostListResponse>{
                override fun onResponse(
                    call: Call<PostListResponse>,
                    response: Response<PostListResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: PostListResponse =response.body()!!
                        EventBus.getDefault().post(GetPostListEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<PostListResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }


    fun getSearch(query:String)
    {
        GlobalScope.launch(Dispatchers.IO) {

            api.getSearch(query).enqueue(object:retrofit2.Callback<SearchResponse>{
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: SearchResponse =response.body()!!
                        EventBus.getDefault().post(GetSearchEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }

}