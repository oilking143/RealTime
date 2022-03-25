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
import talent.jump.Events.LoginEvent
import talent.jump.Events.RegisterEvent
import talent.jump.Events.errorEvent
import talent.jump.data.LoginResponse
import talent.jump.data.RegisterResponse
import java.util.concurrent.TimeUnit

class ApiClient {

    var api= Retrofit.Builder().baseUrl("https://dev-api.asiaplay777.com")
        .client(getOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(ApiRequest::class.java)

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


        val httpClientBuilder = OkHttpClient.Builder()
        //OkHttp进行添加拦截器loggingInterceptor
        httpClientBuilder.addInterceptor(loggingInterceptor).connectTimeout(10, TimeUnit.SECONDS)

        return httpClientBuilder.build()
    }

    fun Login(data: JsonObject)
    {
        GlobalScope.launch(Dispatchers.IO) {


            api.Login(data).enqueue(object:retrofit2.Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: LoginResponse =response.body()!!
                        EventBus.getDefault().post(LoginEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
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
}