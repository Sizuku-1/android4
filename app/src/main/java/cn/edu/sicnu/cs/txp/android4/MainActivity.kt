package cn.edu.sicnu.cs.txp.android4

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvStudentId: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvHobby: TextView
    private lateinit var tvHighScore: TextView

    companion object {
        const val REQUEST_EDIT_INFO = 1
        const val REQUEST_CARD_GAME = 2

        const val KEY_NAME = "name"
        const val KEY_GENDER = "gender"
        const val KEY_STUDENT_ID = "student_id"
        const val KEY_PHONE = "phone"
        const val KEY_EMAIL = "email"
        const val KEY_HOBBY = "hobby"
        const val KEY_HIGH_SCORE = "high_score"
    }

    // 玩家信息（内存中保存，无需写入文件）
    private var playerName = ""
    private var playerGender = ""
    private var playerStudentId = ""
    private var playerPhone = ""
    private var playerEmail = ""
    private var playerHobby = ""
    private var highScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvName = findViewById(R.id.tv_name)
        tvGender = findViewById(R.id.tv_gender)
        tvStudentId = findViewById(R.id.tv_student_id)
        tvPhone = findViewById(R.id.tv_phone)
        tvEmail = findViewById(R.id.tv_email)
        tvHobby = findViewById(R.id.tv_hobby)
        tvHighScore = findViewById(R.id.tv_high_score)

        playerName = savedInstanceState?.getString(KEY_NAME) ?: getString(R.string.default_name)
        playerGender = savedInstanceState?.getString(KEY_GENDER) ?: getString(R.string.default_gender)
        playerStudentId = savedInstanceState?.getString(KEY_STUDENT_ID) ?: getString(R.string.default_student_id)
        playerPhone = savedInstanceState?.getString(KEY_PHONE) ?: getString(R.string.default_phone)
        playerEmail = savedInstanceState?.getString(KEY_EMAIL) ?: getString(R.string.default_email)
        playerHobby = savedInstanceState?.getString(KEY_HOBBY) ?: getString(R.string.default_hobby)

        highScore = savedInstanceState?.getInt(KEY_HIGH_SCORE) ?: 0
        refreshInfo()

        findViewById<Button>(R.id.btn_edit_info).setOnClickListener {
            val intent = Intent(this, PersonInfoActivity::class.java)
            intent.putExtra(KEY_NAME, playerName)
            intent.putExtra(KEY_GENDER, playerGender)
            intent.putExtra(KEY_STUDENT_ID, playerStudentId)
            intent.putExtra(KEY_PHONE, playerPhone)
            intent.putExtra(KEY_EMAIL, playerEmail)
            intent.putExtra(KEY_HOBBY, playerHobby)
            startActivityForResult(intent, REQUEST_EDIT_INFO)
        }

        findViewById<Button>(R.id.btn_card_game).setOnClickListener {
            val intent = Intent(this, CardGameActivity::class.java)
            startActivityForResult(intent, REQUEST_CARD_GAME)
        }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_NAME, playerName)
        outState.putString(KEY_GENDER, playerGender)
        outState.putString(KEY_STUDENT_ID, playerStudentId)
        outState.putString(KEY_PHONE, playerPhone)
        outState.putString(KEY_EMAIL, playerEmail)
        outState.putString(KEY_HOBBY, playerHobby)
        outState.putInt(KEY_HIGH_SCORE, highScore)
    }

    private fun refreshInfo() {
        tvName.text = playerName
        tvGender.text = playerGender
        tvStudentId.text = playerStudentId
        tvPhone.text = playerPhone
        tvEmail.text = playerEmail
        tvHobby.text = playerHobby
        tvHighScore.text = highScore.toString()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) return

        when (requestCode) {
            REQUEST_EDIT_INFO -> {
                playerName = data.getStringExtra(KEY_NAME) ?: playerName
                playerGender = data.getStringExtra(KEY_GENDER) ?: playerGender
                playerStudentId = data.getStringExtra(KEY_STUDENT_ID) ?: playerStudentId
                playerPhone = data.getStringExtra(KEY_PHONE) ?: playerPhone
                playerEmail = data.getStringExtra(KEY_EMAIL) ?: playerEmail
                playerHobby = data.getStringExtra(KEY_HOBBY) ?: playerHobby
                refreshInfo()
            }
            REQUEST_CARD_GAME -> {
                val score = data.getIntExtra(KEY_HIGH_SCORE, 0)
                if (score > highScore) {
                    highScore = score
                    refreshInfo()
                }
            }
        }
    }
}
