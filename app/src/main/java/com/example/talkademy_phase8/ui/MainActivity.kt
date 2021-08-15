package com.example.talkademy_phase8.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.talkademy_phase8.R
import com.example.talkademy_phase8.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.RealmConfiguration

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController

    companion object{
        lateinit var backgroundThreadRealm:Realm
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        navController = Navigation.findNavController(this,R.id.nav_host_fragment)

        NavigationUI.setupActionBarWithNavController(this,navController)

        //initRealm()
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.nav_host_fragment).navigateUp()

    private fun initRealm(){
        Realm.init(applicationContext)

        val realmName = "StudentRealm"
        val config = RealmConfiguration.Builder().name(realmName).build()
        backgroundThreadRealm = Realm.getInstance(config)
    }

}