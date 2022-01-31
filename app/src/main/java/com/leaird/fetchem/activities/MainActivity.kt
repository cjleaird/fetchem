package com.leaird.fetchem.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_TITLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.leaird.fetchem.R
import com.leaird.fetchem.databinding.ActivityMainBinding
import com.leaird.fetchem.viewmodel.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val activityViewModel: ActivityViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var toolbarTitleTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        // We have a custom toolbar view that will be used to display the current
        // fragment label.
        toolbarTitleTv = binding.toolbar.toolbar.findViewById(R.id.toolbar_header_tv)

        setSupportActionBar(binding.toolbar.toolbar)

        // We are using a custom title, so no need to show the default action bar
        // title.
        supportActionBar?.setDisplayOptions(0, DISPLAY_SHOW_TITLE)

        // Control activity-level UI states based on current fragment.
        navHostFragment.navController.addOnDestinationChangedListener { _, _, arguments ->
            arguments?.let {
                if (it.getBoolean(ARGUMENT_SHOW_TOOLBAR, false)) {
                    supportActionBar?.show()
                    binding.toolbar.toolbar.visibility = View.VISIBLE
                } else {
                    supportActionBar?.hide()
                    binding.toolbar.toolbar.visibility = View.GONE
                }
                if (it.getBoolean(ARGUMENT_SHOW_BOTTOM_NAVIGATION, false)) {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                } else {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                val title: Any? = it.get(ARGUMENT_TOOLBAR_TITLE)
                if (title is Int) {
                    toolbarTitleTv.text = getString(title)
                } else if (title is String) {
                    toolbarTitleTv.text = title
                }
            }
        }

        lifecycleScope.launch {
            launch {
                activityViewModel.showAdminView
                    .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                    .collect { showAdmin ->
                        // Depending on the user's role, we'll need to display
                        // different views. An admin user will be able to access
                        // different fragments than those of a normal user.
                        if (showAdmin) {
                            binding.bottomNavigationView.menu.clear()
                            binding.bottomNavigationView.inflateMenu(
                                R.menu.admin_bottom_navigation_menu
                            )
                            appBarConfiguration = AppBarConfiguration(
                                setOf(
                                    R.id.dashboardFragment,
                                    R.id.libraryFragment,
                                    R.id.accountFragment
                                )
                            )
                            setupActionBarWithNavController(navController, appBarConfiguration)
                        } else {
                            binding.bottomNavigationView.menu.clear()
                            binding.bottomNavigationView.inflateMenu(
                                R.menu.user_bottom_navigation_menu
                            )
                            appBarConfiguration = AppBarConfiguration(
                                setOf(
                                    R.id.discoverFragment,
                                    R.id.ratingsFragment,
                                    R.id.accountFragment
                                )
                            )
                            setupActionBarWithNavController(navController, appBarConfiguration)
                        }
                    }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    companion object {
        private const val ARGUMENT_SHOW_TOOLBAR = "show_tool_bar"
        private const val ARGUMENT_SHOW_BOTTOM_NAVIGATION = "show_bottom_navigation"
        private const val ARGUMENT_TOOLBAR_TITLE = "toolbar_title"
    }
}