package com.woong.woong_android.market.bookmark

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.woong.woong_android.R
import com.woong.woong_android.applicationcontroller.ApplicationController
import com.woong.woong_android.home.adapter.MarketBookmarkAdapter
import com.woong.woong_android.home.product.SellerIdx
import com.woong.woong_android.market.get.GetBookmarkResponse
import com.woong.woong_android.market.get.GetBookmarkResponseData
import com.woong.woong_android.network.NetworkService
import com.woong.woong_android.seller_market.SellerMarketActivity
import com.woong.woong_android.woong_marketinfo
import com.woong.woong_android.woong_usertoken
import kotlinx.android.synthetic.main.fragment_bookmark_market.*
import kotlinx.android.synthetic.main.fragment_bookmark_market.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarketBookmark: Fragment(),View.OnClickListener {
    lateinit var getBookmarkAdapter:MarketBookmarkAdapter
    lateinit var bookmarkItems: ArrayList<GetBookmarkResponseData>
    override fun onClick(v: View?) {
        val intent = Intent(context, SellerMarketActivity::class.java)

        var idx : Int = this.rv_bookmark_mymarket.getChildAdapterPosition(v)
        woong_marketinfo.market_id = bookmarkItems[idx].market_id

        startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_bookmark_market,container,false)

        var networkService: NetworkService = ApplicationController.instance.networkService
        var requestManager: RequestManager = Glide.with(this)

        var getBookmark = networkService.getBookmark(woong_usertoken.user_token)
        getBookmark.enqueue(object : Callback<GetBookmarkResponse> {
            override fun onFailure(call: Call<GetBookmarkResponse>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<GetBookmarkResponse>?, response: Response<GetBookmarkResponse>?) {
                if(response!!.isSuccessful){
                    bookmarkItems = response.body().data
                    getBookmarkAdapter = MarketBookmarkAdapter(bookmarkItems, requestManager)
                    getBookmarkAdapter.setOnItemClickListener(this@MarketBookmark)
                    v.rv_bookmark_mymarket.layoutManager = LinearLayoutManager(context)
                    v.rv_bookmark_mymarket.adapter = getBookmarkAdapter
                }
            }
        })
        return v
    }
}