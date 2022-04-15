package org.wit.geosite.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.geosite.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "geosites.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<GeositeModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class GeositeJSONStore(private val context: Context) : GeositeStore {

    var geosites = mutableListOf<GeositeModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override suspend fun findAll(): MutableList<GeositeModel> {
        logAll()
        return geosites
    }

    override suspend fun create(geosite: GeositeModel) {
        geosite.id = generateRandomId()
        geosites.add(geosite)
        serialize()
    }


    override suspend fun update(geosite: GeositeModel) {
        val geositesList = findAll() as ArrayList<GeositeModel>
        var foundGeosite: GeositeModel? = geositesList.find { p -> p.id == geosite.id }
        if (foundGeosite != null) {
            foundGeosite.title = geosite.title
            foundGeosite.description = geosite.description
            foundGeosite.landowner = geosite.landowner
            foundGeosite.phone = geosite.phone
            foundGeosite.drilling = geosite.drilling
            foundGeosite.comment = geosite.comment
            foundGeosite.image = geosite.image
            foundGeosite.location = geosite.location
        }
        serialize()
    }

    override suspend fun delete(geosite: GeositeModel) {
        val foundGeosite: GeositeModel? = geosites.find { it.id == geosite.id }
        geosites.remove(foundGeosite)
        serialize()
    }

    override suspend fun findById(id:Long) : GeositeModel? {
        val foundGeosite: GeositeModel? = geosites.find { it.id == id }
        return foundGeosite
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(geosites, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        geosites = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        geosites.forEach { Timber.i("$it") }
    }

    override suspend fun clear(){
        geosites.clear()
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