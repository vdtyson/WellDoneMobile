package com.versilistyson.welldone.ui.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.versilistyson.MyApplication
import com.versilistyson.welldone.R
import com.versilistyson.welldone.ViewModelProviderFactory
import com.versilistyson.welldone.di.auth.AuthComponent
import javax.inject.Inject

class AuthenticationActivity : AppCompatActivity() {

    private val host by lazy {
        NavHostFragment.create(R.navigation.auth_nav_graph)
    }

    lateinit var authComponent: AuthComponent
    @Inject lateinit var vmProviderFactory: ViewModelProviderFactory
    private lateinit var authSharedViewModel: AuthSharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        authComponent = (applicationContext as MyApplication).appComponent.authComponent()
        authComponent.inject(this)

        authSharedViewModel = ViewModelProviders.of(this, vmProviderFactory)[AuthSharedViewModel::class.java]

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, host)
            .setPrimaryNavigationFragment(host).commit()
    }
}
