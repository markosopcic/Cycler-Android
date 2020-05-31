package com.markosopcic.cycler.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.markosopcic.cycler.R
import com.markosopcic.cycler.view.LoginActivity
import com.markosopcic.cycler.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.profile_fragment.*
import org.koin.android.ext.android.get

class ProfileFragment : Fragment() {


    val viewModel = get<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    fun enableUploadButton(){
     upload_button.isEnabled = true
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        upload_button.setOnClickListener {
            it.isEnabled = false
            viewModel.uploadAllEvents(::enableUploadButton)
        }

        logout_button.setOnClickListener {
            viewModel.logout(::onLogout)
        }

        viewModel.refreshUserData()
        viewModel.userData.observe(viewLifecycleOwner, Observer {
            profile_user_name.text = it.firstName+" "+it.lastName
            profile_email.text = "Email: "+it.email
            profile_joined_date.text = "Joined: "+it.dateJoined
            profile_num_friends.text = "Friends: "+it.numFriends
        })
    }

    fun onLogout(){
        var intent = Intent(activity, LoginActivity::class.java)
         startActivity(intent)
        activity?.finish()
    }

}
