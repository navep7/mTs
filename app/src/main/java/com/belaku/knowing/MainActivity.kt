package com.belaku.knowing

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.belaku.knowing.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var pDialog: ProgressDialog
    private var isDarkModeOn: Boolean = false
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tweetsRvAdapter: TweetsRvAdapter
    private lateinit var rvTweets: RecyclerView
//    private lateinit var arrayAdapter: ArrayAdapter<String>
  //  private lateinit var mListView: ListView



    private lateinit var imageviewDp: ImageView
    private lateinit var imageviewTrail: ImageView
    private var tweets: ArrayList<String> = ArrayList()
    private lateinit var response: Response
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var editTextUname: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
     //   setSupportActionBar(binding.toolbar)

        findViewByIds()
        initOps()

        appContext = applicationContext


        if (editTextUname.text.length > 0)
        getTweets(editTextUname.text.toString())
        else {
            var savedUser: String = sharedPreferences.getString("uname", "").toString();
            if (savedUser != "")
                getTweets(savedUser)
            else {
                makeToast("Enter Twitter Handle")
            }
        }

        editTextUname.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                getTweets(editTextUname.text.toString())
                handled = true
            }
            handled
        })


    }

    private fun initOps() {

        sharedPreferences = getSharedPreferences(
            "sharedPrefs",
            MODE_PRIVATE
        )
        sharedPreferencesEditor = sharedPreferences.edit()
        isDarkModeOn = sharedPreferences.getBoolean(
            "isDarkModeOn",
            false
        )

        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);
         //   btnToggleDark.setText("Disable Dark Mode");
        }
        else {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
         //   btnToggleDark.setText("Enable Dark Mode");
        }

        pDialog = ProgressDialog(this@MainActivity)
        pDialog.setCancelable(false)
    }

    private fun getTweets(str: String) {

        pDialog.setMessage("Loading...");
        pDialog.show();

            getProfile(str)

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://twitter154.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterfaceUserTweets::class.java)

        val retrofitData = retrofitBuilder.getTweets(str)
        
        retrofitData.enqueue(object : Callback<TweetsData?> {

            override fun onResponse(
                call: Call<TweetsData?>,
                response: retrofit2.Response<TweetsData?>
            ) {

                makeToast("Rrrr - " + response.body())

                val nextToken = response.body()?.continuation_token
                val tweetsList = response.body()?.results?.toList()

                if (tweetsList != null) {

                    tweets.clear()

                    for (i in tweetsList.indices)
                        tweets.add(tweetsList[i].text)

                    tweetsRvAdapter.notifyDataSetChanged()

                //    makeToast(tweetsList.size.toString() + " : Tlist")
                //    makeToast("nextToken - " + nextToken)
                    val ran = Random
                    val x = ran.nextInt(6) + 5
           //         makeToast(x.toString() + " : " + tweets.get(x).toString())



                    rvTweets.getChildAt(x)?.setBackgroundColor(
                        Color.parseColor("#00743D"));

                } else makeToast("Some problem, maybe!")

                dismissProgressDialog();

            }

            override fun onFailure(call: Call<TweetsData?>, t: Throwable) {
                makeToast("Failed - " + t.message)

                dismissProgressDialog();
            }

        })
    }

    private fun dismissProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss()
        }
    }

    private fun getProfile(str: String) {

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://twitter154.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterfaceUserDetails::class.java)

        val retrofitData = retrofitBuilder.getDetails(str)

        retrofitData.enqueue(object : Callback<ProfileData?> {

            override fun onResponse(
                call: Call<ProfileData?>,
                response: retrofit2.Response<ProfileData?>
            ) {

                var dpUrl = response.body()?.profile_pic_url
                Picasso.get().load(dpUrl).into(imageviewDp)
                var tpUrl = response.body()?.profile_banner_url
                Picasso.get().load(tpUrl).into(imageviewTrail)
                Log.d("DPurl ", dpUrl.toString())

            }

            override fun onFailure(call: Call<ProfileData?>, t: Throwable) {
                makeToast("Failed - " + t.message)
            }

        })
    }


    private fun findViewByIds() {
        imageviewDp = findViewById(R.id.imgv_dp)
        imageviewTrail = findViewById(R.id.imgv_pp)

        rvTweets = findViewById(R.id.rv_tweets)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        rvTweets.layoutManager = linearLayoutManager
        tweetsRvAdapter = TweetsRvAdapter(applicationContext, tweets)
        rvTweets.adapter = tweetsRvAdapter

        editTextUname = findViewById(R.id.edtx_uname)
    }

    override fun onDestroy() {
        if (editTextUname.text.length > 0) {
            sharedPreferencesEditor.putString("uname", editTextUname.text.toString())
            sharedPreferencesEditor.apply()
        }
        dismissProgressDialog()
        super.onDestroy()
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

    fun postTweet(view: View) {
        val tweetUrl = ("https://twitter.com/intent/tweet?text=")
        val uri = Uri.parse(tweetUrl)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    companion object {
        private lateinit var appContext: Context

        fun makeToast(str: String) {
                Log.d("Toasting", str)
                Toast.makeText(appContext, str, Toast.LENGTH_LONG).show()
        }
    }

    fun DarkModeSwitch(view: View) {

        if (isDarkModeOn) {
            AppCompatDelegate
                .setDefaultNightMode(
                    AppCompatDelegate
                        .MODE_NIGHT_NO
                );

            sharedPreferencesEditor.putBoolean("isDarkModeOn", false);
            sharedPreferencesEditor.apply();
        } else {
            AppCompatDelegate
                .setDefaultNightMode(
                    AppCompatDelegate
                        .MODE_NIGHT_YES
                );

            sharedPreferencesEditor.putBoolean("isDarkModeOn", true);
            sharedPreferencesEditor.apply();
        }
    }


}