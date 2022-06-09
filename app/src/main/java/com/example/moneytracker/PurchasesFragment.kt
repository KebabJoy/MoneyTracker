package com.example.moneytracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneytracker.databinding.PurchasesBinding
import com.example.moneytracker.model.plain_object.purchase.PurchasePlainObject
import com.example.moneytracker.model.plain_object.purchase.PurchaseResponsePlainObject
import com.example.moneytracker.model.plain_object.user.UserSignInPlainObject
import com.example.moneytracker.services.adapters.PurchasesAdapter
import com.example.moneytracker.services.api.PurchaseApi
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


class PurchasesFragment : Fragment() {
    private var _binding: PurchasesBinding? = null
    private val binding get() = _binding!!
    lateinit var purchaseApi: PurchaseApi
    private val compositeDisposable = CompositeDisposable()
    private val list = mutableListOf<PurchasePlainObject>()
    private val adapter = PurchasesAdapter(list)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureRetrofit()
        _binding = PurchasesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.purchasesView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        list.clear()

        getAuthToken()?.let { it ->
            purchaseApi.fetchPurchases(token = it).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.e("BlogApplication", it.toString())
                        setPurchases(it.purchases)
                        binding.itemsCount.text = "Сумма трат: " + it.purchases.size.toString()
                        binding.itemsSum.text = "Количество трат: " + it.amountSum.toString()
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

        binding.createPurchase.setOnClickListener {
            findNavController().navigate(R.id.action_Purchase_to_newPurchase)
        }
    }

    private fun setPurchases(purchases: MutableList<PurchasePlainObject>) {
        purchases.forEach {
            list.add(it)
            adapter.notifyItemRangeInserted(list.size - 1, 1)
        }
        Log.e("comments:", list.toString())
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
