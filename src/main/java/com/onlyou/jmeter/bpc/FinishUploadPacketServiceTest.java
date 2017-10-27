package com.onlyou.jmeter.bpc;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.onlyou.bpc.external.enumerator.ResponseEnum;
import com.onlyou.bpc.external.vo.ResponseVO;
import com.onlyou.bpc.pub.vo.FinishUploadPacketRequestVO;

/**
 * OSS调用完成上传票据服务
 * @author HQH
 *
 */
public class FinishUploadPacketServiceTest extends BaseServiceTest {

	/**
	 * 来源
	 */
	private String source = "zyd";

	/**
	 * 客户ID
	 */
	private String customerId = "ONLYOU00000000";

	/**
     * 包裹编号=流转单编号
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
        params.addArgument("packetCode", packetCode);
        return params;
	}

	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult sampleResult = new SampleResult();
		sampleResult.setSampleLabel("OSS调用完成上传票据服务");
        sampleResult.sampleStart(); // 开始统计响应时间标记
        startTime = System.currentTimeMillis();

        //获取jmeter输入的参数值
        getParamValues(context);

        try {
			//调用上传完成
			FinishUploadPacketRequestVO reqFinish = new FinishUploadPacketRequestVO();
			reqFinish.setPacketCode(packetCode);
			ResponseVO respFinish = super.bpcPublicService.finishUploadPacket(reqFinish);
			logger.info("包裹完成状态：" + respFinish.getCode().getName());
			if(ResponseEnum.SUCCESS.getCode().equals(respFinish.getCode().getCode())) {
				logger.info("包裹编号: " + packetCode + " 上传完成。");
				sampleResult.setSuccessful(true);
                sampleResult.setResponseCodeOK();
                sampleResult.setResponseData("包裹编号:" + packetCode + ",success:"+true, null);
                sampleResult.setDataType(SampleResult.TEXT);
			} else {
				logger.info("包裹编号: " + packetCode + " 上传失败。" + ",success:"+false);
				sampleResult.setSuccessful(false);
                sampleResult.setResponseData("包裹编号:" + packetCode + ",success:"+false, null);
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
    	customerId = context.getParameter("customerId", customerId);
    	packetCode = context.getParameter("packetCode", packetCode);
    }

	@Override
	protected long getStartTime() {
		return startTime;
	}

	public static void main(String[] args) {
		FinishUploadPacketServiceTest service = new FinishUploadPacketServiceTest();
		JavaSamplerContext context = new JavaSamplerContext(service.getDefaultParameters());
    	service.setupTest(context);
    	service.runTest(context);
    	service.teardownTest(context);
	}

}
