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

import com.onlyou.bpc.bpo.vo.CreatePacketRequestVO;
import com.onlyou.bpc.bpo.vo.CreatePacketResponseVO;
import com.onlyou.bpc.bpo.vo.UploadBillRequestVO;
import com.onlyou.bpc.bpo.vo.UploadBillResponseVO;
import com.onlyou.bpc.external.enumerator.ResponseEnum;

/**
 * FTP模式票据上传
 * @author HQH
 *
 */
public class CreatePacketAndFtpBillUploadServiceTest extends BaseServiceTest {
	
	/**
	 * 来源
	 */
	private String source = "zyd";
	
	/**
	 * 票据数据
	 */
	private int billCount = 1;
	
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
		
		CreatePacketRequestVO createPacketRequestVO = new CreatePacketRequestVO();
        createPacketRequestVO.setBranchNum(branchNum);
        createPacketRequestVO.setSource(getSource(source));
        createPacketRequestVO.setBillCount(billCount);
        createPacketRequestVO.setCustomerId(customerId);

		try {
			//创建包裹
            CreatePacketResponseVO response = super.bpoBillService.createPacket(createPacketRequestVO);
            packetCode = response.getPacketCode();
            logger.info("包裹编号：" + packetCode);
			
            long s = System.currentTimeMillis();
			InputStream is = this.getClass().getResourceAsStream(imageFileName);
			byte[] fileBytes = null;
			if (is != null) {
				fileBytes = IOUtils.toByteArray(is);
				if (fileBytes != null) {
					logger.info("影像文件大小：" + fileBytes.length + "byte");
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
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("jmeterTest", "****dont care****"); // 业务数据
			billUploadReq.setData(data);
			long ss = System.currentTimeMillis();
			logger.info("影像读取及数据构建等耗时：" + (ss - s) + "ms");
			UploadBillResponseVO resp = super.bpoBillService.uploadBill(billUploadReq);
			if (resp != null) {
				long e = System.currentTimeMillis();
				logger.info("上传影像耗时：" + (e - ss) + "ms");
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
    
    @Override
	protected long getStartTime() {
		return startTime;
	}
    
    public static void main(String[] args) {
    	CreatePacketAndFtpBillUploadServiceTest service = new CreatePacketAndFtpBillUploadServiceTest();
    	JavaSamplerContext context = new JavaSamplerContext(service.getDefaultParameters());
    	service.setupTest(context);
    	service.runTest(context);
    	service.teardownTest(context);
        //System.exit(0);
	}

}
