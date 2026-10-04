package com.bengkel.app

import android.app.Application
import com.bengkel.app.data.BengkelRepository

class BengkelApplication : Application() {
    lateinit var repository: BengkelRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = BengkelRepository(this)
    }
}
