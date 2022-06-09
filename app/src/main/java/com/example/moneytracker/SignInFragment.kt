package com.example.moneytracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.moneytracker.databinding.SignInFragmentBinding
import com.example.moneytracker.model.plain_object.user.UserSignInPlainObject
import com.example.moneytracker.services.api.UserApi
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SignInFragment : Fragment() {
    private var _binding: SignInFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var userApi: UserApi
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureRetrofit()
        _binding = SignInFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_SignInFragment_to_SignUpFragment)
        }

        binding.signIn.setOnClickListener {
            compositeDisposable.add(
                userApi.signIn(
                    UserSignInPlainObject(email = binding.signInEmail.text.toString(), password = binding.signInPassword.text.toString())
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Log.e("SignInFragment", it.toString())
                            saveTokenToShares(it.token)
                            findNavController().navigate(R.id.action_SignInFragment_to_Purchases)
                        },
                        {
                            Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                            it.message?.let { it1 -> Log.e("SignInFragment", it1) }
                        }
                    )
            )
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
