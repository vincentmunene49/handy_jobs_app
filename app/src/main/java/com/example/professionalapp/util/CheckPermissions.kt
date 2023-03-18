package com.example.professionalapp.util

import android.Manifest
import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

object CheckPermissions {
    fun hasPermissions(context:Context):Boolean{
      return  EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}