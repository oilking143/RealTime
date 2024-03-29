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
import talent.jump.data.*
import java.util.concurrent.TimeUnit

class ApiClient {

    var api= Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
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



    fun verifyMailCode(data: JsonObject)
    {
        GlobalScope.launch(Dispatchers.IO) {


            api.verifyMailCode(data).enqueue(object:retrofit2.Callback<VerifyMailCodeResponse>{
                override fun onResponse(
                    call: Call<VerifyMailCodeResponse>,
                    response: Response<VerifyMailCodeResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: VerifyMailCodeResponse =response.body()!!
                        EventBus.getDefault().post(GetVerifyMailCodeEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.code().toString()))
                    }
                }

                override fun onFailure(call: Call<VerifyMailCodeResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }

    fun verifyMail(data: JsonObject)
    {
        GlobalScope.launch(Dispatchers.IO) {


            api.verifyMail(data).enqueue(object:retrofit2.Callback<VerifyMailReaponse>{
                override fun onResponse(
                    call: Call<VerifyMailReaponse>,
                    response: Response<VerifyMailReaponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: VerifyMailReaponse =response.body()!!
                        EventBus.getDefault().post(GetVerifyMailEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.code().toString()))
                    }
                }

                override fun onFailure(call: Call<VerifyMailReaponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }

    fun frogetPassword(data: JsonObject)
    {
        GlobalScope.launch(Dispatchers.IO) {


            api.frogetPassword(data).enqueue(object:retrofit2.Callback<ForgetPassResponse>{
                override fun onResponse(
                    call: Call<ForgetPassResponse>,
                    response: Response<ForgetPassResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data: ForgetPassResponse =response.body()!!
                        EventBus.getDefault().post(ForgetPassEvent(data))
                    }
                    else{
                        EventBus.getDefault().post(errorEvent(response.message().toString()))
                    }
                }

                override fun onFailure(call: Call<ForgetPassResponse>, t: Throwable) {
                    EventBus.getDefault().post(errorEvent("连线失败请稍后再试"))
                }

            })


        }
    }







    fun updatePassword(data: JsonObject)
    {
        GlobalScope.launch(Dispatchers.IO) {


            api.updatePassword(data).enqueue(object:retrofit2.Callback<LoginResponse>{
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

}