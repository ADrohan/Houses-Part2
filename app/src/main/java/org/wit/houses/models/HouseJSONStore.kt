package org.wit.houses.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.houses.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "houses.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<HouseModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class HouseJSONStore(private val context: Context) : HouseStore {

    var houses = mutableListOf<HouseModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<HouseModel> {
        logAll()
        return houses
    }

    override fun findById(id:Long) : HouseModel? {
        val foundHouse: HouseModel? = houses.find { it.id == id }
        return foundHouse
    }

    override fun create(house: HouseModel) {
        house.id = generateRandomId()
        houses.add(house)
        serialize()
    }
    override fun delete(house: HouseModel) {
        houses.remove(house)
        serialize()
    }

    override fun update(house: HouseModel) {
        var foundHouse: HouseModel? = houses.find { p -> p.id == house.id}
        if (foundHouse != null) {
            foundHouse.address = house.address
            foundHouse.listPrice = house.listPrice
            foundHouse.bedrooms = house.bedrooms
            foundHouse.bathrooms = house.bathrooms
            foundHouse.description = house.description
            foundHouse.soldPrice = house.soldPrice
            foundHouse.auctioneer= house.auctioneer
            foundHouse.image = house.image
            foundHouse.lat = house.lat
            foundHouse.lng = house.lng
            foundHouse.zoom = house.zoom
         //   serialize()
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(houses, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
       houses = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        houses.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
