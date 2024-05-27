package be.howest.ti.taskifox.data.sources.room

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@ProvidedTypeConverter
class Converters {
    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    @TypeConverter
    fun toZonedDateTime(value: String?): ZonedDateTime? {
        return value?.let {
            return formatter.parse(value, ZonedDateTime::from)
        }
    }

    @TypeConverter
    fun fromZonedDateTime(dateTime: ZonedDateTime?): String? {
        return dateTime?.format(formatter)
    }
}