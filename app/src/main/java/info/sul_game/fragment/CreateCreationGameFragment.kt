package info.sul_game.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import info.sul_game.utils.FileUtil
import info.sul_game.databinding.FragmentCreateCreateBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import info.sul_game.R
import info.sul_game.recyclerview.CreateFileAdapter
import java.io.File
import java.io.IOException


class CreateCreationGameFragment : Fragment() {
    companion object {
        private const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1001
        private const val MAXIMUM_CHIP_COUNT = 4
    }
    private fun requestPermissionsIfNecessary() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                ),
                REQUEST_CODE_WRITE_EXTERNAL_STORAGE
            )
        }
    }


    private lateinit var binding: FragmentCreateCreateBinding
    private val selectedChipsCreate = mutableListOf<Chip>()
    private lateinit var gameCreateAdapter: CreateFileAdapter
    private lateinit var introCreateAdapter: CreateFileAdapter
    private lateinit var photoUri: Uri
    private lateinit var videoUri: Uri
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var videoLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var audioLauncher: ActivityResultLauncher<Intent>
    private lateinit var audiofileLauncher:ActivityResultLauncher<Intent>
    private var mediaRecorder: MediaRecorder? = null
    private var audioFilePath: String? = null
    private var isIntroMode = false
    private lateinit var recordButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissionsIfNecessary()


        gameCreateAdapter = CreateFileAdapter(mutableListOf(),this)
        binding.rvMediaCreate.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvMediaCreate.adapter =gameCreateAdapter
        introCreateAdapter = CreateFileAdapter(mutableListOf(),this)
        binding.rvIntroCreate.layoutManager =LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvIntroCreate.adapter = introCreateAdapter

        /*여기서부터 create내 카메라기능*/
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                gameCreateAdapter.addMedia(photoUri,isVideo = false,isAudio = false)
                checkRecyclerViewVisibility()
            }
        }

        videoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (isIntroMode) {
                    introCreateAdapter.addMedia(videoUri, isVideo = true, isAudio = false)
                } else {
                    gameCreateAdapter.addMedia(videoUri, isVideo = true, isAudio = false)
                }
                checkRecyclerViewVisibility()
            }
        }
        audioLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                introCreateAdapter.addMedia(videoUri,isVideo = false,isAudio = true)
                checkRecyclerViewVisibility()
            }

        }


        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data ?: return@registerForActivityResult
                val tempFile = FileUtil.createTempFile(requireContext(), uri)
                val isVideo = tempFile.extension in listOf("mp4", "avi", "mov")
                val isAudio = tempFile.extension in listOf("mp3", "wav", "aac")

                if (isIntroMode) {
                    if (isAudio && introCreateAdapter.isAudioExist()) {
                        Toast.makeText(requireContext(), "인트로는 하나만 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        introCreateAdapter.addMedia(uri, isVideo, isAudio)
                        checkRecyclerViewVisibility()
                    }
                } else {
                    if (isVideo && gameCreateAdapter.isVideoExist()) {
                        Toast.makeText(requireContext(), "비디오는 하나만 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        gameCreateAdapter.addMedia(uri, isVideo, isAudio)
                        checkRecyclerViewVisibility()
                    }
                }
            }
        }

        audiofileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val uri = result.data?.data?:return@registerForActivityResult
                val tempFile = FileUtil.createTempFile(requireContext(),uri)
                val isVideo = tempFile.extension in listOf("mp4", "avi", "mov")
                val isAudio = tempFile.extension in listOf("mp3", "wav", "aac")
                if (isAudio && introCreateAdapter.isAudioExist()) {
                    Toast.makeText(requireContext(), "오디오는 하나만 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    introCreateAdapter.addMedia(uri, isVideo=false,isAudio=true)
                    checkRecyclerViewVisibility()
                }

            }

        }
        binding.rgIntroCreate.setOnCheckedChangeListener { _,checkedId ->
            when(checkedId){
                R.id.btn_introexist_create -> binding.llIntroCreate.visibility = VISIBLE
                R.id.btn_intronothing_create -> binding.llIntroCreate.visibility = GONE
            }

        }


        binding.btnCameraCreate.setOnClickListener {
            showTutorialMediaSelectionDialog()
        }
        binding.btnGalleryCreate.setOnClickListener {
            openGallery()
        }
        binding.btnIntroVideoCreate.setOnClickListener{
            if (introCreateAdapter.isTotalExists()) {
                Toast.makeText(requireContext(), "인트로는 하나만 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
            } else {
                isIntroMode = true
                recordVideo()  // 비디오 촬영
            }
        }
        recordButton = binding.btnIntroAudioCreate
        recordButton.setImageResource(R.drawable.btn_audio)
        recordButton.setOnClickListener{
            if(introCreateAdapter.isTotalExists()){
                Toast.makeText(requireContext(),"인트로는 하나만 추가할 수 있습니다",Toast.LENGTH_SHORT).show()
            }else{ recordAudio()

            }
        }
        binding.btnIntroFileCreate.setOnClickListener{
            showIntroMediaSelectionDialog()

        }


        checkRecyclerViewVisibility()
        /*여기까지 create안에 카메라기능*/

        // ChipGroups와 Button을 가져옵니다.
        val chipGroups = listOf(
            binding.chipGroupLevelCreate,
            binding.chipGroupCountCreate,
            binding.chipGroupNoiseCreate,
            binding.chipGroupEtcCreate
        )

        // ChipGroup의 선택 이벤트 리스너 설정
        for (group in chipGroups) {
            group.setOnCheckedChangeListener { _, _ -> checkSelectionOnChipChange(chipGroups) }
        }

        // Button 클릭 이벤트 설정
        binding.btnEnrollCreate.setOnClickListener {
            checkSelectionOnButtonClick(chipGroups)
        }
    }
    private fun showIntroMediaSelectionDialog() {
        val options = arrayOf("녹음파일", "비디오 파일")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("선택하세요")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> if (introCreateAdapter.isTotalExists()) {
                        Toast.makeText(requireContext(), "인트로는 하나만 추가할 수 있습니다", Toast.LENGTH_SHORT).show()
                    } else {
                        openAudioFilePicker()
                    }
                    1 -> if (introCreateAdapter.isTotalExists()) {
                        Toast.makeText(requireContext(), "인트로는 하나만 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        openVideoFilePicker()
                    }
                }
            }
        isIntroMode = true // Intro 모드로 설정
        builder.show()
    }
    private fun openAudioFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "audio/*"
        }
        audiofileLauncher.launch(intent)
    }

    private fun openVideoFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "video/*"
        }
        galleryLauncher.launch(intent)
    }


/*여기서 부터 함수 카메라 기능임 */
private fun showTutorialMediaSelectionDialog() {
    val options = arrayOf("사진촬영", "비디오 촬영")
    val builder = AlertDialog.Builder(requireContext())
    builder.setTitle("선택하세요")
        .setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> if (gameCreateAdapter.isVideoExist()) {
                    Toast.makeText(requireContext(), "비디오는 하나만 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    recordVideo()  // 비디오 촬영
                }
            }
        }
    isIntroMode = false // Tutorial 모드로 설정
    builder.show()
}

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = createImageFile()
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${info.sul_game.BuildConfig.APPLICATION_ID}.fileprovider",
            file
        )
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        cameraLauncher.launch(intent)
    }

    private fun recordVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        val file = createVideoFile()
        videoUri = FileProvider.getUriForFile(requireContext(), "${info.sul_game.BuildConfig.APPLICATION_ID}.fileprovider", file)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
        videoLauncher.launch(intent)
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/* video/*"
        }
        galleryLauncher.launch(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "권한이 승인되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createImageFile(extension: String = ".jpg"): File {
        return FileUtil.createImageFile(requireContext(), extension)
    }

    private fun createVideoFile(extension: String = ".mp4"): File {
        return FileUtil.createVideoFile(requireContext(), extension)
    }
/*여기까지 카메라기능*/
    /*여기부터는 오디오 기능*/
private fun startRecording() {
    val audioFile = createAudioFile()
    audioFilePath = audioFile.absolutePath

    mediaRecorder = MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        setOutputFile(audioFilePath)

        try {
            prepare()
            start()
            Toast.makeText(requireContext(), "녹음 시작", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "녹음 시작 실패: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        Toast.makeText(requireContext(), "녹음 종료", Toast.LENGTH_SHORT).show()
        // Notify adapter with the recorded audio file
        val audioUri = Uri.fromFile(File(audioFilePath))
        introCreateAdapter.addMedia(audioUri, isVideo = false, isAudio = true)
        checkRecyclerViewVisibility()
    }

    private fun createAudioFile(extension: String = ".mp3"): File {
        return FileUtil.createAudioFile(requireContext(), extension)
    }

    private fun recordAudio() {
        if (mediaRecorder == null) {
            startRecording()
        recordButton.setImageResource(R.drawable.btn_record)// 녹음 시작
        } else {
            stopRecording()
            recordButton.setImageResource(R.drawable.btn_audio)// 녹음 중지
        }
    }


    /*여기까지 오디오 녹음 기능*/
    fun checkRecyclerViewVisibility() {
        binding.rvMediaCreate.visibility = if (gameCreateAdapter.itemCount > 0) VISIBLE else GONE
        binding.rvIntroCreate.visibility = if (introCreateAdapter.itemCount > 0) VISIBLE else GONE
    }



    private fun checkSelectionOnChipChange(chipGroups: List<ChipGroup>) {
        // 4개 이상의 Chip이 선택된 경우 즉시 처리
        if (selectedChipsCreate.size > MAXIMUM_CHIP_COUNT) {
            Toast.makeText(requireContext(), "${MAXIMUM_CHIP_COUNT}개까지만 선택 가능합니다", Toast.LENGTH_SHORT).show()
            val lastSelectedChip = selectedChipsCreate.last()
            lastSelectedChip.isChecked = false
            selectedChipsCreate.remove(lastSelectedChip)
        }
    }

    private fun checkSelectionOnButtonClick(chipGroups: List<ChipGroup>) {
        selectedChipsCreate.clear()

        var validSelection = true

        // 각 ChipGroup에서 최소 1개의 선택이 있는지 확인
        for (group in chipGroups) {
            var groupSelected = false
            for (i in 0 until group.childCount) {
                val chip = group.getChildAt(i) as Chip
                if (chip.isChecked) {
                    groupSelected = true
                    selectedChipsCreate.add(chip)
                }
            }
            if (!groupSelected) {
                validSelection = false
            }
        }


        // 조건에 맞지 않으면 에러 메시지 표시
        if (!validSelection) {
            Toast.makeText(requireContext(), "정보를 전부 입력해야합니다", Toast.LENGTH_SHORT).show()
        }
//        val jsonObject= JSONObject()
//        jsonObject.put("email", email.toString())
//        jsonObject.put("password", binding.etNewPasswordOkInput.text.toString())
    }
}
