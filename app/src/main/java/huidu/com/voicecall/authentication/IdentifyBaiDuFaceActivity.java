package huidu.com.voicecall.authentication;

/**
 * Description:人脸认证
 * Data：2019/1/21-11:42
 * Author: lin
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.idl.face.platform.ui.FaceDetectActivity;

import java.util.HashMap;
import java.util.Map;

import huidu.com.voicecall.VoiceApp;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.main.MainActivity;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

public class IdentifyBaiDuFaceActivity extends FaceDetectActivity {

    private String HAND_BASE64;
    private String status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaiduOCR();
    }

    //初始化配置
    private void initBaiduOCR() {
        // 根据需求添加活体动作
        VoiceApp.livenessList.clear();
        VoiceApp.livenessList.add(LivenessTypeEnum.Eye);
        VoiceApp.livenessList.add(LivenessTypeEnum.Mouth);
        VoiceApp.livenessList.add(LivenessTypeEnum.HeadUp);
        VoiceApp.livenessList.add(LivenessTypeEnum.HeadDown);
        VoiceApp.livenessList.add(LivenessTypeEnum.HeadLeft);
        VoiceApp.livenessList.add(LivenessTypeEnum.HeadRight);
        VoiceApp.livenessList.add(LivenessTypeEnum.HeadLeftOrRight);
        // 为了android和ios 区分授权，appId=appname_face_android ,其中appname为申请sdk时的应用名
        // 应用上下文
        // 申请License取得的APPID
        // assets目录下License文件名
        FaceSDKManager.getInstance().initialize(this, API.licenseID, API.licenseFileName);
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整
        config.setLivenessTypeList(VoiceApp.livenessList);
        config.setLivenessRandom(VoiceApp.isLivenessRandom);
        config.setBlurnessValue(FaceEnvironment.VALUE_BLURNESS);
        config.setBrightnessValue(FaceEnvironment.VALUE_BRIGHTNESS);
        config.setCropFaceValue(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        config.setHeadPitchValue(FaceEnvironment.VALUE_HEAD_PITCH);
        config.setHeadRollValue(FaceEnvironment.VALUE_HEAD_ROLL);
        config.setHeadYawValue(FaceEnvironment.VALUE_HEAD_YAW);
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        config.setOcclusionValue(FaceEnvironment.VALUE_OCCLUSION);
        config.setCheckFaceQuality(true);
        config.setFaceDecodeNumberOfThreads(2);
        FaceSDKManager.getInstance().setFaceConfig(config);
    }

    @Override
    public void onDetectCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onDetectCompletion(status, message, base64ImageMap);

        if (status == FaceStatusEnum.OK && mIsCompletion) {
            Log.i(TAG, "onDetectCompletion: 人脸图像采集成功");
            int size = base64ImageMap.size();
            for (Map.Entry<String, String> entry : base64ImageMap.entrySet()) {
                Log.i(TAG, "Key :" + entry.getKey());
                Log.i(TAG, "Value :" + entry.getValue());
                HAND_BASE64 = entry.getValue();
            }
            uploadImage();
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            Log.i(TAG, "onDetectCompletion: 人脸图像采集超时");
        }
    }

    /**
     * 上传图片
     */
    public void uploadImage() {
        OkHttpUtils.getInstance().auth_face(SPUtils.getValue("token"), HAND_BASE64, new RequestFinish() {
            @Override
            public void onSuccess(BaseModel result, String params) {
                Log.e(TAG, "onSuccess: ");
                ToastUtil.toastShow("认证完成");
                startActivity(new Intent(IdentifyBaiDuFaceActivity.this, MainActivity.class));
            }

            @Override
            public void onError(String result) {
                ToastUtil.toastShow(result);
                Log.e(TAG, "onError: " + result);
            }
        });
    }

}

