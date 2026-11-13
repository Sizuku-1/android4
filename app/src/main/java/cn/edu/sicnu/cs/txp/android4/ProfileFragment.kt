package cn.edu.sicnu.cs.txp.android4

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.edu.sicnu.cs.txp.android4.model.PlayerProfile

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val name = view.findViewById<EditText>(R.id.et_name)
        val studentId = view.findViewById<EditText>(R.id.et_student_id)
        val phone = view.findViewById<EditText>(R.id.et_phone)
        val email = view.findViewById<EditText>(R.id.et_email)
        val gender = view.findViewById<RadioGroup>(R.id.rg_gender)
        val hobbies = mapOf(
            "travel" to view.findViewById<CheckBox>(R.id.cb_travel),
            "read" to view.findViewById<CheckBox>(R.id.cb_read),
            "game" to view.findViewById<CheckBox>(R.id.cb_game),
            "shopping" to view.findViewById<CheckBox>(R.id.cb_shopping)
        )
        var saved = ObjectStore.read(context, PROFILE_FILE, PlayerProfile::class.java)
            ?: PlayerProfile(
                getString(R.string.default_name), getString(R.string.default_student_id),
                getString(R.string.default_phone), getString(R.string.default_email),
                "male", listOf("travel", "read")
            )

        fun showProfile(profile: PlayerProfile) {
            name.setText(profile.name)
            studentId.setText(profile.studentId)
            phone.setText(profile.phone)
            email.setText(profile.email)
            gender.check(if (profile.gender == "female") R.id.rb_female else R.id.rb_male)
            hobbies.forEach { (key, box) -> box.isChecked = key in profile.hobbies }
        }

        fun dismissKeyboard() {
            context.getSystemService(InputMethodManager::class.java)
                .hideSoftInputFromWindow(view.windowToken, 0)
            view.requestFocus()
        }

        showProfile(saved)
        view.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            showProfile(saved)
            dismissKeyboard()
        }
        view.findViewById<Button>(R.id.btn_save).setOnClickListener {
            val edited = PlayerProfile(
                name.text.toString().trim(), studentId.text.toString().trim(),
                phone.text.toString().trim(), email.text.toString().trim(),
                if (gender.checkedRadioButtonId == R.id.rb_female) "female" else "male",
                hobbies.filterValues { it.isChecked }.keys.toList()
            )
            val success = ObjectStore.write(context, PROFILE_FILE, edited)
            if (success) {
                saved = edited
                showProfile(saved)
                dismissKeyboard()
            }
            val message = if (success) R.string.profile_saved else R.string.save_failed
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private companion object {
        const val PROFILE_FILE = "profile.ser"
    }
}
