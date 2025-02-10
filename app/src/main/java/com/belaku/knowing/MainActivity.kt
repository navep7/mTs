package com.belaku.knowing

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.belaku.knowing.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private var tweets: ArrayList<String> = ArrayList()
    private lateinit var response: Response
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var editTextUname: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        findViewByIds()

        editTextUname.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                getTweets(editTextUname.text.toString())
                handled = true
            }
            handled
        })


    }

    private fun getTweets(str: String) {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://twitter154.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getTweets(str)
        
        retrofitData.enqueue(object : Callback<TweetsData?> {

            override fun onResponse(
                call: Call<TweetsData?>,
                response: retrofit2.Response<TweetsData?>
            ) {
                val nextToken = response.body()?.continuation_token
                val tweetsList = response.body()?.results?.toList()

                if (tweetsList != null) {

                    for (i in tweetsList.indices)
                        tweets.add(tweetsList[i].text)

                //    makeToast(tweetsList.size.toString() + " : Tlist")
                //    makeToast("nextToken - " + nextToken)
                    val ran = Random
                    val x = ran.nextInt(6) + 5
                    makeToast(x.toString() + " : " + tweets.get(x).toString())

                    var mListView = findViewById <ListView>(R.id.tweets_listview)
                    var arrayAdapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1, tweets)
                    mListView.adapter = arrayAdapter

                    mListView.getChildAt(x)?.setBackgroundColor(
                        Color.parseColor("#00743D"));

                }
            }

            override fun onFailure(call: Call<TweetsData?>, t: Throwable) {
                makeToast("Failed - " + t.message)
            }

        })
    }

    private fun makeToast(str: String) {
        Log.d("Toasting", str)
        Toast.makeText(applicationContext, str, Toast.LENGTH_LONG).show()

    }

    private fun findViewByIds() {

        editTextUname = findViewById(R.id.edtx_uname)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getTweets(view: View) {
        getTweets(editTextUname.text.toString())
    }


}