package cn.edu.sicnu.cs.txp.android4.model

import java.io.Serializable

internal data class PlayerProfile(
    val name: String,
    val studentId: String,
    val phone: String,
    val email: String,
    val gender: String,
    val hobbies: List<String>
) : Serializable {
    private companion object {
        const val serialVersionUID = 1L
    }
}
