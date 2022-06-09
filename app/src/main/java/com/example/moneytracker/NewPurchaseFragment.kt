package com.example.moneytracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.moneytracker.databinding.NewPurchaseBinding
import com.example.moneytracker.model.plain_object.purchase.PurchaseCreateRequestPlainObject
import com.example.moneytracker.services.api.PurchaseApi
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class NewPurchaseFragment : Fragment() {
    private var _binding: NewPurchaseBinding? = null
    private val binding get() = _binding!!
    lateinit var purchaseApi: PurchaseApi
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureRetrofit()
        _binding = NewPurchaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.createPurchase.setOnClickListener {
            createPurchase()
        }
    }

    private fun createPurchase(){
        val title = binding.purchaseTitle.text
        val amount = binding.purchaseAmount.text
        if(title.isNotEmpty() && amount.isNotEmpty()){
            getAuthToken()?.let { it ->
                purchaseApi.createPurchase(purchase = PurchaseCreateRequestPlainObject(title = title.toString(), amount = amount.toString().toInt()), token = it).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Log.e("BlogApplication", it.toString())
                            findNavController().navigate(R.id.action_newPurchase_to_Purchases)
                        },
                        {
                            Log.e("BlogApplication", it.toString())
                            it.message?.let { it1 -> Log.e("BlogApplication", it1) }
                        }
                    )
            }?.let {
                compositeDisposable.add(
                    it
                )
            }
        } else{
            Snackbar.make(binding.root, "Введите сумму и название", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun getAuthToken(): String? {
        return this.activity?.getSharedPreferences("BlogApplication", Context.MODE_PRIVATE)?.getString("token", "")
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

        purchaseApi = retrofit.create(PurchaseApi::class.java)
    }
}
