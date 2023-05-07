package com.candroid.qrscannerwithfragment

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.candroid.qrscannerwithfragment.databinding.FragmentQrScannerBinding

class QrScannerFragment : Fragment() {

    private lateinit var binding: FragmentQrScannerBinding
    private lateinit var codeScanner: CodeScanner
    private lateinit var deviceMacAddress: String
    private lateinit var deviceServiceUUID: String
    private lateinit var deviceCharacteristicUUID: String
    private lateinit var scannedDevice: ScannedDevice

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentQrScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (requireActivity().checkSelfPermission(android.Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED){
                requireActivity().requestPermissions(arrayOf(android.Manifest.permission.CAMERA),1)
            }
        }

        codeScanner = CodeScanner(requireActivity(), binding.qrScanner)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false
        codeScanner.decodeCallback = DecodeCallback {
            requireActivity().runOnUiThread {
                scannedValueParser(it.text)
//                Log.i("QrScannerFragment", it.text.toString())
                val bundle = Bundle()
                bundle.putSerializable("scanned_qr_value", scannedDevice)
//                val device = bundle.getSerializable("scanned_qr_value") as ScannedDevice
//                Log.i("QrScannerFragment", device.deviceMacAddress)
//                Log.i("QrScannerFragment", device.deviceServiceUUID)
//                Log.i("QrScannerFragment", device.deviceCharacteristicUUID)
                Navigation.findNavController(requireView()).navigate(R.id.action_qrScannerFragment_to_resultFragment, bundle)
                //Navigation.findNavController(requireView()).navigate(R.id.action_qrScannerFragment_to_resultFragment, bundle)
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            requireActivity().runOnUiThread {
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
            }
        }

        binding.qrScanner.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun scannedValueParser(scannedData: String){
        var scannedValue = scannedData
        deviceMacAddress = scannedValue.substringAfter(".").substringBefore("\n")
        scannedValue = scannedValue.substringAfter("\n")
        deviceServiceUUID = scannedValue.substringAfter(".").substringBefore("\n")
        scannedValue = scannedValue.substringAfter("\n")
        deviceCharacteristicUUID = scannedValue.substringAfter(".")
        scannedDevice = ScannedDevice(deviceMacAddress, deviceServiceUUID, deviceCharacteristicUUID)
    }
}