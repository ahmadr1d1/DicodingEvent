package com.ahmadrd.dicodingevent.ui.utils

import android.os.Build
import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

class HelperTime {
    companion object {
        private const val TAG = "HelperTime"

        fun formatBeginTime(isoTime: String): String {
            return try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val localDateTime = LocalDateTime.parse(isoTime, inputFormatter)
                    val zonedDateTimeJakarta = localDateTime.atZone(ZoneId.of("Asia/Jakarta"))
                    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM, HH:mm", Locale("id", "ID"))
                    "${zonedDateTimeJakarta.format(outputFormatter)} WIB"
                } else {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                    val date = inputFormat.parse(isoTime)

                    if (date != null) {
                        val outputFormat = SimpleDateFormat("dd MMMM, HH:mm", Locale("id", "ID"))
                        outputFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
                        "${outputFormat.format(date)} WIB"
                    } else {
                        Log.e(TAG, "Gagal mem-parsing waktu: $isoTime dengan SimpleDateFormat")
                        isoTime
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Terjadi kesalahan saat memformat waktu: ${e.message}", e)
                isoTime
            }
        }
    }
}