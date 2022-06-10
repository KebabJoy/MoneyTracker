package com.example.moneytracker

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.moneytracker.databinding.SignUpFragmentBinding
import com.example.moneytracker.model.plain_object.user.UserCreateRequestPlainObject
import com.example.moneytracker.services.api.UserApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class SignUpFragment : Fragment() {

    private var _binding: SignUpFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var userApi: UserApi
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        configureRetrofit()
        _binding = SignUpFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun userParams(): UserCreateRequestPlainObject {
        return UserCreateRequestPlainObject(
            email = binding.authName.text.toString(),
            password = binding.authPassword.text.toString(),
            passwordConfirmation = binding.authPasswordConfirmation.text.toString()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUp.setOnClickListener {
            compositeDisposable.add(userApi.signUp(user = userParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.e("BlogApplication", it.toString())
                        saveTokenToShares(it.token)
                        findNavController().navigate(R.id.action_SignUpFragment_to_Purchases)
                    },
                    {
                        it.message?.let { it1 -> Log.e("BlogApplication", it1) }
                    }
                )
            )
        }

        binding.signIn.setOnClickListener{
            findNavController().navigate(R.id.action_SignUpFragment_to_SignInFragment)
        }
    }

    private fun saveTokenToShares(token: String) {
        val sharedPrefs = this.activity?.getSharedPreferences("BlogApplication", Context.MODE_PRIVATE)
        sharedPrefs?.let { it.edit().putString("token", token).apply() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configureRetrofit() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://shrouded-beach-77254.herokuapp.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        userApi = retrofit.create(UserApi::class.java)
    }
}