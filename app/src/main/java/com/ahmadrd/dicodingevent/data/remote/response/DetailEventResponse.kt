package com.ahmadrd.dicodingevent.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailEventResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("event")
	val event: ListEventsItem? = null
)
