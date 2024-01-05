package com.visualinnovate.almursheed.auth.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.proxy.AuthApiStatusCodes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.visualinnovate.almursheed.BuildConfig
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.viewmodel.AuthViewModel
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.isEmptySting
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.startHomeActivity
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.databinding.FragmentLoginBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding

    private lateinit var email: String
    private lateinit var password: String
    private var rememberMe: Boolean = false
    private val vm: AuthViewModel by viewModels()

    // social login
    private var googleSignInClient: GoogleSignInClient? = null
    private var googleSignInOptions: GoogleSignInOptions? = null
    private var facebookCallbackManager: CallbackManager? = null
    private var facebookLoginManager: LoginManager? = null
    private var providerName = ""
    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setBtnListener()
        subscribeData()
    }

    private fun initToolbar() {
        binding.appBarLogin.setTitleString(getString(R.string.login))
        binding.appBarLogin.setTitleCenter(true)
        binding.appBarLogin.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
        binding.appBarLogin.showButtonOneWithoutImage(
            getString(R.string.register),
        ) { findNavController().customNavigate(R.id.registerTypeFragment) }
    }

    private fun setBtnListener() {
        binding.btnRememberMe.onDebouncedListener {
            if (!rememberMe) {
                rememberMe = true
                binding.imgRememberMe.setImageResource(R.drawable.ic_remember_me_selected)
            } else {
                rememberMe = false
                binding.imgRememberMe.setImageResource(R.drawable.ic_remember_me_unselected)
            }
        }

        binding.txtForgetPassword.onDebouncedListener {
            findNavController().customNavigate(R.id.forgetPasswordFragment)
        }

        binding.btnFacebook.onDebouncedListener {}
        binding.btnGmail.onDebouncedListener {
            //  (requireActivity() as HandleSocialAuth).signWithGmail()
            initGoogleAuthentication()
        }

        binding.btnLogin.setOnClickListener {
            if (validate()) {
                // call api login
                vm.login(email, password)
            } else {
                binding.btnLogin.handleBtnAnimation()
            }
        }
    }

    private fun subscribeData() {
        vm.loginLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // save user
                    SharedPreference.saveUser(it.data?.user)
                    SharedPreference.setUserToken(it.data?.token)
                    // SharedPreference.setCityId(it.data?.user?.desCityId)

                    if (Constant.ROLE_TOURIST == it.data?.user?.type) {
                        SharedPreference.setCityId(
                            it.data.user?.desCityId ?: it.data.user?.destCityId
                        )
                        SharedPreference.setCountryId(it.data.user?.destCountryId?.toInt())
                    } else {
                        SharedPreference.setCityId(it.data?.user?.stateId)
                        SharedPreference.setCountryId(it.data?.user?.countryId?.toInt())
                    }

                    SharedPreference.setNotificationId(it.data?.user?.notificationId)
                    if (rememberMe) SharedPreference.setUserLoggedIn(true)

                    binding.btnLogin.handleBtnAnimation()
                    requireActivity().startHomeActivity()
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("ResponseHandler.Error", it.message)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    showAuthLoading()
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    hideAuthLoading()
                    binding.btnLogin.handleBtnAnimation()
                }

                else -> {}
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        email = binding.edtEmailAddress.value
        password = binding.edtPassword.value

        // email = "mohamed.driver@gmail.com"
        // password = "123456789"

        // email = "mohamed.nasar8710@gmail.com"
      //  email = "vusabawi@closetab.email" // driver
        email = "yetowag817@talmetry.com" // new driver
        // email = "mohamed.tourist1@gmail.com"
        // email = "cazirera@tutuapp.bid" // tourist
        // email = "mohamed200@gmail.com"
        // email = "guide100@gmail.com"
        // email = "vazohu@tutuapp.bid" // guide
        // email = "nassar@gmail.com"
        password = "123456789"

        if (email.isEmptySting()) {
            binding.edtEmailAddress.error = getString(R.string.required)
            return false
        } else if (!validEmail(email)) {
            binding.edtEmailAddress.error = getString(R.string.please_enter_a_valid_email)
            isValid = true
        } else {
            binding.edtEmailAddress.error = null
        }
        if (password.isEmpty()) {
            binding.edtPassword.error = getString(R.string.required)
            return false
        } else if (password.length < 8) {
            binding.edtPassword.error =
                getString(R.string.password_should_be_more_than_8_characters)
            return false
        } else {
            binding.edtPassword.error = null
        }
        return isValid
    }

    private fun initGoogleAuthentication() {
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestIdToken(BuildConfig.oAuthServerKey)
            .requestServerAuthCode(BuildConfig.oAuthServerKey)
            .build()
        googleSignInClient = googleSignInOptions?.let {
            GoogleSignIn.getClient(
                requireActivity(),
                it,
            )
        }
        val signInIntent: Intent = googleSignInClient!!.signInIntent
        launchActivityResult(signInIntent)
    }

    private fun initFacebookAuthentication() {
        facebookLoginManager = LoginManager.getInstance()
        facebookCallbackManager = CallbackManager.Factory.create()
        facebookLoginManager!!.logInWithReadPermissions(
            this,
            facebookCallbackManager!!,
            listOf("public_profile", "email"),
        )
        facebookLoginManager!!.registerCallback(
            facebookCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    token = result.accessToken.token
                    // vm.setSocialProviderAndToken(token, providerName)
                }

                override fun onCancel() {
                    // Log.d(ContentValues.TAG, "facebookKKK:onCancel")
                }

                override fun onError(error: FacebookException) {
                    // Log.d(ContentValues.TAG, "facebookKKK:onError ${error.message}", error)
                }
            },
        )
    }

    private fun handleGoogleSignInResult(data: Intent) {
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                token = GoogleAuthUtil.getToken(
                    requireActivity(),
                    account.account,
                    "oauth2:" + "https://www.googleapis.com/auth/plus.login ",
                )

                Log.d(
                    "MyDebugData",
                    "MainActivity : handleGoogleSignInResult :  " + account.displayName,
                )
                Log.d("MyDebugData", "MainActivity : handleGoogleSignInResult :  " + account.email)
                Log.d("MyDebugData", "MainActivity : handleGoogleSignInResult :  " + token)

                //  vm.setSocialProviderAndToken(token, providerName)
            }
        } catch (e: ApiException) {
            println("Facebook Error" + AuthApiStatusCodes.getStatusCodeString(e.statusCode))
            println("Facebook Error" + e.localizedMessage)
        }
    }

    private fun launchActivityResult(intent: Intent) {
        try {
            onActivityResultLauncher.launch(intent)
        } catch (ex: Exception) {
            Log.d("myDebug", "BaseFragment launchActivityForResult   " + ex.localizedMessage)
        }
    }

    private var onActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            if (data != null) handleGoogleSignInResult(data)
        }
}
