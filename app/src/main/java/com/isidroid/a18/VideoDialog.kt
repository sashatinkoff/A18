package com.isidroid.a18

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Measure
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import com.isidroid.a18.extensions.visible
import com.isidroid.utils.extensions.screenHeightPx
import com.isidroid.utils.extensions.screenWidthPx
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.sample_dialog.*
import timber.log.Timber

class VideoDialog : DialogFragment() {
    private val id by lazy { arguments?.getString("id").orEmpty() }
    private val isYoutube by lazy { arguments?.getBoolean("isYoutube") ?: false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.sample_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        lifecycle.addObserver(youtubeview)

        if (isYoutube) createYoutube()
        else createWebview()
    }

    private fun createYoutube() {
        youtubeview.visible(true)
        youtubeview.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(id, 0f)
            }
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun createWebview() {
        webview.apply {
            setBackgroundColor(Color.TRANSPARENT)
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
            }

            webChromeClient = object : WebChromeClient() {}
            webViewClient = object : WebViewClient() {}

            visible(true)
        }

        val style = "<style>\n" +
                ".video-container { \n" +
                "position: relative; \n" +
                "padding-bottom: 56.25%; \n" +
                "padding-top: 35px; \n" +
                "height: 0; \n" +
                "overflow: hidden; \n" +
                "}\n" +
                ".video-container iframe { \n" +
                "position: absolute; \n" +
                "top:0; \n" +
                "left: 0; \n" +
                "width: 100%; \n" +
                "height: 100%; \n" +
                "}\n" +
                "</style>"

        val html = "$style<div class=\"video-container\">" +
                "<iframe " +
                "src=\"https://www.youtube.com/embed/$id?autoplay=1\" allowtransparency=\"true\"" +
                "frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope;\" allowfullscreen></iframe>" +
                "</div>"

        webview.loadData(html, "text/html", "utf8")
    }

    override fun onDestroy() {
        super.onDestroy()
        webview.destroy()
        youtubeview.release()
    }
}