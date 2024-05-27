package be.howest.ti.taskifox

import android.app.Application
import be.howest.ti.taskifox.data.AppContainer
import be.howest.ti.taskifox.data.AppDataContainer

class TaskiFoxApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}