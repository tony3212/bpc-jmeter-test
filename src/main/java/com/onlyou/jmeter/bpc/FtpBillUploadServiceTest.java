package com.onlyou.jmeter.bpc;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.onlyou.bpc.bpo.vo.UploadBillRequestVO;
import com.onlyou.bpc.bpo.vo.UploadBillResponseVO;
import com.onlyou.bpc.external.enumerator.ResponseEnum;

/**
 * FTP模式票据上传
 * @author HQH
 *
 */
public class FtpBillUploadServiceTest extends BaseServiceTest {

	/**
	 * 来源
	 */
	private String source = "zyd";

	/**
	 * 票据数据
	 */
	private int billCount = 10;

	/**
	 * 客户ID
	 */
	private String customerId = "ONLYOU00000000";

	/**
	 * 上传影像绝对路径
	 * eg: d:/xxx/abc.jpg
	 */
	private String uploadImageFile;

	/**
     * 流转单编号(包裹编号)
     */
    private String packetCode;

    /**
     * 设置可用参数及的默认值；
     * 这个方法是用来自定义java方法入参的。
     * params.addArgument("num1","");表示入参名字叫num1，默认值为空。
     */
	@Override
    public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
        params.addArgument("source", source);
        params.addArgument("customerId", customerId);
        params.addArgument("billCount", billCount + "");
        params.addArgument("uploadImageFile", uploadImageFile);
        params.addArgument("packetCode", packetCode);

		params.addArgument("ComeFrom", "Bpo-Iscan");
		params.addArgument("FeeCode", "");
		params.addArgument("packetCode", "V510100905");
		params.addArgument("Scanername", "华锐集团");
		params.addArgument("QRCode", "");
		params.addArgument("AppVersion", "17102116");
		params.addArgument("ImgSuffix", ".jpg");
		params.addArgument("RotateImg", "");
		params.addArgument("partnerId", "364ae996eae24aebb3ac03fbeea89786");
		params.addArgument("ModeCode", "");
		params.addArgument("Scaneruser", "efa664dd2bb8420baad0e014b3c5daae");
		params.addArgument("MakeDate", "20171024104942");
		params.addArgument("SequenceNO", "0003");
		params.addArgument("ImgName", "364ae996eae24aebb3ac03fbeea89786/81bc3717015b47eba89174d5dbd538b6/201709/I_0/336.jpg");
		params.addArgument("SourceName", "336.jpg");
		params.addArgument("OcrType", "");
		params.addArgument("FeeID", "");
		params.addArgument("StoreID", "");
		params.addArgument("RecordFlag", "");
		params.addArgument("BatchNo", "");
		params.addArgument("AccountDate", "201709");
		params.addArgument("ImgType", "KJDZ0123");
		params.addArgument("CustCode", "1bc3717015b47eba89174d5dbd538b6");
		params.addArgument("imageUri", "364ae996eae24aebb3ac03fbeea89786/81bc3717015b47eba89174d5dbd538b6/201709/V510100905/336.jpg");
		params.addArgument("packetCode", "V510100905");
		params.addArgument("branchNum", "1004");
		params.addArgument("imageData", "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");
        return params;
	}

	/**
	 * 开始执行测试
	 */
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult sampleResult = new SampleResult();
		sampleResult.setSampleLabel("Ftp模式下票据上传");
		// 开始统计响应时间标记
		sampleResult.sampleStart();
		startTime = System.currentTimeMillis();
		// 获取jmeter输入的参数值
		getParamValues(context);

		try {
			InputStream is = this.getClass().getResourceAsStream(imageFileName);
			byte[] fileBytes = null;
			if (is != null) {
				fileBytes = IOUtils.toByteArray(is);
				if (fileBytes != null) {
					logger.info("影像文件大小：" + fileBytes.length);
				}
				is.close();
			}

			UploadBillRequestVO billUploadReq = new UploadBillRequestVO();
			billUploadReq.setBranchNum(branchNum);
			// 关联流转编码
			billUploadReq.setPacketCode(this.packetCode);
			String imageUri = getImageUri();
			billUploadReq.setImageUri(imageUri); // 影像路径
			// 影像文件数据
			if (StringUtils.isNotEmpty(uploadImageFile)) {
				File file = new File(uploadImageFile);
				billUploadReq.setImageData(FileUtils.readFileToByteArray(file));
			} else if (fileBytes != null && fileBytes.length > 0) {
				billUploadReq.setImageData(fileBytes);
			}
			billUploadReq.setData(getData(context));

			UploadBillResponseVO resp = super.bpoBillService.uploadBill(billUploadReq);
			if (resp != null) {
				String respCode = resp.getCode().getCode();
				if (ResponseEnum.SUCCESS.getCode().equals(respCode)) {
					logger.info("票据上传成功，URI:" + imageUri);
				} else {
					logger.info("票据上传失败，URI:" + imageUri);
				}
				sampleResult.setSuccessful(true);
				sampleResult.setResponseCodeOK();
				sampleResult.setResponseData("票据编码:" + resp.getBillCode() + ",success:" + true, null);
				sampleResult.setDataType(SampleResult.TEXT);
			} else {
				logger.warn("create billcode fail, return null.");
				sampleResult.setSuccessful(false);
				sampleResult.setResponseData("创建票据失败" + ",success:" + false, null);
				sampleResult.setDataType(SampleResult.TEXT);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sampleResult.setSuccessful(false);
			sampleResult.setResponseData(e.getMessage() + ",success:" + false, null);
			sampleResult.setDataType(SampleResult.TEXT);
		} finally {
			sampleResult.sampleEnd(); // 结束统计响应时间标记
		}
		return sampleResult;
	}

	/**
     * 获取jmeter输入的参数值
     */
    private void getParamValues(JavaSamplerContext context) {
    	source = context.getParameter("source", source);
    	String billCountStr = context.getParameter("billCount", billCount + "");
    	billCount = Integer.parseInt(billCountStr);
    	customerId = context.getParameter("customerId", customerId);
    	uploadImageFile = context.getParameter("uploadImageFile", uploadImageFile);
    	packetCode = context.getParameter("packetCode", packetCode);
    }

    /**
     * 生成影像URI
     * @return
     */
    private String getImageUri() {
    	String sep = "/";
    	StringBuilder sb = new StringBuilder();
    	sb.append(sep).append(customerId).append(sep).append(UUID.randomUUID()).append(".jpg");
    	return sb.toString();
    }

	private Map<String, Object> getData(JavaSamplerContext context) {
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("ComeFrom", context.getParameter("ComeFrom", "Bpo-Iscan"));
		data.put("FeeCode", context.getParameter("FeeCode", ""));
		data.put("packetCode", context.getParameter("packetCode", "V510100905"));
		data.put("Scanername", context.getParameter("Scanername", "华锐集团"));
		data.put("QRCode", context.getParameter("QRCode", ""));
		data.put("AppVersion", context.getParameter("AppVersion", "17102116"));
		data.put("ImgSuffix", context.getParameter("ImgSuffix", ".jpg"));
		data.put("RotateImg", context.getParameter("RotateImg", ""));
		data.put("partnerId", context.getParameter("partnerId", "364ae996eae24aebb3ac03fbeea89786"));
		data.put("ModeCode", context.getParameter("ModeCode", ""));
		data.put("Scaneruser", context.getParameter("Scaneruser", "efa664dd2bb8420baad0e014b3c5daae"));
		data.put("MakeDate", context.getParameter("MakeDate", "20171024104942"));
		data.put("SequenceNO", context.getParameter("SequenceNO", "0003"));
		data.put("ImgName", context.getParameter("ImgName", "364ae996eae24aebb3ac03fbeea89786/81bc3717015b47eba89174d5dbd538b6/201709/I_0/336.jpg"));
		data.put("SourceName", context.getParameter("SourceName", "336.jpg"));
		data.put("OcrType", context.getParameter("OcrType", ""));
		data.put("FeeID", context.getParameter("FeeID", ""));
		data.put("StoreID", context.getParameter("StoreID", ""));
		data.put("RecordFlag", context.getParameter("RecordFlag", ""));
		data.put("BatchNo", context.getParameter("BatchNo", ""));
		data.put("AccountDate", context.getParameter("AccountDate", "201709"));
		data.put("ImgType", context.getParameter("ImgType", "KJDZ0123"));
		data.put("CustCode", context.getParameter("CustCode", "1bc3717015b47eba89174d5dbd538b6"));
		data.put("imageUri", context.getParameter("imageUri", "364ae996eae24aebb3ac03fbeea89786/81bc3717015b47eba89174d5dbd538b6/201709/V510100905/336.jpg"));
		data.put("packetCode", context.getParameter("packetCode", "V510100905"));
		data.put("branchNum", context.getParameter("branchNum", "1004"));
		data.put("imageData", context.getParameter("imageData", "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"));

		return data;
	}

    @Override
	protected long getStartTime() {
		return startTime;
	}

    public static void main(String[] args) {
    	FtpBillUploadServiceTest service = new FtpBillUploadServiceTest();
    	JavaSamplerContext context = new JavaSamplerContext(service.getDefaultParameters());
    	service.setupTest(context);
    	service.runTest(context);
    	service.teardownTest(context);
        //System.exit(0);
	}

}
