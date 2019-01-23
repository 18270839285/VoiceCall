package huidu.com.voicecall.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nanchen.compresshelper.CompressHelper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.widget.WheelView;
import de.hdodenhof.circleimageview.CircleImageView;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.SpareBean;
import huidu.com.voicecall.bean.UserInfo;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.AddressInitTask;
import huidu.com.voicecall.utils.FileUtils;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.MiPictureHelper;
import huidu.com.voicecall.utils.PicturePicker;
import huidu.com.voicecall.utils.PicturePickerFragment;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 编辑资料
 */
public class EditingMaterialsActivity extends BaseActivity implements RequestFinish {
    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_userId)
    TextView tv_userId;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;
    @BindView(R.id.tv_constellation)
    TextView tv_constellation;
    @BindView(R.id.tv_area)
    TextView tv_area;
    @BindView(R.id.et_sign)
    EditText et_sign;
    @BindView(R.id.et_nickname)
    EditText et_nickname;

    @BindView(R.id.ll_sex)
    LinearLayout ll_sex;

    private List<String> zodiacs = new ArrayList<>();
    private String head_image = "";
    private String nickname = "";
    private String sex = "";
    private String birthday = "";
    private String zodiac = "";
    private String introduce = "";
    private String address = "";

    Loading loading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_editing_materials;
    }

    @Override
    protected void initView() {
        tv_title.setText("编辑资料");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("保存");
        zodiacs.add("未选择");
        zodiacs.add("白羊座");
        zodiacs.add("金牛座");
        zodiacs.add("双子座");
        zodiacs.add("巨蟹座");
        zodiacs.add("狮子座");
        zodiacs.add("处女座");
        zodiacs.add("天秤座");
        zodiacs.add("天蝎座");
        zodiacs.add("射手座");
        zodiacs.add("摩羯座");
        zodiacs.add("水瓶座");
        zodiacs.add("双鱼座");
        loading = new Loading(this);
    }

    @Override
    protected void initData() {
        OkHttpUtils.getInstance().user_info(SPUtils.getValue("token"), SPUtils.getValue("user_id"), this);
        tv_userId.setText(SPUtils.getValue("user_id"));
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        switch (params) {
            case API.USER_INFO:
                UserInfo userInfo = (UserInfo) result.getData();
                if (userInfo.getHead_image() != null && !userInfo.getHead_image().isEmpty()) {
                    head_image = userInfo.getHead_image();
                    Glide.with(this).load(head_image).apply(new RequestOptions().error(R.mipmap.wd_tx_nor)).into(iv_head);
                }
                if (userInfo.getIntroduce() != null && !userInfo.getIntroduce().isEmpty()) {
                    nickname = userInfo.getNickname();
                    et_nickname.setText(nickname);
                }
                if (userInfo.getSex() != null && !userInfo.getSex().isEmpty()) {
                    sex = userInfo.getSex();
                    if (sex.equals("1")){
                        tv_sex.setText( "男");
                        ll_sex.setEnabled(false);
                    }
                    else if (sex.equals("2")){
                        tv_sex.setText("女");
                        ll_sex.setEnabled(false);
                    } else{
                        tv_sex.setText("请选择");
                        ll_sex.setEnabled(true);
                    }

                }
                if (userInfo.getBirthday() != null && !userInfo.getBirthday().isEmpty()) {
                    birthday = userInfo.getBirthday();
                    tv_birthday.setText(birthday);
                }
                if (userInfo.getZodiac() != null && !userInfo.getZodiac().isEmpty()) {
                    zodiac = userInfo.getZodiac();
                    if (zodiac.length() == 1)
                        tv_constellation.setText(zodiacs.get(Integer.parseInt(zodiac)));
                    else
                        tv_constellation.setText(zodiac);
                }
                if (userInfo.getIntroduce() != null && !userInfo.getIntroduce().isEmpty()) {
                    introduce = userInfo.getIntroduce();
                    et_sign.setText(introduce);
                }
                if (userInfo.getAddr() != null && !userInfo.getAddr().isEmpty()) {
                    address = userInfo.getAddr();
                    tv_area.setText(address);
                }
                break;
            case API.USER_INFO_EDIT:
                ToastUtil.toastShow("修改成功");
                finish();
                break;
            case API.COMMON_IMAGE_UPLOAD:
                loading.dismiss();
                SpareBean spareBean = (SpareBean) result.getData();
                head_image = spareBean.getImage_url();
                Glide.with(this).load(head_image).apply(new RequestOptions().error(R.mipmap.wd_tx_nor)).into(iv_head);
                break;
        }
    }

    @Override
    public void onError(String result) {
        loading.dismiss();
        ToastUtil.toastShow(result);
    }

    //选择生日
    public void selectBirthDay() {
        final DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 10));
        picker.setRangeEnd(2010, 1, 1);
        picker.setRangeStart(1950, 12, 30);
        picker.setSelectedItem(2000, 10, 10);
        picker.setResetWhileWheel(false);

        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
//                showToast(year + "-" + month + "-" + day);
                tv_birthday.setText(year + "-" + month + "-" + day);
                birthday = tv_birthday.getText().toString();
            }
        });

        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    //选择星座
    public void selectConstellation() {
        boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
        OptionPicker picker = new OptionPicker(this,
                isChinese ? new String[]{
                        "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座",
                        "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座",
                } : new String[]{
                        "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer",
                        "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"
                });
        picker.setCycleDisable(true);//不禁用循环
        picker.setTopBackgroundColor(0xFFEEEEEE);
        picker.setTopHeight(30);
        picker.setTopLineColor(0xFFEE0000);
        picker.setTopLineHeight(1);
        picker.setTitleText(isChinese ? "请选择" : "Please pick");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(14);
        picker.setCancelTextColor(0xFFEE0000);
        picker.setCancelTextSize(14);
        picker.setSubmitTextColor(0xFFEE0000);
        picker.setSubmitTextSize(14);
        picker.setTextColor(0xFFEE0000, 0xFF999999);
        WheelView.DividerConfig config = new WheelView.DividerConfig();
        config.setColor(0xFFEE0000);//线颜色
        config.setAlpha(140);//线透明度
        config.setRatio((float) (1.0 / 8.0));//线比率
        picker.setDividerConfig(config);
        picker.setBackgroundColor(0xFFE1E1E1);
        picker.setSelectedItem(zodiac.isEmpty() ? "白羊座" : zodiac);
//        picker.setSelectedIndex(7);
        picker.setCanceledOnTouchOutside(true);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
//                showToast("index=" + index + ", item=" + item);
                tv_constellation.setText(item);
                zodiac = item;
            }
        });
        picker.show();
    }

    //选择地址
    public void selectAddress() {
        new AddressInitTask(this, new AddressInitTask.InitCallback() {
            @Override
            public void onDataInitFailure() {
                ToastUtil.toastShow("数据初始化失败");
            }

            @Override
            public void onDataInitSuccess(ArrayList<Province> provinces) {
                AddressPicker picker = new AddressPicker(EditingMaterialsActivity.this, provinces);
                picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        String provinceName = province.getName() + " ";
                        String cityName = "";
                        if (city != null) {
                            cityName = city.getName() + " ";
                            //忽略直辖市的二级名称
                            if (cityName.equals("市辖区") || cityName.equals("市") || cityName.equals("县")) {
                                cityName = "";
                            }
                        }
                        String countyName = "";
                        if (county != null) {
                            countyName = county.getName();
                        }
                        tv_area.setText(provinceName + "" + cityName + "" + countyName);

//                        showToast(provinceName + " " + cityName + " " + countyName);
                    }
                });
                picker.show();
            }
        }).execute();
    }

    //选择性别
    public void selectSex() {
        OptionPicker picker = new OptionPicker(this, new String[]{
                "男", "女"
        });
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setShadowColor(Color.GRAY, 40);
        picker.setSelectedIndex(0);
        picker.setCycleDisable(true);
        picker.setTextSize(16);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                tv_sex.setText(item);
                sex = (index + 1) + "";
//                showToast("index=" + index + ", item=" + item);
            }
        });
        picker.show();
    }

    private void checkMessage() {
        nickname = et_nickname.getText().toString();
        introduce = et_sign.getText().toString();
        if (head_image.isEmpty()) {
            ToastUtil.toastShow("头像不能为空");
            return;
        }
        if (nickname.isEmpty()) {
            ToastUtil.toastShow("昵称不能为空");
            return;
        }

        for (int i = 1; i < zodiacs.size(); i++) {
            if (zodiacs.get(i).equals(tv_constellation.getText().toString())) {
                OkHttpUtils.getInstance().user_info_edit(SPUtils.getValue("token"), head_image, nickname, sex,
                        birthday, i + "", introduce, this);
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_right, R.id.ll_head, R.id.ll_sex, R.id.ll_birthday, R.id.ll_constellation, R.id.ll_area})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                //保存
                checkMessage();
                break;
            case R.id.ll_head:
                //头像
                PicturePicker picker = PicturePicker.init(this);
                picker.showPickDialog(this);
                break;
            case R.id.ll_sex:
                //性别
                selectSex();
                break;
            case R.id.ll_birthday:
                //生日
                selectBirthDay();
                break;
            case R.id.ll_constellation:
                //星座
                selectConstellation();
                break;
            case R.id.ll_area:
                //地址
                selectAddress();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    private String cropPath;
    private File tempFile;
    private File newFile;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PicturePickerFragment.PICK_TACK_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                MiPictureHelper.cropHandCard1(this, uri);
            }
        } else if (requestCode == PicturePickerFragment.PICK_SYSTEM_PHOTO && resultCode == Activity.RESULT_OK) {//PICK_TACK_PHOTO
            if (MiPictureHelper.hasSdcard()) {
                tempFile = new File(API.FILE_DIR, API.temp_filename);
                String path = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + API.temp_filename;//新文件地址
                newFile = new File(API.FILE_DIR, path);
                tempFile.renameTo(newFile);
                cropPath = Uri.fromFile(newFile).getPath();
                File newFile1 = CompressHelper.getDefault(this).compressToFile(new File(cropPath));
                loading.show();
                OkHttpUtils.getInstance().common_image_upload(FileUtils.fileToBase64(newFile1), this);
            } else {
                ToastUtil.toastShow("未找到存储卡，无法存储照片！");
                return;
            }
        } else if (requestCode == PicturePicker.PHOTO_REQUEST_CUT) {
            if (data == null)
                return;
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                File newFile = FileUtils.saveBitmapFile(photo);
                loading.show();
                OkHttpUtils.getInstance().common_image_upload(FileUtils.fileToBase64(newFile), this);
            } else {
                Uri uri = data.getData();
                if (uri != null) {
                    Bitmap photo = BitmapFactory.decodeFile(uri.getPath());
                    File newFile = FileUtils.saveBitmapFile(photo);
                    loading.show();
                    OkHttpUtils.getInstance().common_image_upload(FileUtils.fileToBase64(newFile), this);
                }
            }
        }
    }

}
