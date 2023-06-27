package com.example.mobileprotecterapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import com.example.mobileprotecterapp.R
import com.example.mobileprotecterapp.ui.login.LoginActivity

class AppUtil {

    interface UserConformationDialogListener {
        fun onPositiveBtnClick()
        fun onNegativeBtnClick()
    }

    companion object {

        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS

        fun hideKeyboard(view: View, context: Context) {
            view.apply {
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        fun hideKeyboard(activity: Activity) {
            val imm =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun showKeyboard(
            v: SearchView,
            activity: Activity
        ) {
            val imm =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(v, 0)
        }

        fun displayInfoDialog(context: Context, message: String) {

            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setMessage(message)
                .setCancelable(false)
                .setTitle("Message")
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = dialogBuilder.create()
            alert.show()
        }

        fun displayInfoDialog(
            context: Context,
            message: String,
            userConformationDialogListener: UserConformationDialogListener,
            title: String = "Message"
        ) {

            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(R.string.ok) { _, _ ->
                    userConformationDialogListener.onPositiveBtnClick()
                }
            val alert = dialogBuilder.create()
            alert.show()
        }

        fun onUnauthenticated(libfile: LibFile, context: Context) {
            libfile.clearLogoutData()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }


        fun getTimeAgo(time: Long): String? {
            var time = time
            if (time < 1000000000000L) { // if timestamp given in seconds, convert to millis
                time *= 1000
            }
            val now: Long = System.currentTimeMillis()
            if (time > now || time <= 0) {
                return null
            }
            // TODO: localize
            val diff = now - time
            return if (diff < MINUTE_MILLIS) {
                "just now"
            } else if (diff < 2 * MINUTE_MILLIS) {
//                "a minute ago"
                "1 minute"
            } else if (diff < 50 * MINUTE_MILLIS) {
                (diff / MINUTE_MILLIS).toString() + " minutes ago"
//                (diff / MINUTE_MILLIS).toString() + "minutes ago"
            } else if (diff < 90 * MINUTE_MILLIS) {
//                "an hour ago"
                "1 hour"
            } else if (diff < 24 * HOUR_MILLIS) {
                (diff / HOUR_MILLIS).toString() + " hours ago"
//                (diff / HOUR_MILLIS).toString() + "h"
            } else if (diff < 48 * HOUR_MILLIS) {
                "yesterday"
            } else {
//                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(time))
                (diff / DAY_MILLIS).toString() + " days ago"
            }
        }

        fun checkDigit(number: Long): String? {
            return if (number <= 9) "0$number" else number.toString()
        }

        fun rupeeFormat(value: String): String? {
            var value = value
            value = value.replace(",", "")
            val lastDigit = value[value.length - 1]
            var result = ""
            val len = value.length - 1
            var nDigits = 0
            for (i in len - 1 downTo 0) {
                result = value[i].toString() + result
                nDigits++
                if (nDigits % 2 == 0 && i > 0) {
                    result = ",$result"
                }
            }
            return result + lastDigit
        }
        fun rupeeFormatDecimal(value: String): String? {

            var decimal = ""
            if (value.contains(".")) {
                decimal = value.substring(value.indexOf("."))
            }
            var value = value.replace(decimal, "")
            value = value.replace(",", "")
            val lastDigit = value[value.length - 1]
            var result = ""
            val len = value.length - 1
            var nDigits = 0
            for (i in len - 1 downTo 0) {
                result = value[i].toString() + result
                nDigits++
                if (nDigits % 2 == 0 && i > 0) {
                    result = ",$result"
                }
            }
            return result + lastDigit + decimal
        }
    }
}