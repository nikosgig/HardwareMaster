package hardwaremaster.com.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import hardwaremaster.com.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity  : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //setting our navigation controller to our fragment found in xml
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bottom_nav.setupWithNavController(navController)

        //todo this affects toolbar to show back arrow - fix it
        NavigationUI.setupActionBarWithNavController(this, navController)

    }

    //todo this affects toolbar to make back arrow to work - fix it
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}