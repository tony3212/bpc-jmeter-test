package com.onlyou.jmeter.bpc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.alibaba.fastjson.JSON;
import com.onlyou.jmeter.util.HttpClientUtil;

/**
 * OSS回调服务
 * @author HQH
 *
 */
public class OssCallbackServiceTest extends BaseServiceTest {
	
	/**
	 * 客户ID
	 */
	private String customerId = "ONLYOU00000000";
	
	/**
     * 包裹编号=流转单编号
     */
    private String packetCode;
    
    /**
     * 包裹ID
     */
    private String packetId;
	
	/**
     * 设置可用参数及的默认值；
     * 这个方法是用来自定义java方法入参的。
     * params.addArgument("num1","");表示入参名字叫num1，默认值为空。
     */
	@Override
    public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
        params.addArgument("customerId", customerId);
        params.addArgument("packetCode", packetCode);
        params.addArgument("packetId", packetId);
        return params;
	}

	@SuppressWarnings("unchecked")
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult sampleResult = new SampleResult();
		sampleResult.setSampleLabel("OSS回调服务");
        sampleResult.sampleStart(); // 开始统计响应时间标记
        startTime = System.currentTimeMillis();
        
        //获取jmeter输入的参数值
        getParamValues(context);
        
        try {
			//OSS回调
        	//每上传一张影像，执行回调一次
			String jsonString = getJsonStrParameter(this.packetId);
			String httpResp = HttpClientUtil.post(ossCallbackUri, jsonString);
			logger.info("OSS回调返回结果：" + httpResp);
			if(StringUtils.isNotEmpty(httpResp)) {
				HashMap<String, Object> respMap = JSON.parseObject(httpResp, HashMap.class);
				boolean success = (Boolean)respMap.get("success");
				logger.info("OSS回调返回结果(success)：" + success);
				sampleResult.setSuccessful(true);
                sampleResult.setResponseCodeOK();
                sampleResult.setResponseData("OSS回调返回结果:" + success + ",success:" + true, null);
                sampleResult.setDataType(SampleResult.TEXT);
			} else {
				sampleResult.setSuccessful(false);
                sampleResult.setResponseData("OSS回调返回结果:" + httpResp + ",success:" + false, null);
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
    	customerId = context.getParameter("customerId", customerId);
    	packetCode = context.getParameter("packetCode", packetCode);
    	packetId = context.getParameter("packetId", packetId);
    }

	@Override
	protected long getStartTime() {
		return startTime;
	}
	
	/**
	 * 获取请求参数
	 * @param packetId 包裹ID
	 * @return
	 */
	private String getJsonStrParameter(String packetId) {
		String imageName = UUID.randomUUID() + ".jpg";
		return "{" +
				 	"\"object\": \"" + this.customerId + "/eebc7236ff8a47acbfb02d9f0f38269a/201709/59b251f84e9d160007e100ba/"+ imageName +"\"," +
				 	"\"packetId\": \"" + packetId + "\"," +
				 	"\"data\": \"{\\\"BillImg\\\":{\\\"packetId\\\":null,\\\"MakeDate\\\":\\\"20170908161844\\\",\\\"CustCode\\\":\\\"eebc7236ff8a47acbfb02d9f0f38269a\\\",\\\"Images\\\":{\\\"SequenceNO\\\":\\\"0047\\\",\\\"OcrType\\\":null,\\\"ImgType\\\":null,\\\"SourceName\\\":null,\\\"ImgName\\\":\\\"/1c21c9574e57437298a9310330565138/eebc7236ff8a47acbfb02d9f0f38269a/201709/59b251f84e9d160007e100ba/51bd18b0-946e-11e7-8b8e-1b27f7440f32.jpg\\\",\\\"ImgSuffix\\\":\\\".jpg\\\"},\\\"FeeID\\\":null,\\\"Scaneruser\\\":\\\"2a556296ffb944cf96568f916bd7004f\\\",\\\"BatchNo\\\":null,\\\"Scanername\\\":\\\"林章峰\\\",\\\"FeeCode\\\":null,\\\"ModeCode\\\":\\\"01\\\",\\\"StoreID\\\":null,\\\"AccountDate\\\":\\\"201710\\\",\\\"ComeFrom\\\":null}}\"" +
			    "}";
	}
	
	public static void main(String[] args) {
		OssCallbackServiceTest service = new OssCallbackServiceTest();
		JavaSamplerContext context = new JavaSamplerContext(service.getDefaultParameters());
    	service.setupTest(context);
    	service.runTest(context);
    	service.teardownTest(context);
	}

}
