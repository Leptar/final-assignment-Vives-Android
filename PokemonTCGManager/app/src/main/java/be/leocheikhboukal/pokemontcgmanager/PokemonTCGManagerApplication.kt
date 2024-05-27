package be.leocheikhboukal.pokemontcgmanager

import android.app.Application
import be.leocheikhboukal.pokemontcgmanager.data.AppContainer
import be.leocheikhboukal.pokemontcgmanager.data.AppDataContainer

class PokemonTCGManagerApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}