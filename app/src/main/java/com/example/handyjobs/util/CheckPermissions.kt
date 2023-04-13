package com.example.handyjobs.util

import android.Manifest
import android.content.Context
import android.os.Build
import pub.devrel.easypermissions.EasyPermissions

object CheckPermissions {

    fun hasPermissions(context:Context)  =
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,

            )

    fun hasNotificationPermission(context: Context):Boolean{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
           return  EasyPermissions.hasPermissions(
               context,
               Manifest.permission.POST_NOTIFICATIONS
           )
       }else{
           return true
        }
    }


}