package com.codefuelindia.wecarefarm.Utils

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.codefuelindia.wecarefarm.R

class UIUtils {


    companion object {

        fun showLoader(context: Context,layoutInflater: LayoutInflater): AlertDialog {

            val dialogBuilder = AlertDialog.Builder(context)


            val dialogView = layoutInflater.inflate(R.layout.alert_label_editor, null)
            dialogBuilder.setView(dialogView)

            GlideApp.with(context)
                    .load(context?.getDrawable(R.drawable.loader)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(dialogView.findViewById<ImageView>(R.id.ivGif))
            dialogBuilder.setCancelable(false)
            val alertDialog = dialogBuilder.create()
            alertDialog.window.setBackgroundDrawable(context?.getDrawable(android.R.color.transparent))


            return alertDialog


        }

    }



}