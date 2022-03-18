package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var tvCount: TextView

    lateinit var client:TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        tvCount = findViewById(R.id.tvCharCount)

        client = TwitterApplication.getRestClient(this)

        // Handling the user's click on the Tweet button
        btnTweet.setOnClickListener{

            // Grab the content of editText(etCompose)
            val tweetContent = etCompose.text.toString()

            // 1. Make sure the tweet isn't empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty tweets not allowed!", Toast.LENGTH_SHORT).show()
            }

            // 2. Make sure the tweet is under character count
            else if (tweetContent.length > 140) {
                Toast.makeText(this, "Tweet is too long! Limit is 140 characters.", Toast.LENGTH_SHORT).show()
            } else {
                client.publishTweet(tweetContent, object: JsonHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        Log.i(TAG, "Published tweet successfully!")

                        val tweet = Tweet.fromJson(json.jsonObject)
                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed to publish Tweet", throwable)
                    }
                })
            }
        }


        etCompose.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)
                // Count characters
                var charCount = etCompose.length()

                // Convert to String
                var charCountString = charCount.toString()

                // Set to text view
                tvCount.setText(charCountString + "/280")
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Fires before text changes
            }

            override fun afterTextChanged(s: Editable) {
                // Fires right after the text has changed
                if (etCompose.length() > 280) {
                    tvCount.setTextColor(Color.parseColor("#FF0000"))
//                    btnTweet.setEnabled(false)
                } else {
                    tvCount.setTextColor(Color.parseColor("#000000"))
//                    btnTweet.setEnabled(true)
                }
            }
        })
    }

    companion object {
        val TAG = "ComposeActivity"
    }
}