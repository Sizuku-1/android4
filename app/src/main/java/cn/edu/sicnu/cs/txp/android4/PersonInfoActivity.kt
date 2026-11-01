package cn.edu.sicnu.cs.txp.android4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class PersonInfoActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etStudentId: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var cbTravel: CheckBox
    private lateinit var cbRead: CheckBox
    private lateinit var cbGame: CheckBox
    private lateinit var cbShopping: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_info)

        etName = findViewById(R.id.et_name)
        etStudentId = findViewById(R.id.et_student_id)
        etPhone = findViewById(R.id.et_phone)
        etEmail = findViewById(R.id.et_email)
        rbMale = findViewById(R.id.rb_male)
        rbFemale = findViewById(R.id.rb_female)
        cbTravel = findViewById(R.id.cb_travel)
        cbRead = findViewById(R.id.cb_read)
        cbGame = findViewById(R.id.cb_game)
        cbShopping = findViewById(R.id.cb_shopping)

        // 回填已有信息
        etName.setText(intent.getStringExtra(MainActivity.KEY_NAME))
        etStudentId.setText(intent.getStringExtra(MainActivity.KEY_STUDENT_ID))
        etPhone.setText(intent.getStringExtra(MainActivity.KEY_PHONE))
        etEmail.setText(intent.getStringExtra(MainActivity.KEY_EMAIL))

        val gender = intent.getStringExtra(MainActivity.KEY_GENDER)
        if (gender == getString(R.string.label_gender_female)) {
            rbFemale.isChecked = true
        } else {
            rbMale.isChecked = true
        }

        val hobby = intent.getStringExtra(MainActivity.KEY_HOBBY).orEmpty()
        cbTravel.isChecked = hobby.contains(getString(R.string.label_hobby_travel))
        cbRead.isChecked = hobby.contains(getString(R.string.label_hobby_read))
        cbGame.isChecked = hobby.contains(getString(R.string.label_hobby_game))
        cbShopping.isChecked = hobby.contains(getString(R.string.label_hobby_shopping))

        findViewById<Button>(R.id.btn_save).setOnClickListener {
            saveAndFinish()
        }

        findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun saveAndFinish() {
        val name = etName.text.toString().trim()
        val studentId = etStudentId.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val email = etEmail.text.toString().trim()

        val gender = if (rbFemale.isChecked) {
            getString(R.string.label_gender_female)
        } else {
            getString(R.string.label_gender_male)
        }

        val hobbies = mutableListOf<String>()
        if (cbTravel.isChecked) hobbies.add(getString(R.string.label_hobby_travel))
        if (cbRead.isChecked) hobbies.add(getString(R.string.label_hobby_read))
        if (cbGame.isChecked) hobbies.add(getString(R.string.label_hobby_game))
        if (cbShopping.isChecked) hobbies.add(getString(R.string.label_hobby_shopping))
        val hobbyText = hobbies.joinToString("、")

        val data = Intent().apply {
            putExtra(MainActivity.KEY_NAME, name)
            putExtra(MainActivity.KEY_GENDER, gender)
            putExtra(MainActivity.KEY_STUDENT_ID, studentId)
            putExtra(MainActivity.KEY_PHONE, phone)
            putExtra(MainActivity.KEY_EMAIL, email)
            putExtra(MainActivity.KEY_HOBBY, hobbyText)
        }
        setResult(RESULT_OK, data)
        finish()
    }
}
