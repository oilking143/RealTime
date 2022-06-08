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
            api.getViewer(id,data).enqueue(object:retrofit2.Callback<String>{
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
                        EventBus.getDefault().post(GetUserEvent(data))
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


    fun getListStream(page:String,per_page:String,type:String,version:String)
    {
        GlobalScope.launch(Dispatchers.IO) {

            api.getLiveStream(page,per_page,type,version).enqueue(object:retrofit2.Callback<StreamListResponse>{
                override fun onResponse(
                    call: Call<StreamListResponse>,
                    response: Response<StreamListResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: StreamListResponse =response.body()!!
                        EventBus.getDefault().post(GetStreamEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<StreamListResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }

}