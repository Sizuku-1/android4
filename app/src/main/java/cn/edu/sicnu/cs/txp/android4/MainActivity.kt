package cn.edu.sicnu.cs.txp.android4

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_root)) { view, insets ->
            val safe = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            val keyboard = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.updatePadding(
                left = safe.left, top = safe.top, right = safe.right,
                bottom = maxOf(safe.bottom, keyboard.bottom)
            )
            WindowInsetsCompat.CONSUMED
        }

        val tabs = findViewById<TabLayout>(R.id.page_tabs)
        val selected = savedInstanceState?.getInt(SELECTED_TAB) ?: 0
        tabs.getTabAt(selected)?.select()
        if (savedInstanceState == null) showPage(selected)

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) = showPage(tab.position)
            override fun onTabUnselected(tab: TabLayout.Tab) = Unit
            override fun onTabReselected(tab: TabLayout.Tab) = Unit
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SELECTED_TAB, findViewById<TabLayout>(R.id.page_tabs).selectedTabPosition)
        super.onSaveInstanceState(outState)
    }

    private fun showPage(position: Int) {
        val fragment = when (position) {
            0 -> ProfileFragment()
            1 -> GameFragment.newInstance(2)
            else -> GameFragment.newInstance(3)
        }
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId != R.id.action_about) return super.onOptionsItemSelected(item)
        AlertDialog.Builder(this)
            .setTitle(R.string.menu_about)
            .setMessage(getString(R.string.about_message, getString(R.string.app_name)))
            .setPositiveButton(android.R.string.ok, null)
            .show()
        return true
    }

    private companion object {
        const val SELECTED_TAB = "selected_tab"
    }
}
