package com.candroid.qrscannerwithfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.candroid.qrscannerwithfragment.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private lateinit var scannedDevice: ScannedDevice

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        scannedDevice = arguments?.getSerializable("scanned_qr_value") as ScannedDevice
        binding.deviceMacAddress.text = scannedDevice.deviceMacAddress
        binding.deviceServiceUUID.text = scannedDevice.deviceServiceUUID
        binding.deviceCharacteristicUUID.text = scannedDevice.deviceCharacteristicUUID
        return binding.root
    }
}