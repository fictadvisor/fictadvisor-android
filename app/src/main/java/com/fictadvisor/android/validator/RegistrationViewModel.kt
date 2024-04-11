import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fictadvisor.android.validator.InputValidator

class RegistrationViewModel : ViewModel(){
    private val inputValidator = InputValidator()

    val nameErrorLiveData = MutableLiveData<String?>()
    val lastnameErrorLiveData = MutableLiveData<String?>()
    val middleNameErrorLiveData = MutableLiveData<String?>()
    val groupErrorLiveData = MutableLiveData<String?>()

    val emailErrorLiveData = MutableLiveData<String?>()
    val passwordErrorLiveData = MutableLiveData<String?>()
    val passwordConfirmErrorLiveData = MutableLiveData<String?>()
    val usernameErrorLiveData = MutableLiveData<String?>()

    private var result : Boolean = true

    fun validateStudentData(name: String, lastname: String, middleName: String, group: String): Boolean {
        result = true
        val nameValidationResult = inputValidator.isNameValid(name)
        if (!nameValidationResult.isValid) {
            nameErrorLiveData.value = nameValidationResult.errorMessage
            result = false
        } else {
            nameErrorLiveData.value = null
        }

        val lastnameValidationResult = inputValidator.isLastnameValid(lastname)
        if (!lastnameValidationResult.isValid) {
            lastnameErrorLiveData.value = lastnameValidationResult.errorMessage
            result = false
        } else {
            lastnameErrorLiveData.value = null
        }

        val middleNameValidationResult = inputValidator.isMiddleNameValid(middleName)
        if (!middleNameValidationResult.isValid) {
            middleNameErrorLiveData.value = middleNameValidationResult.errorMessage
            result = false
        } else {
            middleNameErrorLiveData.value = null
        }

        val groupValidationResult = inputValidator.isGroupValid(group)
        if (!groupValidationResult.isValid) {
            groupErrorLiveData.value = groupValidationResult.errorMessage
            result = false
        } else {
            groupErrorLiveData.value = null
        }

        return result
    }

    fun validateUserData(email: String, password: String, passwordConfirm: String, username: String): Boolean {
        result = true
        val emailValidationResult = inputValidator.isEmailValid(email)
        if (!emailValidationResult.isValid) {
            emailErrorLiveData.value = emailValidationResult.errorMessage
            result = false
        } else {
            emailErrorLiveData.value = null
        }

        val passwordValidationResult = inputValidator.isPasswordValid(password)
        if (!passwordValidationResult.isValid) {
            passwordErrorLiveData.value = passwordValidationResult.errorMessage
            result = false
        } else {
            passwordErrorLiveData.value = null
        }

        if(password != passwordConfirm) {
            passwordConfirmErrorLiveData.value = "Паролі не співпадають"
            result = false
        } else {
            passwordConfirmErrorLiveData.value = null
        }

        val usernameValidationResult = inputValidator.isUsernameValid(username)
        if (!usernameValidationResult.isValid) {
            usernameErrorLiveData.value = usernameValidationResult.errorMessage
            result = false
        } else {
            usernameErrorLiveData.value = null
        }

        return result
    }
}
